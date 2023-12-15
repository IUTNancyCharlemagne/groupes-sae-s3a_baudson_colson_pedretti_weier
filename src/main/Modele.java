package main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.observateur.Observateur;

import java.util.ArrayList;

public class Modele implements Sujet{

    private ArrayList<Observateur> observateurs;
    private String vueCourante;
    public static final String COLONNE = "Colonne";
    public static final String LIGNE = "Ligne";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";
    private ArrayList<Liste> listeTaches;
    public HBox paneBureau = new HBox();

    public StackPane stackPane = new StackPane();

    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.listeTaches = new ArrayList<Liste>();
        this.vueCourante = Modele.COLONNE;
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

    public void ajouterListeTaches(Liste liste) {
        this.listeTaches.add(liste);
    }

    public void supprimerListeTaches(Liste liste) {
        this.listeTaches.remove(liste);
    }

    public ArrayList<Liste> getListeTaches() {
        return this.listeTaches;
    }

    public Liste getListeTaches(String nom) {
        for (Liste liste : this.listeTaches) {
            if (liste.getNom().equals(nom)) {
                return liste;
            }
        }
        return null;
    }

}
