package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.OpenGL.GénérateurMaillage;
import org.Traffix.OpenGL.Nuanceur;
import org.Traffix.OpenGL.Objet;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Transformée;
import org.Traffix.maths.Vec2;
import org.Traffix.maths.Vec3;
import org.Traffix.maths.Vec4;
import org.Traffix.utils.Chargeur;

//cspell:ignore nuanceur recalcul
public class Navigateur {
    protected Véhicule véhicule = null;
    protected Route prochainTournant = null;
    protected long tempsDernierRecalcul = System.currentTimeMillis();

    protected Route[] itinéraireActuel = null;
    protected int indexeRouteActuelle = 0;
    protected String[] routine = null;
    protected int indexeRoutine = 0;
    protected Vec2 posDest = null;

    private boolean ralentisPourArrêt = false;
    private float ralentissementPourArrêt = 0f;

    public boolean estBrisé = false;
    private boolean enAttenteDePlace = false;

    private final float ESPACEMENT_VOITURES = 3f; // en mètres
    protected final int CYCLE_RECALCUL = 10000;
    protected final int CYCLE_RECALCUL_DÉCALAGE = Maths.randint(-1000, 1000);
    private long tempsAttente = System.currentTimeMillis();
    private final long MAX_ATTENTE = 30000;
    
    private long tempsDebug = System.currentTimeMillis();

    protected Objet itinéraireObjet = null;
    protected Objet miniItinéraireObjet = null;

    public Navigateur(Véhicule véhicule) {
        this.véhicule = véhicule;
        
        Nuanceur nuanceur = null;
        try {
            nuanceur = Chargeur.chargerNuanceurFichier("nuaColoré");
        } catch (Exception e) {
            e.printStackTrace();
        }
        itinéraireObjet = new Objet("itinéraire", null, nuanceur, new Vec4(0.2f,0.5f,0.9f,1f), null, new Transformée().positionner(new Vec3(0,0.2f,0)));
        miniItinéraireObjet = new Objet("miniItinéraire", null, nuanceur, new Vec4(0.2f,0.5f,0.9f,1f), null, new Transformée().positionner(new Vec3(0,0.2f,0)));
    }

    public void donnerRoutine(String[] adresses){
        this.routine = adresses;
    }

