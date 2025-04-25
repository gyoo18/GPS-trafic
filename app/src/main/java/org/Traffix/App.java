package org.Traffix;

import javax.swing.JFrame;

import org.Traffix.circulation.UsineRéseau;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        UsineRéseau.enregistrerRéseauEnOBJ(UsineRéseau.générerRéseau());
        System.out.println("Goodbye World!");
    }
}
