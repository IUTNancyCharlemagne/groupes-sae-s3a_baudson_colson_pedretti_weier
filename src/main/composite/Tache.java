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
}
