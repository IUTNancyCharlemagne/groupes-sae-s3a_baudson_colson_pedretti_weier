package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

import java.io.File;

public class ControlAjouterSousTache implements EventHandler<ActionEvent> {

    private Modele modele;
    private Composant composant;

    public ControlAjouterSousTache(Modele modele, Composant composant) {
        this.modele = modele;
        this.composant = composant;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Button btn = (Button) actionEvent.getSource();

        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        TextField nom = new TextField(
                ""
        );

        ImageView tacheImage = new ImageView();

        HBox imageSelection = new HBox();
        Button btnImage1 = new Button("Ajouter une image");
        btnImage1.getStyleClass().add("btn");
        imageSelection.getChildren().add(btnImage1);
        btnImage1.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            tacheImage.setFitHeight(200);
            tacheImage.setFitWidth(200);
            tacheImage.setPreserveRatio(true);
            tacheImage.setImage(new Image(selectedFile.toURI().toString()));
            imageSelection.getChildren().add(tacheImage);

        });

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            if (composant instanceof Tache) {
                boolean trouve = false;
                if (nom.getText().isEmpty()) {
                    System.out.println("Le nom de la tâche ne peut pas être vide.");
                } else {
                    for (Liste liste : modele.getProjet().getListeTaches()) {
                        for (Composant composant : liste.getComposants()) {
                            if (composant.getNom().equals(nom.getText())) {
                                trouve = true;
                            } else {
                                if (composant instanceof Tache) {
                                    for (Composant sousTache : ((Tache) composant).getSousTaches()) {
                                        if (sousTache.getNom().equals(nom.getText())) {
                                            trouve = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (trouve) {
                    System.out.println("La tâche existe déjà.");
                } else {
                    if (tacheImage.getImage() == null) {
                        ((Tache) composant).ajouter(new Tache(nom.getText(), null, 0));
                    } else {
                        ((Tache) composant).ajouter(new Tache(nom.getText(), tacheImage.getImage().getUrl(), 0));
                    }
                    modele.notifierObservateur();
                    stage.close();
                }
            }
        });

        imageSelection.setAlignment(Pos.CENTER);
        imageSelection.setSpacing(10);

        nom.getStyleClass().

                add("description");
        overlay.getChildren().

                add(nom);
        overlay.getChildren().

                add(imageSelection);
        overlay.getChildren().

                add(btnValider);
        overlay.setAlignment(Pos.CENTER);

        Scene scene = new Scene(overlay);
        scene.setOnKeyPressed(e ->
        {
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
        stage.setScene(scene);
        stage.setTitle("Ajouter une sous-tâche");
        stage.show();

    }
}