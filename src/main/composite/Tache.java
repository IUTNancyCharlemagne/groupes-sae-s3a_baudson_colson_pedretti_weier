package main.composite;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Modele;
import main.Tag;
import main.controleurs.ControlAfficherTache;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente une tâche dans le projet <br>
 * Elle hérite de la classe Composant
 * @see Composant
 */
public class Tache extends Composant{

    /**
     * Liste des sous-tâches de la tâche
     */
    protected List<Composant> sousTaches;

    /**
     * Liste des dépendances de la tâche
     */
    protected List<Tache> dependances;

    @Override
    public String toString() {
        return "Tache{" +
                "enfants=" + sousTaches +
                ", dependances=" + dependances +
                ", estTerminee=" + estTerminee +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", estArchive=" + estArchive +
                ", tags=" + tags +
                '}';
    }

    public Tache(String nom) {
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.sousTaches = new ArrayList<Composant>();
        this.dependances = new ArrayList<Tache>();
        this.estTerminee = false;
        this.nbTags = 0;
    }

    @Override
    public VBox afficher(Modele modele) {

        // Création du Pane de la tâche
        VBox paneTache = new VBox();
        paneTache.getStyleClass().add("paneTache");

        // Création du texte du nom de la tâche
        Text textNom = new Text(this.nom);
        paneTache.getChildren().add(textNom);

        // Ajout des sous-tâches
        for (Composant c : sousTaches) {
            paneTache.getChildren().add(c.afficher(modele));
        }

        paneTache.setOnMouseClicked(new ControlAfficherTache(modele));
        return paneTache;
    }

    /**
     * Méthode qui permet d'ajouter une sous-tâche à la tâche
     * @param c sous-tâche
     */
    public void ajouter(Composant c) {
        this.sousTaches.add(c);
    }

    /**
     * Méthode qui permet de retirer une sous-tâche à la tâche
     * @param c sous-tâche
     */
    public void retirer(Composant c) {
        this.sousTaches.remove(c);
    }

    /**
     * Méthode qui permet de voir si les dépendances d'une tâche sont terminées.
     * @return vrai si toutes les dépendances sont terminées, faux sinon
     */
    public boolean dependancesOk(){
        boolean ok = true;
        for(Tache t : this.dependances){
            ok = ok && t.getEstTerminee();
        }
        return ok;
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Composant> getSousTaches() {
        return this.sousTaches;
    }
    public List<Tache> getDependances() {
        return dependances;
    }
}
