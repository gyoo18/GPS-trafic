package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.maths.Vec2;
import org.checkerframework.checker.units.qual.min;

/**
 * 
 * Classe de Route temporaire, à remplacer
 * 
 */

public class Route {
    public final String nom;
    public final float limiteVitesse;  // en m/s pour accélérer et faciliter les calculs
    public Intersection intersectionA;
    public Intersection intersectionB;
    public boolean possèdeAdresses = false;

    public float facteurRalentissement; // facteur de ralentissement dû aux accidents, en %

    // La voie A est la voie en direction de l'intersection A et vice-versa.
    public ArrayList<Véhicule> véhiculesSensA = new ArrayList<>();
    public ArrayList<Véhicule> véhiculesSensB = new ArrayList<>();

    // Les adresses du sens A sont du côté droit lorsqu'en direction vers A et vice-versa.
    public int[] adressesSensANuméro;
    private Vec2[] adressesSensAPosition;
    public int[] adressesSensBNuméro;
    private Vec2[] adressesSensBPosition;

    private ArrayList<String> accès = new ArrayList<>();
    
    public Route(String nom, int limiteVitesseKmH, Intersection intersectionA, Intersection intersectionB) {
        this.nom = nom.toLowerCase();
        this.limiteVitesse = (float)limiteVitesseKmH/3.6f; // transformer la limite de vitesse de km/h en m/s
        this.facteurRalentissement = 1f;
        this.intersectionA = intersectionA;
        this.intersectionB = intersectionB;
        intersectionA.ajouterRoute(this);
        intersectionB.ajouterRoute(this);
    }

    public void donnerAdresses(int[] listeAdressesSensANuméro, Vec2[] listeAdresseSensAPosition, int[] listeAdressesSensBNuméro, Vec2[] listeAdresseSensBPosition){
        if(
            listeAdressesSensANuméro != null && listeAdresseSensAPosition != null && listeAdressesSensBNuméro != null && listeAdresseSensBPosition != null &&
            listeAdressesSensANuméro.length > 0 && listeAdresseSensAPosition.length > 0 && listeAdressesSensBNuméro.length > 0 && listeAdresseSensBPosition.length > 0
        ){
            this.adressesSensANuméro = listeAdressesSensANuméro;
            this.adressesSensAPosition = listeAdresseSensAPosition;
            this.adressesSensBNuméro = listeAdressesSensBNuméro;
            this.adressesSensBPosition = listeAdresseSensBPosition;
            this.possèdeAdresses = true;
        }
    }

    public float avoirLongueur() {
        return Vec2.distance(intersectionA.position, intersectionB.position);
    }

    /**
     * Transforme la limite de vitesse de m/s en km/h
     * @return int limite en km/h
     */
    public int avoirLimiteKmH(){
        return (int)(limiteVitesse*3.6f);
    }

    /**
     * Renvoie la limite de vitesse, affectée par les limitations des accidents
     * @return float limite effective en m/s
     */
    public float avoirLimiteEffective(){
        // TODO gestionnaireAccidents

        return limiteVitesse*facteurRalentissement;
    }

    public float avoirVitesseVéhicules(boolean dansSensA){
        float vitesseVéhicules = Float.MAX_VALUE;
        if(dansSensA && véhiculesSensA.size() > 2){
            vitesseVéhicules = 0;
            for (int i = 0; i < véhiculesSensA.size(); i++) {
                vitesseVéhicules += véhiculesSensA.get(i).vitesse;
            }
            vitesseVéhicules = vitesseVéhicules/véhiculesSensA.size();
        }else if(!dansSensA && véhiculesSensB.size() > 2){
            vitesseVéhicules = 0;
            for (int i = 0; i < véhiculesSensB.size(); i++) {
                vitesseVéhicules += véhiculesSensB.get(i).vitesse;
            }
            vitesseVéhicules = vitesseVéhicules/véhiculesSensB.size();
        }
        return vitesseVéhicules;
    }

    /**
     * Renvoie la limite de vitesse effective en km/h. Voir `avoirLimiteEffective()
     * @return int limite effective en km/h
     */
    public int avoirLimiteEffectiveKmH(){
        return (int)(avoirLimiteEffective()*3.6f);
    }

