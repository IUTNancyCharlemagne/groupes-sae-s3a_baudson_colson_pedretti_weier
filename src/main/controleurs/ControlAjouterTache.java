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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

import java.io.File;

/**
 * ControlAjouterTache est la classe qui represente le controleur qui ajoute une tâche à une liste.
 */
public class ControlAjouterTache implements EventHandler<ActionEvent> {

    /**
     * Le modele.
     */
    private Modele modele;

    /**
     * Constructeur de ControlAjouterTache
     *
     * @param modele le modele
     */
    public ControlAjouterTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Méthode qui ajoute une tâche à une liste.
     *
     * @param actionEvent l'action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Button btn = (Button) actionEvent.getSource();
        // Récupère le nom de la liste
        String nomListe = btn.getId();

        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        Text title = new Text("Nom de la tâche");

        TextField nom = new TextField(
                ""
        );

        Text dureeText = new Text("Durée de la tâche en jours");

        TextField duree = new TextField(
                ""
        );

        duree.setPromptText("5");

        ImageView tacheImage = new ImageView();

        VBox imageSelection = new VBox();
        Button btnImage1 = new Button("Ajouter une image");
        btnImage1.getStyleClass().add("btn");
        imageSelection.getChildren().add(tacheImage);
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
            if (selectedFile != null) tacheImage.setImage(new Image(selectedFile.toURI().toString()));

        });

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom de la tâche ne peut pas être vide.");
            } else {
                boolean trouve = false;
                for (Liste liste : modele.getProjet().getListeTaches()) {
                    for (Composant tache : liste.getComposants()) {
                        if (tache.getNom().equals(nom.getText())) {
                            trouve = true;
                        }
                    }
                }
                if (trouve) {
                    System.out.println("La tâche existe déjà.");
                } else {
                    String imgUrl = null;
                    int dureeInt = 5;
                    if (tacheImage.getImage() != null) {
                        imgUrl = tacheImage.getImage().getUrl();
                    }
                    if (!duree.getText().isEmpty()) {
                        try {
                            dureeInt = Integer.parseInt(duree.getText());
                        } catch (NumberFormatException ex) {
                            System.out.println("La durée doit être un nombre.");
                        }
                    }

                    modele.getProjet().getListeTaches(nomListe).ajouterComposant(new Tache(nom.getText(), imgUrl, dureeInt));

                    modele.notifierObservateur();
                }
                stage.close();
            }
        });

        imageSelection.setAlignment(Pos.CENTER);
        imageSelection.setSpacing(10);


        nom.getStyleClass().add("description");
        overlay.getChildren().add(title);
        overlay.getChildren().add(nom);
        overlay.getChildren().add(dureeText);
        overlay.getChildren().add(duree);
        overlay.getChildren().add(imageSelection);
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
        stage.setTitle("Ajouter une tâche");
        stage.setScene(scene);
        stage.show();
    }
}
