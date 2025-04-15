package org.Traffix;

import javax.swing.JFrame;

import org.Traffix.GUI.Fenêtre;
import org.Traffix.GUI.UsineFenêtre;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Fenêtre fenêtre = UsineFenêtre.faireFenêtreGPS();
        fenêtre.jframe.setVisible(true);


        while(fenêtre.active){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        System.out.println("Goodbye World!");
    }
}
