package main;

import javafx.scene.paint.Color;

public class Tag{
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
