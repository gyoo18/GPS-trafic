<!-- Présent pour l'imprimage en pdf
<style>
  *{
    font-size: 0.9em;
  }
</style>-->


# Modélisation du programme

## Identification des classes

- Réseau routier
  - Graphe représentant les routes du réseau.
  - Contient des intersections reliées par des routes

- Intersection
  - Relie plus d'une route entre elles et s'occupe de transférer les véhicules d'une intersection à l'autre.
  - Référence de routes.
  - Possède une position physique.
  - Gère le traffic.

- Route
  - Relie deux intersections et s'occupe de faire naviguer les véhicules sur sa longueur
  - Possède deux intersections
  - Possède une longueur
  - À des fins de simplicités, ne possède qu'une voie.
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

- Système de contrôle du véhicule
  - Système qui reçoit les événements du GUI pour contrôler un véhicule spécialisé pour le contrôle de l'utilisateur.

- Système de GUI avec swing
  - Système qui implémente les objets swing et contient l'interface graphique. Reçoit et traite les événements et appelle les fonctions nécessaires à l'exécution de la logique du système.

- Système de traitement des événements d'interaction
  - Système qui s'accroche au GUI pour écouter les événements d'interraction utilisateur, les traiter et appeler les fonctions nécessaires à leurs exécutions.

- Système de création et de gestions d'événements aléatoires
  - Responsable de créer des accidents, des ralentissements, des blocages de la construction, etc. et d'en gérer leur évolution

- Système de traitement des informations
  - Communique avec le système de GUI et le réseau routier pour aller chercher les informations nécessaires à l'affichage et les traduire pour l'affichage

- Système de génération de réseau routier
  - Créé un réseau aléatoirement au démarrage du programme et créé les routines des véhicules.

- *(Potentiel)* Système de représentation routier
  - Il se peut que swing ne soit pas suffisant pour représenter le réseau routier ou la vue GPS. Il se peut qu'un système séparé pour cette fin soie nécessaire. Le système de GUI le posséderait et le traiterait comme une composante swing.

## Tableau des classes

