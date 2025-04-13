package org.Traffix.GUI;

import java.util.Map;

public class Parseur {
    
    public static void extraireFenêtre(String chemin){

    }

    private static Map<String,JsonObjet> extraireJson(String json){
        String[] jsonJetons = extraireJetons(json);
        if(jsonJetons.length == 0){
            System.err.println("[ERREUR] Parseur.extraireJSON : le fichier est vide.");
            return null;
        }
        if (jsonJetons[0] == "{"){
            return extraireJsonDict(jsonJetons, 0);
        }else{
            System.err.println("[ERREUR] Parseur.extraireJSON : jeton inattendus : "+jsonJetons[0]+", le fichier doit commencer avec un dictionnaire.");
            return null;
        }
    }

    private static String[] extraireJetons(String json){return null;}
    private static void extraireJsonObjet(String[] json, int i){}
    private static Map<String,JsonObjet> extraireJsonDict(String[] json, int i){return null;}
    private static void extraireJsonListe(String[] json, int i){}
}

enum TYPE{
    NULL,
    BOOL,
    INT,
    FLOAT,
    STRING,
    LISTE,
    DICT
}

class JsonObjet{
    public TYPE type;
    public Object valeur;
}