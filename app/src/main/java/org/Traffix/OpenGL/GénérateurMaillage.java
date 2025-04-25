package org.Traffix.OpenGL;

import java.util.Map;

import org.Traffix.OpenGL.Maillage.TypeDonnée;
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

    public static Maillage faireMaillageRéseau(Réseau réseau){
        float[] positions = new float[réseau.routes.size()*4*3];
        int[] indexes = new int[réseau.routes.size()*6]; 
        for (int i = 0; i < réseau.routes.size(); i++) {
            Vec2 posA = réseau.routes.get(i).intersectionA.position;
            Vec2 posB = réseau.routes.get(i).intersectionB.position;
            Vec2 tan = Vec2.sous(posA,posB).norm();
            Vec2 cotan = new Vec2(tan.y,-tan.x);
            float largeur = (float)réseau.routes.get(i).avoirLimiteKmH()/100f;

            positions[i*12 + 0] = posA.x + cotan.x*largeur;
            positions[i*12 + 1] = 0;
            positions[i*12 + 2] = posA.y + cotan.y*largeur;

            positions[i*12 + 3] = posB.x + cotan.x*largeur;
            positions[i*12 + 4] = 0;
            positions[i*12 + 5] = posB.y + cotan.y*largeur;

            positions[i*12 + 6] = posA.x - cotan.x*largeur;
            positions[i*12 + 7] = 0;
            positions[i*12 + 8] = posA.y - cotan.y*largeur;

            positions[i*12 + 9] = posB.x - cotan.x*largeur;
            positions[i*12 +10] = 0;
            positions[i*12 +11] = posB.y - cotan.y*largeur;

            indexes[i*6 + 0] = i*4 + 0;
            indexes[i*6 + 1] = i*4 + 1;
            indexes[i*6 + 2] = i*4 + 3;
            indexes[i*6 + 3] = i*4 + 3;
            indexes[i*6 + 4] = i*4 + 0;
            indexes[i*6 + 5] = i*4 + 2;
        }

        Maillage maillage = new Maillage(Map.of(TypeDonnée.FLOAT, 1), true);
        maillage.ajouterAttributListe(positions, 3);
        maillage.ajouterIndexesListe(indexes);
        return maillage;
    }
}