    public void miseÀJour(float deltaTempsSecondes, boolean debug){

        // if((véhicule.estSensA && !véhicule.routeActuelle.véhiculesSensA.contains(véhicule))||(!véhicule.estSensA && !véhicule.routeActuelle.véhiculesSensB.contains(véhicule))){
        //     System.out.print("!");
        // }

        if(estBrisé){return;}
        //System.out.print(".");

        if(véhicule.vitesseMoyenne > 0.01f){
            tempsAttente = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - tempsAttente > MAX_ATTENTE){
            estBrisé = true;
            return;
        }

        if (prochainTournant == null){
            prochainTournant = chercherProchainTournant(debug);
            if(prochainTournant == null){
                estBrisé = true;
                return;
            }
            if(debug){System.out.println("Choisit prochain tournant");}
        }

        Intersection interB = véhicule.estSensA?véhicule.routeActuelle.intersectionA:véhicule.routeActuelle.intersectionB;
        // Si le prochain tournant est dans l'autre sens et n'est pas sur la destination (chercherProchain tournant renvoie véhicule.routeActuelle sit tel est le cas).
        if((!interB.routes.contains(prochainTournant) || enAttenteDePlace) && prochainTournant != véhicule.routeActuelle){
            if(véhicule.estSensA && véhicule.routeActuelle.sensBPossèdePlace(véhicule.longueur)){
                Route routeActuelle = véhicule.routeActuelle;
                routeActuelle.retirerVéhiculeSensA(véhicule);
                routeActuelle.ajouterVéhiculeSensB(véhicule);
                enAttenteDePlace = false;
            }else if(!véhicule.estSensA && véhicule.routeActuelle.sensAPossèdePlace(véhicule.longueur)){
                Route routeActuelle = véhicule.routeActuelle;
                routeActuelle.retirerVéhiculeSensB(véhicule);
                routeActuelle.ajouterVéhiculeSensA(véhicule);
                enAttenteDePlace = false;
            }else if(véhicule.routeActuelle.avoirVitesseVéhicules(véhicule.estSensA) < 0.01f){
                estBrisé = true;
            }else{
                enAttenteDePlace = true;
            }
        }
        boolean estProchainTournantSensA = interB == prochainTournant.intersectionB;

        boolean estDestination = itinéraireActuel[itinéraireActuel.length-1]==véhicule.routeActuelle;
        float distanceDest = Vec2.distance(véhicule.position(), posDest);

        float distanceInter = (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur();
        Véhicule avant = véhicule.routeActuelle.avoirVéhiculeEnAvant(véhicule);
        // if(avant == null && distanceInter < véhicule.vitesse*5f){
        //     if(interB==prochainTournant.intersectionB){
        //         avant = prochainTournant.avoirDernierVéhiculeSensA();
        //     }else{
        //         avant = prochainTournant.avoirDernierVéhiculeSensB();
        //     }
        // }
        float accélération = 0f;

        if(estDestination && Math.abs(véhicule.routeActuelle.avoirAdresse(véhicule.position())-extraireNuméro(routine[indexeRoutine])) <= 1){
            // Si le véhicule est arrivé à destination.
            if(debug){System.out.println("Arrivé à destination");}
            avancerRoutine();
            prochainTournant = null;
        }else if(distanceInter < 0.1f && !estDestination && !enAttenteDePlace){
            if(avant != null){
                System.out.println(".");
            }
            // Si le véhicule est à l'intersection.
            if(
                (estProchainTournantSensA && prochainTournant.sensAPossèdePlace(véhicule.longueur)) || // Si le prochain tournant a la place restante
                (!estProchainTournantSensA && prochainTournant.sensBPossèdePlace(véhicule.longueur))    // nécessaire pour que le véhicule s'engage.
            ){
                // S'il a le droit de s'engager dans l'intersection
                // l'approche en deux étapes est nécessaire pour, car appeler IntersectionArrêt.peutEngager() change l'état de ce dernier.
                if(interB.peutEngager(véhicule.routeActuelle, prochainTournant)){
                    if(debug){System.out.println("Passe dans l'intersection");}
                    if(véhicule.estSensA){
                        véhicule.routeActuelle.retirerVéhiculeSensA(véhicule);
                    }else{
                        véhicule.routeActuelle.retirerVéhiculeSensB(véhicule);
                    }

                    if(estProchainTournantSensA){
                        prochainTournant.ajouterVéhiculeSensA(véhicule);
                    }else{
                        prochainTournant.ajouterVéhiculeSensB(véhicule);
                    }
                    
                    prochainTournant = null;
                    ralentisPourArrêt = false;
                }else{
                    véhicule.vitesse = 0;
                    véhicule.positionRelative = 1f;
                }
            }else{
                véhicule.vitesse = 0;
                véhicule.positionRelative = 1f;
            }
        }else if(
            Math.abs(véhicule.vitesse) > 0f &&
            distanceInter/véhicule.vitesse < 5f &&
            !interB.peutPasser(véhicule.routeActuelle, prochainTournant) &&
            avant == null &&
            !estDestination
        ){
            // Si on est à moins de 5 secondes de passer l'intersection, qu'on est les premiers et qu'on ne peut pas simplement passer tout droit,
            // Ralentir pour s'arrêter à l'intersection.
            if(!ralentisPourArrêt){
                ralentissementPourArrêt = (véhicule.vitesse*véhicule.vitesse)/(2f*distanceInter);
                accélération -= ralentissementPourArrêt;
                ralentisPourArrêt = true;
                if(debug){System.out.println("Ralentis pour s'arrêter à l'intersection");}
            }else if(véhicule.vitesse > 0.1f){
                accélération -= ralentissementPourArrêt;
            }else if(véhicule.vitesse < 0.1f){
                véhicule.vitesse = 0.1f;
            }
        }else if(
            Math.abs(véhicule.vitesse) > 0f &&
            véhicule.vitesse != 0f &&
            distanceDest/véhicule.vitesse < 5f &&
            estDestination
        ){
            // Si on est à moins de 5 secondes d'arriver à destination,
            // Ralentir pour s'arrêter à l'intersection.
            if(!ralentisPourArrêt){
                ralentissementPourArrêt = (véhicule.vitesse*véhicule.vitesse)/(2f*distanceDest);
                accélération -= ralentissementPourArrêt;
                ralentisPourArrêt = true;
                if(debug){System.out.println("Ralentis pour s'arrêter à destination");}
            }else if(véhicule.vitesse > 0.1f){
                accélération -= ralentissementPourArrêt;
            }else if(véhicule.vitesse < 0.1f){
                véhicule.vitesse = 0.1f;
            }
        }else if(
            avant != null && 
            (( 
                avant.positionRelative*véhicule.routeActuelle.avoirLongueur() - avant.longueur/2f - ESPACEMENT_VOITURES + avant.vitesse*deltaTempsSecondes < véhicule.positionRelative*véhicule.routeActuelle.avoirLongueur() + véhicule.longueur/2f + véhicule.vitesse*deltaTempsSecondes &&
                avant.vitesse < véhicule.vitesse)
            || véhicule.routeActuelle.avoirLimiteEffective() < véhicule.vitesse)
        ){
            accélération += (Math.min(avant.vitesse,véhicule.routeActuelle.avoirLimiteEffective())-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Ralentis à cause d'un véhicule ou de la limite de vitesse");}

        }else if(
            avant != null && 
            (
                avant.vitesse > véhicule.vitesse || 
                avant.positionRelative*véhicule.routeActuelle.avoirLongueur() - avant.longueur/2f - ESPACEMENT_VOITURES + avant.vitesse*deltaTempsSecondes > véhicule.positionRelative*véhicule.routeActuelle.avoirLongueur() + véhicule.longueur/2f + véhicule.vitesse*deltaTempsSecondes
            ) && véhicule.routeActuelle.avoirLimiteEffective() > véhicule.vitesse
        ){
            if(avant.positionRelative*véhicule.routeActuelle.avoirLongueur() - avant.longueur/2f - ESPACEMENT_VOITURES + avant.vitesse*deltaTempsSecondes < véhicule.positionRelative*véhicule.routeActuelle.avoirLongueur() + véhicule.longueur/2f + véhicule.vitesse*deltaTempsSecondes){
                accélération += (Math.min(avant.vitesse,véhicule.routeActuelle.avoirLimiteEffective())-véhicule.vitesse)/deltaTempsSecondes;
            }else{
                accélération += (véhicule.routeActuelle.avoirLimiteEffective()-véhicule.vitesse)/deltaTempsSecondes;
            }
            if(debug){System.out.println("Accélère à la vitesse d'un véhicule ou de la limite de vitesse");}

        }else if(avant == null && véhicule.routeActuelle.avoirLimiteEffective()+1f < véhicule.vitesse){
            accélération += (véhicule.routeActuelle.avoirLimiteEffective()-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Ralentis à la limite de vitesse");}

        }else if(avant == null && véhicule.routeActuelle.avoirLimiteEffective()-1f > véhicule.vitesse){
            accélération += (véhicule.routeActuelle.avoirLimiteEffective()-véhicule.vitesse)/deltaTempsSecondes;
            if(debug){System.out.println("Accélère à la limite de vitesse");}

        }

        // Si on vas rentrer dans la voiture en avant.
        if (
            avant != null && 
            avant.positionRelative*véhicule.routeActuelle.avoirLongueur() - avant.longueur/2f - ESPACEMENT_VOITURES + avant.vitesse*deltaTempsSecondes < véhicule.positionRelative*véhicule.routeActuelle.avoirLongueur() + véhicule.longueur/2f + véhicule.vitesse*deltaTempsSecondes
        ){
            accélération -= 1f/Vec2.distance(avant.position(), véhicule.position());
            if(debug){System.out.println("Ralentis pour ne pas foncer dans la voiture en avant");}
        }

        véhicule.vitesse += Math.min(véhicule.ACCÉLÉRATION,Math.abs(accélération))*Math.signum(accélération)*deltaTempsSecondes;
        véhicule.vitesse = Math.max(véhicule.vitesse, 0);
        véhicule.avancer(deltaTempsSecondes);
        //if(debug){System.out.println(deltaTempsSecondes);}
        if(debug && System.currentTimeMillis()-tempsDebug > 200){System.out.println("Position relative :"+véhicule.positionRelative+" Vitesse : "+véhicule.vitesse*3.6f+" rue : "+véhicule.routeActuelle.nom+" destination : "+routine[indexeRoutine]+" indexeRoutine : "+indexeRoutine+" temps de trajet : "+(int)avoirTempsTrajetRestant()+"s");tempsDebug=System.currentTimeMillis();}
        // if((véhicule.estSensA && !véhicule.routeActuelle.véhiculesSensA.contains(véhicule))||(!véhicule.estSensA && !véhicule.routeActuelle.véhiculesSensB.contains(véhicule))){
        //     System.out.print("!");
        // }
    }

    protected Route chercherProchainTournant(boolean debug){
    /*
        if ((itinéraireActuel == null || System.currentTimeMillis()-tempsDernierRecalcul > CYCLE_RECALCUL) && !enAttenteDePlace){
            itinéraireActuel = AÉtoile.chercherChemin(véhicule.avoirAdresse(), routine[indexeRoutine], véhicule.estSensA);
            if(itinéraireActuel == null){
                return null;
            }
            posDest = itinéraireActuel[itinéraireActuel.length-1].avoirPosition(extraireNuméro(routine[indexeRoutine]));
            indexeRouteActuelle = 0;

            // Si on est sur la route de la destination, il n'y a pas de prochain tournant.
            if(itinéraireActuel.length == 1 && itinéraireActuel[0] == véhicule.routeActuelle){
                return itinéraireActuel[0];
            }

            if(véhicule.routeActuelle.intersectionA.routes.contains(itinéraireActuel[0])){
                if(!véhicule.routeActuelle.véhiculesSensA.contains(véhicule)){
                    if(!véhicule.routeActuelle.sensAPossèdePlace(véhicule.longueur)){
                        enAttenteDePlace = true;
                        return null;
                    }
                    Route routeActuelle = véhicule.routeActuelle;
                    routeActuelle.retirerVéhiculeSensB(véhicule);   // contient véhicule.routeActuelle = null
                    routeActuelle.ajouterVéhiculeSensA(véhicule);
                }
                véhicule.estSensA = true;
            }else if(véhicule.routeActuelle.intersectionB.routes.contains(itinéraireActuel[0])){
                if(!véhicule.routeActuelle.véhiculesSensB.contains(véhicule)){
                    if(!véhicule.routeActuelle.sensBPossèdePlace(véhicule.longueur)){
                        enAttenteDePlace = true;
                        return null;
                    }
                    Route routeActuelle = véhicule.routeActuelle;
                    routeActuelle.retirerVéhiculeSensA(véhicule);    // contient véhicule.routeActuelle = null
                    routeActuelle.ajouterVéhiculeSensB(véhicule);
                }
                véhicule.estSensA = false;
            }

            tempsDernierRecalcul = System.currentTimeMillis();
        }else if(enAttenteDePlace){
            if(véhicule.routeActuelle.intersectionA.routes.contains(itinéraireActuel[0])){
                if(!véhicule.routeActuelle.sensAPossèdePlace(véhicule.longueur)){
                    enAttenteDePlace = true;
                    return null;
                }
                Route routeActuelle = véhicule.routeActuelle;
                routeActuelle.retirerVéhiculeSensB(véhicule);   // contient véhicule.routeActuelle = null
                routeActuelle.ajouterVéhiculeSensA(véhicule);
            }else if(véhicule.routeActuelle.intersectionB.routes.contains(itinéraireActuel[0])){
                if(!véhicule.routeActuelle.sensBPossèdePlace(véhicule.longueur)){
                    enAttenteDePlace = true;                    
                    return null;
                }
                Route routeActuelle = véhicule.routeActuelle;
                routeActuelle.retirerVéhiculeSensA(véhicule);    // contient véhicule.routeActuelle = null
                routeActuelle.ajouterVéhiculeSensB(véhicule);
            }

            tempsDernierRecalcul = System.currentTimeMillis();
            enAttenteDePlace = false;
        }

        itinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel,1.5f));
        miniItinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel,4.5f));

        Route retour = itinéraireActuel[indexeRouteActuelle];
        indexeRouteActuelle++;
        indexeRouteActuelle = indexeRouteActuelle%itinéraireActuel.length;
        return retour;
    */    
        if(véhicule.routeActuelle == null){
            return null;
        }

        if(routine == null){
            return null;
        }

        if(itinéraireActuel == null || System.currentTimeMillis()-tempsDernierRecalcul > CYCLE_RECALCUL+CYCLE_RECALCUL_DÉCALAGE){
            itinéraireActuel = AÉtoile.chercherChemin(véhicule.avoirAdresse(), routine[indexeRoutine], véhicule.estSensA);

            if(itinéraireActuel == null){
                return null;
            }

            posDest = itinéraireActuel[itinéraireActuel.length-1].avoirPosition(extraireNuméro(routine[indexeRoutine]));
            itinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel, 1.5f));
            miniItinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel, 4.5f));

            indexeRouteActuelle = -1;
            tempsDernierRecalcul = System.currentTimeMillis();
        }

