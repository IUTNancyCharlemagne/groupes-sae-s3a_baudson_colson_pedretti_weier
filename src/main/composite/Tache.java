package main.composite;

import java.util.ArrayList;
import java.util.List;

public class Tache extends Composant{
    protected List<Composant> enfants;

    public Tache(String nom){
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.enfants = new ArrayList<Composant>();
    }

    @Override
    public void operation() {
        System.out.println("Tache " + this.nom + " : " + this.description);
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
}
