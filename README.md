<!--cspell:ignore gradlew traffix -->
# Traffix

Ceci est le répôt du projet du devoirII du cours de programmation II

## Construire (build)

sur Windows

```PowerShell
# Dans le dossier racine
gradlew.bat build
gradlew.bat run
```

sur *Unix/MacOSX

```Shell
# Dans le dossier racine
./gradlew build
./gradlew run
```

La première commande compilera le programme, exécutera les tests et compressera le programme dans une forme exécutable compacte. La deuxième commande exécutera le programme.

Afin de sortir les exécutables et les installer :

Dans le dossier `app/distributions` se trouveront deux fichier : `Traffix.zip` et `Traffix.tar`. Déplacez-les dans un dossier qui convient pour des programmes, décompressez-les et dans `Traffix/bin/` exécutez `Traffix` sur *Unix/MacOSX ou `Traffix.bat` sur Windows.

## Comment utiliser

### Interface utilisateur

![L'interface montre 5 zones importantes, qui sont détaillées ci-dessous](./devoir/Cap1.png)
Lors de l'ouverture du programme, vous serez présenté avec l'image ci-haut.
1. L'utilisation du programme consiste à naviguer à travers un réseau routier généré procéduralement à l'aide d'un GPS. Vous pouvez voir votre voitre au centre, ainsi que les voitures qui vous accompagnent autour.
2. Des boutons de navigations vous sont offerts en bas à gauche de l'écran. Vous pouvez accélérer, ralentir, tourner à gaucher, tourner à droite et faire demi-tour. Ces boutons sont aussi disponible sur votre clavier, il suffit simplement d'utiliser les flèches et la barre d'espace.
3. Une minicarte vous est présenté. Vous pouvez zoomer/dézoomer en utilisant la molette de la souris. Le haut de la carte pointera toujours dans votre direction de voyage.
4. Vous pouvez maximiser/minimiser la minicarte afin d'avoir un meilleur apperçu des alentours.
5. Une barre de recherche vous est offerte afin de marquer votre itinéraire. L'utilisation de plusieurs destinations est supportée. *Vous devez cliquer sur le bouton « rechercher » à droite afin de confirmer votre destination*.

![Un exemple de la minicarte maximisée](./devoir/Cap2.png)

1. Vous pouvez minimiser la minicarte après l'avoir maximisé.
2. Vous pouvez cliquer sur la carte afin de sélectionner votre destination. Cette action remplira la barre de recherche, comme montré plus bas, afin que vous puissiez confirmer votre destination en appuyant sur le bouton de recherche. De plus, si vous cliquez avec le bouton `Ctrl` enfoncé, vous pourrez créer un accident à l'endroit de votre clic.

![Le champ de recherche s'est remplis pour vous permettre de confirmer votre destination](./devoir/Cap3.png)

Une fois la destination confirmée, vous aurez accès à votre itinéraire :

![plusieurs éléments, qui sont décrits ci-dessous, sont apparus](./devoir/Cap4.png)

1. Une carte résumant votre destination est apparue sous la barre de recherche. Elle comporte plusieurs éléments d'information : 
   1. La couleur du cercle indique le type de destination : Vert indique départ, Orange indique un arrêt en chemin et Rouge la destination finale.
   2. L'adresse de la destination
   3. La durée du trajet et l'heure d'arrivée
2. 4. Un bouton pour supprimer la destination
   5. En cliquant sur le bouton de réordonnancement, vous pourrez glisser les cartes pour réorganiser votre itinéraire. Ces changements prendront effets immédiatement et vous pourrez commencer à suivre le trajet sans tarder.
3. Vous pouvez observer votre trajet en bleu sur la route devant vous et sur la minicarte. Il se mettra à jour automatiquement, réagissant à la congestion, aux accidents et à vos propres décisions.
4. Des indications de trajets se trouvent en haut à gauche. Une flèche indique le prochain tournant, ainsi que le nom de la prochaine rue, et une boîte d'indication affiche le temps restant avant d'atteindre la prochaine destination ainsi que l'heure d'arrivée.

![Une route avec des contours sombres](./devoir/Cap5.png)
![Une zone d'accident en cercle](./devoir/Cap6.png)

1. On peut observer sur les deux images ci-dessus, des zones d'accidents qui ralentissent les véhicules par un certain pourcentage. Leur couleur rouge foncé indiquent la gravité de l'accident, le plus foncé indiquant les accidents qui ralentissent le plus.
2. On peut observer que lorsque le véhicule pénètre une zone d'accident, des informations à son propos s'affichent en dessous des informations de temps de trajet, à gauche.
3. Vous pouvez à tout moment faire `Ctrl+clic` sur la carte afin de créer un nouvel accident.

## Organisation du répertoire

Ce projet utilise [Gradle](https://gradle.org/) comme outil de build.

Prévisualisation de l'arbre des dossiers:

```text
.
├── app
│   ├── build - - - - - - - Dossier généré par gradle
│   │   ├── distributions
│   │   │   ├── app.tar   - fichiers compressé contenants les exécutables
│   │   │   └── app.zip
│   │   ├── reports/tests/test/
│   │   │   ├── index.html - Page html indiquant les rapports d'exécution des tests
│   │   │   └── tests/...
│   │   ...
│   ├── build.gradle - fichier de configuration gradle
│   └── src
│       ├── lib
│       │   └── lwjgl-awt/... - Bibliothèque utilisée pour lier lwjgl (ou OpenGL) et awt (ou Swing)
│       ├── main - - - - - - - - Code source
│       │   ├── java/org/Traffix/
│       │   │   ├── App.java
│       │   │   ├── animations
│       │   │   │   ├── Animable.java
│       │   │   │   ├── FonctionFinAnimation.java
│       │   │   │   └── GestionnaireAnimations.java
│       │   │   ├── circulation
│       │   │   │   ├── AÉtoile.java
│       │   │   │   ├── GestionnaireAccidents.java
│       │   │   │   ├── IntersectionArrêt.java
│       │   │   │   ├── IntersectionFeux.java
│       │   │   │   ├── Intersection.java
│       │   │   │   ├── IntersectionLaissezPasser.java
│       │   │   │   ├── Navigateur.java
│       │   │   │   ├── NavigateurManuel.java
│       │   │   │   ├── Réseau.java
│       │   │   │   ├── Route.java
│       │   │   │   ├── UsineRéseau.java
│       │   │   │   └── Véhicule.java
│       │   │   ├── GUI
│       │   │   │   ├── Bouton.java
│       │   │   │   ├── Destination.java
│       │   │   │   ├── Fenêtre.java
│       │   │   │   ├── GestionnaireContrôles.java
│       │   │   │   ├── GestionnaireInfos.java
│       │   │   │   ├── RoundPane.java
│       │   │   │   ├── TexteEntrée.java
│       │   │   │   └── UsineFenêtre.java
│       │   │   ├── maths
│       │   │   │   ├── Mat4.java
│       │   │   │   ├── Maths.java
│       │   │   │   ├── Transformée.java
│       │   │   │   ├── Vec2.java
│       │   │   │   ├── Vec3.java
│       │   │   │   └── Vec4.java
│       │   │   ├── OpenGL
│       │   │   │   ├── Caméra.java
│       │   │   │   ├── GénérateurMaillage.java
│       │   │   │   ├── GLCanvas.java
│       │   │   │   ├── Maillage.java
│       │   │   │   ├── Nuanceur.java
│       │   │   │   ├── Objet.java
│       │   │   │   ├── Scène.java
│       │   │   │   └── Texture.java
│       │   │   └── utils
│       │   │       ├── Chargeur.java
│       │   │       └── Octarbre.java
│       │   └── resources - - - - - Fichiers non-java chargés lors de l'exécution
│       │       ├── nuanceurs - - - Programmes s'exécutants sur le GPU ("Shaders")
│       │       │   ├── nuaColoré.frag
│       │       │   ├── nuaColoréPoints.frag
│       │       │   ├── nuaColoréPoints.vert
│       │       │   └── nuaColoré.vert
│       │       └── textures
│       │           ├── continuer.png
│       │           ├── demi-tour.png
│       │           ├── tourner-droite_-45.png
│       │           ├── tourner-droite_45.png
│       │           ├── tourner-droite_90.png
│       │           ├── tourner-gauche_-45.png
│       │           ├── tourner-gauche_45.png
│       │           └── tourner-gauche_90.png
│       └── test - - - - - - - - - - - - - - - - tests JUnits
│           └── java
│               └── org
│                   └── Traffix
│                       └── circulation
│                           ├── AÉtoileTest.java
│                           ├── Communs.java
│                           ├── IntersectionArrêtTest.java
│                           └── IntersectionLaissezPasserTest.java
│
├── devoir - - - - - - - Dossier contenant les fichiers reliés à la remise du devoir
│   ├── Calendrier.md
│   ├── Calendrier.png
│   ├── Diagrammes.drawio
│   ├── FluxA_.drawio.svg
│   ├── Maquette_1.png
│   ├── Modélisation.md
│   ├── Organisation.md
│   ├── Schéma_fonctionnel.svg
│   ├── Spécifications.md
│   └── UML.svg
├── gradle/... - - - - - - - Dossier d'exécution de gradle
├── gradle.properties
├── gradlew - - - - - - - -  Scripts d'interface gradle agnostics à la machine
├── gradlew.bat - - /
├── README.md - - - - - - -  Le présent fichier de présentation
└── settings.gradle - - - -  Fichier de configuration général gradle
```

---

### Dossier racine ( `./` )

Dossier racine. Contient quelques fichiers importants:
- **README.md** le présent *README*.
- **settings.gradle** Paramètre de configurations globales de gradle.
- **gradlew** et **gradlew.bat** Script d'interface CLI avec gradle, afin d'être agnostic à la machine.
- **.git/** Dossier contenant toutes les informations concernant le répôt git.
- **gradle/** Dossier de fonctionnement de gradle

### `devoir/`

Dossier contenant tout les fichiers relatifs à la remise du devoir, soit les rapports, les diagrammes, etc.

### `app/build/`

À la première ouverture du projet, ce dossier n'existera pas. Il est créé par gradle à l'exécution des commandes ci-dessus. Il comporte quelques dossiers intéressants :

#### `app/build/distributions/`

Contient les fichiers compressés pour l'exécution du programme. Ils sont présents sous forme de `.zip` et de `.tar` et possèdent la structure suivante :

```text
Traffix.zip
└── app
    ├── lib/... - Contient tout les fichiers exécutables java (les .jar)
    └── bin
        ├── app.bat - Scripts de démarrages de l'application
        └── app
```

#### `app/build/reports/`

Contient le rapport d'exécution des tests, visionnable sous forme de page html, en ouvrant le fichier [indexe.html](./app/build/reports/tests/test/index.html) dans un navigateur web.

### `app/src/lib/`

Gradle se charge de gérer les bibliothèques communes afin d'éviter d'avoir à télécharger une bibliothèque si un ordinateur la possède déjà et afin de diminuer la taille du répôt. Cependant, certaines bibliothèques ne sont pas disponibles aussi facilement en ligne et nécessitent d'être inclues directement dans le projet. Dans notre cas, il nous a été nécessaire d'inclure `lwjgl-awt` pour faire l'interface entre `LWJGL` (ou OpenGL) et `AWT` (ou Swing).

### `app/src/tests`

Contient les tests JUnits qui testent les fonctionnalités du projet. Lors de la compilation, Gradle exécute ces tests automatiquement. Nous aurions aimés mieux fournir cette section, mais le temps nous a manqué.

### `app/src/main/ressources/`

Contient les fichiers ressources qui doivent être chargés en mémoire à l'exécution du programme. Ce dossier inclut toutes les images utilisés dans le programme et tout les fichiers de nuanceurs OpenGL.

### `app/src/main/java/org/Traffix/`

Package contenant le code source du devoir. La fonction `main()` se trouve dans `App.java`. Le projet est divisé en 6 sous-dossiers :
- **GUI/** Contient toutes les classes qui interragissent avec Swing
- **OpenGL/** Contient toutes les classes qui interragissent avec OpenGL, notamment GLCanvas, qui est responsable du dessin sur un `AWTGLPanel`.
- **circulation/** Contient toutes les classes responsables de la représentation du réseau routier et des véhicules
- **maths/** Contient une suite de classes et de fonctions utilitaires mathématiques pour utiliser OpenGL. Ce code a été réutilisé et perfectionné d'un autre projet d'un des membres de l'équipe, que vous pouvez voir [ici](https://github.com/gyoo18/Battleship/).
- **utils/** Contient quelques classes utilitaires, comme un chargeur de fichier. Ce code a aussi été tiré du projet mentionné ci-haut.
- **animations/** Relique du projet mentionné ci-haut. Permet de créer des transitions simples entre plusieurs clés d'animations. Nous pensions l'utiliser pour ce projet, mais son utilité ne s'est pas avéré.

## Bibliothèques utilisées
- [Java Swing](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html) Pour l'interface graphique et les événements d'interaction utilisateur
- [LightWeight Java Game Library (LWGL)](https://www.lwjgl.org/) pour l'interface avec OpenGL
- [lwjgl-awt](https://github.com/LWJGLX/lwjgl3-awt) pour faire interface entre LGJWL 3 et AWT.
- Utilisation de [Gradle](https://gradle.org/) pour la compilation et la gestion des dépendances
- Utilisaton de [JUnit](https://junit.org/junit5/) pour les tests unitaires
- Merci à [Jonas K](https://stackoverflow.com/users/1640501/jonas-k) sur StackOverflow pour son algorithme pour [déterminer si un String est un nombre](https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java)

## Fonctionnement Général

![Diagramme UML de Traffix](devoir/UML.svg)

### Initialisation

La classe `UsineFenêtre` génère une fenêtre qui fait l'interface avec un `JFrame` Swing. L'intention était de pouvoir obtenir plusieurs fenêtre interchangeables en fonction du menu, mais le temps nous a manqué. 

`UsineRéseau` génère un `Réseau` routier composé de `Route`, d'`Intersection` et de `Véhicule` avec un algorithme de pousse en branches. Il commence par faire pousser des Autoroutes, puis il fait pousser des Boulevards depuis les Autoroutes et finalement des petites rues. Comme cette structure mime la croissance des plantes, il en résulte un réseau à l'allure organique. Cependant, cette approche mène souvent à des erreurs de génération difficiles à retracer. C'est pourquoi un test de validité est exécuté à la fin de la génération et la recommence s'il échoue afin d'assurer un réseau valide.

Les deux classes sont utilisés pour initialiser `GestionnaireContrôles`, `GestionnaireInfos`, `AÉtoile` et `GestionnaireAccidents`.

Les objets OpenGL sont initialisés. Il est à noter que la vue principale et la mini carte possèdent deux contextes OpenGL différents, ce qui implique qu'ils ne peuvent pas se partager les ressources et qu'elles doivent être dupliqués pour les deux instances.

### Boucle principale

1. Le réseau est mis à jour
   1. Chaque **véhicule** sur le réseau est mis à jour. Ceci implique l'avancement du mouvement, la mise à jour de l'arbre de décision, l'exécution des commandes de l'utilisateur et le recalcul de l'itinéraire au besoin.
   2. Les **intersections** sont mises à jour. Ceci affecte les `IntersectionArrêt` et `IntersectionFeux`, quoique cette dernière n'est pas utilisée.
2. Le `GestionnaireAccidents` est mis à jour. Cela implique la création et la destruction d'accidents.
3. Le `GestionnaireInfo` est mis à jour. Cela implique la collecte de données sur le réseau et la modifications des composantes Swing nécessaires
4. La caméra est mise à jour.

Le système de dessin à l'écran est géré par Swing dans son propre fil d'exécution, ce qui implique que la boucle de dessin OpenGL n'est pas contrôlable.

La gestion des événements se fait à travers le `GestionnaireContrôles`. Il reçoit et exécute les événements afin d'envoyer les bonnes commandes aux bons endroits et de modifier les valeurs correctement.