package org.Traffix.circulation;

import java.util.ArrayList;

/**
 * 
 * Classe de Navigateur temporaire, à remplacer
 * 
 */

public class Navigateur {
    private ArrayList<Route> itinéraire;
    private int indexRouteActuelle;
    
    public Navigateur() {
        this.itinéraire = new ArrayList<>();
        this.indexRouteActuelle = 0;
    }
    
    
    public void ajouterRoute(Route route) {
        itinéraire.add(route);
    }
    
   
    public boolean aProchainRoute() {
        return indexRouteActuelle < itinéraire.size() - 1;
    }
    
    
    public Route getProchainRoute() {
        if (aProchainRoute()) {
            indexRouteActuelle++;
            return itinéraire.get(indexRouteActuelle);
        }
        return null;
    }
    
    
    public Route getRouteActuelle() {
        if (itinéraire.isEmpty()) {
            return null;
        }
        return itinéraire.get(indexRouteActuelle);
    }
    
   
    public float getDistanceTotale() {
        float distanceTotale = 0;
        for (Route route : itinéraire) {
            distanceTotale += route.getLongueur();
        }
        return distanceTotale;
    }
    
    
    public float getTempsEstime(float vitesseMoyenne) {
        if (vitesseMoyenne <= 0) {
            return Float.POSITIVE_INFINITY;
        }
        // Distance en km / vitesse en km/h = temps en heures
        return getDistanceTotale() / 1000 / vitesseMoyenne;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Itinéraire: ");
        sb.append(itinéraire.size()).append(" routes, ");
        sb.append(String.format("%.1f", getDistanceTotale() / 1000)).append(" km au total\n");

        for (int i = 0; i < itinéraire.size(); i++) {
            Route route = itinéraire.get(i);
            sb.append(i + 1).append(". ");
            sb.append(route.getNom()).append(" (");
            sb.append(String.format("%.1f", route.getLongueur() / 1000)).append(" km)\n");
        }
        
        return sb.toString();
    }
}
