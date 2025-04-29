package org.Traffix.circulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.Traffix.maths.Vec2;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntersectionArrêtTest {

    // Pour débuger les test
    public static void main(String[] args){
        IntersectionArrêtTest.init();
        IntersectionArrêtTest a = new IntersectionArrêtTest();
        a.ralentis2();
        a.ralentis3();
        a.ralentis4();
        a.continue2();
        a.continue3();
        a.continue4();
        a.continue2voitureDerrière();
        a.continue3voitureDerrière();
        a.continue4voitureDerrière();
        a.continue3Conflit();
        a.continue4Conflit();
    }

    @BeforeClass
    public static void init() {
        try {
            //System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("IntersectionArrêtTest.log"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======== TESTS RALENTIS ========
    // Teste si le véhicule ralentis pour s'arrêter avant d'entrer dans l'intersection pour un arrêt à 2, 3 et 4 voies.

    @Test
    public void ralentis2() {
        System.out.println("====== ralentis2 =====");
        Réseau réseau = Communs.créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis3() {
        System.out.println("====== ralentis3 =====");
        Réseau réseau = Communs.créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis4() {
        System.out.println("====== ralentis4 =====");
        Réseau réseau = Communs.créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testRalentis(véhicule, réseau);
    }

    private void testRalentis(Véhicule véhicule, Réseau réseau){
        // Avancer le véhicule jusqu'à l'intersection
        while (véhicule.positionRelative < 1f) {
            véhicule.miseÀJour(0.1f, true);
            réseau.miseÀJour(0.1f, false);
            assertFalse(réseau.intersections.get(1).peutPasser(réseau.routes.get(0), réseau.routes.get(1)));
        }
    }

    // ======== TESTS CONTINUE ========
    // Teste si le véhicule franchis correctement l'intersection en passant tout droite dans une intersection à 2, 3 et 4 voies

    @Test
    public void continue2() {
        System.out.println("====== continue2 =====");
        Réseau réseau = Communs.créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue3() {
        System.out.println("====== continue3 =====");
        Réseau réseau = Communs.créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue4() {
        System.out.println("====== continue4 =====");
        Réseau réseau = Communs.créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testContinue(véhicule, réseau);
    }

    private void testContinue(Véhicule véhicule, Réseau réseau){

        // Avancer le véhicule jusqu'à l'intersection
        while ((1f - véhicule.positionRelative) * véhicule.routeActuelle.avoirLongueur() >= 0.1f) {
            véhicule.miseÀJour(0.1f, true);
            réseau.miseÀJour(0.15f, false);
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
        Réseau réseau = Communs.créerIntersection2Routes();

        testerVéhiculeDerrière(réseau);
    }

    @Test
    public void continue3voitureDerrière() {
        System.out.println("====== continue3voitureDerrière =====");
        Réseau réseau = Communs.créerIntersection3Routes();

        testerVéhiculeDerrière(réseau);
    }

    @Test
    public void continue4voitureDerrière() {
        System.out.println("====== continue4voitureDerrière =====");
        Réseau réseau = Communs.créerIntersection4Routes();

        testerVéhiculeDerrière(réseau);
    }

    private void testerVéhiculeDerrière(Réseau réseau){

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Véhicule v1 = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        Véhicule v2 = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(v1);
        v1.positionRelative = 0.06f;
        routeXnég.ajouterVéhiculeSensA(v2);

        // Avancer les deux véhicules jusqu'à ce que v2 soit à l'intersection
        while ((1f - v2.positionRelative) * v2.routeActuelle.avoirLongueur() >= 0.1f) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
            réseau.miseÀJour(0.15f, false);
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
        Réseau réseau = Communs.créerIntersection3Routes();

        testerConflits(réseau);
    }

    @Test
    public void continue4Conflit() {
        System.out.println("====== continue4Conflit =====");
        Réseau réseau = Communs.créerIntersection4Routes();

        testerConflits(réseau);
    }


    private void testerConflits(Réseau réseau){

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Route routeYnég = réseau.routes.get(2);
        Intersection inter = réseau.intersections.get(1);
        Véhicule v1 = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        Véhicule v2 = Communs.nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(v1);
        routeYnég.ajouterVéhiculeSensA(v2);

        // Avancer les deux véhicules jusqu'à ce que v1 soit à l'intersection
        while ((1f - v1.positionRelative) * v1.routeActuelle.avoirLongueur() >= 0.1f) {
            v1.miseÀJour(0.1f, true);
            v2.miseÀJour(0.1f, true);
            réseau.miseÀJour(0.15f, false);
        }

        assertFalse(inter.peutEngager(routeXnég, routeXpos)); // v1 demande de passage refusée
        assertFalse(inter.peutEngager(routeYnég, routeXpos));  // v2 demande de passage refusée
        réseau.miseÀJour(0.1f, false);
        assertTrue(inter.peutEngager(routeXnég, routeXpos));   // v1 demande de passage accordée
        assertFalse(inter.peutEngager(routeYnég, routeXpos));  // v2 demande de passage refusée
        v1.miseÀJour(0.1f, true);    // v1 s'engage
        v2.miseÀJour(0.1f, true);    // v2 ne fait pas de demande, car la route est occupée
        réseau.miseÀJour(0.1f, false);
        assertEquals(v1.routeActuelle, routeXpos);  // v1 est passé

        // Laisser le temps à v1 de dégager la voie en face pour que v2 s'y engage
        while (!réseau.routes.get(1).sensBPossèdePlace(v1.longueur)) {
            v2.miseÀJour(0.1f, true);
            v1.miseÀJour(0.1f, true);
            réseau.miseÀJour(0.1f, false);
        }

        assertTrue(inter.peutEngager(routeYnég, routeXpos)); // v2 2ème demande accordée
        v1.miseÀJour(0.1f, true);
        v2.miseÀJour(0.1f, true);   // v2 s'engage
        réseau.miseÀJour(0.1f, false);
        assertEquals(v2.routeActuelle,routeXpos);   // v2 est passé
    }
}