        if(indexeRouteActuelle == itinéraireActuel.length - 1){
            return véhicule.routeActuelle;  // Si on est arrivé à destination, il n'y a pas de prochain tournant.
        }

        indexeRouteActuelle++;
        indexeRouteActuelle = indexeRouteActuelle%itinéraireActuel.length;
        return itinéraireActuel[indexeRouteActuelle];
    }

    protected void avancerRoutine(){
        indexeRoutine++;
        indexeRoutine = indexeRoutine%routine.length;
        indexeRouteActuelle = 0;
        itinéraireActuel = null;
    }

    public float avoirTempsTrajetRestant(){
        float temps = (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur()/véhicule.vitesse;
        for (int i = 0; i < itinéraireActuel.length; i++) {
            temps += itinéraireActuel[i].avoirLongueur()/itinéraireActuel[i].avoirLimiteEffective();
        }
        return temps;
    }
    
    protected int extraireNuméro(String adresse){
        char[] chiffres = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        String numéro = "";
        for(int i = 0; i < adresse.length(); i++){
            for(int j = 0; j < chiffres.length; j++){
                if(adresse.charAt(i) == chiffres[j]){
                    numéro += adresse.charAt(i);
                    break;
                }
            }
        }
        return Integer.parseInt(numéro);
    }

    public Objet avoirItinéraire(){
        return itinéraireObjet;
    }

    public Objet avoirMiniItinéraire(){
        return miniItinéraireObjet;
    }
}
