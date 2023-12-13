package main.composite;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Tache extends Composant {
    protected List<Composant> enfants;
    protected List<Tache> dependances;
    protected boolean estTerminee;

    public Tache(String nom) {
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.enfants = new ArrayList<Composant>();
        this.dependances = new ArrayList<Tache>();
        this.estTerminee = false;
    }

    /**
     * Méthode qui permet de récupérer l'élément JavaFX pour l'afficher dans la méthode main
     * @return un objet Pane correspondant
     */
    @Override
    public Pane afficher() {

        Pane paneTache = new Pane();

        Text textNom = new Text(this.nom);
        paneTache.getChildren().add(textNom);

        for (Composant c : enfants) {
            paneTache.getChildren().add(c.afficher());
        }
        return paneTache;
    }

    /**
     * Méthode qui permet d'ajouter une sous-tâche à la tâche
     * @param c sous-tâche
     */
    public void ajouter(Composant c) {
        this.enfants.add(c);
    }

    public void retirer(Composant c) {
        this.enfants.remove(c);
    }

    public List<Composant> getEnfants() {
        return this.enfants;
    }

    public boolean estTerminee(){return this.estTerminee;}
    public void setTerminee(boolean b){
        this.estTerminee = b;
    }

    public List<Tache> getDependances() {
        return dependances;
    }

    /**
     * Méthode qui permet de voir si les dépendances d'une tâche sont terminées.
      * @return vrai si toutes les dépendances sont terminées, faux sinon
     */
    public boolean dependancesOk(){
        boolean ok = true;
        for(Tache t : this.dependances){
            ok = ok && t.estTerminee();
        }
        return ok;
    }
}
