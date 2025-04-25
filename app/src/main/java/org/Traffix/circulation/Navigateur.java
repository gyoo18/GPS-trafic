package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;

/**
 * 
 * Classe de Navigateur temporaire, à remplacer
 * 
 */

public class Navigateur {
    private ArrayList<Route> itinéraire;
    private int indexRouteActuelle;

    private Véhicule véhicule = null;
    private Route prochainTournant = null;

    private final float ESPACEMENT_VOITURES = 3f; // en mètres
    
    public Navigateur(Véhicule véhicule) {
        this.itinéraire = new ArrayList<>();
        this.indexRouteActuelle = 0;
        this.véhicule = véhicule;
    }

    public void miseÀJour(float deltaTempsSecondes){

        Intersection interB = véhicule.estSensA?véhicule.routeActuelle.intersectionA:véhicule.routeActuelle.intersectionB;

        if (prochainTournant == null){
            prochainTournant = interB.routes.get(Maths.randint(0, interB.routes.size()-1));
        }

        Véhicule avant = véhicule.routeActuelle.avoirVéhiculeEnAvant(véhicule);
        if(avant != null){
            if(avant.vitesse <= véhicule.vitesse){
                véhicule.vitesse -= véhicule.ACCÉLÉRATION*deltaTempsSecondes;
            }
        }else if(Math.abs(véhicule.vitesse) > 0f && (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse <= 20f){
            // est premier à l'intersection et passera dans les 20 prochaines secondes.
            if(!véhicule.estSensA?véhicule.routeActuelle.intersectionA.peutPasser(véhicule.routeActuelle, prochainTournant):véhicule.routeActuelle.intersectionA.peutPasser(véhicule.routeActuelle, prochainTournant)){
                // Si on ne peut pas passer
                véhicule.vitesse -= véhicule.ACCÉLÉRATION*deltaTempsSecondes;
            }else if()
        }else if(Math.abs(véhicule.vitesse) < 0.01f && interB.)


        véhicule.avancer(deltaTempsSecondes);
    }
    
    
    // public void ajouterRoute(Route route) {
    //     itinéraire.add(route);
    // }
    
   
    // public boolean aProchainRoute() {
    //     return indexRouteActuelle < itinéraire.size() - 1;
    // }
    
    
    // public Route getProchainRoute() {
    //     if (aProchainRoute()) {
    //         indexRouteActuelle++;
    //         return itinéraire.get(indexRouteActuelle);
    //     }
    //     return null;
    // }
    
    
    // public Route getRouteActuelle() {
    //     if (itinéraire.isEmpty()) {
    //         return null;
    //     }
    //     return itinéraire.get(indexRouteActuelle);
    // }
    
   
    // public float getDistanceTotale() {
    //     float distanceTotale = 0;
    //     for (Route route : itinéraire) {
    //         distanceTotale += route.avoirLongueur();
    //     }
    //     return distanceTotale;
    // }
    
    
    // public float getTempsEstime(float vitesseMoyenne) {
    //     if (vitesseMoyenne <= 0) {
    //         return Float.POSITIVE_INFINITY;
    //     }
    //     // Distance en km / vitesse en km/h = temps en heures
    //     return getDistanceTotale() / 1000 / vitesseMoyenne;
    // }
    
    // @Override
    // public String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("Itinéraire: ");
    //     sb.append(itinéraire.size()).append(" routes, ");
    //     sb.append(String.format("%.1f", getDistanceTotale() / 1000)).append(" km au total\n");

    //     for (int i = 0; i < itinéraire.size(); i++) {
    //         Route route = itinéraire.get(i);
    //         sb.append(i + 1).append(". ");
    //         sb.append(route.nom).append(" (");
    //         sb.append(String.format("%.1f", route.avoirLongueur() / 1000)).append(" km)\n");
    //     }
        
    //     return sb.toString();
    // }
}
