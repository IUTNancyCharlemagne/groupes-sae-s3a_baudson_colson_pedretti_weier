package main.objet;

import javafx.scene.paint.Color;
import main.objet.composite.Tache;

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

    /**
     * Retourne le nom du tag
     * @return
     */
    public String getNom(){
        return nom;
    }

    /**
     * Modifie le nom du tag
     * @param nom Nouveau nom du tag
     */
    public void setNom(String nom){
        this.nom = nom;
    }

    /**
     * Retourne la couleur du tag
     * @return
     */
    public Color getCouleur(){
        return couleur;
    }

    /**
     * Modifie la couleur du tag
     * @param couleur Nouvelle couleur du tag
     */
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
