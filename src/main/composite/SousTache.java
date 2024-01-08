package main.composite;

import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import main.Modele;
import main.Tag;

import java.util.ArrayList;

/**
 * Classe qui représente une sous-tâche <br>
 * Elle hérite de la classe Composant
 * @see Composant
 */
public class SousTache extends Composant{

    /**
     * Constructeur de la classe SousTache
     * @param nom le nom de la sous-tâche
     */
    public SousTache(String nom){
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
    }

    @Override
    public TreeItem<Tache> afficher(Modele modele) {
        return null;
    }

    @Override
    public Tache getComposant(String nom) {
        return null;
    }
}
