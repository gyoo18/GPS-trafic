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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.Traffix.GUI.Destination.Type;
import org.Traffix.OpenGL.Caméra;
import org.Traffix.OpenGL.GLCanvas;
import org.Traffix.circulation.AÉtoile;
import org.Traffix.circulation.Route;
import org.Traffix.circulation.Réseau;
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
                cam.planProche = 1f; //cam.avoirVue().avoirRayon() - 10f;
                cam.planLoin = 2000f; //cam.avoirVue().avoirRayon() + 10f;
                cam.refaireProjection();
                miniCarte.revalidate();
                miniCarte.repaint();
            }
        });

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                
                Caméra caméra = miniCarte.scène.caméra;
                float xPosRel = (float)e.getX()/(float)miniCarte.getWidth(); // Position du curseur, relative à la miniCarte
                float yPosRel = (float)e.getY()/(float)miniCarte.getHeight();
                float ratio = (float)miniCarte.getWidth()/(float)miniCarte.getHeight();

                // Obtenir la position du curseur sur la carte

                // Comme la transformée de la caméra est en mode Orbite, caméra.avoirPos() renvoie le centre d'orbite.
                // Il faut donc manuellement calculer la position de la caméra.
                Vec3 camPos = Mat4.mulV(caméra.avoirVue().avoirInv(), new Vec3(0f));

                // Construction du vecteur qui pointe dans la direction du curseur
                // Direction vers laquelle pointe la caméra
                Vec3 camDir = Vec3.sous(caméra.avoirPos(),camPos).norm();
                // Transformation de la position relative du curseur sur un plan en 3D à planProche de
                // distance en face de la caméra
                float posX =  (float)Math.tan((Math.PI/180f)*caméra.FOV/2f)*(xPosRel*2f - 1f)*caméra.planProche;
                float posY = -(float)Math.tan((Math.PI/180f)*caméra.FOV/2f)*(yPosRel*2f - 1f)*caméra.planProche/ratio;
                // Création du vecteur normal qui pointe dans la direction du curseur
                Vec3 pointeurDir = new Vec3(posX,posY,caméra.planProche).norm();
                // Il faut maintenant orienter le vecteur dans l'orientation de la caméra avec une transformation matricielle
                Vec3 Z = camDir; // Vecteur Z de la caméra
                Vec3 X = new Vec3((float)Math.cos(caméra.avoirRot().y),0,(float)-Math.sin(caméra.avoirRot().y)); // Vecteur X de la caméra
                Vec3 Y = Vec3.croix(Z, X);  // Vecteur Y de la caméra
                Mat4 rotation = new Mat4(new float[]{
                    X.x, X.y, X.z, 0,
                    Y.x, Y.y, Y.z, 0,
                    Z.x, Z.y, Z.z, 0,
                    0,   0,   0,   1
                });     // Matrice de transformation de l'espace vue vers l'espace univers
                pointeurDir = Mat4.mulV(rotation, pointeurDir); // Multiplication matricielle

                Vec3 pointeurPos = Maths.intersectionPlan(new Vec3(0), new Vec3(0,1,0), pointeurDir, camPos);
                
                Vec2 positionCarte = new Vec2(pointeurPos.x,pointeurPos.z);
                String adresse = réseau.avoirAdresse(positionCarte);

                ((TexteEntrée) fenêtre.obtenirÉlémentParID("adresseEntrée")).setText(adresse);
            }
        };
        miniCarte.canvas.addMouseListener(ma);
        miniCarte.canvas.addMouseMotionListener(ma);
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
