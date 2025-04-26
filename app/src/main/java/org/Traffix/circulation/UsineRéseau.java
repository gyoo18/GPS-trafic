package org.Traffix.circulation;

import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore.Entry;
import java.util.ArrayList;

public class UsineRéseau {

    private static final int MAX_ITÉRATIONS = 30;
    private static final int NOMBRE_VÉHICULES = 3000;

    // En mètres
    private static final float LARGEUR_MAISON = 15f;
    private static final float ESPACEMENT_AVANT_MAISON = 5f;

    private static final float RAYON_INITIAL = 100f;
    private static final float RAYON_VILLE = 3000f;
    private static final float DISTANCE_POUSSE_MIN = LARGEUR_MAISON+1f;
    private static final float DISTANCE_POUSSE_MAX = 200f;

    private static Réseau réseau;

    // Le résultat de avoirIntersections
    private static Vec2 intersectionRoutes = null;
    private static Route routeIntersecté = null;

    private static String[] boutsNoms = new String[]{"an", "be", "ca", "de", "el", "fi", "ga", "he", "il", "jo", "la", "ma", "no", "on", "pi", "ra", "so", "ti", "va", "yu", "ana","ben","cam","dia","eli","fay","gal","hen","isa","jol","lia","mar","nol","oli","pal","ren","sol","tia","val","yva"};
    private static ArrayList<String> nomsGénérés = new ArrayList<>();

    /**
     * Génère un réseau routier de manière procédurale par un modèle de pousse en arbre. On commence par faire pousser des autoroutes,
     * desquels pousseront des boulevards, desquels pousseront des routes.
     * @return Réseau généré
     */
    public static Réseau générerRéseau(){
        boolean succèsGénération = false;
        while(!succèsGénération){
            System.out.println("Génération du réseau routier...");
            réseau = new Réseau();
            ArrayList<Intersection> intersectionsActives = new ArrayList<>();

            System.out.println("Génération des autoroutes...");
            // Phase 1 génération des autoroutes principales
            for (int i = 0; i < Maths.randint(1,10); i++) {
                float angle = (float)(Math.random()*2*Math.PI);
                float rayon = (float)(Math.random()*RAYON_INITIAL);
                Intersection interA = new IntersectionLaissezPasser( new Vec2( (float)(Math.cos(angle)*rayon), (float)(Math.sin(angle)*rayon) ) );
                réseau.intersections.add( interA );

                float anglePousse = (float)(Math.random()*2*Math.PI);
                float distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
                fairePousserRoute(new Vec2((float)Math.cos(anglePousse), (float)Math.sin(anglePousse)), distancePousse, false, "autoroute", 100, interA, intersectionsActives);
            }

            // Phase 1 partie 2 faire pousser les autoroutes
            for (int i = 0; i < MAX_ITÉRATIONS; i++) {
                if (intersectionsActives.size() == 0){
                    break;
                }

                for (int j = intersectionsActives.size() - 1; j >= 0; j--) {
                    Intersection interA = intersectionsActives.remove(j);
                    Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                    float anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(10)) + (float)Math.atan2(dirO.y, dirO.x);
                    float distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                    
                    fairePousserRoute(new Vec2( (float)Math.cos(anglePousse), (float)Math.sin(anglePousse) ), distancePousse, true, null, 100, interA, intersectionsActives);
                }
            }

            System.out.println("Génération des boulevards...");
            // Phase 2 faire pousser des boulevards depuis les autoroutes.
            intersectionsActives.clear();
            for (int i = réseau.intersections.size()-1; i >=0; i--) {
                if(Maths.randint(1, 100) < 20){
                    Intersection interA = réseau.intersections.get(i);
                    Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                    float anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(20)) + (float)Math.atan2(dirO.y, dirO.x);
                    float distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                    Vec2 dirB = new Vec2( (float)Math.cos(anglePousse+Math.PI/2), (float)Math.sin(anglePousse+Math.PI/2) );
                    Vec2 dirC = new Vec2( (float)Math.cos(anglePousse-Math.PI/2), (float)Math.sin(anglePousse-Math.PI/2) );
                    
                    Vec2 dir = null;

                    int nbBranches = (int)(Math.pow(Math.random(),10.0*interA.position.longueur()/RAYON_VILLE)*2.0 + 1.0);
                    for (int k = 0; k < nbBranches; k++) {
                        switch(k){
                            case 0: dir = dirB; break;
                            case 1: dir = dirC; break;
                        }

                        fairePousserRoute(dir, distancePousse, false,"bd", 70, interA, intersectionsActives);
                    }
                }
            }

