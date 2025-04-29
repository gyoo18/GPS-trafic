package org.Traffix.maths;

import org.Traffix.OpenGL.Caméra;

public class Maths {
    public static Vec3 intersectionPlan(Vec3 planPos, Vec3 planNorm, Vec3 rayonDir, Vec3 rayonOrigine){
        if (Vec3.scal(planNorm,rayonDir) == 0){
            return null;
        }
        float t = Vec3.scal(planNorm,Vec3.sous(planPos, rayonOrigine))/Vec3.scal(planNorm, rayonDir);
        return Vec3.mult(rayonDir, t).addi(rayonOrigine);
    }

    public static float distanceLignePoint(Vec2 ligneDir, Vec2 ligneOrigine, Vec2 pointPos){
        Vec2 dirPoint = Vec2.sous(pointPos,ligneOrigine).norm();
        float sin0 = Math.abs(ligneDir.x*dirPoint.y - ligneDir.y*dirPoint.x)/(ligneDir.longueur());
        return Vec2.distance(ligneOrigine, pointPos)*sin0;
    }

    public static float distanceLignePoint(Vec3 ligneDir, Vec3 ligneOrigine, Vec3 pointPos){
        Vec3 dirPoint = Vec3.sous(pointPos,ligneOrigine).norm();
        float sin0 = Vec3.croix(ligneDir, dirPoint).longueur()/(ligneDir.longueur());
        return Vec3.distance(ligneOrigine, pointPos)*sin0;
    }

    public static Vec2 pointPlusPrèsLignePoint(Vec2 ligneDir, Vec2 ligneOrigine, Vec2 point){
        Vec2 dirPoint = Vec2.sous(point,ligneOrigine).norm();
        float cos0 = Vec2.scal(dirPoint,Vec2.norm(ligneDir));
        float t = Vec2.distance(point, ligneOrigine)*cos0;
        return Vec2.mult(Vec2.norm(ligneDir),t).addi(ligneOrigine);
    }

    public static Vec2 pointPlusPrèsSegmentPoint(Vec2 posA, Vec2 posB, Vec2 point){
        Vec2 dirPoint = Vec2.sous(point,posA).norm();
        float cos0 = Vec2.scal(dirPoint,Vec2.sous(posB,posA).norm());
        float t = Vec2.distance(point, posA)*cos0;
        Vec2 pointPlusPrès = Vec2.mult(Vec2.sous(posB,posA).norm(),t).addi(posA);
        if(t < 0){
            return posA.copier();
        }
        if (t > Vec2.distance(posA, posB)){
            return posB;
        }
        return pointPlusPrès;
    }

    public static float distanceSegmentPoint(Vec2 posA, Vec2 posB, Vec2 point){
        return Vec2.distance( pointPlusPrèsLignePoint(Vec2.sous(posB, posA), posA, point), point );
    }

    /**
     * Génère un nombre aléatoire entre a et b
     * @param a borne inférieure
     * @param b borne supérieure
     * @return int entre a et b
     */
    public static int randint(int a, int b){
        return (int)(Math.random()*((float)b-(float)a+0.9) + (float)a);
    }

    /**
     * Trouve l'intersection entre deux lignes définies comme suit:
     * P = mt + b
     * où P, m et b sont des vecteurs et t ∊ ℝ
     * @param mA pente de la première ligne
     * @param bA origine de la première ligne
     * @param mB pente de la deuxième ligne
     * @param bB origine de la deuxième ligne
     * @return Vec2 la position de l'intersection ou `null` si les lignes sont parallèles
     */
    public static Vec2 intersectionLignes(Vec2 mA, Vec2 bA, Vec2 mB, Vec2 bB){
        // Si les deux lignes sont parallèles
        if(Math.abs(mA.x*mB.y-mA.y*mB.x) < 0.01f ){
            return null;
        }

        float t = (-mB.y*(bB.x-bA.x) + mB.x*(bB.y-bA.y))/(-mA.x*mB.y + mA.y*mB.x);
        return Vec2.mult(mA,t).addi(bA);
    }

    public static Vec3 curseurPosÀPointeur3D(Caméra caméra, float curseurX, float curseurY, float largeurÉcran, float hauteurÉcran){
        float xPosRel = curseurX/largeurÉcran; // Position du curseur, relative à la miniCarte
        float yPosRel = curseurY/hauteurÉcran;
        float ratio = largeurÉcran/hauteurÉcran;

        // Construction du vecteur qui pointe dans la direction du curseur
        // Direction vers laquelle pointe la caméra
        Vec3 camDir = Mat4.mulV(caméra.avoirVue().avoirRotMatInv(), new Vec3(0,0,1));
        // Transformation de la position relative du curseur sur un plan en 3D à planProche de
        // distance en face de la caméra
        float posX =  (float)Math.tan((Math.PI/180f)*caméra.FOV/2f)*(xPosRel*2f - 1f)*caméra.planProche;
        float posY = -(float)Math.tan((Math.PI/180f)*caméra.FOV/2f)*(yPosRel*2f - 1f)*caméra.planProche/ratio;
        // Création du vecteur normal qui pointe dans la direction du curseur
        Vec3 pointeurDir = new Vec3(posX,posY,caméra.planProche).norm();
        // Il faut maintenant orienter le vecteur dans l'orientation de la caméra avec une transformation matricielle
        Vec3 Z = camDir; // Vecteur Z de la caméra
        Vec3 X = new Vec3((float)Math.cos(caméra.avoirRot().y),0,(float)-Math.sin(caméra.avoirRot().y)); // Vecteur X de la caméra
        Vec3 Y = Vec3.croix(Z, X);  // Vecteur Y de la caméra
        Mat4 rotation = new Mat4(new float[]{
            X.x, X.y, X.z, 0,
            Y.x, Y.y, Y.z, 0,
            Z.x, Z.y, Z.z, 0,
            0,   0,   0,   1
        });     // Matrice de transformation de l'espace vue vers l'espace univers
        return Mat4.mulV(rotation, pointeurDir); // Multiplication matricielle
    }
}
