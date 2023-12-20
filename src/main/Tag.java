package main;

import javafx.scene.paint.Color;
import main.composite.Tache;

import java.io.Serializable;

/**
 * Classe permettant de créer un tag pour les tâches
 * @see Tache
 */
public class Tag implements Serializable {

    /**
     * Nom du tag
     */
    private String nom;

    /**
     * Couleur du tag
     */
    private Color couleur;

    /**
     * Constructeur de Tag
     * @param nom Nom du tag
     * @param couleur Couleur du tag
     */

    public Tag(String nom, Color couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public String getNom(){
        return nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public Color getCouleur(){
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
