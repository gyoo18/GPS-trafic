package org.Traffix;

import java.util.Scanner;

import org.Traffix.GUI.Fenêtre;
import org.Traffix.GUI.GestionnaireContrôles;
import org.Traffix.GUI.UsineFenêtre;
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

        GLCanvas miniCarte = (GLCanvas)fenêtre.obtenirÉlémentParID("GLCarte2");
        Objet miniPlancher = plancher.copierProfond(); miniPlancher.donnerTransformée(plancher.avoirTransformée());
        miniCarte.scène.ajouterObjet(miniPlancher);
        Objet miniRéseau = réseauObjet.copierProfond();
        miniRéseau.donnerMaillage(GénérateurMaillage.faireMaillageRéseau(réseau, 3f));
        miniCarte.scène.ajouterObjet(miniRéseau);
        miniCarte.scène.caméra.faireRotation(new Vec3((float)Math.toRadians(-90f),0,0));
        miniCarte.scène.caméra.planProche = 1f;
        miniCarte.scène.caméra.planLoin = 1000f;
        // miniCarte.scène.caméra.FOV = 110f;
        miniCarte.scène.caméra.avoirVue().estOrbite = true;
        miniCarte.scène.caméra.avoirVue().changerRayon(200);
        for (int i = 0; i < réseau.véhicules.length; i++) {
            Objet miniVéhicule = réseau.véhicules[i].objetRendus.copierProfond(); miniVéhicule.donnerTransformée(réseau.véhicules[i].objetRendus.avoirTransformée());
            miniCarte.scène.ajouterObjet(miniVéhicule);
        }
        miniCarte.scène.ajouterObjet(réseau.véhicules[0].avoirNavigateur().avoirMiniItinéraire());

        long tempsA = System.currentTimeMillis();
        while(fenêtre.active){
            long deltaTempsMillis = System.currentTimeMillis()-tempsA;
            tempsA = System.currentTimeMillis();
            réseau.miseÀJour(0.1f*(float)deltaTempsMillis/1000f, false);
            carte.scène.caméra.positionner(réseau.véhicules[0].objetRendus.avoirTransformée().avoirPos());
            carte.scène.caméra.faireRotation( new Vec3((float)Math.toRadians(-45f), réseau.véhicules[0].objetRendus.avoirTransformée().avoirRot().y+(float)Math.PI,0f));
            miniCarte.scène.caméra.positionner(réseau.véhicules[0].objetRendus.avoirTransformée().avoirPos());
            miniCarte.scène.caméra.faireRotation( new Vec3((float)Math.toRadians(-90f), réseau.véhicules[0].objetRendus.avoirTransformée().avoirRot().y+(float)Math.PI,0f));
        }        

        fenêtre.fermer();
        System.out.println("Goodbye World!");
    }

    private static void générerNumérosRues(Réseau réseau){
        float LARGEUR_MAISON = 15f;
        float ESPACEMENT_AVANT_MAISON = 5f;
        for (ArrayList<Route> tronçon : réseau.tronçons.values()) {
            // Aucun numéro de rue sur les autoroutes.
            if(tronçon.get(0).nom.contains("Autoroute")){
                continue;
            }

            // Trouver l'intersection au bout
            Intersection interA = null;
            for (int j = 0; j < tronçon.get(0).intersectionA.avoirRoutes().size(); j++) {
                if(tronçon.get(0).intersectionA.avoirRoutes().get(j) != tronçon.get(0) && tronçon.get(0).intersectionA.avoirRoutes().get(j).nom == tronçon.get(0).nom){
                    interA = tronçon.get(0).intersectionB;
                }
            }
            if(interA == null){
                interA = tronçon.get(0).intersectionA;
            }

            int adressesCompte = 0;
            //Passer à travers toutes les routes du tronçon.
            for (int i = 0; i < tronçon.size(); i++) {
                Route route = tronçon.get(i);
                int nbAdresses = (int)Math.ceil(tronçon.get(i).avoirLongueur()/LARGEUR_MAISON);
                Vec2 tanAbs = Vec2.sous(route.intersectionA.position,route.intersectionB.position).norm().mult(interA==route.intersectionA?-1f:1f);
                Vec2 tanLoc = Vec2.sous(route.intersectionA.position,route.intersectionB.position).norm();
                Vec2 cotan = new Vec2(tanLoc.y,-tanLoc.x);

                int[] numérosSensA = new int[nbAdresses];
                int[] numérosSensB = new int[nbAdresses];
                Vec2[] positionsSensA = new Vec2[nbAdresses];
                Vec2[] positionsSensB = new Vec2[nbAdresses];
                for (int j = 0; j < nbAdresses; j++) {
                    Vec2 pos = Vec2.mult(tanAbs,LARGEUR_MAISON*(float)j).addi(interA.position);
                    if(interA == route.intersectionA){
                        numérosSensA[j] = 2*j+adressesCompte;
                        numérosSensB[j] = 2*j+1+adressesCompte;
                    }else{
                        numérosSensA[j] = (adressesCompte+nbAdresses*2)-(2*j);
                        numérosSensB[j] = (adressesCompte+nbAdresses*2)-(2*j+1);
                    }
                    positionsSensA[j] = Vec2.addi(pos,Vec2.mult(cotan,ESPACEMENT_AVANT_MAISON));
                    positionsSensB[j] = Vec2.sous(pos,Vec2.mult(cotan,ESPACEMENT_AVANT_MAISON));
                }
                route.donnerAdresses(numérosSensA, positionsSensA, numérosSensB, positionsSensB);
                adressesCompte += nbAdresses*2;
                interA = interA==route.intersectionA?route.intersectionB:route.intersectionA;
            }
        }
    }
}
