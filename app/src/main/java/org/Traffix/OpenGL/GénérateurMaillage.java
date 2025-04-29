package org.Traffix.OpenGL;

import java.lang.reflect.Type;
import java.util.Map;

import org.Traffix.OpenGL.Maillage.TypeDonnée;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
import org.Traffix.maths.Vec2;

public class GénérateurMaillage {
    
    public static Maillage générerGrille(int nSomX, int nSomY){
        float[] positions = new float[nSomX*nSomY*3];
        float[] normales = new float[nSomX*nSomY*3];
        float[] uv = new float[nSomX*nSomY*2];
        int[] indexes = new int[(nSomX-1)*(nSomY-1)*6];

        for (int i = 0; i < positions.length/3; i++){
            positions[3*i+0] = (float)Math.floorMod(i, nSomX)/(float)(nSomX-1);
            positions[3*i+1] = 0;
            positions[3*i+2] = (float)Math.floor(i/nSomX)/(float)(nSomY-1);

            normales[3*i+0] = 0;
            normales[3*i+1] = 1;
            normales[3*i+2] = 0;
            
            uv[2*i+0] = positions[3*i+0];
            uv[2*i+1] = positions[3*i+2];
        }

        for (int y = 0; y < (nSomX-1); y++){
            for (int x = 0; x < (nSomY-1); x++){
                indexes[6*(x+y*(nSomX-1))+0] =  (x+y*nSomX);
                indexes[6*(x+y*(nSomX-1))+1] = ((x+y*nSomX)+1);
                indexes[6*(x+y*(nSomX-1))+2] = ((x+y*nSomX)+nSomX);

                indexes[6*(x+y*(nSomX-1))+3] = ((x+y*nSomX)+nSomX);
                indexes[6*(x+y*(nSomX-1))+4] = ((x+y*nSomX)+1);
                indexes[6*(x+y*(nSomX-1))+5] = ((x+y*nSomX)+1+nSomX);
            }
        }

        Maillage maillage = new Maillage(Map.of(Maillage.TypeDonnée.FLOAT,3), true);
        maillage.ajouterAttributListe(positions, 3);
        maillage.ajouterAttributListe(normales, 3);
        maillage.ajouterAttributListe(uv, 2);
        maillage.ajouterIndexesListe(indexes);
        return maillage;
    }

    public static Maillage générerDisque(int résolution){
        float[] positions = new float[(résolution+1)*3];
        float[] normales = new float[(résolution+1)*3];
        int[] indexes = new int[résolution*3];

        positions[0] = 0;
        positions[1] = 0;
        positions[2] = 0;

        normales[0] = 0;
        normales[1] = 1;
        normales[2] = 0;

        for (int i = 0; i < résolution; i++) {
            positions[(i+1)*3 + 0] = (float)Math.cos(Math.PI*2.0*((double)i+1)/(double)résolution);
            positions[(i+1)*3 + 1] = 0;
            positions[(i+1)*3 + 2] = (float)Math.sin(Math.PI*2.0*((double)i+1)/(double)résolution);

            normales[(i+1)*3 + 0] = 0;
            normales[(i+1)*3 + 1] = 1;
            normales[(i+1)*3 + 2] = 0;

            indexes[i*3 + 0] = 0;
            indexes[i*3 + 1] = i+1;
            indexes[i*3 + 2] = i+2;
        }

        indexes[résolution*3-1] = 1;

        Maillage m = new Maillage(Map.of(TypeDonnée.FLOAT, 2), true);
        m.ajouterAttributListe(positions, 3);
        m.ajouterAttributListe(normales, 3);
        m.ajouterIndexesListe(indexes);
        return m;
    }

