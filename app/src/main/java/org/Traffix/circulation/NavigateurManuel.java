package org.Traffix.circulation;

import java.util.ArrayList;

import org.Traffix.OpenGL.GénérateurMaillage;
import org.Traffix.OpenGL.Objet;
import org.Traffix.maths.Vec2;

public class NavigateurManuel extends Navigateur {

    public enum Commande{
        ACCÉLÉRER,
        RALENTIR,
        TOURNER_DROITE,
        TOURNER_GAUCHE
    }

    private final int OPTION_GAUCHE = 0;
    private final int OPTION_DROIT = 1;
    private final int OPTION_DROITE = 2;

    private ArrayList<Commande> commandes = new ArrayList<>();
    private Route[] optionsTournants = new Route[3];
    private int optionChoisie = OPTION_DROIT;

    public NavigateurManuel(Véhicule véhicule) {
        super(véhicule);
    }

    public void miseÀJour(float deltaTempsSecondes, boolean debug){
        float accélération = 0;
        optionChoisie = OPTION_DROIT;
        if(commandes.size() > 0){
            Commande commande = commandes.remove(0);
            switch(commande){
                case ACCÉLÉRER:{
                    accélération = véhicule.ACCÉLÉRATION;
                    break;
                }
                case RALENTIR:{
                    accélération = -véhicule.ACCÉLÉRATION;
                    break;
                }
                case TOURNER_DROITE:{
                    optionChoisie = OPTION_DROITE;
                    break;
                }
                case TOURNER_GAUCHE:{
                    optionChoisie = OPTION_GAUCHE;
                    break;
                }
                default:{
                    throw new IllegalArgumentException("La valeur "+commande+" n'est pas supportée.");
                }
            }
        }

        Intersection interB = véhicule.estSensA?véhicule.routeActuelle.intersectionA:véhicule.routeActuelle.intersectionB;

        boolean estDestination = itinéraireActuel[itinéraireActuel.length-1]==prochainTournant;
        float distanceDest = Vec2.distance(véhicule.position(), posDest);

        float distanceInter = (1f-véhicule.positionRelative)*véhicule.routeActuelle.avoirLongueur();

        if(distanceDest < 0.1f && estDestination){
            // Si le véhicule est arrivé à destination.
            avancerRoutine();
            super.chercherProchainTournant();
        }else if(distanceInter < 0.1f && !estDestination){
            // Si le véhicule est à l'intersection.
            if(
                interB.peutEngager(véhicule.routeActuelle, optionsTournants[optionChoisie]) && // S'il a le droit de s'engager dans l'intersection
                ((interB==optionsTournants[optionChoisie].intersectionA && optionsTournants[optionChoisie].sensBPossèdePlace(véhicule.longueur)) || // Si le prochain tournant a la place restante
                 (interB==optionsTournants[optionChoisie].intersectionB && optionsTournants[optionChoisie].sensAPossèdePlace(véhicule.longueur)))  // nécessaire pour que le véhicule s'engage.
            ){
                if(véhicule.estSensA){
                    véhicule.routeActuelle.retirerVéhiculeSensA();
                }else{
                    véhicule.routeActuelle.retirerVéhiculeSensB();
                }

                if(interB == prochainTournant.intersectionA){
                    prochainTournant.ajouterVéhiculeSensB(véhicule);
                }else{
                    prochainTournant.ajouterVéhiculeSensA(véhicule);
                }

                véhicule.estSensA = interB == prochainTournant.intersectionB;
                véhicule.positionRelative = 0;
                véhicule.routeActuelle = prochainTournant;
                calculerOptions();
                super.chercherProchainTournant();
            }
        }

        véhicule.vitesse += Math.min(véhicule.ACCÉLÉRATION,Math.abs(accélération))*Math.signum(accélération)*deltaTempsSecondes;
        véhicule.avancer(deltaTempsSecondes);
    }

    protected void calculerOptions() {
        Intersection interB = véhicule.estSensA?véhicule.routeActuelle.intersectionA:véhicule.routeActuelle.intersectionB;

        if(interB.routes.size() == 1){
            optionsTournants = null;
        }

        Vec2 dirActuel = Vec2.sous(véhicule.routeActuelle.intersectionA.position, véhicule.routeActuelle.intersectionB.position).norm().mult(véhicule.estSensA?1f:-1f);
        float[] alignements = new float[interB.routes.size()];
        float[] orientations = new float[interB.routes.size()];

        // Calculer l'alignement et l'orientation de chaque route de l'intersection
        for (int i = 0; i < alignements.length; i++) {
            // Sauter si elle correspond à la route d'origine.
            if(interB.routes.get(i) == véhicule.routeActuelle){
                alignements[i] = Float.MAX_VALUE;
                orientations[i] = Float.MAX_VALUE;
                continue;
            }
            Vec2 dirB = Vec2.sous(interB.routes.get(i).intersectionA.position, interB.routes.get(i).intersectionB.position).norm().mult(interB==interB.routes.get(i).intersectionA?-1f:1f);
            alignements[i] = Vec2.scal(dirActuel, dirB);    // Produit scalaire
            orientations[i] = dirB.x*dirActuel.y - dirB.y*dirActuel.x;  // Produit vectoriel
        }

        // Sélectionner la route la plus à droite, la plus à gauche et la plus alignée avec le centre.
        float maxAlignement = -Float.MAX_VALUE;
        float maxDroite = -Float.MAX_VALUE;
        float maxGauche = -Float.MAX_VALUE;
        int maxAlignementI = -1;
        int maxDroiteI = -1;
        int maxGaucheI = -1;
        for (int i = 0; i < alignements.length; i++) {
            if(alignements[i] >= Float.MAX_VALUE){
                continue;
            }

            if(alignements[i] > maxAlignement){
                maxAlignement = alignements[i];
                maxAlignementI = i;
            }
            if(orientations[i] > maxDroite){
                maxDroite = orientations[i];
                maxDroiteI = i;
            }
            if(-orientations[i] > maxGauche){
                maxGauche = orientations[i];
                maxGaucheI = i;
            }
        }

        optionsTournants[OPTION_GAUCHE] = interB.routes.get(maxGaucheI);
        optionsTournants[OPTION_DROIT] = interB.routes.get(maxAlignementI);
        optionsTournants[OPTION_DROITE] = interB.routes.get(maxDroiteI);
    }

    @Override
    protected Route chercherProchainTournant(){
        if (itinéraireActuel == null){
            itinéraireActuel = AÉtoile.chercherChemin(véhicule.avoirAdresse(), routine[indexeRoutine]);
            posDest = itinéraireActuel[itinéraireActuel.length-1].avoirPosition(extraireNuméro(routine[indexeRoutine]));
            indexeRouteActuelle = 0;

            itinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel));
        }

        if(System.currentTimeMillis()-tempsDernierRecalcul > CYCLE_RECALCUL){
            itinéraireActuel = AÉtoile.chercherChemin(véhicule.avoirAdresse(), routine[indexeRoutine]);
            tempsDernierRecalcul = System.currentTimeMillis();
            indexeRouteActuelle = 0;

            itinéraireObjet.donnerMaillage(GénérateurMaillage.faireMaillageItinéraire(itinéraireActuel));
        }

        Route retour = itinéraireActuel[indexeRouteActuelle];
        indexeRouteActuelle++;
        return retour;
    }
}
