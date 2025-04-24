package org.Traffix.GUI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.OverlayLayout;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.Traffix.OpenGL.GLCanvas;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UsineFenêtre {
    public static Fenêtre faireFenêtreGPS(){

        /*
         * ┌─────────────────────────────────────────────────────────────────────────────┐\
         * │┌─────────────────────────────────────────────────────────╥╤════════════════╗│▓\
         * ││┌─────────────────────────────────────────────────────┐  ║│           ┌───┐║│▓ \
         * │││                                                     │\ ║│           │ = │║│▓  \
         * │││ ╔════════════╤────────────────────────────────────────┐║└───────────╧═══╧╣│▓   \
         * │││ ║╭──────────╮│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░┊ │║╔═══════════════╤╣│▓    \
         * │││ ║│          ││░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░┊ │║║ ◯ Maison ~~ = │║│▓     \
         * │││ ║│          ││░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░┊ │║╟───────────────┘║│▓      \
         * │││ ║│   ┌─────────────────────────────────────────────────────────────────────────────┐
         * │││ ║╰───│┄┄┄┄┄┄╯┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆┆░░░░░░░░░░░░░░░┆┆┆░       │▓
         * │││ ║╭───│┄┄┄┄┄┄╮┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┘┆┆░       │▓
         * │││ ║╰───│┄┄┄┄┄┄╯┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┐┆┆░       │▓
         * │││ ║░░░░│▒▒▒▒▒▒▒┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆┆░░░░░░░░░░░░░░░┆┆┆░       │▓
         * │││ ║░░░░│▒▒▒▒▒▒▒┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┘┆┆░       │▓
         * │││ ║░░░░│▒▒▒▒▒▒▒┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆┆░░░░░░░░░░░░░░░░┆┆░       │▓
         * │││ ║░░░░│▒▒▒▒▒▒▒┆▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒┊ ┆┆┆░░░░░░░░░░░░░░░░┆┆░       │▓
         * ││└─║┈┈┈┈│┈┈┈┈┈┈┈┆┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┘ ┆┆┆░░░░░░░░░░░░░░░░┆┆░       │▓
         * ││ \║    │       ┆                                       \┆┆┆░░░░░░░░░░░░░░░░┆┆░       │▓
         * ││  ╚════│┄┄┄┄┄┄┄┴┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┘┆┆░░░░░░░░░░░░░░░░┆┆░       │▓
         * │└───────│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┴┴┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┘┆░       │▓
         * └────────│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┏━━━━━━━━━━━━━━┓┄┄┄┄┘░       │▓
         *  \▓▓▓▓▓▓▓│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░┃░░░░░░░░░░░░░░┃░░░░░░\      │▓          
         *   \      │                                                ┃░░░░░░░░░░░░░░┃       \     │▓          
         *    \     │                                                ┃░░░░░░░░░░░░░░┃        \    │▓          
         *     \    │                                                ┃░░░░░░░░░░░░░░┃         \   │▓          
         *      \   │                                                ┃░░░░░░░░░░░░░░┃          \  │▓          
         *       \  │                                                ┗━━━━━━━━━━━━━━┛           \ │▓          
         *        \ │                                                                            \│▓          
         *         \└─────────────────────────────────────────────────────────────────────────────┘▓          
         *           ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
         */

        ///////////////////////////////////////////////////////////////////////////////////////////////
        /// Structure principale                                                                    ///
        ///////////////////////////////////////////////////////////////////////////////////////////////
        Fenêtre fenêtre = new Fenêtre();
        fenêtre.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenêtre.jframe.setPreferredSize(new Dimension(800,800));
        fenêtre.jframe.setLayout(new BorderLayout());
        fenêtre.jframe.getContentPane().setBackground(new Color(1f,0f,1f));
        fenêtre.jframe.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent event){
                fenêtre.active = false;
            }
        });
        
        JPanel couchesPrincipales = new JPanel();
        couchesPrincipales.setOpaque(false);
        couchesPrincipales.setLayout(new OverlayLayout(couchesPrincipales));
        fenêtre.jframe.add(couchesPrincipales);

        JPanel coucheBase = new JPanel();
        coucheBase.setOpaque(false);
        coucheBase.setPreferredSize(fenêtre.jframe.getPreferredSize());
        coucheBase.setLayout(new BorderLayout());
        couchesPrincipales.add(coucheBase);
        fenêtre.ajouterÉlémentParID(coucheBase, "coucheBase");
        
        JPanel coucheDéplacement = new JPanel();
        coucheDéplacement.setOpaque(false);
        coucheDéplacement.setPreferredSize(fenêtre.jframe.getPreferredSize());
        coucheDéplacement.setLayout(new BorderLayout());
        coucheDéplacement.setMixingCutoutShape(coucheDéplacement.getBounds());  // Empêche les couches supérieures de cacher les objets « Heavyweight » des couches inférieures
        couchesPrincipales.add(coucheDéplacement);
        fenêtre.ajouterÉlémentParID(coucheDéplacement, "coucheDéplacement");
        
        JPanel coucheMiniCarte = new JPanel();
        coucheMiniCarte.setOpaque(false);
        coucheMiniCarte.setLayout(null);
        coucheMiniCarte.setMixingCutoutShape(coucheMiniCarte.getBounds());
        couchesPrincipales.add(coucheMiniCarte);

        // Réordonne les couches. Elles sont dessinée en ordre de z décroissant, donc 0 est en avant et +∞ est en arrière.
        couchesPrincipales.setComponentZOrder(coucheBase, 2);
        couchesPrincipales.setComponentZOrder(coucheDéplacement, 1);
        couchesPrincipales.setComponentZOrder(coucheMiniCarte, 0);

        //////////////////////////////////////////////////////////////////////////////////////////////
        /// Carte                                                                                  ///
        //////////////////////////////////////////////////////////////////////////////////////////////
        JPanel sectionCarte = new JPanel();
        sectionCarte.setOpaque(false);
        sectionCarte.setLayout(new BorderLayout());
        coucheBase.add(sectionCarte, BorderLayout.CENTER);

        JPanel carteCouches = new JPanel();
        carteCouches.setLayout(new OverlayLayout(carteCouches));
        sectionCarte.add(carteCouches, BorderLayout.CENTER);

        JPanel carteCoucheCarte = new JPanel();
        carteCoucheCarte.setOpaque(false);
        //carteCoucheCarte.setBackground(Color.RED);
        carteCoucheCarte.setLayout(new BorderLayout());
        carteCouches.add(carteCoucheCarte);

        GLCanvas carte = new GLCanvas();
        carte.setOpaque(false);
        carteCoucheCarte.add(carte,BorderLayout.CENTER);
        
        JPanel carteCoucheGUI = new JPanel();
        carteCoucheGUI.setOpaque(false);
        carteCoucheGUI.setLayout(new BorderLayout());
        carteCoucheGUI.setMixingCutoutShape(carteCoucheGUI.getBounds());
        carteCouches.add(carteCoucheGUI);

        carteCouches.setComponentZOrder(carteCoucheCarte, 1);
        carteCouches.setComponentZOrder(carteCoucheGUI, 0);

        JPanel carteInfosGauche = new JPanel();
        carteInfosGauche.setBackground(new Color(0,0,0,0));
        carteInfosGauche.setOpaque(false);
        carteInfosGauche.setMixingCutoutShape(carteInfosGauche.getBounds());
        carteInfosGauche.setLayout(new BorderLayout());
        carteCoucheGUI.add(carteInfosGauche, BorderLayout.WEST);

        JPanel carteInfosGaucheHaut = new JPanel();
        carteInfosGaucheHaut.setOpaque(false);
        carteInfosGaucheHaut.setLayout(new BoxLayout(carteInfosGaucheHaut, BoxLayout.Y_AXIS));
        carteInfosGaucheHaut.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        carteInfosGaucheHaut.setMixingCutoutShape(carteInfosGaucheHaut.getBounds());
        carteInfosGauche.add(carteInfosGaucheHaut, BorderLayout.NORTH);

        JPanel carteInfoTournantBoîte = new JPanel();
        //carteInfoTournantBoîte.borderRadius = 50;
        carteInfoTournantBoîte.setBackground(Color.GREEN);
        //carteInfoTournantBoîte.setMixingCutoutShape(carteInfoTournantBoîte.getBounds());  // TODO trouver un moyen d'acoir des coins ronds
        JEditorPane carteInfoTournant = new JEditorPane("text/html","");
        carteInfoTournant.setOpaque(false);
        carteInfoTournant.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));       // Crée un espace entre le contenu et les bords du contenant
        String path = UsineFenêtre.class.getClassLoader().getResource("test.png").toString();
        carteInfoTournant.setText(
            "<html>"+
            "<body>"+
            "<img width='200' height='auto' src='"+path+"'>"+
            "<h2 width='200' style='text-align:center;'>The quick brown fox jumps over the lazy dog.</h2>"+
            "</body>"+
            "</html>"
            );
        carteInfoTournantBoîte.add(carteInfoTournant);
        fenêtre.ajouterÉlémentParID(carteInfoTournant, "carteInfoTournant");
        carteInfosGaucheHaut.add(carteInfoTournantBoîte);

        // Crée un espace entre les deux boîtes de contenu
        JPanel espace = new JPanel();
        espace.setPreferredSize(new Dimension(0, 10));
        espace.setBackground(new Color(0,0,0,0));
        espace.setOpaque(false);
        espace.setMixingCutoutShape(espace.getBounds());
        carteInfosGaucheHaut.add(espace);

        JPanel carteInfoTempsBoîte = new JPanel();
        //carteInfoTempsBoîte.borderRadius = 50;
        carteInfoTempsBoîte.setBackground(Color.GREEN);
        carteInfoTempsBoîte.setMixingCutoutShape(carteInfoTempsBoîte.getBounds());
        JEditorPane carteInfoTemps = new JEditorPane("text/html","");
        carteInfoTemps.setOpaque(false);
        carteInfoTemps.setBackground(new Color(0,0,0,0));
        carteInfoTemps.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // Crée un espace entre le contenu et les bords du contenant
        carteInfoTemps.setText(
            "<html>"+
            "<body>"+
            "<h2 width='200' style='text-align:center;'>The quick brown fox jumps over the lazy dog.</h2>"+
            "</body>"+
            "</html>"
            );
        carteInfoTempsBoîte.add(carteInfoTemps);
        fenêtre.ajouterÉlémentParID(carteInfoTemps, "carteInfoTemps");
        carteInfosGaucheHaut.add(carteInfoTempsBoîte);

        JPanel carteInfosGaucheBas = new JPanel();
        carteInfosGaucheBas.setBackground(new Color(0,0,0,0));
        carteInfosGaucheBas.setOpaque(false);
        carteInfosGaucheBas.setMixingCutoutShape(carteInfosGaucheBas.getBounds());
        carteInfosGaucheBas.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        carteInfosGaucheBas.setLayout(new GridBagLayout());
        carteInfosGauche.add(carteInfosGaucheBas, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;

        Bouton boutonHaut = new Bouton("↑");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonHaut,gbc);
        fenêtre.ajouterÉlémentParID(boutonHaut, "boutonAccélérer");

        Bouton boutonGauche = new Bouton("←");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonGauche,gbc);
        fenêtre.ajouterÉlémentParID(boutonGauche, "boutonTournerGauche");
        
        Bouton boutonDroite = new Bouton("→");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonDroite,gbc);
        fenêtre.ajouterÉlémentParID(boutonDroite, "boutonTournerDroit");

        Bouton boutonBas = new Bouton("↓");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonBas,gbc);
        fenêtre.ajouterÉlémentParID(boutonBas, "boutonRalentir");

        //////////////////////////////////////////////////////////////////////////////////////////////
        /// Paramètres                                                                             ///
        //////////////////////////////////////////////////////////////////////////////////////////////
        JPanel sectionParamètres = new JPanel();
        sectionParamètres.setMinimumSize(new Dimension(800,100));
        sectionParamètres.setLayout(new BorderLayout());
        coucheBase.add(sectionParamètres, BorderLayout.EAST);

        JPanel menuDétailléBoîte = new JPanel();
        menuDétailléBoîte.setBackground(new Color(0,0,0,0));
        menuDétailléBoîte.setOpaque(false);
        menuDétailléBoîte.setLayout(new BorderLayout());
        menuDétailléBoîte.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        Bouton menuDétailléBouton = new Bouton("☰");
        menuDétailléBoîte.add(menuDétailléBouton, BorderLayout.EAST);
        sectionParamètres.add(menuDétailléBoîte,BorderLayout.NORTH);
        fenêtre.ajouterÉlémentParID(menuDétailléBouton, "menuDétailléBouton");

        JPanel B = new JPanel();                // Peu importe ce que j'essaie, les composantes à l'intérieur de BorderLayout.CENTER vont toujours être étiré pour prendre
        B.setBackground(new Color(0,0,0,0));    // le maximum de place possible. Afin d'y remédier, je met un nouveau BorderLayout et je met les éléments dans sa case NORTH
        B.setOpaque(false);
        B.setLayout(new BorderLayout());
        JPanel paramètresTrajets = new JPanel();
        paramètresTrajets.setBackground(new Color(0,0,0,0));
        paramètresTrajets.setOpaque(false);
        paramètresTrajets.setLayout(new BoxLayout(paramètresTrajets, BoxLayout.Y_AXIS));
        paramètresTrajets.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        sectionParamètres.add(B, BorderLayout.CENTER);
        B.add(paramètresTrajets,BorderLayout.NORTH);
        fenêtre.ajouterÉlémentParID(paramètresTrajets, "paramètresTrajets");

        JPanel adresseConteneur = new JPanel();
        adresseConteneur.setOpaque(false);
        adresseConteneur.setLayout(new BorderLayout());
        paramètresTrajets.add(adresseConteneur);

        TexteEntrée adresseEntrée = new TexteEntrée("Veuillez entrer une adresse.");
        adresseConteneur.add(adresseEntrée, BorderLayout.CENTER);
        fenêtre.ajouterÉlémentParID(adresseEntrée, "adresseEntrée");

        Bouton adresseChercherBouton = new Bouton("🔍");
        adresseChercherBouton.setBackground(new Color(0,0,0,0));
        adresseChercherBouton.setMargin(new Insets(5, 5, 5, 5));
        adresseChercherBouton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        adresseConteneur.add(adresseChercherBouton, BorderLayout.EAST);
        fenêtre.ajouterÉlémentParID(adresseChercherBouton, "adresseChercherBouton");

        JPanel espace2 = new JPanel();
        espace2.setBackground(new Color(0,0,0,0));
        espace2.setOpaque(false);
        espace2.setPreferredSize(new Dimension(10,10));
        paramètresTrajets.add(espace2);

        Destination destinationA = new Destination("Maison", Destination.Type.DÉPART);
        destinationA.changerDurée(3665);
        paramètresTrajets.add(destinationA);
        fenêtre.ajouterÉlémentParID(destinationA, "destinationA");

        Destination destinationB = new Destination("Maison", Destination.Type.ARRÊT);
        destinationB.changerDurée(3665);
        paramètresTrajets.add(destinationB);
        fenêtre.ajouterÉlémentParID(destinationB, "destinationB");

        Destination destinationC = new Destination("Maison", Destination.Type.FIN);
        destinationC.changerDurée(3665);
        paramètresTrajets.add(destinationC);
        fenêtre.ajouterÉlémentParID(destinationC, "destinationC");

        //////////////////////////////////////////////////////////////////////////////////////////////
        /// Mini-Carte                                                                             ///
        //////////////////////////////////////////////////////////////////////////////////////////////
        JPanel miniCarteCouches = new JPanel();
        miniCarteCouches.setLayout(new OverlayLayout(miniCarteCouches));
        miniCarteCouches.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10, true));
        miniCarteCouches.setBackground(new Color(0,0,0,0));
        miniCarteCouches.setOpaque(false);
        coucheMiniCarte.add(miniCarteCouches);

        JPanel miniCarteConteneur = new JPanel();
        miniCarteConteneur.setOpaque(false);
        miniCarteConteneur.setLayout(new BorderLayout());
        miniCarteCouches.add(miniCarteConteneur);
        fenêtre.ajouterÉlémentParID(miniCarteConteneur, "miniCarteConteneur");

        GLCanvas GLCanvas2 = new GLCanvas();
        GLCanvas2.setOpaque(false);
        miniCarteConteneur.add(GLCanvas2,BorderLayout.CENTER);

        JPanel boutonMiniCarteConteneur = new JPanel();
        boutonMiniCarteConteneur.setBackground(new Color(0,0,0,0));
        boutonMiniCarteConteneur.setOpaque(false);
        boutonMiniCarteConteneur.setMixingCutoutShape(boutonMiniCarteConteneur.getBounds());
        boutonMiniCarteConteneur.setLayout(new GridBagLayout());
        miniCarteCouches.add(boutonMiniCarteConteneur);

        miniCarteCouches.setComponentZOrder(miniCarteConteneur, 1);
        miniCarteCouches.setComponentZOrder(boutonMiniCarteConteneur, 0);

        Bouton boutonMiniCarte = new Bouton("⛶");
        boutonMiniCarte.setBackground(new Color(0f,0f,0f,0.1f));
        //boutonMiniCarte.setMixingCutoutShape(boutonMiniCarte.getBounds());
        boutonMiniCarte.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        boutonMiniCarte.setMargin(new Insets(4, 7, 4, 7));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        boutonMiniCarteConteneur.add(boutonMiniCarte, gbc);
        fenêtre.ajouterÉlémentParID(boutonMiniCarte, "boutonMiniCarte");

        // Les éléments qui ont une taille proportionnelle à leurs parents ne se mettent pas à jour automatiquement.
        fenêtre.jframe.getContentPane().addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent event){
                Dimension jfdim = fenêtre.jframe.getContentPane().getSize();
                coucheBase.setSize(jfdim);
                coucheMiniCarte.setSize(jfdim);
                Dimension sectionCarteTaille = new Dimension( (int)(coucheBase.getSize().width * 0.8f), coucheBase.getSize().height );
                sectionCarte.setPreferredSize(sectionCarteTaille);
                carteCoucheCarte.setSize(sectionCarteTaille);
                carteCoucheGUI.setSize(sectionCarteTaille);
                sectionParamètres.setPreferredSize( new Dimension( Math.max((int)(coucheBase.getSize().width * 0.2f),300), coucheBase.getSize().height ) );
                int minTaille = Math.min(coucheMiniCarte.getSize().width, coucheMiniCarte.getSize().height);
                miniCarteCouches.setBounds( (int)(jfdim.width * 0.8f - (minTaille * 0.15f)), (int)(jfdim.height * 0.75f - (minTaille * 0.15f)), (int)(minTaille * 0.3f), (int)(minTaille * 0.3f) );
                Dimension miniCarteDimension = new Dimension(miniCarteCouches.getSize().width-20, miniCarteCouches.getSize().height-20);
                miniCarteConteneur.setBounds(10,10,miniCarteDimension.width, miniCarteDimension.height);
                //boutonMiniCarteConteneur.setBounds(10,10,miniCarteDimension.width, miniCarteDimension.height);
                
                // Changer la taille des sous-éléments ne prend effet que lors du prochain changement de taille de la fenêtre :
                // les éléments sont alors en retard. La ligne suivante force la mise à jour de toutes les composantes.
                fenêtre.jframe.revalidate();
            }

        });

        fenêtre.jframe.pack();
        fenêtre.jframe.setVisible(true);
        Runnable renderLoop = new Runnable() {
			@Override
            public void run() {
				if (carte.canvas.isValid()) {
                    carte.canvas.render();
                }

                if (GLCanvas2.canvas.isValid()) {
                    GLCanvas2.canvas.render();
                }
                SwingUtilities.invokeLater(this);
			}
		};
		SwingUtilities.invokeLater(renderLoop);
        return fenêtre;
    }
}
