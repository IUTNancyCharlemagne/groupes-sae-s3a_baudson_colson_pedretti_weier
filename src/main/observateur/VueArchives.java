package main.observateur;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Modele;
import main.Sujet;
import main.objet.composite.Composant;

public class VueArchives implements Observateur {

    public static int maxParLigne = 15;

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele modele)) return;
        modele.getPaneBureau().getChildren().clear();
        modele.getPaneBureau().getStyleClass().clear();
        modele.getPaneBureau().getStyleClass().add("paneArchives");

        VBox mainBox = new VBox();
        Label titre = new Label("Archives");
        titre.setFont(new Font("Arial", 32));
        GridPane pane = new GridPane();
        int y = 0;
        int x = 0;
        for (Composant c : modele.getProjet().getArchives()) {
            x++;
            if(x > maxParLigne){
                x = 0;
                y++;
            }
            if(c.afficherArchive(modele) != null) pane.add(c.afficherArchive(modele), x, y);
        }
        titre.setPadding(new Insets(20));
        pane.setPadding(new Insets(20));
        mainBox.getChildren().addAll(titre, pane);
        modele.getPaneBureau().getChildren().add(mainBox);
    }
}