    /**
     * Renvoie la position du numéro de rue spécifié et `null` si la rue ne possèdes pas le numéro.
     * @param adresse numéro de rue
     * @return Vec2 position de l'adresse.
     */
    public Vec2 avoirPosition(int adresse){
        if(!possèdeAdresses){
            return null;
        }
        
        for (int i = 0; i < adressesSensANuméro.length; i++) {
            if (adressesSensANuméro[i] == adresse){
                return adressesSensAPosition[i];
            }
        }
        for (int i = 0; i < adressesSensBNuméro.length; i++){
            if(adressesSensBNuméro[i] == adresse){
                return adressesSensBPosition[i];
            }
        }
        return null;
    }

    /**
     * Renvoie le numéro de rue de l'adresse la plus proche de la position spécifiée sur cette rue et -1 si la rue ne possède pas d'adresses.
     * @param position position à tester.
     * @return int numéro d'adresse la plus proche.
     */
    public int avoirAdresse(Vec2 position){
        if(!possèdeAdresses){
            return -1;
        }

        float minDist = Float.MAX_VALUE;
        int minDistAdresse = -1;
        float dist = 0;
        for (int i = 0; i < adressesSensAPosition.length; i++) {
            dist = Vec2.distance(position, adressesSensAPosition[i]);
            if (dist < minDist){
                minDist = dist;
                minDistAdresse = adressesSensANuméro[i];
            }
        }

        for (int i = 0; i < adressesSensBPosition.length; i++) {
            dist = Vec2.distance(position, adressesSensBPosition[i]);
            if (dist < minDist){
                minDist = dist;
                minDistAdresse = adressesSensBNuméro[i];
            }
        }
        
        return minDistAdresse;
    }

    /**
     * Indique s'il reste de la place pour rentrer un véhicule de longueur `longueur` dans la voie A
     * @param longueur longueur à tester
     * @return boolean indiquant s'il reste suffisamment de place.
     */
    public boolean sensAPossèdePlace(float longueur){
        if(véhiculesSensA.size() == 0){
            return true;
        }

        float espace = 1f/avoirLongueur();
        Véhicule dernierVéhicule = véhiculesSensA.get(véhiculesSensA.size()-1);
        return (
            dernierVéhicule.positionRelative - (dernierVéhicule.longueur/(2f*avoirLongueur())) -
            espace -
            (longueur/(2f*avoirLongueur()))
            > 0f);
    }

    /**
     * Indique s'il reste de la place pour rentrer un véhicule de longueur `longueur` dans la voie B
     * @param longueur longueur à tester
     * @return boolean indiquant s'il reste suffisamment de place.
     */
    public boolean sensBPossèdePlace(float longueur){
        if(véhiculesSensB.size() == 0){
            return true;
        }

        float espace = 1f/avoirLongueur();
        Véhicule dernierVéhicule = véhiculesSensB.get(véhiculesSensB.size()-1);
        return (
            dernierVéhicule.positionRelative - (dernierVéhicule.longueur/(2f*avoirLongueur())) -
            espace -
            (longueur/(2f*avoirLongueur()))
            > 0f);
    }

    /**
     * Ajoutes un véhicule à l'arrière de la file de véhicules sur la voie A
     * Lance une erreur élégante si la voie n'a pas la place nécessaire.
     * @param véhicule véhicule à ajouter
     */
    public void ajouterVéhiculeSensA(Véhicule véhicule){
        // accès.add("===== ajouterVéhiculeSensA( Véhicule : "+véhicule+" ) ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if(!sensAPossèdePlace(véhicule.longueur)){
            System.err.println("[ERREUR] impossible d'ajouter un véhicule à la voie A.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return;
        }

        véhiculesSensA.add(véhicule);
        véhicule.positionRelative = 0;
    }

    /**
     * Ajoute un véhicule à l'arrière de la file de véhicules sur la voie B.
     * Lance une erreur élégante si la voie n'a pas la place nécessaire.
     * @param véhicule véhicule à ajouter
     */
    public void ajouterVéhiculeSensB(Véhicule véhicule){
        // accès.add("===== ajouterVéhiculeSensB( Véhicule : "+véhicule+" ) ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if(!sensBPossèdePlace(véhicule.longueur)){
            System.err.println("[ERREUR] impossible d'ajouter un véhicule à la voie B.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return;
        }

        véhiculesSensB.add(véhicule);
        véhicule.positionRelative = 0;
    }

