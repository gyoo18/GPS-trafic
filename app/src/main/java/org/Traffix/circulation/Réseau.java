package org.Traffix.circulation;

import java.util.ArrayList;
import java.util.HashMap;

public class Réseau {
    public ArrayList<Route> routes = new ArrayList<>();
    public ArrayList<Intersection> intersections = new ArrayList<>();
    public HashMap<String, ArrayList<Route>> tronçons = new HashMap<>(); // Liste de routes portants le même nom

    public void construireTronçons(){
        System.out.println("Construction des tronçons...");
        for (int i = 0; i < routes.size(); i++) {
            if(!tronçons.containsKey(routes.get(i).nom)){
                // Vérifier que ce tronçon est un bout
                boolean estABout = true;
                for (int j = 0; j < routes.get(i).intersectionA.routes.size(); j++) {
                    if(routes.get(i).intersectionA.routes.get(j) != routes.get(i) && routes.get(i).intersectionA.routes.get(j).nom == routes.get(i).nom){
                        estABout = false;
                        break;
                    }
                }

                boolean estBBout = true;
                for (int j = 0; j < routes.get(i).intersectionB.routes.size(); j++) {
                    if(routes.get(i).intersectionB.routes.get(j) != routes.get(i) && routes.get(i).intersectionB.routes.get(j).nom == routes.get(i).nom){
                        estBBout = false;
                        break;
                    }
                }

                if(!estABout && !estBBout){
                    continue;
                }

                ArrayList<Route> tronçon = new ArrayList<>();
                tronçon.add(routes.get(i));
                tronçons.put(routes.get(i).nom,tronçon);

                if(estABout && estBBout){
                    continue;
                }

                ajouterConnexionÀTronçon(routes.get(i), estABout, tronçon);
            }
        }
    }

    private void ajouterConnexionÀTronçon(Route route, boolean estAFait, ArrayList<Route> tronçon){
        if(estAFait){
            for (int i = 0; i < route.intersectionB.routes.size(); i++) {
                Route routeB = route.intersectionB.routes.get(i);
                if(routeB != route && routeB.nom == route.nom){
                    tronçon.add(routeB);
                    boolean estAFaitB = routeB.intersectionA==route.intersectionB;
                    ajouterConnexionÀTronçon(routeB, estAFaitB, tronçon);
                    break;
                }
            }
        }else{
            for (int i = 0; i < route.intersectionA.routes.size(); i++) {
                Route routeB = route.intersectionA.routes.get(i);
                if(routeB != route && routeB.nom == route.nom){
                    tronçon.add(routeB);
                    boolean estAFaitB = routeB.intersectionA==route.intersectionA;
                    ajouterConnexionÀTronçon(routeB, estAFaitB, tronçon);
                    break;
                }
            }
        }
    }
}
