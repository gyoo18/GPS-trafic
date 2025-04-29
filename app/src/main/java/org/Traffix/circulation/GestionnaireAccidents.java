package org.Traffix.circulation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.Traffix.OpenGL.GLCanvas;
import org.Traffix.OpenGL.GénérateurMaillage;
import org.Traffix.OpenGL.Maillage;
import org.Traffix.OpenGL.Nuanceur;
import org.Traffix.OpenGL.Objet;
import org.Traffix.circulation.GestionnaireAccidents.Accident.TypeAccident;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Transformée;
import org.Traffix.maths.Vec2;
import org.Traffix.maths.Vec3;
import org.Traffix.maths.Vec4;
import org.Traffix.utils.Chargeur;
import org.checkerframework.checker.units.qual.min;
import org.checkerframework.checker.units.qual.t;

public class GestionnaireAccidents {

    private static ArrayList<Accident> accidents = new ArrayList<>();
    private static Réseau réseau = null;
    private static GLCanvas carte;
    private static GLCanvas miniCarte;

    public static class Accident {

        public final float pourcentageRalentissement; // Pourcentage entre 0.0 (pas de ralentissement) et 1.0 (arrêt complet)
        public final float durée;       // en secondes
        public final long tempsCréation = System.currentTimeMillis();

        public final String description;
        public final TypeAccident type;

        public final Objet objetRendus;
        public final Objet miniObjetRendus;

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

        private Route[] routesAffectés;
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
                this.rayon = (float)Math.random();
                this.rayon *= this.rayon*300f;
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

            Maillage m;
            Maillage miniM;
            Transformée t;

            ArrayList<Route> routes = GestionnaireAccidents.réseau.routes;
            switch (this.type) {
                case TypeAccident.ACCIDENT_VÉHICULE:
                case TypeAccident.VÉHICULE_EN_PANNE:{
                    this.rayon = 10f;
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
                        tronçon.get(i).ajouterAccident(this);
                    }
                    routesAffectés = new Route[réseau.tronçons.get(minDistRoute.nom).size()];
                    réseau.tronçons.get(minDistRoute.nom).toArray(routesAffectés);
                    m = GénérateurMaillage.faireMaillageItinéraire(routesAffectés, 3f);
                    miniM = GénérateurMaillage.faireMaillageItinéraire(routesAffectés, 6f);
                    t = new Transformée().positionner(new Vec3(0,-0.05f,0));
                    this.description = this.type.toString()+" sur "+minDistRoute.nom+". Gravité : "+(int)(this.pourcentageRalentissement*100f)+"% durée : "+formatterTemps((int)this.durée,false);
                    break;
                }
                case TypeAccident.TRAVAUX:
                case TypeAccident.INTEMPÉRIE:
                case TypeAccident.MANIFESTATION:{
                    float minDist = Float.MAX_VALUE;
                    Route minDistRoute = null;
                    ArrayList<Route> routestmp = new ArrayList<>();
                    for(int i = 0; i < routes.size(); i++){
                        float dist = Maths.distanceSegmentPoint( routes.get(i).intersectionA.position,routes.get(i).intersectionB.position, this.position);
                        if(dist < minDist){
                            minDist = dist;
                            minDistRoute = routes.get(i);
                        }
                        if(dist <= this.rayon){
                            routestmp.add(routes.get(i));
                            routes.get(i).ajouterAccident(this);
                        }
                    }
                    routesAffectés = new Route[routestmp.size()];
                    routestmp.toArray(routesAffectés);
                    m = GénérateurMaillage.générerDisque(64);
                    miniM = m.copier();
                    t = new Transformée().échelonner(new Vec3(this.rayon)).positionner(new Vec3(this.position.x,-0.05f,this.position.y));
                    this.description = this.type.description+" sur "+((float)((int)this.rayon)/1000f)+"km autour de "+minDistRoute.nom+". Gravité : "+(int)(this.pourcentageRalentissement*100f)+"% durée : "+formatterTemps((int)this.durée,false);
                    break;
                }
                default:
                    throw new IllegalArgumentException(this.type.toString()+" n'est pas une valeur supportée.");
            }

