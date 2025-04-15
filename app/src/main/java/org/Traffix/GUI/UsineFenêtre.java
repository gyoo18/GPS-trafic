package org.Traffix.GUI;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UsineFenêtre {
    public static Fenêtre faireFenêtreGPS(){

        Fenêtre fenêtre = new Fenêtre();
        fenêtre.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenêtre.jframe.setSize(800,800);
        fenêtre.jframe.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent event){
                fenêtre.active = false;
            }

            @Override
            public void 
        });

        //fenêtre.jframe.setLayout(new BorderLayout());

        JLayeredPane couchesPrincipales = new JLayeredPane();
        // couchesPrincipales.setLayout(n);
        // couchesPrincipales.setOpaque(false);
        // couchesPrincipales.setBackground(new Color(0,0,0,0));
        fenêtre.jframe.add(couchesPrincipales);

        JPanel coucheBase = new JPanel();
        coucheBase.setSize(fenêtre.jframe.getSize());
        coucheBase.setLayout(new BorderLayout());
        couchesPrincipales.add(coucheBase,0);
        
        JPanel coucheMiniCarte = new JPanel();
        coucheMiniCarte.setSize(fenêtre.jframe.getSize());
        coucheMiniCarte.setOpaque(false);
        couchesPrincipales.add(coucheMiniCarte,JLayeredPane.MODAL_LAYER);
        
        JPanel carte = new JPanel();
        carte.setBackground(Color.RED);
        int carteLargeur = (int)(coucheBase.getSize().width * 0.8f);
        int carteHauteur = coucheBase.getSize().height;
        carte.setPreferredSize(new Dimension(carteLargeur, carteHauteur));
        coucheBase.add(carte, BorderLayout.CENTER);

        JPanel paramètres = new JPanel();
        paramètres.setBackground(Color.YELLOW);
        int paramètresLargeur = (int)(coucheBase.getSize().width * 0.2f);
        int paramètresHauteur = coucheBase.getSize().height;
        paramètres.setPreferredSize(new Dimension(paramètresLargeur, paramètresHauteur));
        coucheBase.add(paramètres, BorderLayout.EAST);
        return fenêtre;
    }
}
