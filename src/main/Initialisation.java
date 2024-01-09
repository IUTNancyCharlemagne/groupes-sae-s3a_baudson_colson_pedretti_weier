package main;

import main.composite.Tache;

/**
 * Classe Initialisation qui initialise le projet
 */
public class Initialisation {

    /**
     * Méthode qui initialise le projet
     * <ul>
     *     <li>Ajoute la liste "A faire" avec une tâche et une sous-tâche</li>
     *     <li>Ajoute les listes "En cours" et "Terminé"</li>
     * </ul>
     *
     * @param modele Modèle de l'application
     */
    public static void init(Modele modele) {
        Projet projet = modele.getProjet();

        Liste liste = new Liste("À faire");
        Tache tache = new Tache("Tâche 1", null, 0);
        Tache sousTache = new Tache("Sous-tâche 1", null, 0);

        projet.getListeTouteTaches().add(tache);
        projet.getListeTouteTaches().add(sousTache);

        tache.ajouter(sousTache);
        liste.ajouterComposant(tache);
        projet.ajouterListeTaches(liste);
        projet.ajouterListeTaches(new Liste("En cours"));
        projet.ajouterListeTaches(new Liste("Terminé"));
        modele.notifierObservateur();
    }
}
