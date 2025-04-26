package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

/**
 * Spécialisation de Intersection qui gère le traffic selon une logique d'un panneau d'arrêt partout.
 */
public class IntersectionArrêt extends Intersection {

    private ArrayList<Route> demandesPassage = new ArrayList<>();

    public IntersectionArrêt(Vec2 pos){
        super(pos);
    }

    public IntersectionArrêt(Vec2 pos, ArrayList routes){
        super(pos, routes);
    }

    public IntersectionArrêt(Intersection copie){
        super(copie);
    }

    @Override
    public boolean peutEngager(Route routeDépart, Route routeDestination) {
        if (routeDépart == null || routeDestination == null){
            System.err.println("[ERREUR] routeDépart et routeDestination ne peuvent pas être null");
            System.err.println(Thread.currentThread().getStackTrace());
            return false;
        }

        if (demandesPassage.size() == 0 || demandesPassage.get(0) == routeDépart){
            if(demandesPassage.size() != 0){
                demandesPassage.remove(0);
            }
            return true;
        }else if(!demandesPassage.contains(routeDépart)){
            demandesPassage.add(routeDépart);
        }

        return false;
    }

    @Override
    public boolean peutPasser(Route routeDépart, Route routeDestination){
        return false;
    }

    @Override
    public void miseÀJour() {}
    
}