| Classe                    | Responsabilité & description                                                                                                              | Attributs principaux                                                                                                                                                      | Méthodes essentielles                                                                                                                                                                                                                                                                                                                                                                                    |
|:--------------------------|:------------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Réseau                    | Représente un réseau routier                                                                                                              | listeRoutes <br> listeIntersections                                                                                                                                       | `avoirPosition(Adresse)` → une position en fonction d'une adresse <br> `avoirAdresse(Position)` → une adresse en fonction d'une position                                                                                                                                                                                                                                                                 |
| Route                     | Représente un tronçon de route                                                                                                            | nom <br> longueur <br> vitesseMax <br> intersectionA <br> intersectionB <br> queueVéhiculesSensA <br> queueVéhiculeSensB <br> listeAdresses <br> ralentissementArtificiel | `avoirPosition(NuméroRue)` → la position du numéro de rue et `Null` si ne possède pas <br> `avoirAdresse(Position)` → l'adresse la plus proche de la position <br> `avoirVitesseMoyenne(Sens)` → avoir la vitesse moyenne actuelle des voitures roulant dans ce sens <br> `estAccessible()` → indique si une voiture peut s'y engager <br> `ajouterVéhicule(Véhicule,Sens)` <br> `retirerVéhicule(Sens)` |
| Intersection              | Classe Abstraite représentant la jonction entre 3+ routes                                                                                 | listeRoutes <br> position                                                                                                                                                 | `miseÀJour()` : met à jour l'état de l'intersection <br> `peutPasser(RouteDépart, RouteDestination)` → indique si un véhicule est authorisé à traverser l'intersection                                                                                                                                                                                                                                   |
| IntersectionFeux          | Intersection qui utilise la logique des feux de circulation pour gérer le traffic                                                         | tempsCycleFeux <br> duréeCycleFeux                                                                                                                                        |                                                                                                                                                                                                                                                                                                                                                                                                          |
| IntersectionArrêt         | Intersection qui utilise la logique des paneaux d'arrêts pour gérer le traffic                                                            |                                                                                                                                                                           |                                                                                                                                                                                                                                                                                                                                                                                                          |
| IntersectionLaissezPasser | Intersection qui utilise la logique de voies prioritaires pour gérer le traffic                                                           | listeEstVoiePrioritaire (booléen décrivant si chaque route est prioriaire ou non)                                                                                         |                                                                                                                                                                                                                                                                                                                                                                                                          |
| Véhicule                  | Représente un véhicule se déplaçant sur la route                                                                                          | longueur <br> position <br> vitesse <br> routeActuelle <br> navigateur                                                                                                    |                                                                                                                                                                                                                                                                                                                                                                                                          |
| AÉtoile                   | Classe statique implémentant l'algorithme A*                                                                                              |                                                                                                                                                                           | `chemin(PositionA\|AdresseA, PositionB\|AdresseB)` → un chemin à prendre pour traverser le réseau routier, du point A au point B                                                                                                                                                                                                                                                                         |
| Navigateur                | Composante du véhicule qui exécute ses déplacements                                                                                       | destinationActuelle <br> routineListeDestinations <br> cheminActuel                                                                                                       | `miseÀJour()` : avance le véhicule sur son chemin, s'il le peut et avance à travers l'intersection si nécessaire                                                                                                                                                                                                                                                                                         |
| NavigateurManuel          | Hérite de Navigateur et agit comme interface entre l'utilisateur et l'intersection                                                        | listeActionsUtilisateur                                                                                                                                                   | `ajouterAction(Action)` : ajoutes une action utilisateur à la liste des actions à effectuer                                                                                                                                                                                                                                                                                                              |
| GestionnaireAccidents     | Classe statique responsable de la création et de la gestion d'événements aléatoires, comme des accidents de la construction, etc.         | listeAccidents <br> listeTempsAccidents <br> listeDuréeAccidents                                                                                                          | `miseÀJour()` : créée, détruit, gère et applique les accidents                                                                                                                                                                                                                                                                                                                                           |
| Fenêtre                   | Représente une fenêtre utilisateur. Contient le GUI.                                                                                      | *(liste des composantes swings nécessaires)*                                                                                                                              | `miseÀJour()` : met à jour touts les composants swings nécessaires <br> `avoirComposantParID(ID)` → un composant swing en fonction d'un ID                                                                                                                                                                                                                                                               |
| GestionnaireInfos         | Classe statique responsable de la collecte et du traitement des informations pour l'affichage                                             | *(N'est pas responsable du stockage de cette information)*                                                                                                                | `miseÀJour()` : collecte et traites toutes les informations nécessaires à l'affichage                                                                                                                                                                                                                                                                                                                    |
| GestionnaireContrôles     | Classe statique qui se connecte à la fenêtre afin de collecter les événements d'interactions, les traiter et appeler les bonnes fonctions | trajetDestinationMaintenueID                                                                                                                                              | `initialisation()` : met en place tout les eventListeners nécessaires, qui appelleront les méthodes de traitements suivantes : <br> `surMouvementSouris()` <br> `surTournantDroit()` <br> `surTournantGauche()` <br> `surAccélère()` <br> `surFreine()` <br> `surMiniCarteAgrandie()` <br> `surMiniCarteRapetissée()` <br> `surMiniCarteClic()` <br> `surTrajetParamètresOuvrir()` <br> `surTrajetParamètresFermer()` <br> `surTrajetDestinationClic(ID)` : appelé lorsqu'une destination est sélectionnée pour être réordonnée <br> `surTrajetDestinationRelâché()` <br> `surTrajetDestinationMaintenueMouvementSouris()` : apelée par `surMouvementSouris()` <br> `surParamètresOuvrir()` <br> `surParamètresFermer()` <br> etc...|
| UsineFenêtre              | Classe statique qui crée les fenêtres voulues                                                                                             |                                                                                                                                                                           | `avoirGPS()` → la fenêtre principale qui représente l'interface GPS <br> `avoirParamètres()` → la fenêtre des paramètres                                                                                                                                                                                                                                                                                 |
| UsineRéseau               | Classe statique qui crée et initialise le réseau routier                                                                                  |                                                                                                                                                                           | `créerRéseau()` → un réseau routier généré aléatoirement                                                                                                                                                                                                                                                                                                                                                 |
| ***(Potentiel)***         | Assortiment de classes OpenGL permettant de visualiser le réseau routier.                                                                 |                                                                                                                                                                           |                                                                                                                                                                                                                                                                                                                                                                                                          |

<!--Sont présents pour l'impression en pdf
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>-->

### Diagramme UML

![Diagramme UML du projet](UML.svg)

<!-- Sont présents pour l'impression en pdf
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>-->

### Diagramme de flux de A*

![Diagramme de flux de A*](FluxA_.drawio.svg)