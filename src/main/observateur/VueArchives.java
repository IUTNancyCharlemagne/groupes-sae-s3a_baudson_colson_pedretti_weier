package main.observateur;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;

public class VueArchives implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele modele)) return;
        modele.getPaneBureau().getChildren().clear();
        modele.getPaneBureau().getStyleClass().clear();
        modele.getPaneBureau().getStyleClass().add("paneArchives");

        VBox pane = new VBox();
        for (Composant c : modele.getProjet().getArchives()) {
            pane.getChildren().add(c.afficher(modele));
            System.out.println(c);
        }
        modele.getPaneBureau().getChildren().add(pane);
    }
}
