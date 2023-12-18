package main.composite;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Modele;

import java.io.Serializable;
import java.util.List;

public abstract class Composant implements Serializable {
    protected String nom;
    protected String description;
    protected boolean estArchive;
    protected List<Tag> tags;
    protected int nbTags; // SERT POUR LA SAUVEGARDE

    public abstract VBox afficher(Modele modele);

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public boolean getEstArchive() {
        return estArchive;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstArchive(boolean estArchive) {
        this.estArchive = estArchive;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag){
        tags.add(tag);
        this.nbTags++;
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
        this.nbTags--;
    }

    public int getNbTags(){
        return this.nbTags;
    }
}
