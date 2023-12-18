package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

public class ControlAjouterSousTache implements EventHandler<ActionEvent> {

    private Modele modele;
    private Composant composant;

    public ControlAjouterSousTache(Modele modele, Composant composant) {
        this.modele = modele;
        this.composant = composant;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String nomListe = btn.getId();

        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        TextArea nom = new TextArea(
                ""
        );

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            if (composant instanceof Tache) {
                boolean trouve = false;
                if (nom.getText().isEmpty()) {
                    System.out.println("Le nom de la tâche ne peut pas être vide.");
                } else {
                    for (Composant tache : ((Tache) composant).getSousTaches()) {
                        if (tache.getNom().equals(nom.getText())) {
                            trouve = true;
                        }
                    }
                }
                if (trouve) {
                    System.out.println("La tâche existe déjà.");
                } else {
                    ((Tache) composant).ajouter(new Tache(nom.getText()));
                    modele.notifierObservateur();
                }
                modele.getStackPane().getChildren().remove(overlay);
            }
        });

        nom.setWrapText(true);
        nom.getStyleClass().add("description");
        overlay.getChildren().add(nom);
        overlay.getChildren().add(btnValider);

        modele.getStackPane().getChildren().add(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
    }
}
