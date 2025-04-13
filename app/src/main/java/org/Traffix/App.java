package org.Traffix;

import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {

    static boolean estFermée = false;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setSize(800,800);

        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event){
                App.estFermée = true;
            }
        });

        jframe.setVisible(true);



        while(!estFermée){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        System.out.println("Goodbye World!");
    }
}
