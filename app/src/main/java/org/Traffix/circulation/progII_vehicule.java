
public abstract class Vehicule {

    private double longueur;          
    private double position;        
    private double vitesse;           
    private Route routeActuelle;      
    private Navigateur navigateur;    
    
    
    public Vehicule(double longueur, double position, double vitesse, Route routeActuelle) {
        this.longueur = longueur;
        this.position = position;
        this.vitesse = vitesse;
        this.routeActuelle = routeActuelle;
        this.navigateur = null;  
    }
    
    
    public Vehicule(double longueur, double position, double vitesse, Route routeActuelle, Navigateur navigateur) {
        this(longueur, position, vitesse, routeActuelle);
        this.navigateur = navigateur;
    }
    
    
    public void avancer(double tempsEnSecondes) {
        // Conversion de km/h en m/s puis calcul de la distance parcourue
        double distanceParcourue = (vitesse * 1000 / 3600) * tempsEnSecondes;
        position += distanceParcourue;
        
        // Vérification si le véhicule a atteint la fin de la route
        if (position > routeActuelle.getLongueur()) {
            if (navigateur != null && navigateur.aProchainRoute()) {
                double depassement = position - routeActuelle.getLongueur();
                routeActuelle = navigateur.getProchainRoute();
                position = depassement;  
                System.out.println("Véhicule a changé de route vers: " + routeActuelle.getNom());
            } else {
                position = routeActuelle.getLongueur();  // Reste à la fin de la route
                vitesse = 0;  // S'arrête
                System.out.println("Véhicule arrivé à destination");
            }
        }
    }
    
    ###
    public void changerVitesse(double nouvelleVitesse) {
        if (nouvelleVitesse >= 0) {
            this.vitesse = nouvelleVitesse;
        } else {
            System.out.println("Erreur: la vitesse ne peut pas être négative");
        }
    }
    
    ####

    public void setNavigateur(Navigateur navigateur) {
        this.navigateur = navigateur;
    }
    
    
    public double distance(Vehicule autreVehicule) {
        if (this.routeActuelle.equals(autreVehicule.routeActuelle)) {
            return Math.abs(this.position - autreVehicule.position);
        } else {
            return -1;  // Pas sur la même route
        }
    }
    
    //### Getters et setters
    public double getLongueur() {
        return longueur;
    }
    
    public void setLongueur(double longueur) {
        if (longueur > 0) {
            this.longueur = longueur;
        }
    }
    
    public double getPosition() {
        return position;
    }
    
    public void setPosition(double position) {
        if (position >= 0 && position <= routeActuelle.getLongueur()) {
            this.position = position;
        }
    }
    
    public double getVitesse() {
        return vitesse;
    }
    
    public Route getRouteActuelle() {
        return routeActuelle;
    }
    
    public void setRouteActuelle(Route routeActuelle) {
        this.routeActuelle = routeActuelle;
        this.position = 0;  // Remet la position à 0 sur la nouvelle route
    }
    
    public Navigateur getNavigateur() {
        return navigateur;
    }
    
    @Override
    public String toString() {
        return "Vehicule{" +
                "longueur=" + longueur +
                " m, position=" + position +
                " m, vitesse=" + vitesse +
                " km/h, route='" + routeActuelle.getNom() + '\'' +
                ", a un navigateur=" + (navigateur != null) +
                '}';
    }
}


public class Route {
    private String nom;
    private double longueur;  // en mètres
    private int nombreVoies;
    private int limiteVitesse;  // en km/h
    
    public Route(String nom, double longueur, int nombreVoies, int limiteVitesse) {
        this.nom = nom;
        this.longueur = longueur;
        this.nombreVoies = nombreVoies;
        this.limiteVitesse = limiteVitesse;
    }
    
    // Getters et setters
    public String getNom() {
        return nom;
    }
    
    public double getLongueur() {
        return longueur;
    }
    
    public int getNombreVoies() {
        return nombreVoies;
    }
    
    public int getLimiteVitesse() {
        return limiteVitesse;
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "nom='" + nom + '\'' +
                ", longueur=" + longueur +
                " m, voies=" + nombreVoies +
                ", limite=" + limiteVitesse +
                " km/h}";
    }
}


public class Navigateur {
    private List<Route> itineraire;
    private int indexRouteActuelle;
    
    public Navigateur() {
        this.itineraire = new ArrayList<>();
        this.indexRouteActuelle = 0;
    }
    
    
    public void ajouterRoute(Route route) {
        itineraire.add(route);
    }
    
   
    public boolean aProchainRoute() {
        return indexRouteActuelle < itineraire.size() - 1;
    }
    
    
    public Route getProchainRoute() {
        if (aProchainRoute()) {
            indexRouteActuelle++;
            return itineraire.get(indexRouteActuelle);
        }
        return null;
    }
    
    
    public Route getRouteActuelle() {
        if (itineraire.isEmpty()) {
            return null;
        }
        return itineraire.get(indexRouteActuelle);
    }
    
   
    public double getDistanceTotale() {
        double distanceTotale = 0;
        for (Route route : itineraire) {
            distanceTotale += route.getLongueur();
        }
        return distanceTotale;
    }
    
    
    public double getTempsEstime(double vitesseMoyenne) {
        if (vitesseMoyenne <= 0) {
            return Double.POSITIVE_INFINITY;
        }
        // Distance en km / vitesse en km/h = temps en heures
        return getDistanceTotale() / 1000 / vitesseMoyenne;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Itinéraire: ");
        sb.append(itineraire.size()).append(" routes, ");
        sb.append(String.format("%.1f", getDistanceTotale() / 1000)).append(" km au total\n");
        
        for (int i = 0; i < itineraire.size(); i++) {
            Route route = itineraire.get(i);
            sb.append(i + 1).append(". ");
            sb.append(route.getNom()).append(" (");
            sb.append(String.format("%.1f", route.getLongueur() / 1000)).append(" km)\n");
        }
        
        return sb.toString();
    }
}
