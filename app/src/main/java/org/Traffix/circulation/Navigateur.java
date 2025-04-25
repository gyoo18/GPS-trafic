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
    
    private long tempsDebug = System.currentTimeMillis();

    public Navigateur(Véhicule véhicule) {
        this.itinéraire = new ArrayList<>();
        this.indexRouteActuelle = 0;
        this.véhicule = véhicule;
    }

    public void miseÀJour(float deltaTempsSecondes, boolean debug){

        Intersection interB = véhicule.estSensA?véhicule.routeActuelle.intersectionA:véhicule.routeActuelle.intersectionB;

        if (prochainTournant == null){
            prochainTournant = interB.routes.get(Maths.randint(0, interB.routes.size()-1));
            if(debug){System.out.println("Choisit prochain tournant");}
        }

        Véhicule avant = véhicule.routeActuelle.avoirVéhiculeEnAvant(véhicule);
        float distanceInter = (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur();
        float accélération = 0f;

        // Si le véhicule est à l'intersection, passer l'intersection.
        if(distanceInter < 0.1f){
            // Si le véhicule est à l'intersection.
            if(
                interB.peutEngager(véhicule.routeActuelle, prochainTournant) && // S'il a le droit de s'engager dans l'intersection
                ((interB==prochainTournant.intersectionA && prochainTournant.sensBPossèdePlace(véhicule.longueur)) || // Si le prochain tournant a la place restante
                 (interB==prochainTournant.intersectionB && prochainTournant.sensAPossèdePlace(véhicule.longueur)))  // nécessaire pour que le véhicule s'engage.
            ){
                if(debug){System.out.println("Tourne dans l'intersection");}
                if(véhicule.estSensA){
                    véhicule.routeActuelle.retirerVéhiculeSensA();
                }else{
                    véhicule.routeActuelle.retirerVéhiculeSensB();
                }

                if(interB == prochainTournant.intersectionA){
                    prochainTournant.ajouterVéhiculeSensB(véhicule);
                }else{
                    prochainTournant.ajouterVéhiculeSensA(véhicule);
                }

                véhicule.estSensA = interB == prochainTournant.intersectionB;
                véhicule.positionRelative = 0;
                véhicule.routeActuelle = prochainTournant;
                prochainTournant = null;
            }
        }else if(
            Math.abs(véhicule.vitesse) > 0f &&
            distanceInter/véhicule.vitesse < 20f &&
            !interB.peutPasser(véhicule.routeActuelle, prochainTournant) &&
            avant == null
        ){
            // Si on est à moins de 20 secondes de passer l'intersection, qu'on est les premiers et qu'on ne peut pas simplement passer tout droit,
            // Ralentir pour s'arrêter à l'intersection.
            accélération -= (véhicule.vitesse*véhicule.vitesse)/(2f*distanceInter);
            if(debug){System.out.println("Ralentis pour s'arrêter à l'intersection");}
        }else if(avant != null && (avant.vitesse < véhicule.vitesse || véhicule.routeActuelle.avoirLimiteEffective() < véhicule.vitesse)){
            accélération -= (Math.min(avant.vitesse,véhicule.routeActuelle.avoirLimiteEffective())-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Ralentis à cause d'un véhicule ou de la limite de vitesse");}

        }else if(avant != null && avant.vitesse > véhicule.vitesse && véhicule.routeActuelle.avoirLimiteEffective() > véhicule.vitesse){
            accélération -= (Math.min(avant.vitesse,véhicule.routeActuelle.avoirLimiteEffective())-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Accélère à la vitesse d'un véhicule ou de la limite de vitesse");}

        }else if(avant == null && véhicule.routeActuelle.avoirLimiteEffective()+1f < véhicule.vitesse){
            accélération -= (véhicule.routeActuelle.avoirLimiteEffective()-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Ralentis à la limite de vitesse");}

        }else if(avant == null && véhicule.routeActuelle.avoirLimiteEffective()-1f > véhicule.vitesse){
            accélération += (véhicule.routeActuelle.avoirLimiteEffective()-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Accélère à la limite de vitesse");}

        }

        // Si on vas rentrer dans la voiture en avant.
        if (
            avant != null && 
            avant.positionRelative*véhicule.routeActuelle.avoirLongueur() - avant.longueur/2f - ESPACEMENT_VOITURES + avant.vitesse*deltaTempsSecondes < véhicule.positionRelative*véhicule.routeActuelle.avoirLongueur() + véhicule.longueur/2f + véhicule.vitesse*deltaTempsSecondes
        ){
            accélération -= véhicule.ACCÉLÉRATION;
            if(debug){System.out.println("Ralentis pour ne pas foncer dans la voiture en avant");}
        }

        véhicule.vitesse += Math.min(véhicule.ACCÉLÉRATION,Math.abs(accélération))*Math.signum(accélération)*deltaTempsSecondes;
        véhicule.avancer(deltaTempsSecondes);
        if(debug){System.out.println(deltaTempsSecondes);}
        if(debug && System.currentTimeMillis()-tempsDebug > 500){System.out.println("Position : "+véhicule.position().x+";"+véhicule.position().y+" Vitesse : "+véhicule.vitesse+" rue : "+véhicule.routeActuelle.nom);tempsDebug=System.currentTimeMillis();}
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
