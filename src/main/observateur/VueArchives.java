package main.observateur;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Modele;
import main.Sujet;
import main.TreeViewActions;
import main.controleurs.ControlChangerTitreListe;
import main.controleurs.ControlOnDragDroppedListe;
import main.controleurs.ControlOnDragOver;
import main.objet.composite.Composant;

public class VueArchives implements Observateur {
    /**
     * Nombre maximum de tâches par ligne
     */
    public static int maxParLigne = 15;

    /**
     * Actualise la vue
     * @param s Sujet
     */
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
            if (x > maxParLigne) {
                x = 0;
                y++;
            }
            if (c.afficherArchive(modele) != null) pane.add(c.afficherArchive(modele), x, y);
        }
        titre.setPadding(new Insets(20));
        pane.setPadding(new Insets(20));
        mainBox.getChildren().addAll(titre, afficherTreeView(s));
        modele.getPaneBureau().getChildren().add(mainBox);
    }

    public VBox afficherTreeView(Sujet s) {
        if (!(s instanceof Modele modele)) return null;
        // Liste de tâches
        VBox paneListe = new VBox();
        paneListe.setId("ARCHIVES");

        // Informations de la liste
        HBox infosListe = new HBox();
        infosListe.setAlignment(Pos.CENTER);
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        infosListe.setPadding(new Insets(10, 10, 2, 10));

        Text textNom = new Text("Archives");
        textNom.setStyle("" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;");
        paneListe.getChildren().add(infosListe);

        for (Composant c : modele.getProjet().getArchives()) {
            if(c.getParent() == null){
                TreeView<Composant> treeView = new TreeView<>(c.afficher(modele));
                TreeViewActions.addTreeAction(modele, treeView);
                paneListe.getChildren().add(treeView);
            }
        }

        paneListe.setOnDragDropped(new ControlOnDragDroppedListe(modele));
        paneListe.setOnDragOver(new ControlOnDragOver(modele));

        return paneListe;

    }
}
