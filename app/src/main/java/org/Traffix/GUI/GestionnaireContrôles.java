package org.Traffix.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.Traffix.GUI.Destination.Type;
import org.Traffix.circulation.AÉtoile;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
import org.Traffix.maths.Vec2;
import java.awt.event.MouseEvent;

public class GestionnaireContrôles {

    public static ArrayList<Destination> destinations = new ArrayList<>();

    public static void initialiserGPS(Fenêtre fenêtre, Réseau réseau){
        ((JButton) fenêtre.obtenirÉlémentParID("adresseChercherBouton")).addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String adresse = ((TexteEntrée) fenêtre.obtenirÉlémentParID("adresseEntrée")).getText();
                Vec2 pos = réseau.avoirPosition(adresse);
                if(pos == null){
                    ((JLabel) fenêtre.obtenirÉlémentParID("adresseEntréeMessageErreur")).setText("Impossible de trouver l'adresse spécifiée.");
                }else{
                    ((TexteEntrée) fenêtre.obtenirÉlémentParID("adresseEntrée")).setText("");
                    ((JLabel) fenêtre.obtenirÉlémentParID("adresseEntréeMessageErreur")).setText("Adresse Trouvée");
                    JPanel liste = (JPanel) fenêtre.obtenirÉlémentParID("paramètresTrajets");
                    ajouterDestination(new Destination(adresse, adresse, Type.FIN), liste, fenêtre, réseau);
                    liste.revalidate();
                    liste.repaint();
                }
            }            
        });

        ((JButton) fenêtre.obtenirÉlémentParID("boutonMiniCarte")).addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                ((JPanel) fenêtre.obtenirÉlémentParID("miniCarteCouches")).setBounds(30,30, fenêtre.jframe.getContentPane().getWidth()-60,fenêtre.jframe.getContentPane().getHeight()-60);
                fenêtre.changerDrapeau("miniCarte minimisé", false);
                ((JButton) fenêtre.obtenirÉlémentParID("adresseChercherBouton")).setText("");
                fenêtre.jframe.revalidate();
                fenêtre.jframe.repaint();
            }
        });
    }

    private static void ajouterDestination(Destination destination, JPanel liste, Fenêtre fenêtre, Réseau réseau){
        liste.add(destination);
        if(destinations.size() == 0){
            destination.changerType(Type.DÉPART);
        }else if(destinations.size() > 1){
            destinations.getLast().changerType(Type.ARRÊT);
        }
        destinations.add(destination);
        destination.détruire.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                liste.remove(destination);
                destinations.remove(destination);
                destination.détruire.removeActionListener(this);
                liste.revalidate();
                liste.repaint();
                recalculerItinéraire(fenêtre, réseau);
            }
        });
        MouseAdapter ma = new MouseAdapter() {
            JPanel espace = null;
            int indexe = -1;
            JPanel coucheDéplacement = null;

            @Override
            public void mousePressed(MouseEvent e) {
                destination.estAttrapé = true;

                Component[] éléments = liste.getComponents();
                for (int i = 0; i < éléments.length; i++) {
                    if(éléments[i]==destination){
                        indexe = i;
                        break;
                    }
                }

                coucheDéplacement = (JPanel) fenêtre.obtenirÉlémentParID("coucheDéplacement");
                coucheDéplacement.add(destination);

                MouseEvent c = SwingUtilities.convertMouseEvent(destination.attraper, e, destination.getParent());
                Point X = SwingUtilities.convertPoint(liste, new Point(liste.getX(), liste.getY()), coucheDéplacement);
                destinations.get(0).setBounds(X.x+10,c.getY()-35,280,70);

                coucheDéplacement.revalidate();
                coucheDéplacement.repaint();

                espace = new JPanel();
                espace.setBackground(Color.LIGHT_GRAY);
                espace.setPreferredSize(new Dimension(10,10));
                espace.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                liste.add(espace,indexe);

                liste.remove(destination);
                liste.revalidate();
                liste.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                destination.estAttrapé = false;

                liste.remove(espace);
                liste.add(destination, indexe);

                coucheDéplacement.remove(destination);

                liste.revalidate();
                liste.repaint();
                coucheDéplacement.revalidate();
                coucheDéplacement.repaint();

                recalculerItinéraire(fenêtre, réseau);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(destination.estAttrapé){
                    MouseEvent c = SwingUtilities.convertMouseEvent(destination.attraper, e, destination.getParent());
                    Point X = SwingUtilities.convertPoint(liste, new Point(liste.getX(), liste.getY()), coucheDéplacement);
                    destination.setBounds(X.x+10,c.getY()-35,280,70);
                    coucheDéplacement.revalidate();
                    coucheDéplacement.repaint();

                    MouseEvent Y = SwingUtilities.convertMouseEvent(destination.attraper, e, liste);
                    if(indexe-4 != destinations.size() && Y.getY() > destinations.getLast().getY()){
                        liste.remove(espace);
                        liste.add(espace);
                        destinations.remove(indexe-3);
                        destinations.add(destination);
                        indexe = destinations.size()+2;
                        liste.revalidate();
                        liste.repaint();
                    }else{
                        for (int i = 0; i < destinations.size(); i++) {
                            if(i != indexe-3 && destinations.get(i).getY() > Y.getY()){
                                if(i != indexe-2){
                                    liste.remove(espace);
                                    liste.add(espace,i+3);
                                    destinations.remove(indexe-3);
                                    destinations.add(i,destination);
                                    indexe = i+3;
                                    liste.revalidate();
                                    liste.repaint();
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
        destination.attraper.addMouseListener(ma);
        destination.attraper.addMouseMotionListener(ma);

        recalculerItinéraire(fenêtre, réseau);
    }

    private static void recalculerItinéraire(Fenêtre fenêtre, Réseau réseau){
        // TODO coupler avec le Navigateur manuel
        destinations.get(0).changerType(Type.DÉPART);
        destinations.get(0).changerDurée(-1);
        for (int i = 1; i < destinations.size()-1; i++) {
            destinations.get(i).changerType(Type.ARRÊT);
        }
        destinations.getLast().changerType(Type.FIN);

        int tempsSec = 0;
        for (int i = 1; i < destinations.size(); i++) {
            Route[] chemin = AÉtoile.chercherChemin(destinations.get(i-1).adresse, destinations.get(i).adresse);
            if(chemin == null){
                ((JLabel) fenêtre.obtenirÉlémentParID("adresseEntréeMessageErreur")).setText("Erreur : il n'existe aucun chemin entre "+destinations.get(i-1).adresse+" et "+destinations.get(i).adresse);
                break;
            }
            
            tempsSec += AÉtoile.avoirDuréeDernierTrajetSec();
            destinations.get(i).changerDurée(tempsSec);
        }
    }
}
