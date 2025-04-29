package org.Traffix.circulation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntersectionLaissezPasserTest {

    public static void main(String[] args){
        IntersectionLaissezPasserTest.init();;
        IntersectionLaissezPasserTest a = new IntersectionLaissezPasserTest();
        a.passe2();
        a.passe3();
        a.passe4();
        a.passe3Côté();
        a.passe4Côté();
        a.continue2();
        a.continue3();
        a.continue4();
        a.passeConflitPrioritaire();
        a.passeConflitCéder();
    }

    @BeforeClass
    public static void init(){
        try {
            //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("IntersectionLaissezPasserTest.log"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void passe2(){
        System.out.println("====== passe2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), true);
        }
    }

    @Test
    public void passe3(){
        System.out.println("====== passe3 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), true);
        }
    }

    @Test
    public void passe3Côté(){
        System.out.println("====== passe3Côté =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(2), réseau.routes.get(1)), false);
        }
    }

    @Test
    public void passe4(){
        System.out.println("====== passe4 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)), true);
        }
    }

    @Test
    public void passe4Côté(){
        System.out.println("====== passe4Côté =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            assertEquals(réseau.intersections.get(1).peutPasser(réseau.routes.get(2), réseau.routes.get(1)), false);
        }
    }

    @Test
    public void continue2(){
        System.out.println("====== continue2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue3(){
        System.out.println("====== continue3 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void continue4(){
        System.out.println("====== continue4 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void tourneGauchePrioritaire(){
        System.out.println("====== tourneGauchePrioritaire =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(3));
    }

    @Test
    public void tourneDroitePrioritaire(){
        System.out.println("====== tourneDroitePrioritaire =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,-100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(2));
    }

    @Test
    public void tourneGaucheCéder(){
        System.out.println("====== tourneGaucheCéder =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(-100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
    }

    @Test
    public void tourneDroiteCéder(){
        System.out.println("====== tourneDroiteCéder =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void passeConflitPrioritaire(){
        System.out.println("====== passeConflitPrioritaire =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.01f, true);
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.01f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void passeConflitCéder(){
        System.out.println("====== passeConflitCéder =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
        }

        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(3));
    }

    @Test
    public void tourneGaucheConflitPrioritaire(){
        System.out.println("====== tourneGaucheConflitPrioritaire =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(0), réseau.routes.get(2)), true);
            }
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(3));
    }

    @Test
    public void tourneDroiteConflitPrioritaire(){
        System.out.println("====== tourneDroiteConflitPrioritaire =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,-100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(0), réseau.routes.get(2)), true);
            }
        }

        assertEquals(véhicule.routeActuelle, réseau.routes.get(0));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule.routeActuelle, réseau.routes.get(2));
    }

    @Test
    public void tourneGaucheConflitCéder1(){
        System.out.println("====== tourneGaucheConflitCéder1 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(-100,0))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f && véhicule.vitesse != 0f && (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse < 5f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(2), réseau.routes.get(0)), false);
            }
        }

        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(0));
    }

    @Test
    public void tourneGaucheConflitCéder2(){
        System.out.println("====== tourneGaucheConflitCéder2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(1).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,-100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(-100,0))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f && véhicule.vitesse != 0f && (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse < 5f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(2), réseau.routes.get(0)), false);
            }
        }

        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(0));
    }

    @Test
    public void tourneDroiteConflitCéder1(){
        System.out.println("====== tourneDroiteConflitCéder1 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(0).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule2.positionRelative)*véhicule2.routeActuelle.avoirLongueur() >= 0.1f && véhicule.vitesse != 0f && (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse < 5f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(2), réseau.routes.get(1)), false);
            }
        }

        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
        véhicule.miseÀJour(0.1f, true);
        véhicule2.miseÀJour(0.1f, true);
        assertEquals(véhicule2.routeActuelle, réseau.routes.get(1));
    }

    @Test
    public void tourneDroiteConflitCéder2(){
        System.out.println("====== tourneDroiteConflitCéder2 =====");
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(-100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(0,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(100,0)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,-100)));
        réseau.intersections.add(new IntersectionLaissezPasser(new Vec2(10,100)));

        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(3)));
        réseau.routes.add(new Route("rue B", 40, réseau.intersections.get(1), réseau.intersections.get(4)));

        réseau.construireTronçons();
        générerNumérosRues(réseau);

        Véhicule véhicule = new Véhicule(4.2f);
        réseau.routes.get(1).ajouterVéhiculeSensA(véhicule);
        String[] routine = new String[]{réseau.avoirAdresse(new Vec2(0,-100))};
        véhicule.avoirNavigateur().donnerRoutine(routine);
        Véhicule véhicule2 = new Véhicule(4.2f);
        réseau.routes.get(2).ajouterVéhiculeSensA(véhicule2);
        routine = new String[]{réseau.avoirAdresse(new Vec2(100,0))};
        véhicule2.avoirNavigateur().donnerRoutine(routine);
        
        while ((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            véhicule2.miseÀJour(0.1f, true);
            if((1f-véhicule2.positionRelative)*véhicule.routeActuelle.avoirLongueur() >= 0.1f && véhicule.vitesse != 0f && (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse < 5f){
                assertEquals(réseau.intersections.get(1).peutEngager(réseau.routes.get(2), réseau.routes.get(1)), true);
            }
        }

        assertEquals(véhicule2.routeActuelle, réseau.routes.get(2));
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
                Vec2 tan = Vec2.sous(route.intersectionA.position,route.intersectionB.position).norm().mult(interA==route.intersectionA?-1f:1f);
                Vec2 cotan = new Vec2(tan.y,-tan.x);

                int[] numérosSensA = new int[nbAdresses];
                int[] numérosSensB = new int[nbAdresses];
                Vec2[] positionsSensA = new Vec2[nbAdresses];
                Vec2[] positionsSensB = new Vec2[nbAdresses];
                for (int j = 0; j < nbAdresses; j++) {
                    Vec2 pos = Vec2.mult(tan,LARGEUR_MAISON*(float)j).addi(interA.position);
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
