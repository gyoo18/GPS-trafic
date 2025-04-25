package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

/**
 * Spécialisation de la classe Intersection qui gère le traffic selon une logique de voie prioritaire.
 */
public class IntersectionLaissezPasser extends Intersection {

    private boolean estConstruit = false;
    
    // Les routes A et B sont les routes prioritaire et les plus alignées. Les autres véhicules devront laisser passer ceux qui sont sur A ou B.
    private Route routeA = null;
    private Route routeB = null;

    // Temps minimum pour lequel l'intersection doit être libre pour que les véhicules non-prioritaires puissent passer, en secondes.
    private final float TEMPS_MANŒUVRE = 20f;

    public IntersectionLaissezPasser(Vec2 pos){
        super(pos);
    }

    public IntersectionLaissezPasser(Vec2 pos, ArrayList routes){
        super(pos, routes);
    }

    public IntersectionLaissezPasser(Intersection copie){
        super(copie);
    }

    @Override
    public boolean peutPasser(Route routeDépart, Route routeDestination) {
        if(!estConstruit){
            construire();
        }

        if (routeDépart == null || routeDestination == null){
            System.err.println("[ERREUR] routeDépart et routeDestination ne peuvent pas être null");
            System.err.println(Thread.currentThread().getStackTrace());
            return false;
        }

        // Si les véhicules sont et continuent sur le tronçon prioritaire.
        if ((routeDépart == routeA && routeDestination == routeB) || (routeDépart == routeB && routeDestination == routeA)){
            return true;
        }

        // Si les véhicules sont sur le tronçon prioritaire et tournent à droite.
        if (routeDépart == routeA || routeDépart == routeB){
            Vec2 directionDépart = Vec2.sous(routeDépart.intersectionA.position,routeDépart.intersectionB.position).mult(this==routeDépart.intersectionA?1f:-1f);
            Vec2 directionDestination = Vec2.sous(routeDestination.intersectionA.position,routeDestination.intersectionB.position).mult(this==routeDestination.intersectionA?1f:-1f);
            if( directionDestination.x*directionDépart.y - directionDestination.y*directionDépart.x < 0){
                return true;
            }else{
                // Si les véhicules tournent à gauche
                return estIntersectionLibre(routeDépart);
            }
        }

        // Si les véhicules ne sont pas sur le tronçon prioritaire et tournent à droite.
        if (routeDépart != routeA && routeDépart != routeB){
            Vec2 directionDépart = Vec2.sous(routeDépart.intersectionA.position,routeDépart.intersectionB.position).mult(this==routeDépart.intersectionA?1f:-1f);
            Vec2 directionDestination = Vec2.sous(routeDestination.intersectionA.position,routeDestination.intersectionB.position).mult(this==routeDestination.intersectionA?1f:-1f);
            if( directionDestination.x*directionDépart.y - directionDestination.y*directionDépart.x < 0){
                return estIntersectionLibre(routeDestination==routeA?routeB:routeA); // Ne compter que la voie qui s'en vient à gauche.
            }else{
                // Si les véhicules tournent à gauche
                return estIntersectionLibre(null);
            }
        }

        // Devrait être impossible à atteindre.
        return false;
    }

    /**
     * Indique si l'intersection sera libre pour la durée de TEMPS_MANŒUVRE
     * @param exception route à ne pas examiner. Utile si un véhicule prioritaire veut tourner à gauche : il occupe une voie prioritaire, donc l'intersection n'est pas libre.
     * @return booléen indiquant si l'intersection sera libre.
     */
    private boolean estIntersectionLibre(Route exception){
        if (exception != routeA){
            Véhicule premierVéhicule = this==routeA.intersectionA?routeA.avoirPremierVéhiculeSensA():routeA.avoirPremierVéhiculeSensB();
            float distance = Vec2.distance(null, position); // TODO changer la position du véhicule en Vec2
            if (distance/premierVéhicule.getVitesse() < TEMPS_MANŒUVRE){
                return false;
            }
        }
        if(exception != routeB){
            Véhicule premierVéhicule = this==routeB.intersectionA?routeB.avoirPremierVéhiculeSensA():routeB.avoirPremierVéhiculeSensB();
            float distance = Vec2.distance(null, position); //TODO changer la position du véhicule en Vec2
            if(distance/premierVéhicule.getVitesse() < TEMPS_MANŒUVRE){
                return false;
            }
        }
        return true;
    }

    public void construire(){
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
        for (int i = 0; i < alignements.length; i++) {
            alignements[i] = new float[routes.size()];
            for (int j = 0; j < alignements.length; j++) {
                if(i == j){
                    continue;
                }
                alignements[i][j] = -Vec2.scal(directions[i],directions[j]); // On prend le produit scalaire négatif, car les routes se font face.
                if(alignements[i][j] > alignementMax){
                    alignementMax = alignements[i][j];
                    routeA = routes.get(i);
                    routeB = routes.get(j);
                }
            }
        }

        estConstruit = true;
    }

    @Override
    public void miseÀJour() {}
    
}

