package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.Tag;
import main.composite.Composant;
import main.composite.Tache;

/**
 * ControlAjouterListe est la classe qui represente le controleur qui ajoute une liste au projet
 */
public class ControlAjouterTag implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * La tache
     */

    private Composant composant;

    /**
     * Constructeur de ControlAjouterListe
     *
     * @param modele le modele
     */
    public ControlAjouterTag(Modele modele, Composant composant) {
        this.composant = composant;
        this.modele = modele;
    }

    /**
     * Methode qui ajoute un tag à la tache
     *
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = new Stage();
        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Nom du tag :");

        TextField nom = new TextField(
                ""
        );

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.RED);

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom du tag ne peut pas être vide");
            } else {
                boolean trouve = false;
                for (Tag tag : composant.getTags()) {
                    if (tag.getNom().equals(nom.getText())) {
                        trouve = true;
                    }
                }
                if (trouve) {
                    System.out.println("Le tag existe déjà.");
                } else {
                    composant.addTag(new Tag(nom.getText(), colorPicker.getValue()));
                    modele.notifierObservateur();
                }
                stage.close();
            }
        });

        nom.getStyleClass().add("description");
        overlay.getChildren().add(title);
        overlay.getChildren().add(nom);
        overlay.getChildren().add(colorPicker);
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
        stage.setTitle("Ajouter une liste");
        stage.setScene(scene);
        stage.show();
    }
}

