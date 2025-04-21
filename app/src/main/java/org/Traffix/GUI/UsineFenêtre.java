package org.Traffix.GUI;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

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
        fenêtre.jframe.setSize(800,800);
        fenêtre.jframe.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent event){
                fenêtre.active = false;
            }
        });

        JLayeredPane couchesPrincipales = new JLayeredPane();
        fenêtre.jframe.add(couchesPrincipales);

        JPanel coucheBase = new JPanel();
        coucheBase.setLayout(new BorderLayout());
        couchesPrincipales.add(coucheBase,Integer.valueOf(0));

        JPanel coucheDéplacement = new JPanel();
        couchesPrincipales.add(coucheDéplacement, Integer.valueOf(1));
        
        JPanel coucheMiniCarte = new JPanel();
        coucheMiniCarte.setOpaque(false);
        coucheMiniCarte.setLayout(null);
        couchesPrincipales.add(coucheMiniCarte,Integer.valueOf(2));
        
        //////////////////////////////////////////////////////////////////////////////////////////////
        /// Carte                                                                                  ///
        //////////////////////////////////////////////////////////////////////////////////////////////
        JPanel sectionCarte = new JPanel();
        sectionCarte.setBackground(Color.RED);
        sectionCarte.setLayout(new BorderLayout());
        coucheBase.add(sectionCarte, BorderLayout.CENTER);

        JLayeredPane carteCouches = new JLayeredPane();
        sectionCarte.add(carteCouches, BorderLayout.CENTER);

        JPanel carteCoucheCarte = new JPanel();
        carteCoucheCarte.setBackground(Color.BLUE);
        carteCoucheCarte.setLayout(new BorderLayout());
        carteCouches.add(carteCoucheCarte, Integer.valueOf(0));

        Canvas carte = new Canvas();    // TODO remplacer par GLCanvas
        carteCoucheCarte.add(carte);
        
        JPanel carteCoucheGUI = new JPanel();
        carteCoucheGUI.setBackground(new Color(1f,1f,1f,1f));
        carteCoucheGUI.setOpaque(false);
        carteCoucheGUI.setLayout(new BorderLayout());
        carteCouches.add(carteCoucheGUI, Integer.valueOf(1));

        JPanel carteInfosGauche = new JPanel();
        carteInfosGauche.setBackground(new Color(0,0,0,0));
        carteInfosGauche.setOpaque(false);
        carteInfosGauche.setLayout(new BorderLayout());
        carteCoucheGUI.add(carteInfosGauche, BorderLayout.WEST);

        JPanel carteInfosGaucheHaut = new JPanel();
        carteInfosGaucheHaut.setBackground(new Color(0,0,0,0));
        carteInfosGaucheHaut.setOpaque(false);
        carteInfosGaucheHaut.setLayout(new BoxLayout(carteInfosGaucheHaut, BoxLayout.Y_AXIS));
        carteInfosGaucheHaut.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        carteInfosGauche.add(carteInfosGaucheHaut, BorderLayout.NORTH);

        RoundPane carteInfoTournantBoîte = new RoundPane();
        carteInfoTournantBoîte.borderRadius = 50;
        carteInfoTournantBoîte.setBackground(Color.GREEN);
        JEditorPane carteInfoTournant = new JEditorPane("text/html","");
        carteInfoTournant.setOpaque(false);
        carteInfoTournant.setBackground(new Color(0,0,0,0));
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
        carteInfosGaucheHaut.add(carteInfoTournantBoîte);

        // Crée un espace entre les deux boîtes de contenu
        JPanel espace = new JPanel();
        espace.setPreferredSize(new Dimension(0, 10));
        espace.setBackground(new Color(0,0,0,0));
        espace.setOpaque(false);
        carteInfosGaucheHaut.add(espace);

        RoundPane carteInfoTempsBoîte = new RoundPane();
        carteInfoTempsBoîte.borderRadius = 50;
        carteInfoTempsBoîte.setBackground(Color.GREEN);
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
        carteInfosGaucheHaut.add(carteInfoTempsBoîte);

        JPanel carteInfosGaucheBas = new JPanel();
        carteInfosGaucheBas.setBackground(new Color(0,0,0,0));
        carteInfosGaucheBas.setOpaque(false);
        carteInfosGaucheBas.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        carteInfosGaucheBas.setLayout(new GridBagLayout());
        carteInfosGauche.add(carteInfosGaucheBas, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton boutonHaut = new JButton("↑");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonHaut,gbc);

        JButton boutonGauche = new JButton("←");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonGauche,gbc);
        
        JButton boutonDroite = new JButton("→");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonDroite,gbc);

        JButton boutonBas = new JButton("↓");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        carteInfosGaucheBas.add(boutonBas,gbc);

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
        JButton menuDétailléBouton = new JButton("☰");
        menuDétailléBoîte.add(menuDétailléBouton, BorderLayout.EAST);
        sectionParamètres.add(menuDétailléBoîte,BorderLayout.NORTH);

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

        Destination destinationA = new Destination("Maison", Destination.TYPE.DÉPART);
        destinationA.changerDurée(3665);
        paramètresTrajets.add(destinationA);

        JPanel espace2 = new JPanel();
        espace2.setBackground(new Color(0,0,0,0));
        espace2.setOpaque(false);
        espace2.setPreferredSize(new Dimension(10,10));
        paramètresTrajets.add(espace2);

        Destination destinationB = new Destination("Maison", Destination.TYPE.ARRÊT);
        destinationB.changerDurée(3665);
        paramètresTrajets.add(destinationB);

        JPanel espace3 = new JPanel();
        espace3.setBackground(new Color(0,0,0,0));
        espace3.setOpaque(false);
        espace3.setPreferredSize(new Dimension(10,10));
        paramètresTrajets.add(espace3);

        Destination destinationC = new Destination("Maison", Destination.TYPE.FIN);
        destinationC.changerDurée(3665);
        paramètresTrajets.add(destinationC);

        //////////////////////////////////////////////////////////////////////////////////////////////
        /// Mini-Carte                                                                             ///
        //////////////////////////////////////////////////////////////////////////////////////////////
        JLayeredPane miniCarteCouches = new JLayeredPane();
        miniCarteCouches.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10, true));
        miniCarteCouches.setBackground(new Color(0,0,0,0));
        miniCarteCouches.setOpaque(false);
        coucheMiniCarte.add(miniCarteCouches);

        JPanel miniCarteConteneur = new JPanel();
        miniCarteConteneur.setBackground(Color.GREEN);
        miniCarteCouches.add(miniCarteConteneur, Integer.valueOf(0));

        Canvas GLCanvas2 = new Canvas();
        miniCarteConteneur.add(GLCanvas2);

        JPanel boutonMiniCarteConteneur = new JPanel();
        boutonMiniCarteConteneur.setBackground(new Color(0,0,0,0));
        boutonMiniCarteConteneur.setOpaque(false);
        boutonMiniCarteConteneur.setLayout(new GridBagLayout());
        miniCarteCouches.add(boutonMiniCarteConteneur, Integer.valueOf(1));

        JButton boutonMiniCarte = new JButton("⛶");
        boutonMiniCarte.setBackground(new Color(0f,0f,0f,0.1f));
        boutonMiniCarte.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        boutonMiniCarte.setMargin(new Insets(5, 7, 5, 7));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        boutonMiniCarteConteneur.add(boutonMiniCarte, gbc);

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
                sectionParamètres.setPreferredSize( new Dimension( Math.max((int)(coucheBase.getSize().width * 0.2f),250), coucheBase.getSize().height ) );
                int minTaille = Math.min(coucheMiniCarte.getSize().width, coucheMiniCarte.getSize().height);
                miniCarteCouches.setBounds( (int)(jfdim.width * 0.8f - (minTaille * 0.15f)), (int)(jfdim.height * 0.75f - (minTaille * 0.15f)), (int)(minTaille * 0.3f), (int)(minTaille * 0.3f) );
                Dimension miniCarteDimension = new Dimension(miniCarteCouches.getSize().width-20, miniCarteCouches.getSize().height-20);
                miniCarteConteneur.setBounds(10,10,miniCarteDimension.width, miniCarteDimension.height);
                boutonMiniCarteConteneur.setBounds(10,10,miniCarteDimension.width, miniCarteDimension.height);
                
                // Changer la taille des sous-éléments ne prend effet que lors du prochain changement de taille de la fenêtre :
                // les éléments sont alors en retard. La ligne suivante force la mise à jour de toutes les composantes.
                fenêtre.jframe.revalidate();
            }

        });

        return fenêtre;
    }
}
