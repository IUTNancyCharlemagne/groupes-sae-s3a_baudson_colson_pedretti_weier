package main.observateur;

<<<<<<< HEAD
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.Tag;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.ControlAjouterSousTache;
import main.controleurs.ControlAjouterTag;

import java.io.File;

public class VueTache implements Observateur {

    Modele modele;

    public VueTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele modele)) return;
        modele.getPaneBureau().getChildren().clear();

        BorderPane overlayBackground = new BorderPane();
        overlayBackground.getStyleClass().add("overlayBackground");
        overlayBackground.setMinSize(modele.getStackPane().getWidth(), modele.getStackPane().getHeight());

        // Overlay
        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        // Quitter
        Button quitter = new Button("X");
        quitter.getStyleClass().add("quitter");
        quitter.setAlignment(Pos.TOP_RIGHT);
        overlay.getChildren().add(quitter);


        // Titre
        Text titre = new Text(modele.getCurrentTache().getNom());
        titre.getStyleClass().add("titre");
        overlay.getChildren().add(titre);

        // Tags
        VBox tagsGeneral = new VBox();
        Button btnAjouterTag = new Button("Ajouter un tag");
        btnAjouterTag.getStyleClass().add("btn");
        btnAjouterTag.setOnAction(new ControlAjouterTag(modele, modele.getCurrentTache()));
        btnAjouterTag.setId(modele.getCurrentTache().getNom());
        tagsGeneral.getChildren().add(btnAjouterTag);

        HBox tagsBox = new HBox();
        tagsBox.setAlignment(Pos.CENTER);
        tagsBox.setSpacing(10);
        for (Tag tag : modele.getCurrentTache().getTags()) {
            Label label = new Label(tag.getNom());
            label.getStyleClass().add("tag");
            label.setBackground(new Background(new BackgroundFill(tag.getCouleur(), CornerRadii.EMPTY, Insets.EMPTY)));
            tagsBox.getChildren().add(label);
            Button btnSupprimerTag = new Button("X");
            btnSupprimerTag.getStyleClass().add("btn");
            btnSupprimerTag.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    modele.getCurrentTache().removeTag(tag);
                    modele.notifierObservateur();
                }
            });
            tagsBox.getChildren().add(btnSupprimerTag);
        }

        tagsGeneral.getChildren().add(tagsBox);
        overlay.getChildren().add(tagsGeneral);

        // Description
        HBox detailsBox = new HBox();

        TextArea description = new TextArea();
        if (modele.getCurrentTache().getDescription() == null) {
            description.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet nisi at mi imperdiet elementum. Maecenas et tellus vitae enim dapibus sagittis. Nulla interdum enim vitae eros hendrerit pellentesque. Sed pretium tortor sit amet vestibulum finibus. Donec tempus nisl gravida arcu porttitor, ut laoreet lacus fringilla. Curabitur dui est, varius ut consectetur id, accumsan vel tortor. Praesent laoreet accumsan magna eget tristique.");
        } else {
            description.setText(modele.getCurrentTache().getDescription());
        }
        description.setWrapText(true);
        description.getStyleClass().add("description");
        detailsBox.getChildren().add(description);

        // Vbox de bouttons supprimer et archiver
        HBox buttons = new HBox();
        String archiverText;

        // Image
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setSpacing(10);
        Button btnImage = new Button();
        imageBox.getChildren().add(btnImage);
        detailsBox.getChildren().add(imageBox);
        ImageView image = new ImageView();
        if (modele.getCurrentTache().getImage() != null) {
            image.setImage(new Image(modele.getCurrentTache().getImage()));
            image.setFitHeight(200);
            image.setFitWidth(200);
            image.setPreserveRatio(true);
            btnImage.setText("Changer l'image");
            imageBox.getChildren().add(0, image);
        } else {
            btnImage.setText("Ajouter une image");
        }

        btnImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            image.setImage(new Image(selectedFile.toURI().toString()));
            image.setFitHeight(200);
            image.setFitWidth(200);
            image.setPreserveRatio(true);
            imageBox.getChildren().clear();
            imageBox.getChildren().addAll(image, btnImage);
        });

        detailsBox.setAlignment(Pos.CENTER);
        detailsBox.setSpacing(50);
        overlay.getChildren().add(detailsBox);


        Button btnSupprimer = new Button();
        ImageView imgSupp = new ImageView(new Image("file:icons/trash.png"));
        imgSupp.setFitHeight(50);
        imgSupp.setFitWidth(50);
        btnSupprimer.setGraphic(imgSupp);
        btnSupprimer.getStyleClass().add("quitter");
        btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modele.getProjet().supprimerTache(modele.getCurrentTache().getNom());
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        if (modele.getCurrentTache().getEstArchive()) archiverText = "Désarchiver";
        else archiverText = "Archiver";
        Button btnArchiver = new Button(archiverText);
        btnArchiver.getStyleClass().add("quitter");

        btnArchiver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!modele.getCurrentTache().getEstArchive()) {
                    modele.getProjet().archiverTache(modele.getCurrentTache().getNom());
                    modele.setCurrentTache(null);
                    modele.notifierObservateur();
                } else {
                    modele.getProjet().desarchiverTache(modele.getCurrentTache().getNom());
                    modele.setCurrentTache(null);
                    modele.notifierObservateur();
                }
            }
        });

        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.getChildren().addAll(btnSupprimer, btnArchiver);
        overlay.getChildren().add(buttons);

        Text sousTache = new Text("Sous-tâches");
        sousTache.getStyleClass().add("titre");
        overlay.getChildren().add(sousTache);

        VBox vBoxSousTaches = new VBox();
        vBoxSousTaches.getStyleClass().add("sousTaches");
        if (modele.getCurrentTache() instanceof Tache) {
            Tache tache = (Tache) modele.getCurrentTache();
            for (Composant sousTacheComposant : tache.getSousTaches()) {
                vBoxSousTaches.getChildren().add(sousTacheComposant.afficher(modele));
            }
        }
        overlay.getChildren().add(vBoxSousTaches);

        Button btnAjouterSousTache = new Button("Ajouter une sous-tâche");
        btnAjouterSousTache.getStyleClass().add("btn");
        btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, modele.getCurrentTache()));
        btnAjouterSousTache.setId(modele.getCurrentTache().getNom());
        overlay.getChildren().add(btnAjouterSousTache);

        ComboBox comboBox = new ComboBox();
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (!composant.getNom().equals(modele.getCurrentTache().getNom())) {
                    comboBox.getItems().add(composant.getNom());
                }
            }
        }

        overlay.getChildren().add(comboBox);
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modele.getCurrentTache().setDescription(description.getText());
                if (image.getImage() != null) {
                    modele.getCurrentTache().setImage(image.getImage().getUrl());
                }
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        overlay.setAlignment(Pos.CENTER);
        overlayBackground.setCenter(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));

        modele.getPaneBureau().getChildren().add(overlayBackground);
        overlay.setAlignment(Pos.CENTER);
        overlayBackground.setCenter(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
    }
=======
import javafx.scene.layout.VBox;
import main.Modele;
import main.Sujet;

public class VueTache extends VBox implements Observateur {

        @Override
        public void actualiser(Sujet s) {
            if (!(s instanceof main.Modele)) return;

            Modele modele = (Modele) s;





        }
>>>>>>> a9024266a47e98fe5547aa235f624cf37c11fc96
}
