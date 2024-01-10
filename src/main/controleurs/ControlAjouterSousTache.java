package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.SousTache;
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

        HBox dureeHbox = new HBox();

        Text dureeText = new Text("Durée :");
        dureeText.setFont(new Font("Arial", 20));
        TextField dureeField = new TextField();
        dureeField.setPromptText("0");

        dureeHbox.getChildren().addAll(dureeText, dureeField);

        Button btnValider = new Button("Valider");
        btnValider.getStyleClass().add("btn");
        btnValider.setOnAction(e -> {
            boolean trouve = false;
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom de la tâche ne peut pas être vide.");
            } else {
                for (Composant composant : modele.getProjet().getListeTouteTaches()) {
                    if (composant.getNom().equals(nom.getText())) {
                        trouve = true;
                    }
                }

                if (trouve) {
                    System.out.println("La tâche existe déjà.");
                } else {

                    if (composant instanceof SousTache) {
                        Tache newTache = new Tache(composant.getNom(), composant.getImage(), composant.getDateDebut(), composant.getDateFin());
                        if (composant.getParent() == null) {
                            for (Liste liste : modele.getProjet().getListeTaches()) {
                                for (Composant composantCherche : liste.getComposants()) {
                                    if (composantCherche.getNom().equals(composant.getNom())) {
                                        liste.retirerComposant(composant);
                                        liste.ajouterComposant(newTache);
                                    }
                                }
                            }
                        } else {
                            System.out.println("parent : " + composant.getParent().getNom());
                            newTache.setParent(composant.getParent());
                            ((Tache) composant.getParent()).ajouter(newTache);
                            ((Tache) composant.getParent()).retirer(composant);
                        }
                        modele.getProjet().getListeTouteTaches().remove(composant);
                        modele.getProjet().getListeTouteTaches().add(newTache);
                        composant = newTache;
                        modele.setCurrentTache(newTache);
                    }


                    int outputDuree = 0;
                    if (!dureeField.getText().isEmpty()) {
                        outputDuree = Integer.parseInt(dureeField.getText());
                    }
                    if (tacheImage.getImage() == null) {
                        SousTache tache = new SousTache(nom.getText(), null, outputDuree);
                        ((Tache) composant).ajouter(tache);
                        ((Tache) composant).fixDuree();
                        modele.getProjet().getListeTouteTaches().add(tache);
                    } else {
                        SousTache tache = new SousTache(nom.getText(), tacheImage.getImage().getUrl(), outputDuree);
                        ((Tache) composant).ajouter(tache);
                        ((Tache) composant).fixDuree();
                        modele.getProjet().getListeTouteTaches().add(tache);
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

                add(dureeHbox);
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
        scene.getStylesheets().

                add("file:src/main/css/style.css");
        stage.getIcons().

                add(new Image("file:icons/logo.png"));
        stage.setScene(scene);
        stage.setTitle("Ajouter une sous-tâche");
        stage.show();
    }
}