    public static Maillage faireMaillageRéseau(Réseau réseau, float épaisseur){
        float[] positions = new float[réseau.routes.size()*4*3];
        float[] normales = new float[réseau.routes.size()*4*3];
        float[] couleurs = new float[réseau.routes.size()*4*4];
        int[] indexes = new int[réseau.routes.size()*6]; 
        for (int i = 0; i < réseau.routes.size(); i++) {
            Vec2 posA = réseau.routes.get(i).intersectionA.position;
            Vec2 posB = réseau.routes.get(i).intersectionB.position;
            Vec2 tan = Vec2.sous(posA,posB).norm();
            Vec2 cotan = new Vec2(tan.y,-tan.x);
            int vitesse = réseau.routes.get(i).avoirLimiteKmH();
            float largeur = épaisseur*(float)vitesse/100f;

            positions[i*12 + 0] = posA.x + cotan.x*largeur;
            positions[i*12 + 1] = 0;
            positions[i*12 + 2] = posA.y + cotan.y*largeur;

            normales[i*12 + 0] = 0f;
            normales[i*12 + 1] = 1f;
            normales[i*12 + 2] = 1f;

            couleurs[i*16 + 0] = vitesse<50?0.8f:(vitesse<80?0.95f:0.95f);
            couleurs[i*16 + 1] = vitesse<50?0.8f:(vitesse<80?0.90f:0.70f);
            couleurs[i*16 + 2] = vitesse<50?0.8f:(vitesse<80?0.30f:0.30f);
            couleurs[i*16 + 3] = vitesse<50?1.0f:(vitesse<80?1.00f:1.00f);

            positions[i*12 + 3] = posB.x + cotan.x*largeur;
            positions[i*12 + 4] = 0;
            positions[i*12 + 5] = posB.y + cotan.y*largeur;

            normales[i*12 + 3] = 0f;
            normales[i*12 + 4] = 1f;
            normales[i*12 + 5] = 1f;

            couleurs[i*16 + 4] = vitesse<50?0.8f:(vitesse<80?0.95f:0.95f);
            couleurs[i*16 + 5] = vitesse<50?0.8f:(vitesse<80?0.90f:0.70f);
            couleurs[i*16 + 6] = vitesse<50?0.8f:(vitesse<80?0.30f:0.30f);
            couleurs[i*16 + 7] = vitesse<50?1.0f:(vitesse<80?1.00f:1.00f);

            positions[i*12 + 6] = posA.x - cotan.x*largeur;
            positions[i*12 + 7] = 0;
            positions[i*12 + 8] = posA.y - cotan.y*largeur;

            normales[i*12 + 6] = 0f;
            normales[i*12 + 7] = 1f;
            normales[i*12 + 8] = 1f;

            couleurs[i*16 + 8] = vitesse<50?0.8f:(vitesse<80?0.95f:0.95f);
            couleurs[i*16 + 9] = vitesse<50?0.8f:(vitesse<80?0.90f:0.70f);
            couleurs[i*16 +10] = vitesse<50?0.8f:(vitesse<80?0.30f:0.30f);
            couleurs[i*16 +11] = vitesse<50?1.0f:(vitesse<80?1.00f:1.00f);

            positions[i*12 + 9] = posB.x - cotan.x*largeur;
            positions[i*12 +10] = 0;
            positions[i*12 +11] = posB.y - cotan.y*largeur;

            normales[i*12 + 9] = 0f;
            normales[i*12 +10] = 1f;
            normales[i*12 +11] = 1f;

            couleurs[i*16 +12] = vitesse<50?0.8f:(vitesse<80?0.95f:0.95f);
            couleurs[i*16 +13] = vitesse<50?0.8f:(vitesse<80?0.90f:0.70f);
            couleurs[i*16 +14] = vitesse<50?0.8f:(vitesse<80?0.30f:0.30f);
            couleurs[i*16 +15] = vitesse<50?1.0f:(vitesse<80?1.00f:1.00f);

            indexes[i*6 + 0] = i*4 + 0;
            indexes[i*6 + 1] = i*4 + 1;
            indexes[i*6 + 2] = i*4 + 3;
            indexes[i*6 + 3] = i*4 + 3;
            indexes[i*6 + 4] = i*4 + 0;
            indexes[i*6 + 5] = i*4 + 2;
        }

        Maillage maillage = new Maillage(Map.of(TypeDonnée.FLOAT, 3), true);
        maillage.ajouterAttributListe(positions, 3);
        maillage.ajouterAttributListe(normales, 3);
        maillage.ajouterAttributListe(couleurs, 4);
        maillage.ajouterIndexesListe(indexes);
        return maillage;
    }

    public static Maillage faireMaillageItinéraire(Route[] itinéraire,float épaisseur){
        float[] positions = new float[itinéraire.length*4*3];
        float[] normales = new float[itinéraire.length*4*3];
        int[] indexes = new int[itinéraire.length*6]; 
        for (int i = 0; i < itinéraire.length; i++) {
            Vec2 posA = itinéraire[i].intersectionA.position;
            Vec2 posB = itinéraire[i].intersectionB.position;
            Vec2 tan = Vec2.sous(posA,posB).norm();
            Vec2 cotan = new Vec2(tan.y,-tan.x);
            float largeur = épaisseur*(float)itinéraire[i].avoirLimiteKmH()/100f;

            positions[i*12 + 0] = posA.x + cotan.x*largeur;
            positions[i*12 + 1] = 0;
            positions[i*12 + 2] = posA.y + cotan.y*largeur;

            normales[i*12 + 0] = 0;
            normales[i*12 + 1] = 1;
            normales[i*12 + 2] = 0;

            positions[i*12 + 3] = posB.x + cotan.x*largeur;
            positions[i*12 + 4] = 0;
            positions[i*12 + 5] = posB.y + cotan.y*largeur;

            normales[i*12 + 3] = 0;
            normales[i*12 + 4] = 1;
            normales[i*12 + 5] = 0;

            positions[i*12 + 6] = posA.x - cotan.x*largeur;
            positions[i*12 + 7] = 0;
            positions[i*12 + 8] = posA.y - cotan.y*largeur;

            normales[i*12 + 6] = 0;
            normales[i*12 + 7] = 1;
            normales[i*12 + 8] = 0;

            positions[i*12 + 9] = posB.x - cotan.x*largeur;
            positions[i*12 +10] = 0;
            positions[i*12 +11] = posB.y - cotan.y*largeur;

            normales[i*12 + 9] = 0;
            normales[i*12 +10] = 1;
            normales[i*12 +11] = 0;

            indexes[i*6 + 0] = i*4 + 0;
            indexes[i*6 + 1] = i*4 + 1;
            indexes[i*6 + 2] = i*4 + 3;
            indexes[i*6 + 3] = i*4 + 3;
            indexes[i*6 + 4] = i*4 + 0;
            indexes[i*6 + 5] = i*4 + 2;
        }

        Maillage maillage = new Maillage(Map.of(TypeDonnée.FLOAT, 2), true);
        maillage.ajouterAttributListe(positions, 3);
        maillage.ajouterAttributListe(normales, 3);
        maillage.ajouterIndexesListe(indexes);
        return maillage;
    }
}
