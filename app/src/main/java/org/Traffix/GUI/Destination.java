package org.Traffix.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.checkerframework.checker.units.qual.t;

public class Destination extends JPanel {

    enum Type{
        DÉPART,
        ARRÊT,
        FIN
    }

    public Bouton attraper;
    public Bouton détruire;
    
    private RoundPane conteneurPrincipal;
    private RoundPane cercle;
    private JEditorPane nomPane;
    private JEditorPane tempsPane;
    private JPanel marge;

    private Type type;
    private String nom;
    private int durée;
    private int heureArrivée;

    public Destination(String nom, Type type){
        super();

        this.type = type;
        this.nom = nom;
        this.durée = 0;
        Calendar date = Calendar.getInstance();
        this.heureArrivée = date.get(Calendar.SECOND) + date.get(Calendar.MINUTE)*60 + date.get(Calendar.HOUR)*3600 + this.durée;

        setLayout(new BorderLayout());
        setOpaque(false);

        conteneurPrincipal = new RoundPane();
        conteneurPrincipal.borderRadius = 20;
        conteneurPrincipal.setBackground(Color.LIGHT_GRAY);
        conteneurPrincipal.setLayout(new BorderLayout());
        conteneurPrincipal.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        conteneurPrincipal.setPreferredSize(new Dimension(60,60));
        add(conteneurPrincipal, BorderLayout.CENTER);

        cercle = new RoundPane();
        cercle.borderRadius = 40;
        cercle.setPreferredSize(new Dimension(40,40));
        switch(type){
            case DÉPART:
                cercle.setBackground(Color.GREEN);
                break;
            case ARRÊT:
                cercle.setBackground(Color.ORANGE);
                break;
            case FIN:
                cercle.setBackground(Color.RED);
                break;
        }
        conteneurPrincipal.add(cercle,BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setOpaque(false);
        texte.setLayout(new GridLayout(1,2,0,10));

        nomPane = new JEditorPane("text/html","");
        nomPane.setOpaque(false);
        nomPane.setText("<h3 style='text-align:center'>"+nom+"</h3>");
        texte.add(nomPane);

        tempsPane = new JEditorPane("text/html","");
        tempsPane.setOpaque(false);
        texte.add(tempsPane);
        conteneurPrincipal.add(texte, BorderLayout.CENTER);

        JPanel boutons = new JPanel();
        boutons.setOpaque(false);
        boutons.setLayout(new GridLayout(1,2,0,10));
        boutons.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        détruire = new Bouton("❌");
        détruire.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        détruire.setBackground(new Color(0,0,0,0));
        détruire.setOpaque(false);
        détruire.setMargin(new Insets(5, 5, 5, 5));
        boutons.add(détruire);

        attraper = new Bouton("≣");
        attraper.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        attraper.setBackground(new Color(0,0,0,0));
        attraper.setOpaque(false);
        attraper.setMargin(new Insets(5, 5, 5, 5));
        boutons.add(attraper);

        conteneurPrincipal.add(boutons, BorderLayout.EAST);

        marge = new JPanel();
        marge.setBackground(new Color(0,0,0,0));
        marge.setOpaque(false);
        marge.setPreferredSize(new Dimension(10,10));
        add(marge, BorderLayout.SOUTH);
    }

    public void changerDurée(int duréeSec){
        this.durée = duréeSec;
        Calendar date = Calendar.getInstance();
        this.heureArrivée = date.get(Calendar.SECOND) + date.get(Calendar.MINUTE)*60 + date.get(Calendar.HOUR)*3600 + this.durée;
        this.heureArrivée = Math.floorMod(duréeSec, 86400); // S'assurer que l'heure d'arrivée ne dépasse pas 24h
        tempsPane.setText("<div style='text-align:center;'>"+formatterTemps(durée, false)+"<br>"+formatterTemps(this.heureArrivée, true)+"</div>");
    }

    private String formatterTemps(int tempsSec, boolean formatHeure){
        int tempsMin = Math.floorMod(tempsSec/60,60); //Temps en minutes
        int tempsH = Math.floorMod(tempsSec/3600, 60); //Temps en heures
        char sép = formatHeure?'h':':';
        return ""+tempsH+sép+tempsMin;
    }

    public void changerType(Type n_type){
        this.type = n_type;
        switch(type){
            case DÉPART:
                cercle.setBackground(Color.GREEN);
                break;
            case ARRÊT:
                cercle.setBackground(Color.ORANGE);
                break;
            case FIN:
                cercle.setBackground(Color.RED);
                break;
        }
    }
}
