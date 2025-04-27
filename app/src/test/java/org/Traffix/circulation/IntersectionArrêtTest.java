package org.Traffix.circulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.Traffix.maths.Vec2;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntersectionArrêtTest {

    // Pour débuger les test
    public static void main(String[] args){
        IntersectionArrêetTest.init();
        IntersectionArrêetTest a = new IntersectionArrêetTest();
        a.continue3Conflit();
    }

    @BeforeClass
    public static void init(){
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("IntersectionArrêtTest.log"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ralentis2(){
        System.out.println("====== ralentis2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), false);
        }
    }

    @Test
    public void ralentis3(){
        System.out.println("====== ralentis3 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), false);
        }
    }

    @Test
    public void ralentis4(){
        System.out.println("====== ralentis4 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), false);
        }
    }

    @Test
    public void continue2(){
        System.out.println("====== continue2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue3(){
        System.out.println("====== continue3 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue4(){
        System.out.println("====== continue4 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue2voitureDerrière(){
        System.out.println("====== continue2voitureDerrière =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        véhicule.positionRelative = 0.05f;
        Véhicule véhicule2 = new Véhicule(4.2f, réseau.routes.get(0));
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(0));
        while (!réseau.routes.get(1).sensBPossèdePlace(véhicule.longueur)) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue3voitureDerrière(){
        System.out.println("====== continue3voitureDerrière =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        véhicule.positionRelative = 0.06f;
        Véhicule véhicule2 = new Véhicule(4.2f, réseau.routes.get(0));
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(0));
        while (!réseau.routes.get(1).sensBPossèdePlace(véhicule.longueur)) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue4voitureDerrière(){
        System.out.println("====== continue4voitureDerrière =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        véhicule.positionRelative = 0.05f;
        Véhicule véhicule2 = new Véhicule(4.2f, réseau.routes.get(0));
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(0));
        while (!réseau.routes.get(1).sensBPossèdePlace(véhicule.longueur)) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue3voitureCôté(){
        System.out.println("====== continue3voitureCôté =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        véhicule.positionRelative = 0.01f;
        Véhicule véhicule2 = new Véhicule(4.2f, réseau.routes.get(2));
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        while(!réseau.routes.get(1).sensBPossèdePlace(véhicule2.longueur)){
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue4voitureCôté(){
        System.out.println("====== continue4voitureCôté =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f, réseau.routes.get(0));
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        véhicule.positionRelative = 0.01f;
        Véhicule véhicule2 = new Véhicule(4.2f, réseau.routes.get(2));
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        while(!réseau.routes.get(1).sensBPossèdePlace(véhicule2.longueur)){
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    private void générerNumérosRues(Réseau réseau){
        float LARGEUR_MAISON = 15f;
        float ESPACEMENT_AVANT_MAISON = 5f;
        for (ArrayList<Route> tronçon : réseau.tronçons.values()) {
            // Aucun numéro de rue sur les autoroutes.
            if(tronçon.get(0).nom.contains("Autoroute")){
                continue;
            }

            // Trouver l'intersection au bout
            Intersection interA = null;
            for (int j = 0; j < tronçon.get(0).intersectionA.routes.size(); j++) {
                if(tronçon.get(0).intersectionA.routes.get(j) != tronçon.get(0) && tronçon.get(0).intersectionA.routes.get(j).nom == tronçon.get(0).nom){
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
class IntersectionArrêetTest {

    @BeforeClass
    public static void init() {
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("IntersectionArrêtTest.log"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======== TESTS RALENTIS ========
    // Teste si le véhicule ralentis pour s'arrêter avant d'entrer dans l'intersection pour un arrêt à 2, 3 et 4 voies.

    @Test
    public void ralentis2() {
        System.out.println("====== ralentis2 =====");
        Réseau réseau = créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis3() {
        System.out.println("====== ralentis3 =====");
        Réseau réseau = créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis4() {
        System.out.println("====== ralentis4 =====");
        Réseau réseau = créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testRalentis(véhicule, réseau);
    }

    private void testRalentis(Véhicule véhicule, Réseau réseau){
        // Avancer le véhicule jusqu'à l'intersection
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertFalse(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)));
    }

    // ======== TESTS CONTINUE ========
    // Teste si le véhicule franchis correctement l'intersection en passant tout droite dans une intersection à 2, 3 et 4 voies

    @Test
    public void continue2() {
        System.out.println("====== continue2 =====");
        Réseau réseau = créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue3() {
        System.out.println("====== continue3 =====");
        Réseau réseau = créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue4() {
        System.out.println("====== continue4 =====");
        Réseau réseau = créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        
        testContinue(véhicule, réseau);
    }

    private void testContinue(Véhicule véhicule, Réseau réseau){

        // Avancer le véhicule jusqu'à l'intersection
        while ((1f - véhicule.positionRelative) * véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        véhicule.miseÀJour(0.1f, true);     // Demander à s'engager
        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));  // La demande devrait être refusée et être acceptée au prochain tour
        véhicule.miseÀJour(0.1f, true);     // Demander à nouveau et s'engager
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    // ======== TESTS AVEC UN VÉHICULE DERRIÈRE L'AUTRE ========

    @Test
    public void continue2voitureDerrière() {
        System.out.println("====== continue2voitureDerrière =====");
        Réseau réseau = créerIntersection2Routes();

        testerVéhiculeDerrière(réseau);
    }

    @Test
    public void continue3voitureDerrière() {
        System.out.println("====== continue3voitureDerrière =====");
        Réseau réseau = créerIntersection3Routes();

        testerVéhiculeDerrière(réseau);
    }

    @Test
    public void continue4voitureDerrière() {
        System.out.println("====== continue4voitureDerrière =====");
        Réseau réseau = créerIntersection4Routes();

        testerVéhiculeDerrière(réseau);
    }

    private void testerVéhiculeDerrière(Réseau réseau){

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Véhicule v1 = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        Véhicule v2 = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        v1.positionRelative = 0.05f;

        // Avancer les deux véhicules jusqu'à ce que v2 soit à l'intersection
        while ((1f - v2.positionRelative) * v2.routeActuelle.avoirLongueur() >= 0.1f) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);   // v1 devrait être passé
        v2.miseÀJour(0.1f, true);   // v2 demande à passer
        assertEquals(v2.routeActuelle, routeXnég); // demande refusée pour l'instant

        // Laisser le temps à v1 de dégager la voie en face pour que v2 s'y engage
        while (!routeXpos.sensBPossèdePlace(v1.longueur)) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);
        v2.miseÀJour(0.1f, true);   // 2ème demande de passage
        assertEquals(v2.routeActuelle, routeXpos); // demande acceptée
    }

    // ======== TESTS AVEC UN CONFLIT DE PASSAGE ========

    @Test
    public void continue3Conflit() {
        System.out.println("====== continue3Conflit =====");
        Réseau réseau = créerIntersection3Routes();

        tester2Conflits(réseau);
    }

    @Test
    public void continue4Conflit1() {
        System.out.println("====== continue4Conflit =====");
        Réseau réseau = créerIntersection4Routes();

        tester2Conflits(réseau);
    }


    private void tester2Conflits(Réseau réseau){

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Route routeYnég = réseau.routes.get(2);
        Véhicule v1 = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        Véhicule v2 = nouveauVéhicule(réseau, routeYnég, new Vec2(100, 0));

        // Avancer les deux véhicules jusqu'à ce que v1 soit à l'intersection
        while ((1f - v1.positionRelative) * v1.routeActuelle.avoirLongueur() >= 0.1f) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);   // v1 demande à passer
        v2.miseÀJour(0.1f, true);   // v2 demande à passer
        assertEquals(v1.routeActuelle, routeXnég); // demande refusée pour l'instant
        assertEquals(v2.routeActuelle, routeYnég); // demande refusée pour l'instant
        v1.miseÀJour(0.1f, true);   // v1 2ème demande à passer acceptée
        v2.miseÀJour(0.1f, true);   // v2 2ème demande à passer refusée
        assertEquals(v1.routeActuelle, routeXpos); // v1 est passé
        assertEquals(v2.routeActuelle, routeYnég); // v2 n'est pas passé

        // Laisser le temps à v1 de dégager la voie en face pour que v2 s'y engage
        while (!réseau.routes.get(1).sensBPossèdePlace(v1.longueur)) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);
        v2.miseÀJour(0.1f, true);   // 2ème demande de passage
        assertEquals(réseau.routes.get(1), v2.routeActuelle); // demande acceptée
    }

    @Test
    public void continue4Conflit2() {
        System.out.println("====== continue4Conflit =====");
        Réseau réseau = créerIntersection4Routes();

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Route routeYnég = réseau.routes.get(2);
        Route routeYpos = réseau.routes.get(3);
        Véhicule v1 = nouveauVéhicule(réseau, routeXnég, new Vec2(100, 0));
        Véhicule v2 = nouveauVéhicule(réseau, routeYnég, new Vec2(100, 0));
        Véhicule v3 = nouveauVéhicule(réseau, routeYpos, new Vec2(100, 0));

        // Avancer les deux véhicules jusqu'à ce que v1 soit à l'intersection
        while ((1f - v1.positionRelative) * v1.routeActuelle.avoirLongueur() >= 0.1f) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
            v3.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);   // v1 demande à passer
        v2.miseÀJour(0.1f, true);   // v2 demande à passer
        v3.miseÀJour(0.1f, true);   // v3 demande à passer
        assertEquals(v1.routeActuelle, routeXnég); // demande refusée pour l'instant
        assertEquals(v2.routeActuelle, routeYnég); // demande refusée pour l'instant
        assertEquals(v3.routeActuelle, routeYnég); // demande refusée pour l'instant
        v1.miseÀJour(0.1f, true);   // v1 2ème demande à passer acceptée
        v2.miseÀJour(0.1f, true);   // v2 2ème demande à passer acceptée, mais route occupée
        v3.miseÀJour(0.1f, true);   // v3 2ème demande à passer acceptée, mais route occupée
        assertEquals(v1.routeActuelle, routeXpos); // v1 est passé
        assertEquals(v2.routeActuelle, routeYnég); // v2 n'est pas passé

        // Laisser le temps à v1 de dégager la voie en face pour que v2 s'y engage
        while (!réseau.routes.get(1).sensBPossèdePlace(v1.longueur)) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
        }

        v1.miseÀJour(0.1f, true);
        v2.miseÀJour(0.1f, true);   // 2ème demande de passage
        assertEquals(réseau.routes.get(1), v2.routeActuelle); // demande acceptée
    }

    // ======== MÉTHODES PRIVÉES ========

    private Réseau créerIntersection2Routes() {
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100, 0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(0), réseau.intersections.get(1)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        return réseau;
    }

    private Réseau créerIntersection3Routes() {
        Réseau réseau = créerIntersection2Routes();
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0, -100)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        return réseau;
    }

    private Réseau créerIntersection4Routes() {
        Réseau réseau = créerIntersection3Routes();
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0, 100)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        return réseau;
    }

    private Véhicule nouveauVéhicule(Réseau réseau, Route origine, Vec2 destination) {
        Véhicule véhicule = new Véhicule(4.2f, origine);
        String[] routine = { réseau.avoirAdresse(destination) };
        véhicule.avoirNavigateur().donnerRoutine(routine);
        return véhicule;
    }

    private void générerNumérosRues(Réseau réseau){
        float LARGEUR_MAISON = 15f;
        float ESPACEMENT_AVANT_MAISON = 5f;
        for (ArrayList<Route> tronçon : réseau.tronçons.values()) {
            // Aucun numéro de rue sur les autoroutes.
            if(tronçon.get(0).nom.contains("Autoroute")){
                continue;
            }

            // Trouver l'intersection au bout
            Intersection interA = null;
            for (int j = 0; j < tronçon.get(0).intersectionA.routes.size(); j++) {
                if(tronçon.get(0).intersectionA.routes.get(j) != tronçon.get(0) && tronçon.get(0).intersectionA.routes.get(j).nom == tronçon.get(0).nom){
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
