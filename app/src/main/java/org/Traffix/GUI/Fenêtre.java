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

    private HashMap<String,Object> éléments = new HashMap<>();

    private HashMap<String, Object> drapeaux = new HashMap<>();

    public Fenêtre(){}

    public void ajouterÉlémentParID(Object o, String ID){
        éléments.put(ID, o);
    }

    public Object obtenirÉlémentParID(String ID){
        return éléments.get(ID);
    }

    public void ajouterDrapeau(String nom, Object valeur){
        if(drapeaux.containsKey(nom)){
            changerDrapeau(nom, valeur);
        }
        drapeaux.put(nom, valeur);
    }

    public void changerDrapeau(String nom, Object valeur){
        if(!drapeaux.containsKey(nom)){
            ajouterDrapeau(nom, valeur);
        }
        drapeaux.put(nom, valeur);
    }

    public Object avoirDrapeau(String nom){
        if(!drapeaux.containsKey(nom)){
            System.err.println("[ERREUR] Le drapeau "+nom+" n'existe pas.");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.err.println(element);
            }
            return null;
        }
        return drapeaux.get(nom);
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
