package org.Traffix.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.Traffix.GUI.Destination.Type;
import org.Traffix.OpenGL.Caméra;
import org.Traffix.OpenGL.GLCanvas;
import org.Traffix.circulation.AÉtoile;
import org.Traffix.circulation.NavigateurManuel;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
import org.Traffix.circulation.NavigateurManuel.Commande;
import org.Traffix.maths.Mat4;
import org.Traffix.maths.Maths;
import org.Traffix.maths.Vec2;
import org.Traffix.maths.Vec3;
import org.checkerframework.checker.units.qual.min;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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
                    ((JLabel) fenêtre.obtenirÉlémentParID("adresseEntréeMessageErreur")).setText("");
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
                if((Boolean) fenêtre.avoirDrapeau("miniCarte minimisé") ){
                    ((JPanel) fenêtre.obtenirÉlémentParID("miniCarteCouches")).setBounds(30,30, fenêtre.jframe.getContentPane().getWidth()-60,fenêtre.jframe.getContentPane().getHeight()-60);
                    fenêtre.changerDrapeau("miniCarte minimisé", false);
                    ((JButton) fenêtre.obtenirÉlémentParID("boutonMiniCarte")).setText("🗗");
                } else {
                    Dimension jfdim = fenêtre.jframe.getContentPane().getSize();
                    int minTaille = Math.min(jfdim.width, jfdim.getSize().height);
                    ((JPanel) fenêtre.obtenirÉlémentParID("miniCarteCouches")).setBounds( (int)(jfdim.width * 0.8f - (minTaille * 0.15f)), (int)(jfdim.height * 0.75f - (minTaille * 0.15f)), (int)(minTaille * 0.3f), (int)(minTaille * 0.3f) );
                    fenêtre.changerDrapeau("miniCarte minimisé", true);
                    ((JButton) fenêtre.obtenirÉlémentParID("boutonMiniCarte")).setText("⛶");
                }
                fenêtre.jframe.revalidate();
                fenêtre.jframe.repaint();
            }
        });

        GLCanvas miniCarte = ((GLCanvas) fenêtre.obtenirÉlémentParID("GLCarte2"));
        miniCarte.canvas.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                float scroll = (float)e.getPreciseWheelRotation();
                Caméra cam = miniCarte.scène.caméra;
                cam.avoirVue().donnerRayon(Math.clamp( cam.avoirVue().avoirRayon()*(float)Math.pow(2.0,scroll), 20f, 2000f));
                cam.planProche = cam.avoirVue().avoirRayon() - 10f;
                cam.planLoin = cam.avoirVue().avoirRayon() + 10f;
                cam.refaireProjection();
                miniCarte.revalidate();
                miniCarte.repaint();
            }
        });

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                
                // Obtenir la position du curseur sur la carte
                Vec3 camPos = Mat4.mulV(miniCarte.scène.caméra.avoirVue().avoirInv(), new Vec3(0));
                Vec3 pointeurDir = Maths.curseurPosÀPointeur3D(miniCarte.scène.caméra, e.getX(), e.getY(), miniCarte.getWidth(), miniCarte.getHeight());
                Vec3 pointeurPos = Maths.intersectionPlan(new Vec3(0), new Vec3(0,1,0), pointeurDir, camPos);

                // miniCarte.scène.obtenirObjet("pointeur").avoirTransformée().positionner(pointeurPos);
                
                Vec2 positionCarte = new Vec2(pointeurPos.x,pointeurPos.z);
                String adresse = réseau.avoirAdresse(positionCarte);

                ((TexteEntrée) fenêtre.obtenirÉlémentParID("adresseEntrée")).setText(adresse);

                //TODO GestionnaireAccidents
            }
        };
        miniCarte.canvas.addMouseListener(ma);
        miniCarte.canvas.addMouseMotionListener(ma);

        ((JButton) fenêtre.obtenirÉlémentParID("boutonAccélérer")).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.ACCÉLÉRER);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
            }
        });

        ((JButton) fenêtre.obtenirÉlémentParID("boutonRalentir")).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.RALENTIR);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
            }
        });

        ((JButton) fenêtre.obtenirÉlémentParID("boutonTournerGauche")).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_GAUCHE);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
            }
        });

        ((JButton) fenêtre.obtenirÉlémentParID("boutonTournerDroit")).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_DROITE);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
            }
        });

        ((JButton) fenêtre.obtenirÉlémentParID("boutonDemiTour")).addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.DEMI_TOUR);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent k) {
                switch (k.getExtendedKeyCode()) {
                    case KeyEvent.VK_UP:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.ACCÉLÉRER);
                        break;
                    case KeyEvent.VK_DOWN:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.RALENTIR);
                        break;
                    case KeyEvent.VK_LEFT:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_GAUCHE);
                        break;
                    case KeyEvent.VK_RIGHT:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_DROITE);
                        break;
                    case KeyEvent.VK_SPACE:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.DEMI_TOUR);
                        break;
                    case KeyEvent.VK_UNDEFINED:
                        ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
                        break;
                }
                return false;
            }
        });
            //  @Override
            // public void keyPressed(KeyEvent k){
            //     switch (k.getExtendedKeyCode()) {
            //         case KeyEvent.VK_UP:
            //             ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.ACCÉLÉRER);
            //             break;
            //         case KeyEvent.VK_DOWN:
            //             ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.RALENTIR);
            //             break;
            //         case KeyEvent.VK_LEFT:
            //             ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_GAUCHE);
            //             break;
            //         case KeyEvent.VK_RIGHT:
            //             ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.TOURNER_DROITE);
            //             break;
            //         case KeyEvent.VK_SPACE:
            //             ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.DEMI_TOUR);
            //             break;
            //     }
            // }

        //     @Override
        //     public void keyReleased(KeyEvent k){
        //         switch(k.getExtendedKeyCode()){
        //             case KeyEvent.VK_UP:
        //             case KeyEvent.VK_DOWN:
        //             case KeyEvent.VK_LEFT:
        //             case KeyEvent.VK_RIGHT:
        //             case KeyEvent.VK_SPACE:
        //                 ((NavigateurManuel) réseau.véhicules[0].avoirNavigateur()).donnerCommande(Commande.LÂCHER);
        //                 break;
        //         }
        //     }
        // });
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

    public static void recalculerItinéraire(Fenêtre fenêtre, Réseau réseau){
        
        if(destinations.size() == 0){
            réseau.véhicules[0].avoirNavigateur().donnerRoutine(null);
            return;
        }

        destinations.get(0).changerType(Type.DÉPART);
        destinations.get(0).changerDurée(-1);
        for (int i = 1; i < destinations.size()-1; i++) {
            destinations.get(i).changerType(Type.ARRÊT);
        }
        destinations.getLast().changerType(Type.FIN);

        AÉtoile.chercherChemin(réseau.véhicules[0].avoirAdresse(), destinations.get(0).adresse, réseau.véhicules[0].estSensA);
        int tempsSec = AÉtoile.avoirDuréeDernierTrajetSec();
        destinations.get(0).changerDurée(tempsSec);
        for (int i = 1; i < destinations.size(); i++) {
            Route[] chemin = AÉtoile.chercherChemin(destinations.get(i-1).adresse, destinations.get(i).adresse, réseau.véhicules[0].estSensA);
            if(chemin == null){
                ((JLabel) fenêtre.obtenirÉlémentParID("adresseEntréeMessageErreur")).setText("Erreur : il n'existe aucun chemin entre "+destinations.get(i-1).adresse+" et "+destinations.get(i).adresse);
                break;
            }
            
            tempsSec += AÉtoile.avoirDuréeDernierTrajetSec();
            destinations.get(i).changerDurée(tempsSec);
        }

        String[] chemin = new String[destinations.size()];
        for (int i = 0; i < chemin.length; i++) {
            chemin[i] = destinations.get(i).adresse;
        }
        réseau.véhicules[0].avoirNavigateur().donnerRoutine(chemin);
    }
}
