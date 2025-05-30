package org.Traffix.OpenGL;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scène {

    public Caméra caméra = new Caméra();
    public ArrayList<Objet> objets = new ArrayList<>();
    public boolean estConstruite = false;

    public void ajouterObjet(Objet o){
        objets.add(o);
    }

    public void ajouterObjets(Objet[] o){
        objets.addAll(objets);
    }

    public Objet obtenirObjet(int ID){
        for (Objet o : objets){
            if (o.ID == ID){
                return o;
            }
        }
        return null;
    }

    public Objet obtenirObjet(String nom){
        for (Objet o : objets){
            if (o.nom.equals(nom)){
                return o;
            }
        }
        return null;
    }

    public void retirerObjet(Objet o){
        objets.remove(o);
    }

    public ArrayList<Objet> retirerObjet(String nom){
        ArrayList<Objet> retour = new ArrayList<>();
        for (int i = objets.size()-1; i >= 0; i--) {
            if(objets.get(i).nom.equals(nom)){
                retour.add(objets.remove(i));
            }
        }
        return retour;
    }

    public Objet[] obtenirObjets(int[] ID){
        LinkedList<Objet> résultatLL = new LinkedList<>();
        for (Objet o : objets){
            for (int i : ID){
                if (o.ID == i){
                    résultatLL.add(o);
                    break;
                }
            }
        }
        Objet[] résultat = new Objet[résultatLL.size()];
        int i = 0;
        for (Objet o : résultatLL){
            résultat[i] = o;
            i++;
        }
        return résultat;
    }

    public Objet[] obtenirObjets(String[] noms){
        LinkedList<Objet> résultatLL = new LinkedList<>();
        for (Objet o : objets){
            for (String nom : noms){
                if (o.nom.equals(nom)){
                    résultatLL.add(o);
                    break;
                }
            }
        }
        Objet[] résultat = new Objet[résultatLL.size()];
        int i = 0;
        for (Objet o : résultatLL){
            résultat[i] = o;
            i++;
        }
        return résultat;
    }

    public void surModificationFenêtre(float ratio){
        caméra.surFenêtreModifiée(ratio);
    }

    public void construireScène(){
        for (Objet o : objets){
            o.construire();
        }
        estConstruite = true;
    }
}
