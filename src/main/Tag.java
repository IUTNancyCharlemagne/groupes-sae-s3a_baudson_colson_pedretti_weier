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
