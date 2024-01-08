package main;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.composite.Composant;
import main.composite.Tache;
import main.exceptions.ProjectNotFoundException;
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
     * @param projet Projet courant
     */
    public Modele(Projet projet) {
        this.observateurs = new ArrayList<Observateur>();
        this.vueCourante = Modele.COLONNE;
        this.projet = projet;
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateur() {
        for (Observateur o : this.observateurs) {

            if (Objects.equals(vueCourante, Modele.COLONNE)){
                if (o instanceof VueBureau){
                    o.actualiser(this);
                }
            } else if (Objects.equals(vueCourante, Modele.ARCHIVES)) {
                if (o instanceof VueArchives) {
                    o.actualiser(this);
                }
            } else if (Objects.equals(vueCourante, Modele.LISTE)) {
                if (o instanceof VueListe) {
                    o.actualiser(this);
                }
            }

            if (o instanceof VueTache){
                o.actualiser(this);
            }

        }
    }

    @Override
    public String toString() {
        return "Modele{" +
                "observateurs=" + observateurs +
                ", vueCourante='" + vueCourante + '\'' +
                '}';
    }

    /**
     * Méthode permettant de sauvegarder un projet.
     * Le projet est sauvegardé dans un fichier '.trebo' dont le chemin est spécifié dans l'utilisation.
     * @param chemin chemin du fichier de sauvegarder.
     * @throws IOException
     */
    public void sauvegarderProjet(String chemin) throws IOException {
        this.getProjet().sauvegarderProjet(chemin);
    }

    /**
     * Méthode permettant de charger un projet.
     * L'attribut Projet de l'objet change pour devenir le projet chargé.
     * @param chemin Chemin du fichier de sauvegarde '.trebo'
     * @throws ProjectNotFoundException Si le fichier de sauvegarde n'est pas trouvé
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void chargerProjet(String chemin) throws ProjectNotFoundException, IOException, ClassNotFoundException {
        this.setProjet(this.getProjet().chargerProjet(chemin));
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public String getVueCourante() {
        return this.vueCourante;
    }
    public void setVueCourante(String vueCourante) {
        this.vueCourante = vueCourante;
    }
    public Projet getProjet() {
        return projet;
    }
    public void setProjet(Projet projet) {
        this.projet = projet;
    }
    public GridPane getPaneBureau() {
        return paneBureau;
    }
    public StackPane getStackPane() {
        return stackPane;
    }
    public Composant getCurrentTache() {
        return currentTache;
    }
    public void setCurrentTache(Composant currentTache) {
        this.currentTache = currentTache;
    }
}
