package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

/**
 * ControlAjouterListe est la classe qui represente le controleur qui ajoute une liste au projet
 */
public class ControlAjouterListe implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlAjouterListe
     *
     * @param modele le modele
     */
    public ControlAjouterListe(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui ajoute une liste au projet
     *
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Nom de la liste :");

        TextArea nom = new TextArea(
                ""
        );

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom de la liste ne peut pas être vide.");
            } else {
                boolean trouve = false;
                for (Liste liste : modele.getProjet().getListeTaches()) {
                    if (liste.getNom().equals(nom.getText()) && !trouve) {
                        trouve = true;
                    }
                }
                if (trouve) {
                    System.out.println("La liste existe déjà.");
                } else {
                    modele.getProjet().ajouterListeTaches(new Liste(nom.getText()));
                    modele.notifierObservateur();
                }
                modele.getStackPane().getChildren().remove(overlay);
            }
        });

        nom.setWrapText(true);
        nom.getStyleClass().add("description");
        overlay.getChildren().add(title);
        overlay.getChildren().add(nom);
        overlay.getChildren().add(btnValider);

        modele.getStackPane().getChildren().add(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
    }
}

