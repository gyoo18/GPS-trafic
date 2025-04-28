package org.Traffix.circulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        Réseau réseau = créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis3() {
        System.out.println("====== ralentis3 =====");
        Réseau réseau = créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testRalentis(véhicule, réseau);
    }

    @Test
    public void ralentis4() {
        System.out.println("====== ralentis4 =====");
        Réseau réseau = créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
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
        Réseau réseau = créerIntersection2Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue3() {
        System.out.println("====== continue3 =====");
        Réseau réseau = créerIntersection3Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
        routeXnég.ajouterVéhiculeSensA(véhicule);
        
        testContinue(véhicule, réseau);
    }

    @Test
    public void continue4() {
        System.out.println("====== continue4 =====");
        Réseau réseau = créerIntersection4Routes();
        Route routeXnég = réseau.routes.get(0);
        Véhicule véhicule = nouveauVéhicule(réseau, new Vec2(100, 0));
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
        Véhicule v1 = nouveauVéhicule(réseau, new Vec2(100, 0));
        Véhicule v2 = nouveauVéhicule(réseau, new Vec2(100, 0));
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
        Réseau réseau = créerIntersection3Routes();

        testerConflits(réseau);
    }

    @Test
    public void continue4Conflit() {
        System.out.println("====== continue4Conflit =====");
        Réseau réseau = créerIntersection4Routes();

        testerConflits(réseau);
    }


    private void testerConflits(Réseau réseau){

        Route routeXnég = réseau.routes.get(0);
        Route routeXpos = réseau.routes.get(1);
        Route routeYnég = réseau.routes.get(2);
        Intersection inter = réseau.intersections.get(1);
        Véhicule v1 = nouveauVéhicule(réseau, new Vec2(100, 0));
        Véhicule v2 = nouveauVéhicule(réseau, new Vec2(100, 0));
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

    // ======== MÉTHODES PRIVÉES ========

    private Réseau créerIntersection2Routes() {
        Réseau réseau = new Réseau();
        AÉtoile.donnerRéseau(réseau);
        réseau.intersections.add(new IntersectionArrêt(new Vec2(-100, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(0, 0)));
        réseau.intersections.add(new IntersectionArrêt(new Vec2(100, 0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(0)));
        réseau.routes.add(new Route("rue A", 40, réseau.intersections.get(1), réseau.intersections.get(2)));
        réseau.construireTronçons();
        générerNumérosRues(réseau);
        réseau.véhicules = new Véhicule[0];
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

    private Véhicule nouveauVéhicule(Réseau réseau, Vec2 destination) {
        Véhicule véhicule = new Véhicule(4.2f);
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
                if(tronçon.get(0).intersectionA.routes.get(j) != tronçon.get(0) && tronçon.get(0).intersectionA.routes.get(j).nom.equals(tronçon.get(0).nom)){
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
