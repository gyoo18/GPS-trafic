package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

public class AÉtoile {
    private static Réseau réseau = null;

    public static void donnerRéseau(Réseau réseau){
        AÉtoile.réseau = réseau;
    }

    public static Route[] chercherChemin(String adresseA, String adresseB){
        if(réseau == null){
            System.err.println("[ERREUR] le Réseau est null. Veuillez indiquer un réseau à utiliser.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }

        Vec2 posDép = réseau.avoirPosition(adresseA);
        Vec2 posDest = réseau.avoirPosition(adresseB);
        Route routeDép = extraireRoute(adresseA);
        Route routeDest = extraireRoute(adresseB);

        if(routeDép == routeDest){
            System.out.println("[ATTENTION] Le point de départ et le point d'arrivé sont les mêmes.");
            return null;
        }

        ArrayList<Route> noeudsActifs = new ArrayList<>();
        ArrayList<Float> poids = new ArrayList<>();
        ArrayList<Float> temps = new ArrayList<>();
        ArrayList<Route> nouedsPassifs = new ArrayList<>();
        ArrayList<ArrayList<Route>> chemins = new ArrayList<>();
        float meilleurPoid = Float.MAX_VALUE;
        int curseur = -1;
        noeudsActifs.add(routeDép);
        poids.add(Vec2.distance(posDép,posDest)/16.67f); // À une vitesse moyenne de 60km/h
        temps.add(0f);
        chemins.add(new ArrayList<>());
        
        while (true) {
            // Chercher le meilleur poid
            meilleurPoid = Float.MAX_VALUE;
            curseur = -1;
            for (int i = 0; i < noeudsActifs.size(); i++) {
                if(poids.get(i) < meilleurPoid){
                    meilleurPoid = poids.get(i);
                    curseur = i;
                }
            }

            if (curseur == -1){
                System.err.println("[ERREUR] aucun chemin n'existe entre les destinations");
                for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                    System.err.println(element);
                }
                return null;
            }

            // Ajouter ses connections + calculer leurs poids
            Route curRoute = noeudsActifs.get(curseur);
            for(int i = 0; i < 2; i ++){
                Intersection inter = null;
                switch (i) {
                    case 0: inter = curRoute.intersectionA;break;
                    case 1: inter = curRoute.intersectionB;break;
                }

                if(inter == null){
                    continue;
                }

                for (int j = 0; j < inter.routes.size(); j++) {
                    Route nRoute = inter.routes.get(j);
                    Intersection interB = inter==nRoute.intersectionA?nRoute.intersectionB:nRoute.intersectionA;
                    if(curRoute == nRoute || nouedsPassifs.contains(nRoute) || noeudsActifs.contains(nRoute)){
                        continue;
                    }

                    // Vérifier si la connexion est la destination
                    if(nRoute == routeDest){
                        chemins.get(curseur).add(nRoute);
                        Route[] cheminFinal = new Route[chemins.get(curseur).size()];
                        for (int k = 0; k < cheminFinal.length; k++) {
                            cheminFinal[k] = chemins.get(curseur).get(k);
                        }
                        return cheminFinal;
                    }

                    noeudsActifs.add(nRoute);
                    temps.add(temps.get(curseur)+(nRoute.avoirLongueur()/nRoute.avoirLimiteEffective()));
                    poids.add(temps.getLast()+Vec2.distance(interB.position,posDest));
                    chemins.add((ArrayList<Route>)chemins.get(curseur).clone());
                    chemins.getLast().add(nRoute);

                    for (int k = 0; k < chemins.getLast().size()-1; k++) {
                        if(!chemins.getLast().get(k).intersectionA.routes.contains(chemins.getLast().get(k+1)) && !chemins.getLast().get(k).intersectionB.routes.contains(chemins.getLast().get(k+1))){
                            throw new RuntimeException("Attention");
                        }
                    }
                }
            }

            // Retirer le curseur
            nouedsPassifs.add(noeudsActifs.remove(curseur));
            temps.remove(curseur);
            poids.remove(curseur);
            chemins.remove(curseur);
        }
    }

    public static Route[] chercherChemin(Vec2 posA, Vec2 posB){
        if(réseau == null){
            System.err.println("[ERREUR] le Réseau est null. Veuillez indiquer un réseau à utiliser.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }
        return chercherChemin(réseau.avoirAdresse(posA), réseau.avoirAdresse(posB));
    }

    public static Route[] chercherChemin(Vec2 posA, String adresseB){
        if(réseau == null){
            System.err.println("[ERREUR] le Réseau est null. Veuillez indiquer un réseau à utiliser.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }
        return chercherChemin(réseau.avoirAdresse(posA), adresseB);
    }

    public static Route[] chercherChemin(String adresseA, Vec2 posB){
        if(réseau == null){
            System.err.println("[ERREUR] le Réseau est null. Veuillez indiquer un réseau à utiliser.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }
        return chercherChemin(adresseA, réseau.avoirAdresse(posB));
    }

    private static Route extraireRoute(String adresse){

        String[] composantes = adresse.split(" ");
        if(composantes.length > 3 || composantes.length < 2){
            System.err.println("[ERREUR] adresse mal formulée. Formulations acceptées : ## [Bd.|rue] <nom> et [Bd.|rue] <nom> ##");
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
                    System.err.println("[ERREUR] adresse mal formulée. Formulations acceptées : ## [Bd.|rue] <nom> et [Bd.|rue] <nom> ##");
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
            System.err.println("[ERREUR] adresse mal formulée. Formulations acceptées : ## [Bd.|rue] <nom> et [Bd.|rue] <nom> ##");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return null;
        }

        if(!réseau.tronçons.containsKey(nom)){
            System.err.println("[ERREUR] l'adresse n'existe pas");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return null;
        }

        for (int i = 0; i < réseau.tronçons.get(nom).size(); i++) {
            Vec2 position = réseau.tronçons.get(nom).get(i).avoirPosition(numéro);
            if(position != null){
                return réseau.tronçons.get(nom).get(i);
            }
        }

        if(contientNuméro){
            System.err.println("[ERREUR] l'adresse n'existe pas");
            for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
                System.err.println(s);
            }
            return null;
        }

        // Devrait être impossible à atteindre.
        return null;
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
