package main.objet.composite;

import javafx.scene.control.TreeItem;
import main.Modele;

import java.time.LocalDate;

/**
 * Classe qui représente une sous-tâche <br>
 * Elle hérite de la classe Composant
 * @see Composant
 */
public class SousTache extends Composant{

    /**
     * Constructeur par défaut
     * @param nom Nom de la sous-tâche
     * @param image Image de la sous-tâche
     * @param duree Durée de la sous-tâche
     */
    public SousTache(String nom, String image, int duree) {
        super(nom, image, duree);
    }

    /**
     * Constructeur par défaut
     * @param nom Nom de la sous-tâche
     * @param image Image de la sous-tâche
     * @param dateDebut Date de début de la sous-tâche
     * @param dateFin Date de fin de la sous-tâche
     */
    public SousTache(String nom, String image, LocalDate dateDebut, LocalDate dateFin) {
        super(nom, image, dateDebut, dateFin);
    }

    /**
     * Affiche la sous-tâche tâche dans le treeview
     * @param modele
     * @return
     */
    @Override
    public TreeItem<Composant> afficher(Modele modele) {
        TreeItem<Composant> treeItem = new TreeItem<>(this);
        treeItem.setExpanded(true);
        return treeItem;
    }

}
