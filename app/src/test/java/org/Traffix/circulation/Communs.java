package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

public class Communs {
    public static Réseau créerIntersection2Routes() {
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100, 0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        réseau.véhicules = new Véhicule[0];
        return réseau;
    }

    public static Réseau créerIntersection3Routes() {
        Réseau réseau = créerIntersection2Routes();
        réseau.intersections.add(new IntersectionArrêt(new Vec2(10, -100)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        return réseau;
    }

    public static Réseau créerIntersection4Routes() {
        Réseau réseau = créerIntersection3Routes();
        réseau.intersections.add(new IntersectionArrêt(new Vec2(10, 100)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        return réseau;
    }

    public static Véhicule nouveauVéhicule(Réseau réseau, Vec2 destination) {
        Véhicule véhicule = new Véhicule(4.2f);
        String[] routine = { réseau.avoirAdresse(destination) };
        véhicule.avoirNavigateur().donnerRoutine(routine);
        return véhicule;
    }

    public static void générerNumérosRues(Réseau réseau){
        float LARGEUR_MAISON = 15f;
        float ESPACEMENT_AVANT_MAISON = 5f;
        for (ArrayList<Route> tronçon : réseau.tronçons.values()) {
            // Aucun numéro de rue sur les autoroutes.
            if(tronçon.get(0).nom.contains("Autoroute")){
                continue;
            }

            // Trouver l'intersection au bout
            Intersection interA = null;
            for (int j = 0; j < tronçon.get(0).intersectionA.routes.size(); j++) {
                if(tronçon.get(0).intersectionA.routes.get(j) != tronçon.get(0) && tronçon.get(0).intersectionA.routes.get(j).nom.equals(tronçon.get(0).nom)){
                    interA = tronçon.get(0).intersectionB;
                }
            }
            if(interA == null){
                interA = tronçon.get(0).intersectionA;
            }

            int adressesCompte = 0;
            //Passer à travers toutes les routes du tronçon.
            for (int i = 0; i < tronçon.size(); i++) {
                Route route = tronçon.get(i);
                int nbAdresses = (int)Math.ceil(tronçon.get(i).avoirLongueur()/LARGEUR_MAISON);
                Vec2 tan = Vec2.sous(route.intersectionA.position,route.intersectionB.position).norm().mult(interA==route.intersectionA?-1f:1f);
                Vec2 cotan = new Vec2(tan.y,-tan.x);

                int[] numérosSensA = new int[nbAdresses];
                int[] numérosSensB = new int[nbAdresses];
                Vec2[] positionsSensA = new Vec2[nbAdresses];
                Vec2[] positionsSensB = new Vec2[nbAdresses];
                for (int j = 0; j < nbAdresses; j++) {
                    Vec2 pos = Vec2.mult(tan,LARGEUR_MAISON*(float)j).addi(interA.position);
                    if(interA == route.intersectionA){
                        numérosSensA[j] = 2*j+adressesCompte;
                        numérosSensB[j] = 2*j+1+adressesCompte;
                    }else{
                        numérosSensA[j] = (adressesCompte+nbAdresses*2)-(2*j);
                        numérosSensB[j] = (adressesCompte+nbAdresses*2)-(2*j+1);
                    }
                    positionsSensA[j] = Vec2.addi(pos,Vec2.mult(cotan,ESPACEMENT_AVANT_MAISON));
                    positionsSensB[j] = Vec2.sous(pos,Vec2.mult(cotan,ESPACEMENT_AVANT_MAISON));
                }
                route.donnerAdresses(numérosSensA, positionsSensA, numérosSensB, positionsSensB);
                adressesCompte += nbAdresses*2;
                interA = interA==route.intersectionA?route.intersectionB:route.intersectionA;
            }
        }
    }
}
