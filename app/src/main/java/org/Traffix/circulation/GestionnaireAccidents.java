package org.Traffix.circulation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.Traffix.maths.Vec2;

public class GestionnaireAccidents {

    private static ArrayList<Accident> listeAccidents = new ArrayList<>();
    private static Réseau réseau = null;

    public static class Accident {

        public final float pourcentageRalentissement; // Pourcentage entre 0.0 (pas de ralentissement) et 1.0 (arrêt complet)
        public final float durée;
        public float temps = 0f;

        public final String description;
        public final TypeAccident type;

        public enum TypeAccident {
            ACCIDENT_VÉHICULE("Collision entre véhicules"),
            TRAVAUX("Travaux de construction"),
            INTEMPÉRIE("Intempérie"),
            MANIFESTATION("Manifestation"),
            VÉHICULE_EN_PANNE("Véhicule en panne");
    
            final String description;
    
            TypeAccident(String description) {
                this.description = description;
            }
    
        }

        private ArrayList<Route> routesAffectés;
        private Vec2 position;
        private float rayon;

        public Accident(TypeAccident type, Vec2 position, float durée, float pourcentageRalentissement) {
            this.type = type;
            this.position = position;
            this.rayon = rayon;
            this.durée = durée;
            this.pourcentageRalentissement = pourcentageRalentissement;
            
            if(GestionnaireAccidents.réseau == null){
                throw new RuntimeException("[ERREUR] Le gestionnaire d'accidents ne possède pas de réseau.");
            }

            ArrayList<Route> routes = GestionnaireAccidents.réseau.routes;
            for(int i = 0; i < routes.size(); i++){
                if()
            }
        }


        // Constructeur pour compatibilité avec le code existant qui n'utilise qu'une route
        public Accident(TypeAccident type, Route route, float position, double pourcentageRalentissement) {
            this.type = type;
            this.routesAffectés = new ArrayList<>();
            this.routesAffectés.add(route);
            this.position = position;
            this.pourcentageRalentissement = Math.max(0.0, Math.min(1.0, pourcentageRalentissement)); // Entre 1 et 5
            this.actif = true;
            générerDescription();
        }
        
        private void générerDescription() {
            StringBuilder sb = new StringBuilder();
            //sb.append(type.description()).append(" sur ");
            if (routesAffectés.size() == 1) {
                sb.append(routesAffectés.get(0).nom);
            } else {
                sb.append("plusieurs routes: ");
                for (int i = 0; i < routesAffectés.size();  i++) {
                    if (i > 0) sb.append(",");
                    sb.append(routesAffectés.get(i).nom);
                }
            }
            sb.append(" à ").append(String.format("%.1f", position / 1000)).append(" km");
            
            switch (type) {
                case ACCIDENT_VÉHICULE:
                    sb.append(". ").append(getDescriptionRalentissement());
                    sb.append(". ").append((int)(Math.random() * 4) + 1).append(" véhicule(s) impliqué(s)");
                    break;
                case TRAVAUX:
                    sb.append(". Durée estimée: ").append((int)(Math.random() * 9) + 1).append(" jour(s)");
                    break;
                case INTEMPÉRIE:
                    String[] intempéries = {"Neige", "Pluie intense", "Verglas", "Brouillard"};
                    sb.append(". ").append(intempéries[(int)(Math.random() * intempéries.length)]);
                    break;
                case MANIFESTATION:
                    sb.append(". Circulation fortement perturbée");
                    break;
                case VÉHICULE_EN_PANNE:
                    sb.append(". Véhicule sur la voie de droite");
                    break;
            }
            
            this.description = sb.toString();
        }
        
        private String getDescriptionRalentissement() {
            if (pourcentageRalentissement < 0.2) return "Impact mineur";
            else if (pourcentageRalentissement < 0.4) return "Dégâts matériels";
            else if (pourcentageRalentissement < 0.4) return "Dégâts matériels";
            else if (pourcentageRalentissement < 0.6) return "Blessés légers";
            else if (pourcentageRalentissement < 0.8) return "Blessés graves";
            else return "Accident mortel";
        }
                
        public void miseAJour() {

        }
        
        
        // Getters et setters
        public TypeAccident getType() {
            return type;
        }
        
        public ArrayList<Route>  getRoutesAffectés() {
            return routesAffectés;
        }
        public Route getRoutePrincipale() {
            return routesAffectés.isEmpty() ? null : routesAffectés.get(0);
        }

        public float getPosition() {
            return position;
        }
        
        public double getPourcentageRalentissement() {
            return pourcentageRalentissement;
        }
            
        
        public boolean isActif() {
            return actif;
        }
        
