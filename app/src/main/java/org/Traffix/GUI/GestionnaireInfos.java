package org.Traffix.GUI;

import javax.swing.JEditorPane;

import org.Traffix.circulation.Navigateur;
import org.Traffix.circulation.NavigateurManuel;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
import org.Traffix.circulation.Véhicule;
import org.Traffix.circulation.GestionnaireAccidents.Accident;
import org.Traffix.maths.Vec2;

public class GestionnaireInfos {
    private static Véhicule véhicule;
    private static NavigateurManuel navigateurManuel;
    private static Réseau réseau;

    private static Fenêtre fenêtre;

    private static JEditorPane infosTournant;
    private static JEditorPane infosTemps;

    private static long tempsDernièreMiseÀJour = System.currentTimeMillis();

    public static void init(Fenêtre fenêtre, Réseau réseau){
        GestionnaireInfos.fenêtre = fenêtre;
        GestionnaireInfos.réseau = réseau;

        GestionnaireInfos.infosTournant = (JEditorPane)fenêtre.obtenirÉlémentParID("carteInfoTournant");
        GestionnaireInfos.infosTemps = (JEditorPane)fenêtre.obtenirÉlémentParID("carteInfoTemps");

        GestionnaireInfos.véhicule = réseau.véhicules[0];
        GestionnaireInfos.navigateurManuel = (NavigateurManuel)véhicule.avoirNavigateur();
        GestionnaireInfos.réseau = réseau;
    }

    public static void miseÀJour(){

        if (System.currentTimeMillis()-tempsDernièreMiseÀJour < 1000){
            return;
        }
        tempsDernièreMiseÀJour = System.currentTimeMillis();

        // Mettre à jour les cartes de destinations
        GestionnaireContrôles.recalculerItinéraire(fenêtre, réseau);

        // Infos tournant
        if(navigateurManuel.avoirItinéraire() != null && navigateurManuel.avoirProchainTournant() != null){

            Route prochainTournant = navigateurManuel.avoirProchainTournant();
            Vec2 directionRoute = Vec2.sous(véhicule.routeActuelle.intersectionA.position,véhicule.routeActuelle.intersectionB.position).norm().mult(véhicule.estSensA?1f:-1f);
            Vec2 directionTournant = Vec2.sous(prochainTournant.intersectionA.position,prochainTournant.intersectionB.position).norm().mult(prochainTournant.intersectionB.avoirRoutes().contains(véhicule.routeActuelle)?1f:-1f);

            float orientation = directionTournant.x*directionRoute.y - directionTournant.y*directionRoute.x; // Produit vectoriel
            float alignement = Vec2.scal(directionRoute,directionTournant);

            String fichier = "textures/";
            if(
                (véhicule.estSensA && véhicule.routeActuelle.intersectionB.avoirRoutes().contains(prochainTournant))||
                (!véhicule.estSensA && véhicule.routeActuelle.intersectionA.avoirRoutes().contains(prochainTournant))
            ){
                                                                                        fichier += "demi-tour.png";}
            else if(alignement < -0.3827f                         && orientation < 0f) {fichier += "tourner-gauche_-45.png";}
            else if(alignement > -0.3827f && alignement < 0.3827f && orientation < 0f) {fichier += "tourner-gauche_90.png";}
            else if(alignement > 0.3827f && alignement < 0.9239f  && orientation < 0f) {fichier += "tourner-gauche_45.png";}
            else if(alignement < -0.3827f                         && orientation > 0f) {fichier += "tourner-droite_-45.png";}
            else if(alignement > -0.3827f && alignement < 0.3827f && orientation > 0f) {fichier += "tourner-droite_90.png";}
            else if(alignement > 0.3827f && alignement < 0.9239f  && orientation > 0f) {fichier += "tourner-droite_45.png";}
            else if(alignement > 0.9239f)                                              {fichier += "continuer.png";}

            infosTournant.setText(
                "<img width=200 height=auto src="+GestionnaireInfos.class.getClassLoader().getResource(fichier).toString()+">"+
                "<h2 width='200' style='text-align:center;'>"+prochainTournant.nom+"</h2>"
            );

            String texte = "<p>Temps restant/Heure d'arrivée : </p>"+GestionnaireContrôles.destinations.get(navigateurManuel.avoirIndexeRoutine()).avoirTempsHTML().replace("div", "h2");
            Accident maxAccident = véhicule.routeActuelle.avoirPireAccident();
            if (maxAccident != null){
                texte += "<p width=200 style='text-align:center'>Ralentis par :<br>"+maxAccident.description+"</p>";
            }
            infosTemps.setText(texte);

            infosTournant.revalidate();
            infosTournant.repaint();
        }else{
            infosTournant.setText("<h2 width='200' style='text-align:center;'>Veuillez entrer un itinéraire</h2>");
            infosTemps.setText("<h2 width='200' style='text-align:center;'>Veuillez entrer un itinéraire</h2>");
        }
    }
}
