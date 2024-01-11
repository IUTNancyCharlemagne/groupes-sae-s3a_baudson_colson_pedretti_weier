package main.objet;

import javafx.scene.paint.Color;
import main.objet.composite.Tache;

import java.io.Serializable;

/**
 * Classe permettant de créer un tag pour les tâches
 *
 * @see Tache
 */
public class Tag implements Serializable {

    /**
     * Nom du tag
     */
    private String nom;

    private java.awt.Color couleurSerializable;

    /**
     * Constructeur de Tag
     *
     * @param nom     Nom du tag
     * @param couleur Couleur du tag
     */

    public Tag(String nom, Color couleur) {
        this.nom = nom;
        this.couleurSerializable = new java.awt.Color((float) couleur.getRed(),
                (float) couleur.getGreen(),
                (float) couleur.getBlue(),
                (float) couleur.getOpacity());
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    /**
     * Retourne le nom du tag
     *
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du tag
     *
     * @param nom Nouveau nom du tag
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la couleur du tag
     *
     * @return
     */
    public Color getCouleur() {
        return new Color((float) couleurSerializable.getRed()/255, (float) couleurSerializable.getGreen()/255, (float) couleurSerializable.getBlue()/255, (float) couleurSerializable.getTransparency());
    }

    /**
     * Modifie la couleur du tag
     *
     * @param couleur Nouvelle couleur du tag
     */
    public void setCouleur(Color couleur) {
        this.couleurSerializable = new java.awt.Color((float) couleur.getRed(),
                (float) couleur.getGreen(),
                (float) couleur.getBlue(),
                (float) couleur.getOpacity());
    }
}
