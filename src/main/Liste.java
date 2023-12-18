package main;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.composite.Composant;
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
        Text textNom = new Text(this.nom);
        paneListe.getChildren().add(textNom);

        for (Composant c : composants) {
            paneListe.getChildren().add(c.afficher(modele));
        }

        return paneListe;
    }

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