    /**
     * Retire le véhicule à l'avant de la file des véhicules de la voie A
     * @return Véhicule à l'avant de la file ou `null` si la file est vide. 
     */
    public Véhicule retirerVéhiculeSensA(){
        // accès.add("===== retirerVéhiculeSensA -> "+(véhiculesSensA.size()>0?véhiculesSensA.get(0).toString():"null")+" ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if (véhiculesSensA.size() > 0){
            return véhiculesSensA.remove(0);
        }else{
            return null;
        }
    }

    /**
     * Retire le véhicule à l'avant de la file des véhicules de la voie B
     * @return Véhicule à l'avant de la file ou `null` si la file est vide. 
     */
    public Véhicule retirerVéhiculeSensB(){
        // accès.add("===== retirerVéhiculeSensB -> "+(véhiculesSensB.size()>0?véhiculesSensB.get(0).toString():"null")+" ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if (véhiculesSensB.size() > 0){
            return véhiculesSensB.remove(0);
        }else{
            return null;
        }
    }

    public void retirerVéhiculeSensA(Véhicule v){
        // accès.add("===== retirerVéhiculeSensA( "+v+" ) ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if (véhiculesSensA.contains(v)){
            véhiculesSensA.remove(v);
        }
    }

    public void retirerVéhiculeSensB(Véhicule v){
        // accès.add("===== retirerVéhiculeSensB( "+v+" ) ===== t: "+System.currentTimeMillis());
        // for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
        //     accès.add(element.toString());
        // }
        if (véhiculesSensB.contains(v)){
            véhiculesSensB.remove(v);
        }
    }

    /**
     * Renvoie le véhicule à la tête de la file sur la voie A
     * @return Véhicule
     */
    public Véhicule avoirPremierVéhiculeSensA(){
        if(véhiculesSensA.size() > 0){
            return véhiculesSensA.get(0);
        }else{
            return null;
        }
    }

    /**
     * Renvoie le véhicule à la tête de la file sur la voie B
     * @return Véhicule
     */
    public Véhicule avoirPremierVéhiculeSensB(){
        if(véhiculesSensB.size() > 0){
            return véhiculesSensB.get(0);
        }else{
            return null;
        }
    }

    /**
     * Renvoie le véhicule à la tête de la file sur la voie A
     * @return Véhicule
     */
    public Véhicule avoirDernierVéhiculeSensA(){
        if(véhiculesSensA.size() > 0){
            return véhiculesSensA.getLast();
        }else{
            return null;
        }
    }

    /**
     * Renvoie le véhicule à la tête de la file sur la voie B
     * @return Véhicule
     */
    public Véhicule avoirDernierVéhiculeSensB(){
        if(véhiculesSensB.size() > 0){
            return véhiculesSensB.getLast();
        }else{
            return null;
        }
    }

    /**
     * Renvoie le véhicule en avant du véhicule fournit et `null` si aucun véhicule ne se trouve en avant.
     * Lance une erreur élégante si le véhicule fournit ne se trouve pas sur la route.
     * @param véhicule véhicule fournit.
     * @return Véhicule en face ou `null`
     */
    public Véhicule avoirVéhiculeEnAvant(Véhicule véhicule){
        if (véhiculesSensA.contains(véhicule)){
            int i = véhiculesSensA.indexOf(véhicule);
            if (i == 0){
                return null;
            }else{
                return véhiculesSensA.get(i-1);
            }
        }else if(véhiculesSensB.contains(véhicule)){
            int i = véhiculesSensB.indexOf(véhicule);
            if (i == 0){
                return null;
            }else{
                return véhiculesSensB.get(i-1);
            }
        }else{
            System.err.println("[ERREUR] Le véhicule fournit ne se trouve pas sur cette route.");
             for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }
    }

    /**
     * Renvoie le véhicule en arrière du véhicule fournit et `null` si aucun véhicule ne se trouve en arrière.
     * Lance une erreur élégante si le véhicule fournit ne se trouve pas sur la route.
     * @param véhicule véhicule fournit.
     * @return Véhicule en arrière ou `null`
     */
    public Véhicule avoirVéhiculeEnArrière(Véhicule véhicule){
        if (véhiculesSensA.contains(véhicule)){
            int i = véhiculesSensA.indexOf(véhicule);
            if (i == véhiculesSensA.size()-1){
                return null;
            }else{
                return véhiculesSensA.get(i+1);
            }
        }else if(véhiculesSensB.contains(véhicule)){
            int i = véhiculesSensB.indexOf(véhicule);
            if (i == véhiculesSensB.size()){
                return null;
            }else{
                return véhiculesSensB.get(i+1);
            }
        }else{
            System.err.println("[ERREUR] Le véhicule fournit ne se trouve pas sur cette route.");
            System.err.println(Thread.currentThread().getStackTrace());
            return null;
        }
    }
}
