package org.Traffix.circulation;

import org.Traffix.maths.Vec2;

public class Véhicule {

    public final float longueur; // en mètres
    public float positionRelative = 0; // Position en % entre les deux intersections de la route actuelle.
    public float vitesse = 0; // en m/s
    public boolean estSensA = false; // définit si le véhicule se trouve sur la voie A ou la voie B.
    public Route routeActuelle;
    private Navigateur navigateur;

    public final float ACCÉLÉRATION = 6f; // en m/s²
    
    public Véhicule(float longueur, Route routeActuelle) {
        this.longueur = longueur;
        this.routeActuelle = routeActuelle;
        this.navigateur = new Navigateur(this);  
    }
    
    public Véhicule(float longueur, float position, float vitesse, Route routeActuelle, Navigateur navigateur) {
        this(longueur, routeActuelle);
        this.navigateur = navigateur;
    }
    
    public void avancer(float deltaTempsSecondes) {
        float distanceRelativeParcourue = (vitesse * deltaTempsSecondes)/routeActuelle.avoirLongueur();
        
        // Effectuer la collision avec le véhicule en avant
        Véhicule v = routeActuelle.avoirVéhiculeEnAvant(this);
        if(v != null){
            if(v.positionRelative - v.longueur/(2f*routeActuelle.avoirLongueur()) > distanceRelativeParcourue+positionRelative){
                positionRelative += distanceRelativeParcourue;
            }else{
                positionRelative = v.positionRelative - v.longueur/(2f*routeActuelle.avoirLongueur()) - longueur/(2f*routeActuelle.avoirLongueur());
                vitesse = v.vitesse;
            }
        }else{
            positionRelative += distanceRelativeParcourue;
        }

        if(positionRelative+distanceRelativeParcourue >= 1f){
            positionRelative = 1f;
        }
    }
    
    public float distance(Véhicule autreVéhicule) {
        if (this.routeActuelle == autreVéhicule.routeActuelle) {
            return Math.abs(this.positionRelative - autreVéhicule.positionRelative)*routeActuelle.avoirLongueur();
        } else {
            return -1;  // Pas sur la même route
        }
    }
    
    public void changerNavigateur(Navigateur navigateur) {
        this.navigateur = navigateur;
    }

    public Navigateur avoirNavigateur() {
        return navigateur;
    }

    public Vec2 position(){
        if(estSensA){
            return Vec2.addi(Vec2.mult(routeActuelle.intersectionB.position, positionRelative), Vec2.mult(routeActuelle.intersectionA.position, positionRelative));
        }else{
            return Vec2.addi(Vec2.mult(routeActuelle.intersectionA.position, positionRelative), Vec2.mult(routeActuelle.intersectionB.position, positionRelative));
        }
    }

    public Vec2 avoirVitesseVec2(){
        if(estSensA){
            return Vec2.sous(routeActuelle.intersectionA.position,routeActuelle.intersectionB.position).norm().mult(vitesse);
        }else{
            return Vec2.sous(routeActuelle.intersectionB.position,routeActuelle.intersectionA.position).norm().mult(vitesse);
        }
    }
    
    @Override
    public String toString() {
        return "Véhicule{" +
                "longueur=" + longueur +
                " m, position=" + position() +
                " m, vitesse=" + vitesse +
                " km/h, route='" + routeActuelle.nom + '\'' +
                ", a un navigateur=" + (navigateur != null) +
                '}';
    }

    public void miseÀJour(float deltaTempsSecondes, boolean debug){
        navigateur.miseÀJour(deltaTempsSecondes, debug);
    }
}
