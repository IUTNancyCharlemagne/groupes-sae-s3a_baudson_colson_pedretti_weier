package main.composite;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Tag implements Serializable {
    private String nom;
    private Color couleur;

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
