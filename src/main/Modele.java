package main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.composite.Composant;
import main.composite.Tache;
import main.composite.Tag;
import main.exceptions.ProjectNotFoundException;
import main.observateur.Observateur;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Modele implements Sujet, Serializable {

    public static final String BUREAU = "Bureau";

    public static final String LISTE = "Liste";
    public static final String COLONNE = "Colonne";
    public static final String LIGNE = "Ligne";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";
    public HBox paneBureau = new HBox();
    public StackPane stackPane = new StackPane();
    private final List<Observateur> observateurs;
    private String vueCourante;

    private Projet projet;

    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.vueCourante = Modele.COLONNE;
    }

    public Modele(Projet projet) {
        this.observateurs = new ArrayList<Observateur>();
        this.vueCourante = Modele.COLONNE;
        this.projet = projet;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    @Override
    public String toString() {
        return "Modele{" +
                "observateurs=" + observateurs +
                ", vueCourante='" + vueCourante + '\'' +
                '}';
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

    public String getVueCourante() {
        return this.vueCourante;
    }

    public void setVueCourante(String vueCourante) {
        this.vueCourante = vueCourante;
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
