package main.composite;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class SousTache extends Composant{
    public SousTache(String nom){
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
    }

    @Override
    public Pane afficher() {
        Pane s = new Pane();
        return s;
    }
}
