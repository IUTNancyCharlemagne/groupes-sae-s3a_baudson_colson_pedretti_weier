# groupes-sae-s3a_baudson_colson_pedretti_weier

Lien du Trello : https://trello.com/b/9tHvRLrv/taches-danalyse

Projet réalisé par :

* BAUDSON Dylan
* COLSON Lenny
* PEDRETTI Zack
* WEIER Loris

# GUIDE D'UTILISATION

<strong>Modules nécessaires au fonctionnent de l'application (Importer avec Maven):</strong>
- junit\:junit:4.13
- org.openjfx:javafx-base:17.0.1
- org.openjfx:javafx-controls:17.0.1
- org.openjfx:javafx-swing:17.0.0.1

<strong>Raccourcis clavier</strong>
- F11 : mettre en plein écran
- Ctrl + N : Nouveau projet vierge
- Ctrl + S : Sauvergarder le projet
- Ctrl + O : Ouvrir un projet
- Ctrl + B : Changer le fond d'écran
- Ctrl + L : Créer une liste
- Alt + F4 : Fermer l'application

<strong>Fonctionnement de l'application</strong>
* Quand vous êtes dans la vue en colonnes ou la vue en liste, vous pouvez :
  * Créer une liste
  * Si au moins une liste est créée :
    * Modifier la liste en cliquant sur les 3 points
    * Renommer la liste en cliquant sur son nom
    * Ajouter une tâche
    * Si une tâche est créée :
      * Double-cliquer sur la tâche pour l'ouvrir
      * Maintenir le clic enfoncé sur une tâche, la déplacer dans une autre liste et relacher le clic pour déposer la tâche dans la nouvelle liste
    * Si une sous-tâche est créée :
      * Maintenir le clic enfoncé sur une sous-tâche, la déplacer sous une autre tâche et relacher le clic pour déposer la sous-tâche dans la nouvelle tâche
      * Appuyer sur la flèche
* Quand une tâche est ouverte :
  * Changer son nom en cliquant dessus
  * Ajouter des tags
  * Ajouter une description
  * Ajouter ou changer une image
  * Changer la date s'il s'agit d'une tâche mère, la durée s'il s'agit d'une sous-tâche
  * Ajouter une dépendance s'il y a plusieurs tâches
  * Ajouter une sous-tâche
  * Archiver la tâche
  * Supprimer la tâche (bouton avec la corbeille)
  * Fermer l'affichage de la tâche avec la croix
* Quand vous êtes dans la vue du diagramme de Gantt :
  * Cocher pour garder les proportions
  * Déplacer le point de chaque slider pour changer les paramètres
