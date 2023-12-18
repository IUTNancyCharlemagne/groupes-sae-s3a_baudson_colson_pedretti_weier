package main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.observateur.Observateur;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import main.exceptions.ProjectNotFoundException;

/**
 * Classe Modele qui contient les données du projet <br>
 * Implémente l'interface Sujet pour le patron de conception Observateur <br>
 * Implémente l'interface Serializable pour la sauvegarde du projet
 */
public class Modele implements Sujet, Serializable {

    // Constantes pour le style des vues
    public static final String BUREAU = "Bureau";
    public static final String LISTE = "Liste";
    public static final String COLONNE = "Colonne";
    public static final String LIGNE = "Ligne";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";

    /**
     * Panneau principal de l'application
     */
    private HBox paneBureau = new HBox();

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
            o.actualiser(this);
        }
    }

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
    public HBox getPaneBureau() {
        return paneBureau;
    }
    public StackPane getStackPane() {
        return stackPane;
    }

    public void sauvegarderProjet() throws IOException {
        this.getProjet().sauvegarderProjet();
    }

    public void sauvegarderProjet(String nomFichier) throws IOException {
        this.getProjet().sauvegarderProjet(nomFichier);
    }

    public void chargerProjet(String chemin) throws ProjectNotFoundException, IOException, ClassNotFoundException {
        this.setProjet(this.getProjet().chargerProjet(chemin));
    }
}
