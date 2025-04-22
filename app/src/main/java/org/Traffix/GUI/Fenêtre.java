package org.Traffix.GUI;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

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
}
