package main.composite;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Modele;
import main.Tag;
import main.controleurs.ControlAfficherTache;
import main.controleurs.ControlOnDragDetected;

import java.io.Serializable;
import java.util.List;

/**
 * Classe abstraite représentant un composant (Tâche ou sous-tâche)
 * @see Tache
 * @see SousTache
 */
public abstract class Composant implements Serializable {

    /**
     * Nom de la tâche
     */
    protected String nom;

    /**
     * Description de la tâche
     */
    protected String description;

    /**
     * Booléen indiquant si la tâche est archivée ou non
     */
    protected boolean estArchive;

    /**
     * Liste des tags de la tâche
     * @see Tag
     */
    protected List<Tag> tags;

    /**
     * Image de description de la tâche
     */
    protected String image;

    /**
     * Nombre de tags de la tâche
     */
    protected int nbTags; // SERT POUR LA SAUVEGARDE

    /**
     * Booléen qui indique si la tâche est terminée ou non
     */
    protected boolean estTerminee;

    /**
     * Liste des dépendances de la tâche
     */
    protected List<Composant> dependances;

    /**
     * Parent du composant
     */
    protected Composant parent;

    /**
     * Méthode abstraite permettant d'afficher la tâche
     * @param modele Modèle de l'application
     * @return
     */
    public abstract TreeItem<Tache> afficher(Modele modele);

    // ### Test d'affichage ###
    public VBox afficherArchive(Modele modele) {
        // Création du Pane de la tâche
        VBox paneTache = new VBox();
        paneTache.setId(this.nom);
        paneTache.getStyleClass().add("paneTache");

        // Création du texte du nom de la tâche
        Text textNom = new Text(this.nom);
        paneTache.getChildren().add(textNom);

        paneTache.setOnMouseClicked(new ControlAfficherTache(modele));
        paneTache.setOnDragDetected(new ControlOnDragDetected(modele));
        return paneTache;
    }

    // ### Test Get Composant ###
    public abstract Tache getComposant(String nom);

    /**
     * Méthode permettant d'ajouter un tag à la tâche
     * @param tag Tag à ajouter
     */
    public void addTag(Tag tag){
        tags.add(tag);
        this.nbTags++;
    }

    /**
     * Méthode permettant de supprimer un tag de la tâche
     * @param tag Tag à supprimer
     */
    public void removeTag(Tag tag){
        tags.remove(tag);
        this.nbTags--;
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

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
    public int getNbTags(){
        return this.nbTags;
    }
    public boolean getEstTerminee(){return this.estTerminee;}
    public void setEstTerminee(boolean b){
        this.estTerminee = b;
    }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public List<Composant> getDependances() {
        return dependances;
    }

    public void setDependances(List<Composant> dependances) {
        this.dependances = dependances;
    }

    public Composant getParent() {
        return parent;
    }

    public void setParent(Composant parent) {
        this.parent = parent;
    }
}
