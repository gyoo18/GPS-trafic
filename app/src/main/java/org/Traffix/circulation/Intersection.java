package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

public abstract class Intersection {

    public Vec2 position;

    private ArrayList<Route> routes;

    public Intersection(Vec2 pos){
        this.position = pos.copier();
    }

    public Intersection(Vec2 pos, ArrayList routes){
        this.position = pos.copier();
        routes = (ArrayList<Route>)routes.clone();
    }

    public Intersection(Intersection copie){
        this.position = copie.position.copier();
        this.routes = (ArrayList<Route>)copie.routes.clone();
    }

    public void ajouterRoute(Route route){
        routes.add(route);
    }

    public Route retirerRoute(String nom){
        for (int i = 0 ; i < routes.size(); i++){
            if (routes.get(i).nom == nom){
                return routes.remove(i);
            }
        }
        return null;
    }

    public ArrayList<Route> avoirRoutes(){
        return routes;
    }

    public abstract boolean peutPasser(Route routeDépart, Route routeDestination);
}