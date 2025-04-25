package org.Traffix.circulation;

import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class UsineRéseau {

    private static final int MAX_ITÉRATIONS = 30;

    // En mètres
    private static final float RAYON_INITIAL = 100f;
    private static final float RAYON_VILLE = 3000f;
    private static final float DISTANCE_POUSSE_MIN = 10f;
    private static final float DISTANCE_POUSSE_MAX = 200f;

    private static Réseau réseau;

    // Le résultat de avoirIntersections
    private static Vec2 intersectionRoutes = null;
    private static Route routeIntersecté = null;

    private static String[] boutsNoms = new String[]{"an", "be", "ca", "de", "el", "fi", "ga", "he", "il", "jo", "la", "ma", "no", "on", "pi", "ra", "so", "ti", "va", "yu", "ana","ben","cam","dia","eli","fay","gal","hen","isa","jol","lia","mar","nol","oli","pal","ren","sol","tia","val","yva"};
    private static ArrayList<String> nomsGénérés = new ArrayList<>();

    public static Réseau générerRéseau(){
        réseau = new Réseau();
        ArrayList<Intersection> intersectionsActives = new ArrayList<>();

        float angle = (float)(Math.random()*2*Math.PI);
        float rayon = (float)(Math.random()*RAYON_INITIAL);
        Intersection interA = new IntersectionFeux( new Vec2( (float)(Math.cos(angle)*rayon), (float)(Math.sin(angle)*rayon) ) );
        réseau.intersections.add( interA );

        float anglePousse = (float)(Math.random()*2*Math.PI);
        float distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
        Intersection interB = new IntersectionFeux( new Vec2( (float)(Math.cos(anglePousse)*distancePousse), (float)(Math.sin(anglePousse)*distancePousse) ).addi(interA.position) );
        Route route = new Route("Autoroute de la Naissance", 100, interA, interB);
        réseau.intersections.add(interB);
        réseau.routes.add(route);
        intersectionsActives.add(interB);

        for (int i = 0; i < Maths.randint(0,10); i++) {
            angle = (float)(Math.random()*2*Math.PI);
            rayon = (float)(Math.random()*RAYON_INITIAL);
            interA = new IntersectionFeux( new Vec2( (float)(Math.cos(angle)*rayon), (float)(Math.sin(angle)*rayon) ) );
            réseau.intersections.add( interA );

            anglePousse = (float)(Math.random()*2*Math.PI);
            distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
            avoirIntersection(new Vec2( (float)Math.cos(angle), (float)Math.sin(angle) ), interA.position, distancePousse);
            Vec2 inter2Pos = intersectionRoutes;
            if(inter2Pos == null){
                interB = new IntersectionFeux(new Vec2( (float)(Math.cos(angle)*distancePousse), (float)(Math.sin(angle)*distancePousse) ).addi(interA.position));
                Route nouvelleRoute = new Route("Autoroute "+générerNom(), 100, interA, interB);
                réseau.intersections.add(interB);
                réseau.routes.add(nouvelleRoute);
                intersectionsActives.add(interB);
            }else{
                interB = new IntersectionLaissezPasser(inter2Pos);
                Intersection interTmp = routeIntersecté.intersectionB;
                routeIntersecté.intersectionB = interB;
                interTmp.retirerRoute(routeIntersecté);
                Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                Route nouvelleRoute = new Route("Autoroute "+générerNom(), 40, interA, interB);
                réseau.intersections.add(interB);
                réseau.routes.add(nouvelleRoute);
                réseau.routes.add(routeIntersecté2);
            }
        }

        for (int i = 0; i < MAX_ITÉRATIONS; i++) {
            if (intersectionsActives.size() == 0){
                break;
            }

            for (int j = intersectionsActives.size() - 1; j >= 0; j--) {
                interA = intersectionsActives.remove(j);
                Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(10)) + (float)Math.atan2(dirO.y, dirO.x);
                distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                Vec2 dir = new Vec2( (float)Math.cos(anglePousse), (float)Math.sin(anglePousse) );
                
                avoirIntersection(dir, interA.position, distancePousse);
                Vec2 inter2Pos = intersectionRoutes;
                if(inter2Pos == null){
                    interB = new IntersectionLaissezPasser( Vec2.mult(dir,distancePousse).addi(interA.position));
                    Route nouvelleRoute = new Route(interA.routes.get(0).nom, 100, interA, interB);
                    réseau.intersections.add(interB);
                    réseau.routes.add(nouvelleRoute);
                    intersectionsActives.add(interB);
                }else{
                    interB = new IntersectionLaissezPasser(inter2Pos);
                    Intersection interTmp = routeIntersecté.intersectionB;
                    routeIntersecté.intersectionB = interB;
                    interTmp.retirerRoute(routeIntersecté);
                    Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                    Route nouvelleRoute = new Route(interA.routes.get(0).nom, 100, interA, interB);
                    réseau.intersections.add(interB);
                    réseau.routes.add(nouvelleRoute);
                    réseau.routes.add(routeIntersecté2);
                }
            }
        }

        intersectionsActives.clear();
        for (int i = réseau.intersections.size()-1; i >=0; i--) {
            if(Maths.randint(1, 100) < 20){
                interA = réseau.intersections.get(i);
                Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(20)) + (float)Math.atan2(dirO.y, dirO.x);
                distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                Vec2 dirB = new Vec2( (float)Math.cos(anglePousse+Math.PI/2), (float)Math.sin(anglePousse+Math.PI/2) );
                Vec2 dirC = new Vec2( (float)Math.cos(anglePousse-Math.PI/2), (float)Math.sin(anglePousse-Math.PI/2) );
                
                Vec2 dir = null;

                int nbBranches = (int)(Math.pow(Math.random(),10.0*interA.position.longueur()/RAYON_VILLE)*2.0 + 1.0);
                for (int k = 0; k < nbBranches; k++) {
                    switch(k){
                        case 0: dir = dirB; break;
                        case 1: dir = dirC; break;
                    }

                    avoirIntersection(dir, interA.position, distancePousse);
                    Vec2 inter2Pos = intersectionRoutes;
                    if(inter2Pos == null){
                        interB = new IntersectionFeux( Vec2.mult(dir,distancePousse).addi(interA.position));
                        Route nouvelleRoute = new Route("Bd "+générerNom(), 70, interA, interB);
                        réseau.intersections.add(interB);
                        réseau.routes.add(nouvelleRoute);
                        intersectionsActives.add(interB);
                    }else{
                        interB = new IntersectionLaissezPasser(inter2Pos);
                        Intersection interTmp = routeIntersecté.intersectionB;
                        routeIntersecté.intersectionB = interB;
                        interTmp.retirerRoute(routeIntersecté);
                        Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                        Route nouvelleRoute = new Route("Bd "+générerNom(), 70, interA, interB);
                        réseau.intersections.add(interB);
                        réseau.routes.add(nouvelleRoute);
                        réseau.routes.add(routeIntersecté2);
                    }
                }
            }
        }

        for (int i = 0; i < MAX_ITÉRATIONS; i++) {
            if (intersectionsActives.size() == 0){
                break;
            }

            for (int j = intersectionsActives.size() - 1; j >= 0; j--) {
                interA = intersectionsActives.remove(j);
                Vec2 dirO = Vec2.sous(interA.position,interA==interA.routes.get(0).intersectionA?interA.routes.get(0).intersectionB.position:interA.routes.get(0).intersectionA.position);
                anglePousse = (float)((Math.random()*2f - 1f)*Math.toRadians(30)) + (float)Math.atan2(dirO.y, dirO.x);
                distancePousse = (float)( Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN)*(interA.position.longueur()/RAYON_VILLE) + DISTANCE_POUSSE_MIN);
                Vec2 dirA = new Vec2( (float)Math.cos(anglePousse), (float)Math.sin(anglePousse) );
                Vec2 dirB = new Vec2( (float)Math.cos(anglePousse+Math.PI/2), (float)Math.sin(anglePousse+Math.PI/2) );
                Vec2 dirC = new Vec2( (float)Math.cos(anglePousse-Math.PI/2), (float)Math.sin(anglePousse-Math.PI/2) );
                
                Vec2 dir = null;

                int nbBranches = (int)(Math.pow(Math.random(),10.0*interA.position.longueur()/RAYON_VILLE)*3.0 + 1.0);
                for (int k = 0; k < nbBranches; k++) {
                    switch(k){
                        case 0: dir = dirA; break;
                        case 1: dir = dirB; break;
                        case 2: dir = dirC; break;
                    }

                    avoirIntersection(dir, interA.position, distancePousse);
                    Vec2 inter2Pos = intersectionRoutes;
                    if(inter2Pos == null){
                        interB = new IntersectionFeux( Vec2.mult(dir,distancePousse).addi(interA.position));
                        Route nouvelleRoute = new Route(k==0?interA.routes.get(0).nom:"rue "+générerNom(), 40, interA, interB);
                        réseau.intersections.add(interB);
                        réseau.routes.add(nouvelleRoute);
                        intersectionsActives.add(interB);
                    }else{
                        interB = new IntersectionLaissezPasser(inter2Pos);
                        Intersection interTmp = routeIntersecté.intersectionB;
                        routeIntersecté.intersectionB = interB;
                        interTmp.retirerRoute(routeIntersecté);
                        Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                        Route nouvelleRoute = new Route(k==0?interA.routes.get(0).nom:"rue "+générerNom(), 40, interA, interB);
                        réseau.intersections.add(interB);
                        réseau.routes.add(nouvelleRoute);
                        réseau.routes.add(routeIntersecté2);
                    }
                }
            }
        }

        return réseau;
    }

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
                System.out.println(route.nom);
            }
            dos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String générerNom(){
        String nom = "";
        do{
            for (int i = 0; i < Maths.randint(1, 4); i++) {
                nom += boutsNoms[Maths.randint(0, boutsNoms.length-1)];
            }
        }while(nomsGénérés.contains(nom));
        return nom;
    }

}