        public void setActif(boolean actif) {
            this.actif = actif;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String toString() {
            return description + (actif ? " [ACTIF]" : " [TERMINÉ]");
        }
         public boolean affecteRoute(Route route) {
            return routesAffectés.contains(route);
        }
    }

    
    public static void miseAJour() {
        // // Mise à jour des accidents actifs
        // LocalDateTime maintenant = LocalDateTime.now();
        
        // for (int i = 0; i < listeAccidents.size(); i++) {
        //     Accident accident = listeAccidents.get(i);
            
        //     if (accident.isActif()) {
        //         // Mise à jour spécifique à l'accident
        //         accident.miseAJour();
                
        //         // Vérification si l'accident est terminé
        //         LocalDateTime tempsDebut = listeTempsAccidents.get(i);
        //         //int duree = listeDureeAccidents.get(i);
                
        //         long tempsEcoule = java.time.Duration.between(tempsDebut, maintenant).toMinutes();
                
        //         if (tempsEcoule >= duree) {
        //             accident.setActif(false);
        //         }
        //     }
        // }
    }
    
   
    public static Accident générerAccidentAléatoire(Route route) {
        TypeAccident typeAccident = TypeAccident.values()[(int)(Math.random() * TypeAccident.values().length)];
        float position = (float)(Math.random() * route.avoirLongueur()); // Position aléatoire sur la route
        double pourcentageRalentissement = Math.random() * 0.8 + 0.1;//  Entre 0.1 et 0.9

        ArrayList<Route> routes = new ArrayList<>();
        routes.add(route);
        
        // Ajouter occasionnellement une route supplémentaire
        if (Math.random() < 0.3) {
            // Simulons une route supplémentaire - à adapter selon votre structure
            // Ici, on suppose que route.getConnexions() renvoie les routes connectées
            // List<Route> connexions = route.getConnexions();
            // if (connexions != null && !connexions.isEmpty()) {
            //     routes.add(connexions.get((int)(Math.random() * connexions.size())));
            // }
        }
        
        Accident accident = new Accident(typeAccident, routes, position, pourcentageRalentissement);
        
        // Enregistrement de l'accident
        listeAccidents.add(accident);
        listeTempsAccidents.add(LocalDateTime.now());
        listeDuréeAccidents.add(calculerDuréeAccident(accident));
        
        return accident;
    }
    
    
    private static int calculerDuréeAccident(Accident accident) {
    
        int duréeBase;
        
        switch (accident.getType()) {
            case ACCIDENT_VÉHICULE:
                //duréeBase = accident.getPourcentageRalentissement() * 150; // 15 min à 2h30 selon importance
                break;
            case TRAVAUX:
                duréeBase = 240; // 4 heures minimum pour des travaux
                break;
            case INTEMPÉRIE:
                duréeBase = 120; // 2 heures pour les intempéries
                break;
            case MANIFESTATION:
                duréeBase = 180; // 3 heures pour une manifestation
                break;
            case VÉHICULE_EN_PANNE:
                duréeBase = 45; // 45 minutes pour un véhicule en panne
                break;
            default:
                duréeBase = 60;
        }
        
        // Ajout d'une variation aléatoire de ±30%
         //int variation = (int)(duréeBase * 0.3);
        //return duréeBase + (int)(Math.random() * (2 * variation + 1)) - variation;
        return 0;
    }

    // public static ArrayList<Accident> genererAccidentsAleatoires(List<Route> routes, double probabilite) {
    //     ArrayList<Accident> nouveauxAccidents = new ArrayList<>();
        
    //     for (Route route : routes) {
    //         if (Math.random() < probabilite) {
    //             Accident accident = genererAccidentAleatoire(route);
    //             nouveauxAccidents.add(accident);
    //         }
    //     }
        
    //     return nouveauxAccidents;
    // }

    public static ArrayList<Accident> getAccidentsActifsSurRoute(Route route) {
        ArrayList<Accident> accidentsActifs = new ArrayList<>();
        
        for (Accident accident : listeAccidents) {
            if (accident.isActif() && accident.affecteRoute(route)) {
                accidentsActifs.add(accident);
            }
        }
        
        return accidentsActifs;
    }
    
    public static double calculerImpactSurVitesse(Véhicule vehicule, Accident accident) {
        // if (!accident.affecteRoute(vehicule.getRouteActuelle()) || !accident.isActif()) {
        //     return 1.0; // Pas d'impact
        // }
        
        // // Distance entre le véhicule et l'accident
        // double distance = Math.abs(vehicule.getPosition() - accident.getPosition());
        
        // // Si le véhicule est très loin de l'accident, pas d'impact
        // if (distance > 1000) {
        //     return 1.0;
        // }
        
        // // Plus on est proche, plus l'impact est fort
        // double facteurDistance = Math.min(1.0, distance / 1000);
        
        // // Calcul du facteur final (entre 0 et 1, où 0 = arrêt complet et 1 = pas d'impact)
        // return Math.max(0.1, 1.0 - accident.getPourcentageRalentissement() * (1.0 - facteurDistance));
        return 0;
    }

    public static void nettoyerAccidentsTermines() {
        for (int i = listeAccidents.size() - 1; i >= 0; i--) {
            if (!listeAccidents.get(i).isActif()) {
                listeAccidents.remove(i);
                listeTempsAccidents.remove(i);
                //listeDureeAccidents.remove(i);
            }
        }
    }

    public static int getNombreAccidentsActifs() {
        int count = 0;
        for (Accident accident : listeAccidents) {
            if (accident.isActif()) {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<Accident> getListeAccidents() {
        return new ArrayList<>(listeAccidents);
    }
    
   public static ArrayList<Accident> getAccidentsActifs() {
        ArrayList<Accident> actifs = new ArrayList<>();
        for (Accident accident : listeAccidents) {
            if (accident.isActif()) {
                actifs.add(accident);
            }
        }
        return actifs;
    }
} 

   