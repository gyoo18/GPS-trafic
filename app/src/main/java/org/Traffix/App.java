package org.Traffix;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.Traffix.circulation.AÉtoile;
import org.Traffix.circulation.Navigateur;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
import org.Traffix.circulation.UsineRéseau;
import org.Traffix.circulation.Véhicule;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;
import org.checkerframework.checker.units.qual.m;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Réseau réseau = UsineRéseau.générerRéseau();

        int nVides = 0;
        for (int i = 0; i < réseau.routes.size(); i++) {
            if(!réseau.routes.get(i).possèdeAdresses){
                nVides++;
            }
        }
        System.out.println((float)nVides/(float)réseau.routes.size());

        // UsineRéseau.enregistrerRéseauEnOBJ(réseau);

        for (int i = 0; i < réseau.intersections.size(); i++) {
            if(réseau.intersections.get(i).avoirRoutes().size() == 0){
                throw new RuntimeException("Une intersection sans routes a été trouvée.");
            }

            for (int j = 0; j < réseau.intersections.get(i).avoirRoutes().size(); j++) {
                if (
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionA != réseau.intersections.get(i) &&
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionB != réseau.intersections.get(i)
                ){
                    throw new RuntimeException("Une intersection pointant vers une route qui ne pointe pas en retour a été trouvée");
                }
            }
        }

        for (int i = 0; i < réseau.routes.size(); i++) {
            if(réseau.routes.get(i).intersectionA == null && réseau.routes.get(i).intersectionB == null){
                throw new RuntimeException("Une route sans intersections a été trouvée.");
            }

            if(
                !réseau.routes.get(i).intersectionA.avoirRoutes().contains(réseau.routes.get(i))||
                !réseau.routes.get(i).intersectionB.avoirRoutes().contains(réseau.routes.get(i))
            ){
                throw new RuntimeException("Une route qui pointe vers une intersection qui ne pointe pas en retour à été trouvée");
            }
        }

        AÉtoile.donnerRéseau(réseau);
        // System.out.println("Test de AÉtoile");
        // for (int i = 0; i < 1000; i++) {
        //     System.out.print(i+".");
        //     AÉtoile.chercherChemin(new Vec2((float)((Math.random() * 2.0 - 1.0)*3000.0),(float)((Math.random() * 2.0 - 1.0)*3000.0)), new Vec2((float)((Math.random() * 2.0 - 1.0)*3000.0),(float)((Math.random() * 2.0 - 1.0)*3000.0)));
        // }
        
        String[] adresses = new String[10000];
        Vec2[] positions = new Vec2[adresses.length];
        float[] distances = new float[adresses.length];
        ArrayList<Integer> abérations = new ArrayList<>();
        for (int i = 0; i < adresses.length; i++) {
            positions[i] = new Vec2( (float)(Math.random()*2.0-1.0)*3000f, (float)(Math.random()*2.0-1.0)*3000f);
            adresses[i] = réseau.avoirAdresse(positions[i]);
            if(adresses[i] == ""){
                continue;
            }
            Vec2 p = réseau.avoirPosition(adresses[i]);
            distances[i] = Vec2.distance(p, positions[i]);
            // System.out.println(adresses[i] + " dist : " + distances[i]);
        }

        // float min = Float.MAX_VALUE;
        // float max = -Float.MAX_VALUE;
        // float moyenne = 0f;
        // float variance = 0f;
        // float écartType = 0f;
        // for (int i = 0; i < distances.length; i++) {
        //     if(distances[i] < min){
        //         min = distances[i];
        //     }
        //     if(distances[i] > max){
        //         max = distances[i];
        //     }
        //     moyenne += distances[i];
        // }
        // moyenne = moyenne/(float)distances.length;
        // for (int i = 0; i < distances.length; i++) {
        //     variance += (distances[i]-moyenne)*(distances[i]-moyenne);
        // }
        // variance = variance/distances.length;
        // écartType = (float)Math.sqrt(variance);

        // System.out.println("Stats : \n"+
        // "min : "+min+", max : "+max+", moyenne : "+moyenne+"\n"+
        // "variance : "+variance+", écart-type : "+écartType);

        // System.out.println("Liste des abérations : ");
        // for (int i = 0; i < abérations.size(); i++) {
        //     if(Math.abs(distances[i]-moyenne) > 2f*écartType){
        //         System.out.println(i+"| adresse : "+adresses[i]+" distance : "+distances[i]+" écart : "+Math.abs(distances[i]-moyenne));
        //     }
        // }

        Véhicule[] v = new Véhicule[100];
        for (int i = 0; i < v.length; i++) {
            String[] routine = new String[5];
            for (int j = 0; j < routine.length;j++) {
                String adresse = adresses[Maths.randint(0, adresses.length-1)];
                for (int k = 0; k < j; k++) {
                    if(routine[k] == adresse){
                        adresse = "";
                    }
                }
                if(adresse == ""){
                    j--;
                    continue;
                }
                routine[j] = adresse;
            }
            while(v[i] == null){
                Route route = réseau.routes.get(Maths.randint(0, réseau.routes.size()-1));
                if(route.sensAPossèdePlace(1.2f) && route.possèdeAdresses){
                    v[i] = new Véhicule(1.2f,route);
                    v[i].routeActuelle.ajouterVéhiculeSensA(v[i]);
                    v[i].avoirNavigateur().donnerRoutine(adresses);
                }
            }
        }

        long tempsA = System.currentTimeMillis();
        long tempsDebug = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < v.length; i++) {
                v[i].miseÀJour(10f*(float)(System.currentTimeMillis()-tempsA)/1000f, i==0);
            }
            tempsA = System.currentTimeMillis();
        }

        //System.out.println("Goodbye World!");
    }
}
