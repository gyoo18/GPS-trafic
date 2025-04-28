package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.OpenGL.GénérateurMaillage;
import org.Traffix.OpenGL.Maillage;
import org.Traffix.OpenGL.Nuanceur;
import org.Traffix.OpenGL.Objet;
import org.Traffix.maths.Transformée;
import org.Traffix.maths.Vec2;
import org.Traffix.maths.Vec3;
import org.Traffix.maths.Vec4;
import org.Traffix.utils.Chargeur;

public class Véhicule {

    public final float longueur; // en mètres
    public float positionRelative = 0; // Position en % entre les deux intersections de la route actuelle.
    public float vitesse = 0;        // en m/s
    public float vitesseMoyenne = 0; // en m/s
    public boolean estSensA = false; // définit si le véhicule se trouve sur la voie A ou la voie B.
    public Route routeActuelle;
    private Navigateur navigateur;

    public Objet objetRendus = null;

    public final float ACCÉLÉRATION = 6f; // en m/s²

    private ArrayList<String> accèsRouteActuelle = new ArrayList<>();
    
    public Véhicule(float longueur) {
        this.longueur = longueur;
        this.navigateur = new Navigateur(this);  
        
        Maillage maillage = GénérateurMaillage.générerGrille(2, 2);
        Nuanceur nuanceur = null;
        try {
            nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
        } catch (Exception e) {
            e.printStackTrace();
        }
        objetRendus = new Objet("véhicule",maillage,nuanceur,new Vec4(0.9f,0.3f,0.3f,1f),null,new Transformée().échelonner(new Vec3(2f, 1f, longueur)));
    }
    
    public Véhicule(float longueur, Navigateur navigateur) {
        this(longueur);
        this.navigateur = navigateur;
    }
    
    public void avancer(float deltaTempsSecondes) {
        float distanceRelativeParcourue = (vitesse * deltaTempsSecondes)/routeActuelle.avoirLongueur();
        
        // Effectuer la collision avec le véhicule en avant
        Véhicule v = routeActuelle.avoirVéhiculeEnAvant(this);
        if(v != null){
            if(v.positionRelative - v.longueur/(2f*routeActuelle.avoirLongueur()) > distanceRelativeParcourue+positionRelative + longueur/(2f*routeActuelle.avoirLongueur()) + 2f/routeActuelle.avoirLongueur()){
                positionRelative += distanceRelativeParcourue;
            }else{
                positionRelative = v.positionRelative - v.longueur/(2f*routeActuelle.avoirLongueur()) - longueur/(2f*routeActuelle.avoirLongueur()) - 2f/routeActuelle.avoirLongueur();
                vitesse = v.vitesse;
            }
        }else{
            positionRelative += distanceRelativeParcourue;
        }

        positionRelative = Math.clamp(positionRelative, 0f, 1f);
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
            return Vec2.addi(Vec2.mult(routeActuelle.intersectionB.position, 1f-positionRelative), Vec2.mult(routeActuelle.intersectionA.position, positionRelative));
        }else{
            return Vec2.addi(Vec2.mult(routeActuelle.intersectionA.position, 1f-positionRelative), Vec2.mult(routeActuelle.intersectionB.position, positionRelative));
        }
    }

    public String avoirAdresse(){
        return routeActuelle.avoirAdresse(position())+" "+routeActuelle.nom;
    }

    public Vec2 avoirVitesseVec2(){
        if(estSensA){
            return Vec2.sous(routeActuelle.intersectionA.position,routeActuelle.intersectionB.position).norm().mult(vitesse);
        }else{
            return Vec2.sous(routeActuelle.intersectionB.position,routeActuelle.intersectionA.position).norm().mult(vitesse);
        }
    }
    
    // @Override
    // public String toString() {
    //     return "Véhicule{" +
    //             "longueur=" + longueur +
    //             " m, position=" + position() +
    //             " m, vitesse=" + vitesse +
    //             " km/h, route='" + routeActuelle.nom + '\'' +
    //             ", a un navigateur=" + (navigateur != null) +
    //             '}';
    // }

    public void miseÀJour(float deltaTempsSecondes, boolean debug){
        navigateur.miseÀJour(deltaTempsSecondes, debug);
        Vec2 dir = Vec2.sous(routeActuelle.intersectionA.position,routeActuelle.intersectionB.position).norm().mult(estSensA?-1f:1f);
        Transformée t = objetRendus.avoirTransformée();
        t.positionner(
            Vec3.addi( new Vec3(position().x,0.3f,position().y).mult(0.1f), t.avoirPos().mult(0.9f) ))
        .faireRotation(
            Vec3.addi(new Vec3((float)Math.toRadians(0),(float)Math.atan2(dir.x, dir.y),0).mult(0.1f),t.avoirRot().mult(0.9f))
        );
        vitesseMoyenne = (vitesse + vitesseMoyenne*59f)/60f;
    }
}