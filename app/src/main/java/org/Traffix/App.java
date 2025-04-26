package org.Traffix;

import java.util.Scanner;

import org.Traffix.GUI.Fenêtre;
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

        Réseau réseau = UsineRéseau.générerRéseau();

        int nVides = 0;
        for (int i = 0; i < réseau.routes.size(); i++) {
            if(!réseau.routes.get(i).possèdeAdresses){
                nVides++;
            }
        }
        System.out.println((float)nVides/(float)réseau.routes.size());

        for (int i = 0; i < réseau.intersections.size(); i++) {
            if(réseau.intersections.get(i).avoirRoutes().size() == 0){
                throw new RuntimeException("Une intersection sans routes a été trouvée.");
            }

            for (int j = 0; j < réseau.intersections.get(i).avoirRoutes().size(); j++) {
                if (
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionA != réseau.intersections.get(i) &&
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionB != réseau.intersections.get(i)
                ){
                    throw new RuntimeException("Une intersection pointant vers une route qui ne pointe pas en retour a été trouvée");
                }
            }
        }

        for (int i = 0; i < réseau.routes.size(); i++) {
            if(réseau.routes.get(i).intersectionA == null && réseau.routes.get(i).intersectionB == null){
                throw new RuntimeException("Une route sans intersections a été trouvée.");
            }

            if(réseau.routes.get(i).avoirLongueur() == 0){
                throw new RuntimeException("Unr route de longueur 0 a été trouvée.");
            }

            if(
                !réseau.routes.get(i).intersectionA.avoirRoutes().contains(réseau.routes.get(i))||
                !réseau.routes.get(i).intersectionB.avoirRoutes().contains(réseau.routes.get(i))
            ){
                throw new RuntimeException("Une route qui pointe vers une intersection qui ne pointe pas en retour à été trouvée");
            }
        }

        AÉtoile.donnerRéseau(réseau);
        
        String[] adresses = new String[10000];
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
        }

        Véhicule[] v = new Véhicule[100];
        for (int i = 0; i < v.length; i++) {
            String[] routine = new String[5];
            for (int j = 0; j < routine.length;j++) {
                String adresse = adresses[Maths.randint(0, adresses.length-1)];
                for (int k = 0; k < j; k++) {
                    if(routine[k] == adresse){
                        adresse = "";
                    }
                }
                if(adresse == ""){
                    j--;
                    continue;
                }
                routine[j] = adresse;
            }
            while(v[i] == null){
                Route route = réseau.routes.get(Maths.randint(0, réseau.routes.size()-1));
                if(route.sensAPossèdePlace(1.2f) && route.possèdeAdresses){
                    v[i] = new Véhicule(4.2f,route);
                    v[i].routeActuelle.ajouterVéhiculeSensA(v[i]);
                    v[i].avoirNavigateur().donnerRoutine(adresses);
                }
            }
        }

        //Réseau réseau = UsineRéseau.générerRéseau();
        Maillage maillage = GénérateurMaillage.générerGrille(2, 2);
        Nuanceur nuanceur = null;
        try{
            nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
        }catch(Exception e){
            e.printStackTrace();
        }
        Objet plancher = new Objet("plancher", maillage, nuanceur, new Vec4(0.8f,0.7f,0.5f,1f), null, new Transformée(new Vec3(-3000,-1,-3000),new Vec3(0),new Vec3(6000)));
        Objet réseauObjet = new Objet("réseau", GénérateurMaillage.faireMaillageRéseau(réseau), nuanceur, new Vec4(0.1f), null, new Transformée());

        Fenêtre fenêtre = UsineFenêtre.faireFenêtreGPS();
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
        for (int i = 0; i < v.length; i++) {
            carte.scène.ajouterObjet(v[i].objetRendus);
        }
        Objet itinéraire = v[0].avoirNavigateur().avoirItinéraire();
        carte.scène.objets.add(itinéraire);

        long tempsA = System.currentTimeMillis();
        long tempsDebug = System.currentTimeMillis();
        while(fenêtre.active){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
            long deltaTempsMillis = System.currentTimeMillis()-tempsA;
            for (int i = 0; i < v.length; i++) {
                v[i].miseÀJour(2f*(float)(deltaTempsMillis)/1000f, i==0);
            }
            réseau.nettoyer();
            carte.scène.caméra.positionner(v[0].objetRendus.avoirTransformée().avoirPos());
            carte.scène.caméra.faireRotation( new Vec3((float)Math.toRadians(-45f), v[0].objetRendus.avoirTransformée().avoirRot().y+(float)Math.PI,0f));
            //carte.scène.caméra.tourner(new Vec3(0,(float)Math.toRadians(1f),0));
            tempsA = System.currentTimeMillis();
        }        

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
