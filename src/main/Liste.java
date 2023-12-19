package main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.ControlOnDragDropped;
import main.controleurs.ControlOnDragOver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Liste qui permet de créer une liste de tâches
 * @see Composant
 */
public class Liste implements Serializable {

    /**
     * Nom de la liste
     */
    private String nom;

    /**
     * Liste des composants de la liste
     */
    private final List<Composant> composants;

    /**
     * Nombre de tâches dans la liste
     */
    private int nbTaches; // SERT POUR LA SAUVEGARDE


    /**
     * Constructeur de la classe Liste
     * @param nom Nom de la liste
     */
    public Liste(String nom) {
        this.nom = nom;
        composants = new ArrayList<Composant>();
        this.nbTaches = 0;
    }

    /**
     * Ajoute un composant à la liste
     * @param c Composant à ajouter
     */
    public void ajouterComposant(Composant c) {
        this.composants.add(c);
        this.nbTaches++;
    }

    /**
     * Retire un composant de la liste
     * @param c Composant à retirer
     */
    public void retirerComposant(Composant c) {
        this.composants.remove(c);
        this.nbTaches--;
    }

    /**
     * Affiche la liste
     * @param modele Modèle de l'application
     * @return VBox contenant la liste
     */
    public VBox afficher(Modele modele) {

        VBox paneListe = new VBox();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        paneListe.setId(this.nom);
        Text textNom = new Text(this.nom);
        Button suppListe = new Button();
        ImageView imgSupp = new ImageView(new Image("file:icons/trash.png"));
        imgSupp.setFitHeight(20);
        imgSupp.setFitWidth(20);
        suppListe.setGraphic(imgSupp);
        suppListe.getStyleClass().add("quitter");
        suppListe.setOnAction(e -> {
            modele.getProjet().supprimerListeTaches(this);
            modele.notifierObservateur();
        });
        hbox.getChildren().addAll(textNom, suppListe);
        paneListe.getChildren().add(hbox);

        for (Composant c : composants) {
            paneListe.getChildren().add(c.afficher(modele));
        }

        paneListe.setOnDragDropped(new ControlOnDragDropped(modele));
        paneListe.setOnDragOver(new ControlOnDragOver(modele));
        return paneListe;
    }

    public boolean contient(Tache t){return this.getComposants().contains(t);}

    @Override
    public String toString() {
        return "Liste{" +
                "nom='" + nom + '\'' +
                ", composants=" + composants +
                '}';
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public String getNom() {
        return this.nom;
    }

    public void setNom() {
        this.nom = nom;
    }

    public List<Composant> getComposants() {
        return this.composants;
    }

    public int getNbTaches(){
        return this.nbTaches;
    }
}
