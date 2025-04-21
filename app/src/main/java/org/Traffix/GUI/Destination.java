package org.Traffix.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.checkerframework.checker.units.qual.t;

public class Destination extends RoundPane {

    enum TYPE{
        DÉPART,
        ARRÊT,
        FIN
    }
    
    private RoundPane cercle;
    private JEditorPane nomPane;
    private JEditorPane tempsPane;
    private JButton bouton;

    private TYPE type;
    private String nom;
    private int durée;
    private int heureArrivée;

    public Destination(String nom, TYPE type){
        super();

        this.type = type;
        this.nom = nom;
        this.durée = 0;
        Calendar date = Calendar.getInstance();
        this.heureArrivée = date.get(Calendar.SECOND) + date.get(Calendar.MINUTE)*60 + date.get(Calendar.HOUR)*3600 + this.durée;

        borderRadius = 20;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setPreferredSize(new Dimension(60,60));

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
        add(cercle,BorderLayout.WEST);

        JPanel texte = new JPanel();
        texte.setBackground(new Color(0,0,0,0));
        texte.setOpaque(false);
        texte.setLayout(new GridLayout(1,2,0,10));

        nomPane = new JEditorPane("text/html","");
        nomPane.setBackground(new Color(0,0,0,0));
        nomPane.setOpaque(false);
        nomPane.setText("<h3 style='text-align:center'>"+nom+"</h3>");
        texte.add(nomPane);

        tempsPane = new JEditorPane("text/html","");
        tempsPane.setBackground(new Color(0,0,0,0));
        tempsPane.setOpaque(false);
        texte.add(tempsPane);
        add(texte, BorderLayout.CENTER);

        bouton = new JButton("≣");
        bouton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        bouton.setBackground(new Color(0,0,0,0));
        bouton.setOpaque(false);
        add(bouton, BorderLayout.EAST);
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
}
