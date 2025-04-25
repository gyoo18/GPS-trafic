package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;
import org.Traffix.maths.Vec3;
/**
 * Spécialisation de Intersection qui gère le traffic selon la logique d'un feux de circulation.
 * Doit avoir soit 3, soit 4 routes.
 */
public class IntersectionFeux extends Intersection{

    private État état = État.A_PASSER;
    private int duréeCycleMin = 1;
    private long tempCycleMillis = System.currentTimeMillis();

    // Les routes A,B,C et D sont les 3 ou 4 routes de l'intersection. Elles sont organisés comme suit :
    //
    //           B
    //         | | |
    //         | | |
    //   ──────┘ V └-------
    // C ------> O <------- D
    //   ──────┐ ^ ┌-------
    //         | | |
    //         | | |
    //           A
    //
    private Route routeA = null; // Les routes A et B sont toujours les deux plus alignées. Dans un T, elles sont le bras principal, dans une croix
    private Route routeB = null; // elles sont le tronçon central.
    private Route routeC = null; // C est toujours à gauche de A, en pointant vers l'intersection.
    private Route routeD = null; // D, si elle est présente, est en face de C.
    private boolean construit = false;

    private enum État{
        A_PASSER,
        A_TOURNER,
        B_PASSER,
        B_TOURNER
    }

    public IntersectionFeux(Vec2 pos){
        super(pos);
    }

    public IntersectionFeux(Vec2 pos, ArrayList routes){
        super(pos, routes);
    }

    public IntersectionFeux(Intersection copie){
        super(copie);
    }

    @Override public void ajouterRoute(Route route){
        if(routes.size() == 4){
            System.err.println("[ERREUR] IntersectionFeux ne supporte pas la gestion de plus de 4 routes. Veuillez ne fournir que 3 ou 4 routes.");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return;
        }
        super.ajouterRoute(route);
    }

    @Override
    public boolean peutPasser(Route routeDépart, Route routeDestination) {
        if(!testerValidité()){
            return false;
        }

        // S'assurer qu'on a les routes A, B, C et D
        if(!construit){
            construire();
        }

        if (routeDépart == null || routeDestination == null){
            System.err.println("[ERREUR] routeDépart et routeDestination ne peuvent pas être null");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return false;
        }

        switch (état) {
            // Si D n'existe pas (si l'intersection est un T), on ne pourra jamais tourner dessus, puisque routeDestination n'est pas null.
            // On tourne à droite pour entrer sur C depuis B et sur D depuis A.
            case A_PASSER:  return (routeDépart == routeA && routeDestination == routeB) || (routeDépart == routeB && routeDestination == routeA) || (routeDépart == routeB && routeDestination == routeC) || (routeDépart == routeA && routeDestination == routeD);
            case A_TOURNER: return (routeDépart == routeA && routeDestination == routeC) || (routeDépart == routeB && routeDestination == routeC) || (routeDépart == routeA && routeDestination == routeD) || (routeDépart == routeB && routeDestination == routeD);
            // On tourne à droite pour entrer sur A depuis C et sur B depuis D.
            case B_PASSER:  return (routeDépart == routeC && routeDestination == routeD) || (routeDépart == routeD && routeDestination == routeC) || (routeDépart == routeC && routeDestination == routeA) || (routeDépart == routeD && routeDestination == routeB);
            case B_TOURNER: return (routeDépart == routeC && routeDestination == routeB) || (routeDépart == routeD && routeDestination == routeA) || (routeDépart == routeC && routeDestination == routeA) || (routeDépart == routeD && routeDestination == routeB);
            default: throw new IllegalArgumentException("Aucun comportement défini pour état = État."+état.name());
        }
    }

