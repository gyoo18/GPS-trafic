package org.Traffix.circulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;

public class Réseau {
    public ArrayList<Route> routes = new ArrayList<>();
    public ArrayList<Intersection> intersections = new ArrayList<>();
    public HashMap<String, ArrayList<Route>> tronçons = new HashMap<>(); // Liste de routes portants le même nom

    public void construireTronçons(){
        System.out.println("Construction des tronçons...");
        for (int i = 0; i < routes.size(); i++) {
            if(!tronçons.containsKey(routes.get(i).nom)){
                // Vérifier que cette route est un bout
                boolean estABout = true;
                for (int j = 0; j < routes.get(i).intersectionA.routes.size(); j++) {
                    if(routes.get(i).intersectionA.routes.get(j) != routes.get(i) && routes.get(i).intersectionA.routes.get(j).nom.equals(routes.get(i).nom)){
                        estABout = false;
                        break;
                    }
                }

                boolean estBBout = true;
                for (int j = 0; j < routes.get(i).intersectionB.routes.size(); j++) {
                    if(routes.get(i).intersectionB.routes.get(j) != routes.get(i) && routes.get(i).intersectionB.routes.get(j).nom.equals(routes.get(i).nom)){
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
                if(routeB != route && routeB.nom.equals(route.nom)){
                    tronçon.add(routeB);
                    boolean estAFaitB = routeB.intersectionA==route.intersectionB;
                    ajouterConnexionÀTronçon(routeB, estAFaitB, tronçon);
                    break;
                }
            }
        }else{
            for (int i = 0; i < route.intersectionA.routes.size(); i++) {
                Route routeB = route.intersectionA.routes.get(i);
                if(routeB != route && routeB.nom.equals(route.nom)){
                    tronçon.add(routeB);
                    boolean estAFaitB = routeB.intersectionA==route.intersectionA;
                    ajouterConnexionÀTronçon(routeB, estAFaitB, tronçon);
                    break;
                }
            }
        }
    }

    public String avoirAdresse(Vec2 position){
        ArrayList<Intersection> intersectionsVitiées = new ArrayList<>();

        while (true) {
            if(intersectionsVitiées.size() == intersections.size()){
                throw new RuntimeException("[ERREUR FATALE] Aucune route ne possède d'adresses.");
            }

            // Trouver l'intersection la plus proche
            float minDist = Float.MAX_VALUE;
            Intersection minDistInter = null;
            for (int i = 0; i < intersections.size(); i++) {
                if (Vec2.distance(position, intersections.get(i).position) < minDist && !intersectionsVitiées.contains(intersections.get(i))){
                    minDistInter = intersections.get(i);
                    minDist = Vec2.distance(position, minDistInter.position);
                }
            }

            intersectionsVitiées.add(minDistInter);

            // Trouver la position et l'adresse la plus proche parmi les routes de l'intersection
            minDist = Float.MAX_VALUE;
            String minDistAdresse = "";
            for (int i = 0; i < minDistInter.routes.size(); i++) {
                int adresse = minDistInter.routes.get(i).avoirAdresse(position);
                // Il se pourrait que la route n'aie pas d'adresse (elle est trop courte).
                if(adresse == -1){
                    continue;
                }
                Vec2 positionAdresse = minDistInter.routes.get(i).avoirPosition(adresse);

                if(Vec2.distance(position, positionAdresse) < minDist){
                    minDist = Vec2.distance(position, positionAdresse);
                    minDistAdresse = adresse + " " + minDistInter.routes.get(i).nom;
                }
            }

            if (minDistAdresse != ""){
                return minDistAdresse;
            }
        }
    }

    public Vec2 avoirPosition(String adresse){
        
        String[] composantes = adresse.split(" ");
        if(composantes.length > 3 || composantes.length < 2){
            System.err.println("[ERREUR] adresse mal formulée. Formulations acceptées : ## [Bd.|rue] <nom>, [Bd.|rue] <nom> ## et [Bd.|rue] <nom>");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return null;
        }

        boolean contientNuméro = composantes.length==3;
        int numéro = -1;
        String nom = "";
        if(contientNuméro){
            for (int i = 0; i < composantes.length; i++) {
                if (isInteger(composantes[i]) && i == 1){
                    System.err.println("[ERREUR] adresse mal formulée. Formulations acceptées : ## [Bd.|rue] <nom>, [Bd.|rue] <nom> ## et [Bd.|rue] <nom>");
                    for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                        System.err.println(s);
                    }
                    return null;
                }else if(isInteger(composantes[i]) && i != 1){
                    contientNuméro = true;
                    numéro = Integer.parseInt(composantes[i]);
                    if(i == 0){
                        nom = composantes[1].replace(".", "").toLowerCase()+" "+composantes[2].toLowerCase();
                    }else{
                        nom = composantes[0].replace(".", "").toLowerCase()+" "+composantes[1].toLowerCase();
                    }
                }
            }
        }else{
            nom = composantes[0].replace(".", "").toLowerCase()+" "+composantes[1].toLowerCase();
        }

        if(!tronçons.containsKey(nom)){
            System.out.println("Aucune route de ce nom : "+adresse);
            return null;
        }

        if(!contientNuméro){
            ArrayList<Route> tronçon = tronçons.get(nom);
            Route routeMilieu = tronçon.get(tronçon.size()/2);
            Vec2 posMilieu = Vec2.addi(routeMilieu.intersectionA.position, routeMilieu.intersectionB.position).mult(0.5f);
            numéro = routeMilieu.avoirAdresse(posMilieu);
            return routeMilieu.avoirPosition(numéro);
        } else {

            for (int i = 0; i < tronçons.get(nom).size(); i++) {
                Vec2 position = tronçons.get(nom).get(i).avoirPosition(numéro);
                if(position != null){
                    return position;
                }
            }
        }

        if(contientNuméro){
            System.out.println("Aucune adresse correspondante sur cette rue");
        }
        return null;
    }

    public void nettoyer(){
        for (int i = 0; i < routes.size(); i++) {
            for (int j = 0; j < routes.get(i).véhiculesSensA.size(); j++) {
                if(routes.get(i).véhiculesSensA.get(j).avoirNavigateur().estBrisé){
                    recyclerVéhicule(routes.get(i).véhiculesSensA.get(j));
                }
            }
            for (int j = 0; j < routes.get(i).véhiculesSensB.size(); j++) {
                if(routes.get(i).véhiculesSensB.get(j).avoirNavigateur().estBrisé){
                    recyclerVéhicule(routes.get(i).véhiculesSensB.get(j));
                }
            }
        }
    }

    private void recyclerVéhicule(Véhicule v){
        String[] routine = new String[5];
        for (int k = 0; k < routine.length; k++) {
            while(true){
                String adresse = avoirAdresse(new Vec2((float)(Math.random()*2.0 -1.0)*3000f,(float)(Math.random()*2.0 -1.0)*3000f));
                for (int k2 = 0; k2 < k; k2++) {
                    if(routine[k2] == adresse){
                        adresse = "";
                        break;
                    }
                }
                if(adresse.equals("")){
                    continue;
                }
                routine[k] = adresse;
                break;
            }
        }
        v.routeActuelle.retirerVéhiculeSensA(v);
        v.routeActuelle.retirerVéhiculeSensB(v);
        v.routeActuelle = routes.get(Maths.randint(0,routes.size()-1));
        v.avoirNavigateur().donnerRoutine(routine);
        v.avoirNavigateur().estBrisé = false;
    }

    // Merci à Jonas K sur StackOverFlow.
    // Source : https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
