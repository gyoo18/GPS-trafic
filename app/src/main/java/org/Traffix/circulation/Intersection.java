package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;

public abstract class Intersection {

    public Vec2 position;

    protected ArrayList<Route> routes;

    public Intersection(Vec2 pos){
        this.position = pos.copier();
        routes = new ArrayList<>();
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

    public void retirerRoute(Route route){
        routes.remove(route);
    }

    public ArrayList<Route> avoirRoutes(){
        return routes;
    }

    /**
     * Indique si un véhicule a le droit de s'engager à travers l'intersection en direction de la route indiquée
     * @param routeDépart Route que le véhicule quitte
     * @param routeDestination Route sur laquelle le véhicule veut embarquer
     * @return boolean indiquant si le véhicule a le droit de passer.
     */
    public abstract boolean peutEngager(Route routeDépart, Route routeDestination);

    /**
     * Indique si un véhicule pourra passer tout droit à travers l'intersection sans ralentir pendant les 20 prochaines secondes, en direction de la route indiquée.
     * @param routeDépart Route que le véhicule quitte
     * @param routeDestination Route sur laquelle le véhicule veut embarquer
     * @return boolean indiquant si le véhicule a le droit de passer.
     */
    public abstract boolean peutPasser(Route routeDépart, Route routeDestination);

    /**
     * Met à jour les systèmes dynamiques à l'intérieur de l'intersection
     */
    public abstract void miseÀJour();
}