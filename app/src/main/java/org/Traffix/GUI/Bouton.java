package org.Traffix.GUI;

import javax.swing.JButton;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;

public class Bouton extends JButton{
    
    public Bouton(){
        super();
        ajouterStyleDéfaut();
    }

    public Bouton(Action a){
        super(a);
        ajouterStyleDéfaut();
    }

    public Bouton(String text){
        super(text);
        ajouterStyleDéfaut();
    }

    public Bouton(Icon icon){
        super(icon);
        ajouterStyleDéfaut();
    }

    public Bouton(String text, Icon icon){
        super(text, icon);
        ajouterStyleDéfaut();
    }

    private void ajouterStyleDéfaut(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event){
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent event){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
