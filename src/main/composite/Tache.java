package main.composite;

import javafx.scene.control.TreeItem;
import main.Modele;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente une tâche dans le projet <br>
 * Elle hérite de la classe Composant
 *
 * @see Composant
 */
public class Tache extends Composant {

    /**
     * Liste des sous-tâches de la tâche
     */
    protected List<Composant> sousTaches;

    public Tache(String nom, String image, int duree) {
        super(nom, image, duree);
        this.sousTaches = new ArrayList<>();
        this.dependances = new ArrayList<>();
    }

    public Tache(String nom, String image, LocalDate dateDebut, LocalDate dateFin) {
        super(nom, image, dateDebut, dateFin);
        this.duree = 0;
        this.sousTaches = new ArrayList<>();
        this.dependances = new ArrayList<>();
    }

    @Override
    public TreeItem<Composant> afficher(Modele modele) {
        TreeItem<Composant> treeItem = new TreeItem<>(this);
        treeItem.setExpanded(true);
        for (Composant c : this.sousTaches) {
            treeItem.getChildren().add(c.afficher(modele));
        }
        return treeItem;
    }


    /**
     * Méthode qui permet d'ajouter une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void ajouter(Composant c) {
        this.sousTaches.add(c);
        c.setParent(this);
    }

    /**
     * Méthode qui permet de retirer une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void retirer(Composant c) {
        this.duree -= c.getDuree();
        this.sousTaches.remove(c);
        c.setParent(null);
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Composant> getSousTaches() {
        return this.sousTaches;
    }

    public void fixDuree(){
        int duree = 0;
        for (Composant composant : this.sousTaches) {
            duree += composant.getDuree();
        }
        if(this.duree < duree){
            this.duree = duree;
        }
        try{
            this.dateFin = this.calculerDateFin();
        } catch (ParseException e) {
            e.printStackTrace();
        };
        if(this.getParent() != null && this.getParent() instanceof Tache)
            ((Tache) this.getParent()).fixDuree();
    }
}
