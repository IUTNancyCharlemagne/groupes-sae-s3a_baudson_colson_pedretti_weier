package main;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.composite.Composant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Liste implements Serializable {
    private String nom;
    private final ArrayList<Composant> composants;

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

    public VBox afficher() {

        VBox paneListe = new VBox();
        paneListe.setSpacing(10);

        Text textNom = new Text(this.nom);
        paneListe.getChildren().add(textNom);

        for (Composant c : composants) {
            paneListe.getChildren().add(c.afficher());
        }

        return paneListe;
    }
}
