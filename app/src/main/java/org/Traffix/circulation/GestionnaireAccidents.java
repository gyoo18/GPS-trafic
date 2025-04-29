package org.Traffix.circulation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.Traffix.circulation.GestionnaireAccidents.Accident.TypeAccident;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;
import org.checkerframework.checker.units.qual.t;

public class GestionnaireAccidents {

    private static ArrayList<Accident> accidents = new ArrayList<>();
    private static Réseau réseau = null;

    public static class Accident {

        public final float pourcentageRalentissement; // Pourcentage entre 0.0 (pas de ralentissement) et 1.0 (arrêt complet)
        public final float durée;       // en secondes
        public final long tempsCréation = System.currentTimeMillis();

        public final String description;
        public final TypeAccident type;

        public enum TypeAccident {
            ACCIDENT_VÉHICULE("Collision entre véhicules"),
            TRAVAUX("Travaux de construction"),
            INTEMPÉRIE("Intempérie"),
            MANIFESTATION("Manifestation"),
            VÉHICULE_EN_PANNE("Véhicule en panne");
    
            final String description;
    
            TypeAccident(String description) {
                this.description = description;
            }
    
        }

        private ArrayList<Route> routesAffectés = new ArrayList<>();
        private Vec2 position;
        private float rayon;

        public Accident(TypeAccident type, Vec2 position, float rayon, float durée, float pourcentageRalentissement) {
            if(type != null){
                this.type = type;
            }else{
                this.type = TypeAccident.values()[Maths.randint(0, TypeAccident.values().length-1)];
            }
            if(position != null){
                this.position = position;
            }else{
                this.position = new Vec2((float)(Math.random()*2.0-1.0)*3000f,(float)(Math.random()*2.0-1.0)*3000f);
            }
            if(rayon >= 0f){
                this.rayon = rayon;
            }else{
                this.rayon = (float)Math.random()*1000f;
            }
            if(durée > 0f){
                this.durée = durée;
            }else{
                this.durée = (float)Math.random()*120f;
            }
            if(pourcentageRalentissement > 0f){
                this.pourcentageRalentissement = pourcentageRalentissement;
            }else{
                this.pourcentageRalentissement = (float)Math.random();
            }
            
            if(GestionnaireAccidents.réseau == null){
                throw new RuntimeException("[ERREUR] Le gestionnaire d'accidents ne possède pas de réseau.");
            }

            ArrayList<Route> routes = GestionnaireAccidents.réseau.routes;
            switch (this.type) {
                case TypeAccident.ACCIDENT_VÉHICULE:
                case TypeAccident.VÉHICULE_EN_PANNE:{
                    float minDist = Float.MAX_VALUE;
                    Route minDistRoute = null;
                    for(int i = 0; i < routes.size(); i++){
                        float dist = Maths.distanceLignePoint( Vec2.sous(routes.get(i).intersectionA.position,routes.get(i).intersectionB.position), routes.get(i).intersectionA.position, this.position);
                        if(dist < minDist){
                            minDist = dist;
                            minDistRoute = routes.get(i);
                        }
                    }
                    ArrayList<Route> tronçon = réseau.tronçons.get(minDistRoute.nom);
                    for (int i = 0; i < tronçon.size(); i++) {
                        tronçon.get(i).écouteurAccidentRetirés.add(this.écouteur);
                    }
                    routesAffectés.addAll(réseau.tronçons.get(minDistRoute.nom));
                    this.description = this.type.toString()+" sur "+minDistRoute.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                case TypeAccident.TRAVAUX:
                case TypeAccident.INTEMPÉRIE:
                case TypeAccident.MANIFESTATION:{
                    float minDist = Float.MAX_VALUE;
                    Route minDistRoute = null;
                    for(int i = 0; i < routes.size(); i++){
                        float dist = Maths.distanceSegmentPoint( routes.get(i).intersectionA.position,routes.get(i).intersectionB.position, this.position);
                        if(dist < minDist){
                            minDist = dist;
                            minDistRoute = routes.get(i);
                        }
                        if(dist <= this.rayon){
                            routesAffectés.add(routes.get(i));
                        }
                    }
                    this.description = this.type.description+" sur "+(this.rayon/1000f)+"km autour de "+minDistRoute.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                default:
                    throw new IllegalArgumentException(this.type.toString()+" n'est pas une valeur supportée.");
            }

            affecterRoutes();
            System.out.println("Accident créé : "+this.description);
        }


        public Accident(TypeAccident type, Route route, float durée, float pourcentageRalentissement) {
            if(type != null){
                this.type = type;
            }else{
                this.type = TypeAccident.values()[Maths.randint(0, TypeAccident.values().length-1)];
            }
            this.position = Vec2.addi(route.intersectionA.position,route.intersectionB.position).mult(0.5f);
            this.rayon = 0f;
            if(durée > 0f){
                this.durée = durée;
            }else{
                this.durée = (float)Math.random()*120f;
            }
            if(pourcentageRalentissement > 0f){
                this.pourcentageRalentissement = pourcentageRalentissement;
            }else{
                this.pourcentageRalentissement = (float)Math.random();
            }
            
            if(GestionnaireAccidents.réseau == null){
                throw new RuntimeException("[ERREUR] Le gestionnaire d'accidents ne possède pas de réseau.");
            }

            ArrayList<Route> routes = GestionnaireAccidents.réseau.routes;
            switch (this.type) {
                case TypeAccident.ACCIDENT_VÉHICULE:
                case TypeAccident.VÉHICULE_EN_PANNE:{
                    routesAffectés.addAll(réseau.tronçons.get(route.nom));
                    this.description = this.type.toString()+" sur "+route.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                case TypeAccident.TRAVAUX:
                case TypeAccident.INTEMPÉRIE:
                case TypeAccident.MANIFESTATION:{
                    for(int i = 0; i < routes.size(); i++){
                        float dist = Maths.distanceLignePoint( Vec2.sous(routes.get(i).intersectionA.position,routes.get(i).intersectionB.position), routes.get(i).intersectionA.position, this.position);
                        if(dist <= this.rayon){
                            routesAffectés.add(routes.get(i));
                        }
                    }
                    this.description = this.type.description+" sur "+(this.rayon/1000f)+"km autour de "+route.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                default:
                    throw new IllegalArgumentException(this.type.toString()+" n'est pas une valeur supportée.");
            }
            affecterRoutes();
            System.out.println("Accident créé : "+this.description);
        }

        public void affecterRoutes(){
            for (int i = 0; i < routesAffectés.size(); i++) {
                routesAffectés.get(i).facteurRalentissement = Math.min(routesAffectés.get(i).facteurRalentissement, (1f-pourcentageRalentissement));
            }
        }

        public void affecterRoute(Route route){
            if(routesAffectés.contains(route)){
                route.facteurRalentissement = Math.min(route.facteurRalentissement, (1f-pourcentageRalentissement));
            }
        }

        public void détruire(){
            System.out.println("Accident détruit : "+this.description);
            for (int i = 0; i < routesAffectés.size(); i++) {
                for (int j = 0; j < routesAffectés.get(i).écouteurAccidentRetirés.size(); j++) {
                    routesAffectés.get(i).écouteurAccidentRetirés.get(j).retiré(this, routesAffectés.get(i));
                }
                routesAffectés.get(i).écouteurAccidentRetirés.remove(écouteur);
            }
            this.routesAffectés.clear();
        }

        // Getters et setters
        public ArrayList<Route>  avoirRoutesAffectés() {
            return routesAffectés;
        }
    }

    public static void donnerRéseau(Réseau r){
        GestionnaireAccidents.réseau = r;
    }

    public static void miseÀJour() {

        // Détruire accidents
        for (int i = 0; i < accidents.size(); i++) {
            if(System.currentTimeMillis()-accidents.get(i).tempsCréation > accidents.get(i).durée*1000){
                accidents.get(i).détruire();
                accidents.remove(i);
            }
        }

        // Créer Accidents
        if (Math.random()*100.0 < 0.1){
            accidents.add(new Accident(null, null, -1, -1, -1));
        }
    }
}