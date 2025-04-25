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
import org.Traffix.maths.Transformée;
import org.Traffix.maths.Vec3;
import org.Traffix.maths.Vec4;
import org.Traffix.utils.Chargeur;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        //Réseau réseau = UsineRéseau.générerRéseau();
        Maillage maillage = GénérateurMaillage.générerGrille(2, 2);
        Nuanceur nuanceur = null;
        try{
            nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
        }catch(Exception e){
            e.printStackTrace();
        }
        Objet plancher = new Objet("plancher", maillage, nuanceur, new Vec4(0.8f,0.7f,0.5f,1f), null, new Transformée(new Vec3(-3000,-1,-3000),new Vec3(0),new Vec3(6000)));
        Objet réseau = new Objet("réseau", GénérateurMaillage.faireMaillageRéseau(UsineRéseau.générerRéseau()), nuanceur, new Vec4(0.1f), null, new Transformée());

        Fenêtre fenêtre = UsineFenêtre.faireFenêtreGPS();
        GLCanvas carte = (GLCanvas)fenêtre.obtenirÉlémentParID("GLCarte");
        carte.scène.ajouterObjet(plancher);
        carte.scène.ajouterObjet(réseau);
        carte.scène.caméra.positionner(new Vec3(0,50,0));
        carte.scène.caméra.faireRotation(new Vec3((float)Math.toRadians(-45f),0,0));
        carte.scène.caméra.planProche = 0.01f;
        carte.scène.caméra.planLoin = 1000f;
        carte.scène.caméra.avoirVue().estOrbite = true;

        while(fenêtre.active){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
            carte.scène.caméra.tourner(new Vec3(0,(float)Math.toRadians(1f),0));
        }

        System.out.println("Goodbye World!");
    }
}
