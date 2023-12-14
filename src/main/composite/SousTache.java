package main.composite;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Modele;

import java.util.ArrayList;

public class SousTache extends Composant{
    public SousTache(String nom){
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
    }

    @Override
    public VBox afficher(Modele modele) {
        VBox s = new VBox();
        return s;
    }
}
