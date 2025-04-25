package org.Traffix;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.Traffix.circulation.Réseau;
import org.Traffix.circulation.UsineRéseau;
import org.Traffix.maths.Vec2;
import org.checkerframework.checker.units.qual.m;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Réseau réseau = UsineRéseau.générerRéseau();
        UsineRéseau.enregistrerRéseauEnOBJ(réseau);
        try {
            String[] adresses = new String[1000];
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
                System.out.println(adresses[i] + " dist : " + distances[i]);
            }

            float min = Float.MAX_VALUE;
            float max = -Float.MAX_VALUE;
            float moyenne = 0f;
            float variance = 0f;
            float écartType = 0f;
            for (int i = 0; i < distances.length; i++) {
                if(distances[i] < min){
                    min = distances[i];
                }
                if(distances[i] > max){
                    max = distances[i];
                }
                moyenne += distances[i];
            }
            moyenne = moyenne/(float)distances.length;
            for (int i = 0; i < distances.length; i++) {
                variance += (distances[i]-moyenne)*(distances[i]-moyenne);
            }
            variance = variance/distances.length;
            écartType = (float)Math.sqrt(variance);

            System.out.println("Stats : \n"+
            "min : "+min+", max : "+max+", moyenne : "+moyenne+"\n"+
            "variance : "+variance+", écart-type : "+écartType);

            System.out.println("Liste des abérations : ");
            for (int i = 0; i < abérations.size(); i++) {
                if(Math.abs(distances[i]-moyenne) > 2f*écartType){
                    System.out.println(i+"| adresse : "+adresses[i]+" distance : "+distances[i]+" écart : "+Math.abs(distances[i]-moyenne));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Goodbye World!");
    }
}
