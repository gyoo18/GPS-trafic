GestionnaireAccidents 
    
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GestionnaireAccidents {

    private static List<Accident> listeAccidents = new ArrayList<>();
    private static List<LocalDateTime> listeTempsAccidents = new ArrayList<>();
    private static List<Integer> listeDureeAccidents = new ArrayList<>();
    
    // Random pour la génération aléatoire
    private static final Random random = new Random();
    
    public enum TypeEvenement {
        ACCIDENT_VEHICULE("Accident de véhicule"),
        TRAVAUX("Travaux de construction"),
        INTEMPERIE("Intempérie"),
        MANIFESTATION("Manifestation"),
        VEHICULE_EN_PANNE("Véhicule en panne");
        
        private final String description;
        
        TypeEvenement(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
   
    public static class Accident {
        private TypeEvenement type;
        private Route route;
        private double position;     // Position sur la route en mètres
        private int gravite;         // Échelle de 1 à 5
        private boolean actif;
        private String description;
        
        public Accident(TypeEvenement type, Route route, double position, int gravite) {
            this.type = type;
            this.route = route;
            this.position = position;
            this.gravite = Math.max(1, Math.min(5, gravite)); // Entre 1 et 5
            this.actif = true;
            genererDescription();
        }
        
        private void genererDescription() {
            StringBuilder sb = new StringBuilder();
            sb.append(type.getDescription()).append(" sur ").append(route.getNom());
            sb.append(" à ").append(String.format("%.1f", position / 1000)).append(" km");
            
            switch (type) {
                case ACCIDENT_VEHICULE:
                    sb.append(". ").append(getDescriptionGravite());
                    sb.append(". ").append(random.nextInt(1, 5)).append(" véhicule(s) impliqué(s)");
                    break;
                case TRAVAUX:
                    sb.append(". Durée estimée: ").append(random.nextInt(1, 10)).append(" jour(s)");
                    break;
                case INTEMPERIE:
                    String[] intemperies = {"Neige", "Pluie intense", "Verglas", "Brouillard"};
                    sb.append(". ").append(intemperies[random.nextInt(intemperies.length)]);
                    break;
                case MANIFESTATION:
                    sb.append(". Circulation fortement perturbée");
                    break;
                case VEHICULE_EN_PANNE:
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
        public TypeEvenement getType() {
            return type;
        }
        
        public Route getRoute() {
            return route;
        }
        
        public double getPosition() {
            return position;
        }
        
        public int getGravite() {
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
    
   
    public static Accident genererAccidentAleatoire(Route route) {
        TypeEvenement typeEvenement = TypeEvenement.values()[random.nextInt(TypeEvenement.values().length)];
        double position = random.nextDouble() * route.getLongueur(); // Position aléatoire sur la route
        int gravite = random.nextInt(1, 6); // Gravité entre 1 et 5
        
        Accident accident = new Accident(typeEvenement, route, position, gravite);
        
        // Enregistrement de l'accident
        listeAccidents.add(accident);
        listeTempsAccidents.add(LocalDateTime.now());
        listeDureeAccidents.add(calculerDureeAccident(accident));
        
        return accident;
    }
    
    
    private static int calculerDureeAccident(Accident accident) {
    
        int dureeBase;
        
        switch (accident.getType()) {
            case ACCIDENT_VEHICULE:
                dureeBase = accident.getGravite() * 30; // 30 min à 2h30 selon gravité
                break;
            case TRAVAUX:
                dureeBase = 240; // 4 heures minimum pour des travaux
                break;
            case INTEMPERIE:
                dureeBase = 120; // 2 heures pour les intempéries
                break;
            case MANIFESTATION:
                dureeBase = 180; // 3 heures pour une manifestation
                break;
            case VEHICULE_EN_PANNE:
                dureeBase = 45; // 45 minutes pour un véhicule en panne
                break;
            default:
                dureeBase = 60;
        }
        
        // Ajout d'une variation aléatoire de ±30%
        return dureeBase + random.nextInt(-dureeBase * 3 / 10, dureeBase * 3 / 10 + 1);
    }
    
    
    public static void mettreAJourAccidents(int minutes) {
        LocalDateTime maintenant = LocalDateTime.now();
        
        for (int i = 0; i < listeAccidents.size(); i++) {
            if (listeAccidents.get(i).isActif()) {
                LocalDateTime tempsDebut = listeTempsAccidents.get(i);
                int duree = listeDureeAccidents.get(i);
                
    
                long tempsEcoule = java.time.Duration.between(tempsDebut, maintenant).toMinutes();
                
            
                if (tempsEcoule >= duree) {
                    listeAccidents.get(i).setActif(false);
                    System.out.println("Événement terminé: " + listeAccidents.get(i).getDescription());
                }
            }
        }
    }
    
  
    public static List<Accident> genererAccidentsAleatoires(List<Route> routes, double probabilite) {
        List<Accident> nouveauxAccidents = new ArrayList<>();
        
        for (Route route : routes) {
            if (random.nextDouble() < probabilite) {
                Accident accident = genererAccidentAleatoire(route);
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
    
    
    public static double calculerImpactSurVitesse(Vehicule vehicule, Accident accident) {
        if (!vehicule.getRouteActuelle().equals(accident.getRoute()) || !accident.isActif()) {
            return 1.0; // Pas d'impact
        }
        
        // Distance entre le véhicule et l'accident
        double distance = Math.abs(vehicule.getPosition() - accident.getPosition());
        
        // Si le véhicule est très loin de l'accident, pas d'impact
        if (distance > 1000) {
            return 1.0;
        }
        
        // L'impact dépend de la gravité et de la distance
        double impactBase;
        switch (accident.getType()) {
            case ACCIDENT_VEHICULE:
                impactBase = 0.2 * accident.getGravite(); // 0.2 à 1.0 selon gravité
                break;
            case TRAVAUX:
                impactBase = 0.5;
                break;
            case INTEMPERIE:
                impactBase = 0.4;
                break;
            case MANIFESTATION:
                impactBase = 0.7;
                break;
            case VEHICULE_EN_PANNE:
                impactBase = 0.3;
                break;
            default:
                impactBase = 0.3;
        }
        
        // Plus on est proche, plus l'impact est fort
        double facteurDistance = Math.min(1.0, distance / 1000);
        
        // Calcul du facteur final (entre 0 et 1, où 0 = arrêt complet et 1 = pas d'impact)
        return Math.max(0.1, 1.0 - impactBase * (1.0 - facteurDistance));
    }
    
   
    public static void afficherAccidentsActifs() {
        System.out.println("=== ÉVÉNEMENTS ACTIFS (" + getNombreAccidentsActifs() + ") ===");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (int i = 0; i < listeAccidents.size(); i++) {
            Accident accident = listeAccidents.get(i);
            if (accident.isActif()) {
                LocalDateTime debut = listeTempsAccidents.get(i);
                int duree = listeDureeAccidents.get(i);
                LocalDateTime fin = debut.plusMinutes(duree);
                
                System.out.println(accident);
                System.out.println("  Début: " + debut.format(formatter));
                System.out.println("  Fin estimée: " + fin.format(formatter));
                System.out.println("  Route: " + accident.getRoute().getNom() + 
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
                listeDureeAccidents.remove(i);
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
