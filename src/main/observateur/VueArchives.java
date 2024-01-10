package main.observateur;

import javafx.scene.layout.VBox;
import main.Modele;
import main.Sujet;
import main.objet.composite.Composant;

public class VueArchives implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele modele)) return;
        modele.getPaneBureau().getChildren().clear();
        modele.getPaneBureau().getStyleClass().clear();
        modele.getPaneBureau().getStyleClass().add("paneArchives");

        VBox pane = new VBox();
        for (Composant c : modele.getProjet().getArchives()) {
            pane.getChildren().add(c.afficherArchive(modele));
            System.out.println(c);
        }
        modele.getPaneBureau().getChildren().add(pane);
    }
}
