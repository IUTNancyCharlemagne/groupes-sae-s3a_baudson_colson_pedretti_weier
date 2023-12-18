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

public class ControlAjouterTache implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControlAjouterTache(Modele modele) {
        this.modele = modele;
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
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom de la tâche ne peut pas être vide.");
            } else {
                boolean trouve = false;
                for (Liste liste : modele.getProjet().getListeTaches()){
                    for (Composant tache : liste.getComposants()){
                        if (tache.getNom().equals(nom.getText())){
                            trouve = true;
                        }
                    }
                }
                if (trouve){
                    System.out.println("La tâche existe déjà.");
                } else {
                    modele.getProjet().getListeTaches(nomListe).ajouterComposant(new Tache(nom.getText()));
                    modele.notifierObservateur();
                }
                modele.stackPane.getChildren().remove(overlay);
            }
        });

        nom.setWrapText(true);
        nom.getStyleClass().add("description");
        overlay.getChildren().add(nom);
        overlay.getChildren().add(btnValider);

        modele.stackPane.getChildren().add(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
    }
}
