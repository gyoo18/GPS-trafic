package org.Traffix.circulation;

import org.Traffix.maths.Vec2;

public abstract class Véhicule {

    private float longueur;
    private float position;
    private float vitesse;
    private Route routeActuelle;
    private Navigateur navigateur;
    
    public Véhicule(float longueur, float position, float vitesse, Route routeActuelle) {
        this.longueur = longueur;
        this.position = position;
        this.vitesse = vitesse;
        this.routeActuelle = routeActuelle;
        this.navigateur = null;  
    }
    
    public Véhicule(float longueur, float position, float vitesse, Route routeActuelle, Navigateur navigateur) {
        this(longueur, position, vitesse, routeActuelle);
        this.navigateur = navigateur;
    }
    
    public void avancer(float tempsEnSecondes) {
        // Conversion de km/h en m/s puis calcul de la distance parcourue
        float distanceParcourue = (vitesse * 1000 / 3600) * tempsEnSecondes;
        position += distanceParcourue;
        
        // Vérification si le véhicule a atteint la fin de la route
        if (position > routeActuelle.avoirLongueur()) {
            if (navigateur != null && navigateur.aProchainRoute()) {
                float dépassement = position - routeActuelle.avoirLongueur();
                routeActuelle = navigateur.getProchainRoute();
                position = dépassement;  
                System.out.println("Véhicule a changé de route vers: " + routeActuelle.nom);
            } else {
                position = routeActuelle.avoirLongueur();  // Reste à la fin de la route
                vitesse = 0;  // S'arrête
                System.out.println("Véhicule arrivé à destination");
            }
        }
    }
    
    public void changerVitesse(float nouvelleVitesse) {
        if (nouvelleVitesse >= 0) {
            this.vitesse = nouvelleVitesse;
        } else {
            System.out.println("Erreur: la vitesse ne peut pas être négative");
        }
    }
    
    public void setNavigateur(Navigateur navigateur) {
        this.navigateur = navigateur;
    }
    
    public float distance(Véhicule autreVéhicule) {
        if (this.routeActuelle.equals(autreVéhicule.routeActuelle)) {
            return Math.abs(this.position - autreVéhicule.position);
        } else {
            return -1;  // Pas sur la même route
        }
    }
    
    //### Getters et setters
    public float getLongueur() {
        return longueur;
    }
    
    public void setLongueur(float longueur) {
        if (longueur > 0) {
            this.longueur = longueur;
        }
    }
    
    public float getPosition() {
        return position;
    }

    public float avoirPositionRelative(){
        throw new UnsupportedOperationException();
    }
    
    public void setPosition(float position) {
        if (position >= 0 && position <= routeActuelle.avoirLongueur()) {
            this.position = position;
        }
    }
    
    public float getVitesse() {
        return vitesse;
    }
    
    public Route getRouteActuelle() {
        return routeActuelle;
    }
    
    public void setRouteActuelle(Route routeActuelle) {
        this.routeActuelle = routeActuelle;
        this.position = 0;  // Remet la position à 0 sur la nouvelle route
    }
    
    public Navigateur getNavigateur() {
        return navigateur;
    }
    
    @Override
    public String toString() {
        return "Véhicule{" +
                "longueur=" + longueur +
                " m, position=" + position +
                " m, vitesse=" + vitesse +
                " km/h, route='" + routeActuelle.nom + '\'' +
                ", a un navigateur=" + (navigateur != null) +
                '}';
    }
}
