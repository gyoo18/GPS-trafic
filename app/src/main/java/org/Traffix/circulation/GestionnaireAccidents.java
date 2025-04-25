package org.Traffix.circulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionnaireAccidents {

    private static List<Accident> listeAccidents = new ArrayList<>();
    private static List<LocalDateTime> listeTempsAccidents = new ArrayList<>();
    private static List<Integer> listeDuréeAccidents = new ArrayList<>();
    
    // Random pour la génération aléatoire
    private static final Random random = new Random();
    
    public enum TypeÉvénement {
        ACCIDENT_VÉHICULE("Accident de véhicule"),
        TRAVAUX("Travaux de construction"),
        INTEMPÉRIE("Intempérie"),
        MANIFESTATION("Manifestation"),
        VÉHICULE_EN_PANNE("Véhicule en panne");
        
        private final String description;
        
        TypeÉvénement(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public static class Accident {
        private TypeÉvénement type;
        private Route route;
        private float position;      // Position sur la route en mètres
        private int gravite;         // Échelle de 1 à 5
        private boolean actif;
        private String description;
        
        public Accident(TypeÉvénement type, Route route, float position, int gravite) {
            this.type = type;
            this.route = route;
            this.position = position;
            this.gravite = Math.max(1, Math.min(5, gravite)); // Entre 1 et 5
            this.actif = true;
            générerDescription();
        }
        
        private void générerDescription() {
            StringBuilder sb = new StringBuilder();
            sb.append(type.getDescription()).append(" sur ").append(route.nom);
            sb.append(" à ").append(String.format("%.1f", position / 1000)).append(" km");
            
            switch (type) {
                case ACCIDENT_VÉHICULE:
                    sb.append(". ").append(getDescriptionGravite());
                    sb.append(". ").append(random.nextInt(1, 5)).append(" véhicule(s) impliqué(s)");
                    break;
                case TRAVAUX:
                    sb.append(". Durée estimée: ").append(random.nextInt(1, 10)).append(" jour(s)");
                    break;
                case INTEMPÉRIE:
                    String[] intempéries = {"Neige", "Pluie intense", "Verglas", "Brouillard"};
                    sb.append(". ").append(intempéries[random.nextInt(intempéries.length)]);
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
        
        private String getDescriptionGravite() {
            switch (gravite) {
                case 1: return "Impact mineur";
                case 2: return "Dégâts matériels";
                case 3: return "Blessés légers";
                case 4: return "Blessés graves";
                case 5: return "Accident mortel";
                default: return "Gravité inconnue";
            }
        }
        
        // Getters et setters
        public TypeÉvénement getType() {
            return type;
        }
        
        public Route getRoute() {
            return route;
        }
        
        public float getPosition() {
            return position;
        }
        
        public int getGravité() {
            return gravite;
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
    }
    
   
    public static Accident générerAccidentAléatoire(Route route) {
        TypeÉvénement typeÉvénement = TypeÉvénement.values()[random.nextInt(TypeÉvénement.values().length)];
        float position = random.nextFloat() * route.avoirLongueur(); // Position aléatoire sur la route
        int gravite = random.nextInt(1, 6); // Gravité entre 1 et 5
        
        Accident accident = new Accident(typeÉvénement, route, position, gravite);
        
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
                duréeBase = accident.getGravité() * 30; // 30 min à 2h30 selon gravité
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
        return duréeBase + random.nextInt(-duréeBase * 3 / 10, duréeBase * 3 / 10 + 1);
    }
    
    
    public static void mettreAJourAccidents(int minutes) {
        LocalDateTime maintenant = LocalDateTime.now();
        
        for (int i = 0; i < listeAccidents.size(); i++) {
            if (listeAccidents.get(i).isActif()) {
                LocalDateTime tempsDebut = listeTempsAccidents.get(i);
                int durée = listeDuréeAccidents.get(i);
                
    
                long tempsÉcoulé = java.time.Duration.between(tempsDebut, maintenant).toMinutes();
                
            
                if (tempsÉcoulé >= durée) {
                    listeAccidents.get(i).setActif(false);
                    System.out.println("Événement terminé: " + listeAccidents.get(i).getDescription());
                }
            }
        }
    }
    
  
    public static List<Accident> générerAccidentsAléatoires(List<Route> routes, float probabilité) {
        List<Accident> nouveauxAccidents = new ArrayList<>();
        
        for (Route route : routes) {
            if (random.nextFloat() < probabilité) {
                Accident accident = générerAccidentAléatoire(route);
                nouveauxAccidents.add(accident);
                System.out.println("Nouvel événement: " + accident);
            }
        }
        
        return nouveauxAccidents;
    }
    
   
    public static List<Accident> getAccidentsActifsSurRoute(Route route) {
        List<Accident> accidentsActifs = new ArrayList<>();
        
        for (Accident accident : listeAccidents) {
            if (accident.isActif() && accident.getRoute().equals(route)) {
                accidentsActifs.add(accident);
            }
        }
        
        return accidentsActifs;
    }
    
    
    public static float calculerImpactSurVitesse(Véhicule véhicule, Accident accident) {
        if (!véhicule.getRouteActuelle().equals(accident.getRoute()) || !accident.isActif()) {
            return 1f; // Pas d'impact
        }
        
        // Distance entre le véhicule et l'accident
        float distance = Math.abs(véhicule.getPosition() - accident.getPosition());
        
        // Si le véhicule est très loin de l'accident, pas d'impact
        if (distance > 1000) {
            return 1f;
        }
        
        // L'impact dépend de la gravité et de la distance
        float impactBase;
        switch (accident.getType()) {
            case ACCIDENT_VÉHICULE:
                impactBase = 0.2f * accident.getGravité(); // 0.2 à 1.0 selon gravité
                break;
            case TRAVAUX:
                impactBase = 0.5f;
                break;
            case INTEMPÉRIE:
                impactBase = 0.4f;
                break;
            case MANIFESTATION:
                impactBase = 0.7f;
                break;
            case VÉHICULE_EN_PANNE:
                impactBase = 0.3f;
                break;
            default:
                impactBase = 0.3f;
        }
        
        // Plus on est proche, plus l'impact est fort
        float facteurDistance = (float)Math.min(1.0, distance / 1000);
        
        // Calcul du facteur final (entre 0 et 1, où 0 = arrêt complet et 1 = pas d'impact)
        return (float)Math.max(0.1, 1.0 - impactBase * (1.0 - facteurDistance));
    }
    
   
    public static void afficherAccidentsActifs() {
        System.out.println("=== ÉVÉNEMENTS ACTIFS (" + getNombreAccidentsActifs() + ") ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (int i = 0; i < listeAccidents.size(); i++) {
            Accident accident = listeAccidents.get(i);
            if (accident.isActif()) {
                LocalDateTime debut = listeTempsAccidents.get(i);
                int durée = listeDuréeAccidents.get(i);
                LocalDateTime fin = debut.plusMinutes(durée);
                
                System.out.println(accident);
                System.out.println("  Début: " + debut.format(formatter));
                System.out.println("  Fin estimée: " + fin.format(formatter));
                System.out.println("  Route: " + accident.getRoute().nom + 
                                  " à " + String.format("%.1f", accident.getPosition() / 1000) + " km");
                System.out.println();
            }
        }
    }
    
   
    public static void nettoyerAccidentsTermines() {
        for (int i = listeAccidents.size() - 1; i >= 0; i--) {
            if (!listeAccidents.get(i).isActif()) {
                listeAccidents.remove(i);
                listeTempsAccidents.remove(i);
                listeDuréeAccidents.remove(i);
            }
        }
        System.out.println("Nettoyage effectué. " + listeAccidents.size() + " événements actifs restants.");
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
    
    
    public static List<Accident> getListeAccidents() {
        return new ArrayList<>(listeAccidents);
    }
    
    
    public static List<Accident> getAccidentsActifs() {
        List<Accident> actifs = new ArrayList<>();
        for (Accident accident : listeAccidents) {
            if (accident.isActif()) {
                actifs.add(accident);
            }
        }
        return actifs;
    }
}
