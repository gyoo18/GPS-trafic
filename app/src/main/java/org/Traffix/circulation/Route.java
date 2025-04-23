package org.Traffix.circulation;

/**
 * 
 * Classe de Route temporaire, à remplacer
 * 
 */

public class Route {
    private String nom;
    private float longueur;  // en mètres
    private int nombreVoies;
    private int limiteVitesse;  // en km/h
    
    public Route(String nom, float longueur, int nombreVoies, int limiteVitesse) {
        this.nom = nom;
        this.longueur = longueur;
        this.nombreVoies = nombreVoies;
        this.limiteVitesse = limiteVitesse;
    }
    
    // Getters et setters
    public String getNom() {
        return nom;
    }
    
    public float getLongueur() {
        return longueur;
    }
    
    public int getNombreVoies() {
        return nombreVoies;
    }
    
    public int getLimiteVitesse() {
        return limiteVitesse;
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "nom='" + nom + '\'' +
                ", longueur=" + longueur +
                " m, voies=" + nombreVoies +
                ", limite=" + limiteVitesse +
                " km/h}";
    }
}
