package main.composite;

import java.util.ArrayList;

public class SousTache extends Composant{
    public SousTache(String nom){
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
    }

    @Override
    public String afficher() {
         return "Sous-tâche " + this.nom + "\n";
    }
}
