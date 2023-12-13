package main;

import javafx.scene.layout.HBox;
import main.observateur.Observateur;

import java.util.ArrayList;

public class Modele implements Sujet{

    private ArrayList<Observateur> observateurs;
    private String vueCourante;
    public static final String BUREAU = "Bureau";
    public static final String LISTE = "Liste";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";
    private ArrayList<Liste> listesTaches;

    public HBox paneBureau = new HBox();

    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.listesTaches = new ArrayList<Liste>();
        this.vueCourante = Modele.BUREAU;
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

    public void setVueCourante(String vueCourante) {
        this.vueCourante = vueCourante;
    }

    public void ajouterListeTaches(Liste liste) {
        this.listesTaches.add(liste);
    }

    public void supprimerListeTaches(Liste liste) {
        this.listesTaches.remove(liste);
    }

    public ArrayList<Liste> getListesTaches() {
        return this.listesTaches;
    }

    public Liste getListeTaches(String nom) {
        for (Liste liste : this.listesTaches) {
            if (liste.getNom().equals(nom)) {
                return liste;
            }
        }
        return null;
    }

}