    /**
     * Vas chercher les routes A, B, C et D et les organises comme suit décrit plus haut.
     */
    private void construire(){
        // Trouver les routes A, B, C et D.

        // Trouver les directions de chaque route. Les directions pointent vers le centre de l'intersection.
        Vec2[] directions = new Vec2[routes.size()];
        for (int i = 0; i < routes.size(); i++) {
            Intersection moi = routes.get(i).intersectionA==this?routes.get(i).intersectionA:routes.get(i).intersectionB;
            Intersection autre = routes.get(i).intersectionA==this?routes.get(i).intersectionB:routes.get(i).intersectionA;
            directions[i] = Vec2.sous(moi.position, autre.position);
        }

        // Construire une matrice d'alignement entre chaque paire de routes et choisir les deux les mieux alignées.
        float[][] alignements = new float[routes.size()][];
        float alignementMax = -Float.MAX_VALUE;
        int alignementMaxI = -1;
        int alignementMaxJ = -1;
        for (int i = 0; i < alignements.length; i++) {
            alignements[i] = new float[routes.size()];
            for (int j = 0; j < alignements.length; j++) {
                if(i == j){
                    continue;
                }
                alignements[i][j] = -Vec2.scal(directions[i],directions[j]); // On prend le produit scalaire négatif, car les routes se font face.
                if(alignements[i][j] > alignementMax){
                    alignementMax = alignements[i][j];
                    alignementMaxI = i;
                    alignementMaxJ = j;
                }
            }
        }

        if(routes.size() == 3){
            // Trouver la route C
            int k = -1;
            for (int i = 0; i < routes.size(); i++) {
                if(i != alignementMaxI && i != alignementMaxJ){
                    routeC = routes.get(i);
                    k = i;
                    break;
                }
            }

            // Déterminer, de quelle route I et J, laquelle est à droite de C et qui deviendra A et laquelle est à gauche et deviendra B.
            if( directions[k].x*directions[alignementMaxI].y - directions[k].y*directions[alignementMaxI].x > 0 ){
                // Si I est à droite
                routeA = routes.get(alignementMaxI);
                routeB = routes.get(alignementMaxJ);
            }else{
                // Si J est à droite
                routeA = routes.get(alignementMaxJ);
                routeB = routes.get(alignementMaxI);
            }
        }else{
            int k = -1;
            for (int i = 0; i < routes.size(); i++) {
                if(i != alignementMaxI && i != alignementMaxJ && routeC == null){
                    routeC = routes.get(i);
                    k = i;
                }else if(i != alignementMaxI && i != alignementMaxJ && routeC != null){
                    routeD = routes.get(i);
                    break;
                }
            }

            // Déterminer, de quelle route I et J, laquelle est à droite de C et qui deviendra A et laquelle est à gauche et deviendra B.
            if( directions[k].x*directions[alignementMaxI].y - directions[k].y*directions[alignementMaxI].x > 0 ){
                // Si I est à droite
                routeA = routes.get(alignementMaxI);
                routeB = routes.get(alignementMaxJ);
            }else{
                // Si J est à droite
                routeA = routes.get(alignementMaxJ);
                routeB = routes.get(alignementMaxI);
            }
        }
        construit = true;
    }

    public void miseÀJour(){
        if(!testerValidité()){
            return;
        }

        if(System.currentTimeMillis()-tempCycleMillis > duréeCycleMin*60000){
            switch (état) {
                case A_PASSER:
                    état = État.A_TOURNER;
                    break;
                case A_TOURNER:
                    if(routes.size() == 3){état = État.B_TOURNER;}
                    else{état = État.B_PASSER;}
                    break;
                case B_PASSER:
                    état = État.B_TOURNER;
                    break;
                case B_TOURNER:
                    état = État.A_PASSER;
                    break;
            }
            tempCycleMillis = System.currentTimeMillis();
        }
    }

    /**
     * Teste si l'intersection possède précisément 3 ou 4 routes.
     * @return boolean représentant le résultat du test.
     */
    private boolean testerValidité(){
        if(routes.size() > 4 || routes.size() < 3){
            System.err.println("[ERREUR] IntersectionFeux ne supporte que la gestion de 3 ou 4 routes. Nombre de routes actuel : "+routes.size());
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return false;
        }else{
            return true;
        }
    }

}