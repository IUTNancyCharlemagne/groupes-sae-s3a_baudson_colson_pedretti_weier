package main;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import main.objet.composite.Composant;
import main.objet.composite.Tache;
import main.objet.Liste;
import main.observateur.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe Modele qui contient les données du projet <br>
 * Implémente l'interface Sujet pour le patron de conception Observateur <br>
 * Implémente l'interface Serializable pour la sauvegarde du projet
 */
public class Modele implements Sujet, Serializable {

    // Constantes pour le style des vues
    public static final String LISTE = "Liste";
    public static final String COLONNE = "Colonne";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";

    /**
     * Tâche courante
     */
    private Composant currentTache;

    /**
     * Panneau principal de l'application
     */
    private GridPane paneBureau = new GridPane();

    /**
     * Panneau principal de l'application
     */
    private StackPane stackPane = new StackPane();

    /**
     * Liste des observateurs
     */
    private final List<Observateur> observateurs;

    /**
     * Vue courante
     */
    private String vueCourante;

    /**
     * Projet courant
     */
    private Projet projet;

    /**
     * Constructeur par défaut
     */
    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.vueCourante = Modele.COLONNE;
        this.currentTache = null;
    }

    /**
     * Constructeur avec paramètre
     *
     * @param projet Projet courant
     */
    public Modele(Projet projet) {
        this.observateurs = new ArrayList<Observateur>();
        this.vueCourante = Modele.COLONNE;
        this.projet = projet;
        init();
    }

    /**
     * Méthode qui ajoute un observateur à la liste des observateurs
     *
     * @param o Observateur à ajouter
     */
    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    /**
     * Méthode qui supprime un observateur de la liste des observateurs
     *
     * @param o Observateur à supprimer
     */
    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    /**
     * Méthode qui notifie les observateurs
     */
    @Override
    public void notifierObservateur() {
        for (Observateur o : this.observateurs) {

            if (Objects.equals(vueCourante, Modele.COLONNE)) {
                if (o instanceof VueBureau) {
                    o.actualiser(this);
                }
            } else if (Objects.equals(vueCourante, Modele.ARCHIVES)) {
                if (o instanceof VueArchives) {
                    o.actualiser(this);
                }
            } else if (Objects.equals(vueCourante, Modele.GANTT)) {
                if (o instanceof VueGantt) {
                    o.actualiser(this);
                }
            } else if (Objects.equals(vueCourante, Modele.LISTE)) {
                if (o instanceof VueListe) {
                    o.actualiser(this);
                }
            }
            if (o instanceof VueTache) {
                o.actualiser(this);
            }
        }
    }

    /**
     * Méthode qui initialise le projet
     * Ajoute la liste "A faire" avec une tâche et une sous-tâche
     * Ajoute les listes "En cours" et "Terminé"
     */
    public void init() {
        Projet projet = this.getProjet();

        Liste liste = new Liste("À faire");
        Tache tache = new Tache("Tâche 1", null, 1);
        Tache sousTache = new Tache("Sous-tâche 1", null, 1);

        projet.getListeTouteTaches().add(tache);
        projet.getListeTouteTaches().add(sousTache);

        tache.ajouter(sousTache);
        liste.ajouterComposant(tache);
        projet.ajouterListeTaches(liste);
        projet.ajouterListeTaches(new Liste("En cours"));
        projet.ajouterListeTaches(new Liste("Terminé"));
        this.notifierObservateur();
    }

    /**
     * Méthode qui renvoie la liste des observateurs et la vue courante
     * @return String
     */
    @Override
    public String toString() {
        return "Modele{" +
                "observateurs=" + observateurs +
                ", vueCourante='" + vueCourante + '\'' +
                '}';
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    /**
     * Méthode qui permet de récupérer la vue courante
     * @return la vue courante
     */
    public String getVueCourante() {
        return this.vueCourante;
    }

    /**
     * Méthode qui permet de modifier la vue courante
     * @param vueCourante la nouvelle vue courante
     */
    public void setVueCourante(String vueCourante) {
        this.vueCourante = vueCourante;
    }

    /**
     * Méthode qui permet de récupérer le projet courant
     * @return le projet courant
     */
    public Projet getProjet() {
        return projet;
    }

    /**
     * Méthode qui permet de modifier le projet courant
     * @param projet le nouveau projet courant
     */
    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    /**
     * Méthode qui permet de récupérer le panneau principal de l'application
     * @return le panneau principal de l'application
     */
    public GridPane getPaneBureau() {
        return paneBureau;
    }

    /**
     * Méthode qui retourne le stackPane
     * @return
     */
    public StackPane getStackPane() {
        return stackPane;
    }

    /**
     * Méthode qui retourne la tâche courante
     * @return
     */
    public Composant getCurrentTache() {
        return currentTache;
    }

    /**
     * Méthode qui modifie la tâche courante
     * @param currentTache
     */
    public void setCurrentTache(Composant currentTache) {
        this.currentTache = currentTache;
    }
}
