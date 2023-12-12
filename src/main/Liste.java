package main;

import main.composite.Composant;

import java.util.ArrayList;
import java.util.List;

public class Liste {
    private String nom;
    private ArrayList<Composant> composants;

    public Liste(String nom) {
        this.nom = nom;
        composants = new ArrayList<Composant>();
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom() {
        this.nom = nom;
    }

    public List<Composant> getComposants() {
        return this.composants;
    }

    public void ajouterComposant(Composant c) {
        this.composants.add(c);
    }

    public void retirerComposant(Composant c) {
        this.composants.remove(c);
    }

    public String afficher() {
        StringBuilder s = new StringBuilder();
        s.append("Liste ").append(this.nom).append(" :\n");
        for (Composant c : composants) {
            s.append(c.afficher());
        }
        return s.toString();
    }
}
