package org.Traffix.GUI;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class TexteEntrée extends JTextField {
    
    public TexteEntrée(){
        super();
        ajouterStyleDéfaut();
    }

    public TexteEntrée(String texte){
        super(texte);
        ajouterStyleDéfaut();
    }

    public TexteEntrée(int colonnes){
        super(colonnes);
        ajouterStyleDéfaut();
    }

    public TexteEntrée(String texte, int colonnes){
        super(texte, colonnes);
        ajouterStyleDéfaut();
    }

    public TexteEntrée(Document doc, String texte, int colonnes){
        super(doc, texte, colonnes);
        ajouterStyleDéfaut();
    }

    private void ajouterStyleDéfaut(){
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent event){
                setCursor(new Cursor(Cursor.TEXT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent event){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
