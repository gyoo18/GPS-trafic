# Modélisation du programme

## Identification des classes

- Réseau routier
  - Graphe représentant les routes du réseau.
  - Contient des intersections reliées par des routes

- Intersection
  - Relie plus d'une route entre elles et s'occupe de transférer les véhicules d'une intersection à l'autre.
  - Référence de routes.
  - Possède une position physique

- Route
  - Relie deux intersections et s'occupe de faire naviguer les véhicules sur sa longueur
  - Possède deux intersections
  - Possède une longueur
  - Possède un nombre de voies
  - Possède une vitesse maximale
  - Possède un nom
  - Possède des numéros de rue sur sa longueur
  - Référence aux voitures

- Véhicule
  - Se promène sur le réseau routier selon sa routine habituelle pour causer de la congestion.
  - Possède une longueur
  - Possède une destination cible
  - Possède une liste de destinations en fonction de l'heure de la journée pour former une routine
  - Possède un navigateur

- Système de recherche de chemin
  - Communique avec le réseau routier pour trouver le chemin le plus rapide, le plus court ou le moins énergivore entre deux points sur le réseau.

- Navigateur (Système de navigation automatique)
  - Communique avec le système de recherche de chemin, le véhicule et le réseau routier pour trouver et exécuter le chemin le plus court, en fonction des voitures environnantes.

- Système de GUI avec swing
  - Système qui implémente les objets swing et contient l'interface graphique. Reçoit et traite les événements et appelle les fonctions nécessaires à l'exécution de la logique du système.

- *(Potentiel)* Système de représentation routier
  - Il se peut que swing ne soit pas suffisant pour représenter le réseau routier ou la vue GPS. Il se peut qu'un système séparé pour cette fin soie nécessaire. Le système de GUI le posséderait et le traiterait comme une composante swing.

- Système de représentation des informations
  - Communique avec le système de GUI et le réseau routier pour aller chercher les informations nécessaires à l'affichage et les traduire pour l'affichage

- Système de contrôle du véhicule
  - Système qui reçoit les événements du GUI pour contrôler un véhicule spécialisé pour le contrôle de l'utilisateur.

- Système de génération de réseau routier
  - Créé un réseau aléatoirement au démarrage du programme et créé les routines des véhicules