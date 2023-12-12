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
    public String afficher() {
        StringBuilder s = new StringBuilder();
        s.append("Tache ").append(this.nom).append(" :\n");
        for (Composant c : enfants) {
            s.append(c.afficher()).append("\n");
        }
        return s.toString();
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
