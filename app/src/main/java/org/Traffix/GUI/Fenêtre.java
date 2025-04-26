package org.Traffix.GUI;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.Traffix.OpenGL.GLCanvas;

public class Fenêtre {
    public boolean active = true;

    public JFrame jframe = new JFrame();

    private Map<String,Object> éléments = new HashMap<>();

    public Fenêtre(){}

    public void ajouterÉlémentParID(Object o, String ID){
        éléments.put(ID, o);
    }

    public Object obtenirÉlémentParID(String ID){
        return éléments.get(ID);
    }

    public void fermer(){
        Component[] cs = jframe.getComponents();
        for (Component c : cs) {
            if(c instanceof GLCanvas){
                ((GLCanvas)c).détruire();
            }
            if(c instanceof Container){
                fermerComposantes((Container)c);
            }
        }
    }

    private void fermerComposantes(Container component){
        Component[] cs = component.getComponents();
        for (Component c : cs) {
            if(c instanceof GLCanvas){
                ((GLCanvas)c).détruire();
            }
            if(c instanceof Container){
                fermerComposantes((Container)c);
            }
        }
    }
}
