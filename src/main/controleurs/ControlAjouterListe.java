package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.objet.Liste;
import main.Modele;

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
        Stage stage = new Stage();
        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Nom de la liste :");

        TextField nom = new TextField(
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
                stage.close();
            }
        });

        nom.getStyleClass().add("description");
        overlay.getChildren().add(title);
        overlay.getChildren().add(nom);
        overlay.getChildren().add(btnValider);
        overlay.setAlignment(Pos.CENTER);

        Scene scene = new Scene(overlay);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    btnValider.fire();
                    break;
                case ESCAPE:
                    stage.close();
                    break;
            }
        });
        scene.getStylesheets().add("file:src/main/css/style.css");
        stage.getIcons().add(new Image("file:icons/logo.png"));
        stage.setTitle("Ajouter une liste");
        stage.setScene(scene);
        stage.show();
    }
}

