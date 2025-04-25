package org.Traffix.circulation;

import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class UsineRéseau {

    private static final int MAX_ITÉRATIONS = 0;

    // En mètres
    private static final float RAYON_INITIAL = 100f;
    private static final float RAYON_VILLE = 3000f;
    private static final float DISTANCE_POUSSE_MIN = 10f;
    private static final float DISTANCE_POUSSE_MAX = 200f;

    private static Réseau réseau;

    // Le résultat de avoirIntersections
    private static Vec2 intersectionRoutes = null;
    private static Route routeIntersecté = null;

    public static Réseau générerRéseau(){
        réseau = new Réseau();
        ArrayList<Intersection> intersectionsActives = new ArrayList<>();

        float angle = (float)(Math.random()*2*Math.PI);
        float rayon = (float)(Math.random()*RAYON_INITIAL);
        Intersection interA = new IntersectionFeux(new Vec2(100,0)); // new Vec2( (float)(Math.cos(angle)*rayon), (float)(Math.sin(angle)*rayon) ) );
        réseau.intersections.add( interA );

        float anglePousse = (float)(Math.random()*2*Math.PI);
        float distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
        Intersection interB = new IntersectionFeux( new Vec2(-100,0)); // new Vec2( (float)(Math.cos(anglePousse)*distancePousse), (float)(Math.sin(anglePousse)*distancePousse) ).addi(interA.position) );
        Route route = new Route("Naissance", 40, interA, interB);
        interA.ajouterRoute(route);
        interB.ajouterRoute(route);
        réseau.intersections.add(interB);
        réseau.routes.add(route);
        intersectionsActives.add(interB);

        for (int i = 0; i < 1/*Maths.randint(0,10)*/; i++) {
            angle = (float)(Math.random()*2*Math.PI);
            rayon = (float)(Math.random()*RAYON_INITIAL);
            interA = new IntersectionFeux(new Vec2(0,100)); // new Vec2( (float)(Math.cos(angle)*rayon), (float)(Math.sin(angle)*rayon) ) );
            réseau.intersections.add( interA );

            anglePousse = (float)(Math.random()*2*Math.PI);
            distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
            avoirIntersection(new Vec2(0,-1),interA.position, 200); //new Vec2( (float)Math.cos(angle), (float)Math.sin(angle) ), interA.position, distancePousse);
            Vec2 inter2Pos = intersectionRoutes;
            if(inter2Pos == null){
                interB = new IntersectionFeux(new Vec2(0,-100)); //new Vec2( (float)(Math.cos(angle)*distancePousse), (float)(Math.sin(angle)*distancePousse) ).addi(interA.position));
                Route nouvelleRoute = new Route("test", 40, interA, interB);
                réseau.intersections.add(interB);
                réseau.routes.add(nouvelleRoute);
                intersectionsActives.add(interB);
            }else{
                interB = new IntersectionFeux(inter2Pos);
                Intersection interTmp = routeIntersecté.intersectionB;
                routeIntersecté.intersectionB = interB;
                Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                Route nouvelleRoute = new Route("test", 40, interA, interB);
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
                interA = intersectionsActives.remove(i);
                anglePousse = (float)((Math.random()*2 - 1)*Math.toRadians(10));
                distancePousse = (float)(Math.random()*(DISTANCE_POUSSE_MAX-DISTANCE_POUSSE_MIN) + DISTANCE_POUSSE_MIN);
                Vec2 dirA = new Vec2( (float)Math.cos(angle), (float)Math.sin(angle) );
                Vec2 dirB = new Vec2( (float)Math.cos(angle+Math.PI/2), (float)Math.sin(angle+Math.PI/2) );
                Vec2 dirC = new Vec2( (float)Math.cos(angle-Math.PI/2), (float)Math.sin(angle-Math.PI/2) );
                
                Vec2 dir = null;

                for (int k = 0; k < 3; k++) {
                    switch(k){
                        case 0: dir = dirA;
                        case 1: dir = dirB;
                        case 2: dir = dirC;
                    }

                    avoirIntersection(dir, interA.position, distancePousse);
                    Vec2 inter2Pos = intersectionRoutes;
                    if(inter2Pos == null){
                        interB = new IntersectionFeux(new Vec2( (float)(Math.cos(angle)*distancePousse), (float)(Math.sin(angle)*distancePousse) ).addi(interA.position));
                        Route nouvelleRoute = new Route("test", 40, interA, interB);
                        réseau.intersections.add(interB);
                        réseau.routes.add(nouvelleRoute);
                        intersectionsActives.add(interB);
                    }else{
                        interB = new IntersectionFeux(inter2Pos);
                        Intersection interTmp = routeIntersecté.intersectionB;
                        routeIntersecté.intersectionB = interB;
                        Route routeIntersecté2 = new Route(routeIntersecté.nom, routeIntersecté.avoirLimiteKmH(), interB, interTmp);
                        Route nouvelleRoute = new Route("test", 40, interA, interB);
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
                Vec2.scal(Vec2.sous(interPos,b),m) < 0f || // Si l'intersection est derrière l'origine de la route.
                Vec2.distance(interPos, b) > maxDist || // Si l'intersection est trop loin
                //L'intersection n'est pas entre les deux intersections de la route.
                !(interPos.x > Math.min(iA.position.x,iB.position.x) && interPos.x < Math.max(iA.position.x,iB.position.x) && interPos.y > Math.min(iA.position.y,iB.position.y) && interPos.y < Math.max(iA.position.y,iB.position.y)))
            {
                continue;
            }
            
            if (Vec2.distance(interPos, b) < minDist){
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
            }
            dos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
