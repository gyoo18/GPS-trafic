package org.Traffix.maths;

public class Maths {
    public static Vec3 intersectionPlan(Vec3 planPos, Vec3 planNorm, Vec3 rayonDir, Vec3 rayonOrigine){
        if (Vec3.scal(planNorm,rayonDir) == 0){
            return null;
        }
        float t = Vec3.scal(planNorm,Vec3.sous(planPos, rayonOrigine))/Vec3.scal(planNorm, rayonDir);
        return Vec3.mult(rayonDir, t).addi(rayonOrigine);
    }

    /**
     * Génère un nombre aléatoire entre a et b
     * @param a borne inférieure
     * @param b borne supérieure
     * @return int entre a et b
     */
    public static int randint(int a, int b){
        return (int)(Math.random()*(a-b) + b);
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
        if(Math.abs(Vec2.scal(mA,mB)) < 0.0001f ){
            return null;
        }

        float t = (-mB.y*(bB.x-bA.x) + mB.x*(bB.y-bA.y))/(-mA.x*mB.y + mA.y*mB.x);
        return Vec2.mult(mA,t).addi(bA);
    }
}
