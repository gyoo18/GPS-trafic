package org.Traffix;

import java.util.Scanner;

import org.Traffix.GUI.Fenêtre;
import org.Traffix.GUI.GestionnaireContrôles;
import org.Traffix.GUI.UsineFenêtre;
import org.Traffix.OpenGL.Caméra;
import org.Traffix.OpenGL.GLCanvas;
import org.Traffix.OpenGL.GénérateurMaillage;
import org.Traffix.OpenGL.Maillage;
import org.Traffix.OpenGL.Nuanceur;
import org.Traffix.OpenGL.Objet;
import org.Traffix.circulation.Réseau;
import org.Traffix.circulation.UsineRéseau;
import org.Traffix.circulation.AÉtoile;
import org.Traffix.circulation.Intersection;
import org.Traffix.circulation.IntersectionArrêt;
import org.Traffix.circulation.IntersectionLaissezPasser;
import org.Traffix.circulation.Navigateur;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Véhicule;
import org.Traffix.maths.Transformée;
import org.Traffix.maths.Vec3;
import org.Traffix.maths.Vec4;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;
import org.Traffix.utils.Chargeur;
import org.checkerframework.checker.units.qual.min;

import java.util.ArrayList;

import javax.swing.JFrame;

public class App {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        Fenêtre fenêtre = UsineFenêtre.faireFenêtreGPS();

        Réseau réseau;
        while (true) {
            try {
                réseau = UsineRéseau.générerRéseau();
                break;
            } catch (Exception e) {
                System.err.println("[ERREUR] échec de la génération du réseau, nous essaierons à nouveau...");
                e.printStackTrace();
            }
        }
        AÉtoile.donnerRéseau(réseau);
        GestionnaireContrôles.initialiserGPS(fenêtre,réseau);
        Véhicule joueur = réseau.véhicules[0];

        Maillage maillage = GénérateurMaillage.générerGrille(2, 2);
        Nuanceur nuanceur = null;
        Nuanceur nuaRéseau = null;
        try{
            nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
            nuaRéseau = Chargeur.chargerNuanceurFichier("nuaColoréPoints");
        }catch(Exception e){
            e.printStackTrace();
        }
        Objet plancher = new Objet("plancher", maillage, nuanceur, new Vec4(0.8f,0.7f,0.5f,1f), null, new Transformée(new Vec3(-6000,-0.1f,-6000),new Vec3(0),new Vec3(18000)));
        Objet réseauObjet = new Objet("réseau", GénérateurMaillage.faireMaillageRéseau(réseau,1f), nuaRéseau, null, null, new Transformée());

        GLCanvas carte = (GLCanvas)fenêtre.obtenirÉlémentParID("GLCarte");
        carte.bloquer();
        carte.scène.ajouterObjet(plancher);
        carte.scène.ajouterObjet(réseauObjet);
        carte.scène.caméra.positionner(new Vec3(0,20,0));
        carte.scène.caméra.faireRotation(new Vec3((float)Math.toRadians(-45f),-(float)Math.PI/2f,0));
        carte.scène.caméra.planProche = 0.1f;
        carte.scène.caméra.planLoin = 6000f;
        carte.scène.caméra.FOV = 110f;
        carte.scène.caméra.avoirVue().estOrbite = true;
        carte.scène.caméra.avoirVue().changerRayon(20);
        for (int i = 0; i < réseau.véhicules.length; i++) {
            carte.scène.ajouterObjet(réseau.véhicules[i].objetRendus);
        }
        Objet itinéraire = réseau.véhicules[0].avoirNavigateur().avoirItinéraire();
        carte.scène.ajouterObjet(itinéraire);
        carte.débloquer();
        carte.canvas.surFenêtreModifiée();

        Nuanceur nuanceur2 = nuanceur.copier();
        Nuanceur nuaRéseau2 = nuaRéseau.copier();
        GLCanvas miniCarte = (GLCanvas)fenêtre.obtenirÉlémentParID("GLCarte2");
        miniCarte.bloquer();
        Objet miniPlancher = new Objet("miniPlancher", maillage.copier(), nuanceur2, plancher.avoirCouleur().copier(), null, plancher.avoirTransformée().copier());
        miniCarte.scène.ajouterObjet(miniPlancher);
        Objet miniRéseau = new Objet("miniRéseau", GénérateurMaillage.faireMaillageRéseau(réseau, 3f), nuaRéseau2, null, null, new Transformée());
        miniCarte.scène.ajouterObjet(miniRéseau);
        miniCarte.scène.caméra.faireRotation(new Vec3((float)Math.toRadians(-90f),0,0));
        miniCarte.scène.caméra.planProche = 190f;
        miniCarte.scène.caméra.planLoin = 210f;
        // miniCarte.scène.caméra.FOV = 110f;
        miniCarte.scène.caméra.avoirVue().estOrbite = true;
        miniCarte.scène.caméra.avoirVue().changerRayon(200);
        for (int i = 0; i < réseau.véhicules.length; i++) {
            Objet miniVéhicule = réseau.véhicules[i].objetRendus.copierProfond();
            miniVéhicule.donnerNuanceur(nuanceur2);
            miniVéhicule.donnerTransformée(réseau.véhicules[i].objetRendus.avoirTransformée());
            miniCarte.scène.ajouterObjet(miniVéhicule);
        }
        miniCarte.scène.ajouterObjet(réseau.véhicules[0].avoirNavigateur().avoirMiniItinéraire());

        Objet pointeur = new Objet("pointeur", GénérateurMaillage.générerGrille(2, 2), nuanceur2, new Vec4(0,0,1f,1f), null, new Transformée().faireÉchelle(new Vec3(5f)));
        miniCarte.scène.ajouterObjet(pointeur);
        miniCarte.débloquer();
        miniCarte.canvas.surFenêtreModifiée();

        long tempsA = System.currentTimeMillis();
        Caméra caméra = carte.scène.caméra;
        Caméra miniCaméra = miniCarte.scène.caméra;
        while(fenêtre.active){
            long deltaTempsMillis = System.currentTimeMillis()-tempsA;
            tempsA = System.currentTimeMillis();
            réseau.miseÀJour(1f*(float)deltaTempsMillis/1000f, false);

            caméra.positionner(Vec3.addi( Vec3.mult(joueur.objetRendus.avoirTransformée().avoirPos(), 0.1f), caméra.avoirPos().mult(0.9f)));
            caméra.faireRotation(Vec3.addi( new Vec3((float)Math.toRadians(-45f), joueur.objetRendus.avoirTransformée().avoirRot().y+(float)Math.PI,0f).mult(0.1f), caméra.avoirRot().mult(0.9f)));

            miniCarte.scène.caméra.positionner(Vec3.addi(Vec3.mult( joueur.objetRendus.avoirTransformée().avoirPos(), 0.1f), miniCaméra.avoirPos().mult(0.9f)));
            miniCarte.scène.caméra.faireRotation( Vec3.addi(new Vec3((float)Math.toRadians(-90f), joueur.objetRendus.avoirTransformée().avoirRot().y+(float)Math.PI,0f).mult(0.1f),miniCarte.scène.caméra.avoirRot().mult(0.9f)));
        }

        fenêtre.fermer();
        System.out.println("Goodbye World!");
    }
}