            // Phase 2 partie 2 faire pousser les boulevards
            for (int i = 0; i < MAX_ITÉRATIONS; i++) {
                if (intersectionsActives.size() == 0){
                    break;
                }

                for (int j = intersectionsActives.size() - 1; j >= 0; j--) {
                    Intersection interA = intersectionsActives.remove(j);
                    Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                    float anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(10)) + (float)Math.atan2(dirO.y, dirO.x);
                    float distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                    
                    fairePousserRoute(new Vec2( (float)Math.cos(anglePousse), (float)Math.sin(anglePousse) ), distancePousse, true, null, 70, interA, intersectionsActives);
                }
            }

            System.out.println("Génération des rues");
            // Phase 3 faire pousser des rues depuis les boulevards.
            for (int i = réseau.intersections.size()-1; i >=0; i--) {
                if(Maths.randint(1, 100) < 20 && réseau.intersections.get(i).routes.size() <= 2){
                    Intersection interA = réseau.intersections.get(i);
                    Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                    float anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(20)) + (float)Math.atan2(dirO.y, dirO.x);
                    float distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                    Vec2 dirB = new Vec2( (float)Math.cos(anglePousse+Math.PI/2), (float)Math.sin(anglePousse+Math.PI/2) );
                    Vec2 dirC = new Vec2( (float)Math.cos(anglePousse-Math.PI/2), (float)Math.sin(anglePousse-Math.PI/2) );
                    
                    Vec2 dir = null;

                    int nbBranches = (int)(Math.pow(Math.random(),10.0*interA.position.longueur()/RAYON_VILLE)*2.0 + 1.0);
                    for (int k = 0; k < nbBranches; k++) {
                        switch(k){
                            case 0: dir = dirB; break;
                            case 1: dir = dirC; break;
                        }

                        fairePousserRoute(dir, distancePousse, false,"bd", 70, interA, intersectionsActives);
                    }
                }
            }

            // Phase 3 partie 2 faire pousser les rues
            for (int i = 0; i < MAX_ITÉRATIONS; i++) {
                if (intersectionsActives.size() == 0){
                    break;
                }

                for (int j = intersectionsActives.size() - 1; j >= 0; j--) {
                    Intersection interA = intersectionsActives.remove(j);
                    Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                    float anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(30)) + (float)Math.atan2(dirO.y, dirO.x);
                    float distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                    Vec2 dirA = new Vec2( (float)Math.cos(anglePousse), (float)Math.sin(anglePousse) );
                    Vec2 dirB = new Vec2( (float)Math.cos(anglePousse+Math.PI/2), (float)Math.sin(anglePousse+Math.PI/2) );
                    Vec2 dirC = new Vec2( (float)Math.cos(anglePousse-Math.PI/2), (float)Math.sin(anglePousse-Math.PI/2) );
                    
                    Vec2 dir = null;

                    int nbBranches = (int)(Math.pow(Math.random(),10.0*interA.position.longueur()/RAYON_VILLE)*2.9 + 1.0);
                    for (int k = 0; k < nbBranches; k++) {
                        switch(k){
                            case 0: dir = dirA; break;
                            case 1: dir = dirB; break;
                            case 2: dir = dirC; break;
                        }

                        fairePousserRoute(dir, distancePousse, k==0, "rue", 40, interA, intersectionsActives);
                    }
                }
            }

            réseau.construireTronçons();

            System.out.println("Génération des numéros de rues...");
            //Phase 4 générer numéros de rues.
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

            succèsGénération = testerValidité();
            if(!succèsGénération){
                System.err.println("[ERREUR] Échec de la génération du réseau routier. Nous réessayons.");
            }
        }

        System.out.println("Génération des voitures");
        // TODO phase 5: placement des voitures.
        String[] adresses = new String[NOMBRE_VÉHICULES];
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

        réseau.véhicules = new Véhicule[1000];
        for (int i = 0; i < réseau.véhicules.length; i++) {
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
            while(réseau.véhicules[i] == null){
                Route route = réseau.routes.get(Maths.randint(0, réseau.routes.size()-1));
                if(route.sensAPossèdePlace(1.2f) && route.possèdeAdresses){
                    réseau.véhicules[i] = new Véhicule(4.2f,route);
                    réseau.véhicules[i].routeActuelle.ajouterVéhiculeSensA(réseau.véhicules[i]);
                    réseau.véhicules[i].avoirNavigateur().donnerRoutine(adresses);
                }
            }
        }

        System.out.println("Réseau Généré");
        return réseau;
    }

    /**
     * Fait pousser une route
     * @param dir
     * @param dist
     * @param continuerRoute
     * @param nomPréfixe
     * @param limiteVitesse
     * @param origine
     * @param intersectionsActives
     */
    private static void fairePousserRoute(Vec2 dir, float dist, boolean continuerRoute, String nomPréfixe, int limiteVitesse, Intersection origine, ArrayList<Intersection> intersectionsActives){
        avoirIntersection(dir, origine.position, dist); // Vérifier si la route rentre dans une autre route.
        Vec2 interPos = intersectionRoutes;
        if(interPos == null){
            // S'il n'y a pas d'intersection.
            Intersection interB = null;
            if(continuerRoute){
                interB = new IntersectionLaissezPasser(Vec2.mult(dir,dist).addi(origine.position));
            }else{
                interB = new IntersectionArrêt( Vec2.mult(dir,dist).addi(origine.position));
            }
            Route nouvelleRoute = new Route(continuerRoute?origine.routes.get(0).nom:nomPréfixe+" "+générerNom(), continuerRoute?origine.routes.get(0).avoirLimiteKmH():limiteVitesse, origine, interB);
            
            réseau.intersections.add(interB);
            réseau.routes.add(nouvelleRoute);
            intersectionsActives.add(interB);
        }else{
            // S'il y a intersection avec une route existante.
            // On ne veut pas des routes trop courtes pour accueillir des maisons.
            if(Vec2.distance(interPos, origine.position) > DISTANCE_POUSSE_MIN){
                // Couper la route en deux
                Intersection interB = new IntersectionLaissezPasser(interPos);
                Intersection interTmp = routeIntersecté.intersectionB;
                routeIntersecté.intersectionB = interB;
                interB.ajouterRoute(routeIntersecté);
                interTmp.retirerRoute(routeIntersecté);
                Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                // Nouvelle route
                Route nouvelleRoute = new Route(continuerRoute?origine.routes.get(0).nom:nomPréfixe+" "+générerNom(), continuerRoute?origine.routes.get(0).avoirLimiteKmH():limiteVitesse, origine, interB);
                
                réseau.intersections.add(interB);
                réseau.routes.add(nouvelleRoute);
                réseau.routes.add(routeIntersecté2);
            }else if(Vec2.distance(interPos, origine.position) < DISTANCE_POUSSE_MIN && continuerRoute){
                // Si la route devait s'étendre, la rejoindre avec l'intersection.
                // Couper la route en deux
                Intersection interB = new IntersectionLaissezPasser(interPos);
                Intersection interTmp = routeIntersecté.intersectionB;
                routeIntersecté.intersectionB = interB;
                interB.ajouterRoute(routeIntersecté);
                interTmp.retirerRoute(routeIntersecté);
                Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                // Étendre la route d'origine
                if(origine.routes.get(0).intersectionA == origine){
                    origine.routes.get(0).intersectionA = interB;
                }else{
                    origine.routes.get(0).intersectionB = interB;
                }
                interB.ajouterRoute(origine.routes.get(0));
                réseau.routes.remove(origine);
                
                réseau.intersections.add(interB);
                réseau.intersections.remove(origine);
                réseau.routes.add(routeIntersecté2);
            }
        }
    }

    /**
     * Trouve l'intersection entre le réseau routier et un rayon pointé dans une intersection.
     * Le résultat est stocké dans les attributs `intersectionRoute` et `routeIntersecté`.
     * - `intersectionRoute` est un Vec2 qui représente la position de l'intersection entre le rayon
     * et le réseau et est `null` si le rayon ne touche aucune route.
     * - `routeIntersecté` est la route que le rayon a touché et est `null` si aucune route n'a été touchée
     * @param m direction du rayon
     * @param b orgine du rayon
     * @param maxDist distance maximale à vérifier du rayon
     */
    private static void avoirIntersection(Vec2 m, Vec2 b, float maxDist){
        float minDist = Float.MAX_VALUE;
        intersectionRoutes = null;
        routeIntersecté = null;
        for (int i = 0; i < réseau.routes.size(); i++) {
            Intersection iA = réseau.routes.get(i).intersectionA;
            Intersection iB = réseau.routes.get(i).intersectionB;
            Vec2 dir = Vec2.sous(iA.position,iB.position);
            Vec2 interPos = Maths.intersectionLignes(m, b, dir, réseau.routes.get(i).intersectionB.position);
            if(interPos == null){
                continue;
            }
            // La fonction trouve une intersection entre deux lignes infinies. Il faut vérifier si ce qu'elle a trouvée est à l'intérieur des bornes
            if (
                Vec2.scal(Vec2.sous(interPos,b),m) < 0.1f || // Si l'intersection est derrière l'origine de la route.
                Vec2.distance(interPos, b) > maxDist || // Si l'intersection est trop loin
                //L'intersection n'est pas entre les deux intersections de la route.
                !(interPos.x >= Math.min(iA.position.x,iB.position.x) && interPos.x <= Math.max(iA.position.x,iB.position.x) && interPos.y >= Math.min(iA.position.y,iB.position.y) && interPos.y <= Math.max(iA.position.y,iB.position.y)))
            {
                continue;
            }
            
            if (
                Vec2.distance(interPos, b) < minDist && 
            Vec2.distance(interPos,b) > 0.5f    // S'assurer qu'on ne frappes pas la route dont on émerge.
            ){
                minDist = Vec2.distance(interPos, b);
                intersectionRoutes = interPos;
                routeIntersecté = réseau.routes.get(i);
            }
        }
    }

    private static boolean testerValidité(){
        for (int i = 0; i < réseau.intersections.size(); i++) {
            if(réseau.intersections.get(i).avoirRoutes().size() == 0){
                System.err.println("Une intersection sans routes a été trouvée.");
                for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                    System.err.println(e);
                }
                return false;
            }

            for (int j = 0; j < réseau.intersections.get(i).avoirRoutes().size(); j++) {
                if (
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionA != réseau.intersections.get(i) &&
                    réseau.intersections.get(i).avoirRoutes().get(j).intersectionB != réseau.intersections.get(i)
                ){
                    System.err.println("Une intersection pointant vers une route qui ne pointe pas en retour a été trouvée");
                    for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                        System.err.println(e);
                    }
                    return false;
                }
            }
        }

        for (int i = 0; i < réseau.routes.size(); i++) {
            if(réseau.routes.get(i).intersectionA == null && réseau.routes.get(i).intersectionB == null){
                System.err.println("Une route sans intersections a été trouvée.");
                for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                    System.err.println(e);
                }
                return false;
            }

            if(réseau.routes.get(i).avoirLongueur() == 0){
                System.err.println("Une route de longueur 0 a été trouvée.");
                for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                    System.err.println(e);
                }
                return false;
            }

            if(
                !réseau.routes.get(i).intersectionA.avoirRoutes().contains(réseau.routes.get(i))||
                !réseau.routes.get(i).intersectionB.avoirRoutes().contains(réseau.routes.get(i))
            ){
                System.err.println("Une route qui pointe vers une intersection qui ne pointe pas en retour à été trouvée");
                for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                    System.err.println(e);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode de débugage.
     * @param réseau
     */
    public static void enregistrerRéseauEnOBJ(Réseau réseau){
        try {
            File file = new File("réseau.obj");
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            for (int i = 0; i < réseau.intersections.size(); i++) {
                String ligne = "v ";
                Intersection inter = réseau.intersections.get(i);
                ligne += inter.position.x + " " + inter.position.y + " 0\n";
                dos.writeUTF(ligne);
            }
            for (int i = 0; i < réseau.routes.size(); i++) {
                String ligne = "l ";
                Route route = réseau.routes.get(i);
                ligne += (réseau.intersections.indexOf(route.intersectionA)+1) + " " + (réseau.intersections.indexOf(route.intersectionB)+1) + "\n";
                dos.writeUTF(ligne);
                //System.out.println(route.nom);
            }
            dos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Génère un nom aléatoire.
     * @return
     */
    private static String générerNom(){
        String nom = "";
        do{
            for (int i = 0; i < Maths.randint(1, 4); i++) {
                nom += boutsNoms[Maths.randint(0, boutsNoms.length-1)];
            }
        }while(nomsGénérés.contains(nom));
        nomsGénérés.add(nom);
        return nom;
    }

}