            Nuanceur nuanceur = null;
            try {
                nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Vec4 couleur = new Vec4(0.8f,0.5f,0.5f,1f).mult(1f-this.pourcentageRalentissement); couleur.w = 1f;
            this.objetRendus = new Objet("Accident", m, nuanceur, couleur, null, t);
            this.miniObjetRendus = new Objet("miniAccident", miniM, nuanceur.copier(), this.objetRendus.avoirCouleur().copier(), null, t);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    carte.scène.ajouterObjet(objetRendus);
                    miniCarte.scène.ajouterObjet(miniObjetRendus);
                }
            });
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
                    this.rayon = 10f;
                    //routesAffectés.addAll(réseau.tronçons.get(route.nom));
                    this.description = this.type.toString()+" sur "+route.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                case TypeAccident.TRAVAUX:
                case TypeAccident.INTEMPÉRIE:
                case TypeAccident.MANIFESTATION:{
                    for(int i = 0; i < routes.size(); i++){
                        float dist = Maths.distanceLignePoint( Vec2.sous(routes.get(i).intersectionA.position,routes.get(i).intersectionB.position), routes.get(i).intersectionA.position, this.position);
                        if(dist <= this.rayon){
                            //routesAffectés.add(routes.get(i));
                        }
                    }
                    this.description = this.type.description+" sur "+(this.rayon/1000f)+"km autour de "+route.nom+". Gravité : "+this.pourcentageRalentissement*100f+"% durée : "+this.durée+"s";
                    break;
                }
                default:
                    throw new IllegalArgumentException(this.type.toString()+" n'est pas une valeur supportée.");
            }

            Nuanceur nuanceur = null;
            try {
                nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Vec4 couleur = new Vec4(0.8f,0.5f,0.5f,1f).mult(1f-this.pourcentageRalentissement); couleur.w = 1f;
            this.objetRendus = new Objet("Accident", GénérateurMaillage.générerDisque(16), nuanceur, couleur, null, new Transformée().échelonner(new Vec3(this.rayon)).positionner(new Vec3(this.position.x,-0.05f,this.position.y)));
            this.miniObjetRendus = new Objet("miniAccident", GénérateurMaillage.générerDisque(16), nuanceur.copier(), this.objetRendus.avoirCouleur().copier(), null, this.objetRendus.avoirTransformée());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    carte.scène.ajouterObjet(objetRendus);
                    miniCarte.scène.ajouterObjet(miniObjetRendus);
                }
            });
            System.out.println("Accident créé : "+this.description);
        }

        public void détruire(){
            System.out.println("Accident détruit : "+this.description);
            for (int i = 0; i < routesAffectés.length; i++) {
                routesAffectés[i].retirerAccident(this);
            }
            this.routesAffectés = null;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    carte.scène.retirerObjet(objetRendus);
                    miniCarte.scène.retirerObjet(miniObjetRendus);
                }
            });
        }

        // Getters et setters
        public Route[]  avoirRoutesAffectés() {
            return routesAffectés;
        }
    }

    public static void donnerRéseau(Réseau r){
        GestionnaireAccidents.réseau = r;
    }

    public static void donnerGLCanvas(GLCanvas carte, GLCanvas miniCarte){
        GestionnaireAccidents.carte = carte;
        GestionnaireAccidents.miniCarte = miniCarte;
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

    private static String formatterTemps(int tempsSec, boolean formatHeure){
        int sec = tempsSec%60;
        int tempsMin = (tempsSec/60)%60; //Temps en minutes
        int tempsH = (tempsSec/3600)%60; //Temps en heures
        return ""+(tempsH>0?tempsH+(formatHeure?"h":":"):"")+(tempsMin>0||tempsH>0?tempsMin+(formatHeure?"min":":"):"")+(tempsSec>0?sec+(formatHeure?"sec":":"):"");
    }
}