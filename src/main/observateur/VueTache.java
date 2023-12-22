package main.observateur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
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
import main.controleurs.ControlChangerTitre;

import java.io.File;

public class VueTache implements Observateur {

    Modele modele;

    public VueTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void actualiser(Sujet s) {

        if (!(s instanceof Modele modele)) return;

        // ### Déclaration des variables ###

        StackPane   overlayBackground = new StackPane();
        GridPane    overlay =           new GridPane();

        // ### Box ###
        HBox tagsGeneral = new HBox();
        VBox imageBox = new VBox();
        VBox vBoxSousTaches = new VBox();
        ComboBox comboBox = new ComboBox();

        // ### Boutons ###
        Button quitter = new Button("X");
        Button btnAjouterTag = new Button("+");
        Button btnImage = new Button();
        Button btnSupprimer = new Button();
        Button btnArchiver;
        Button btnAjouterSousTache = new Button("Ajouter une sous-tâche");

        // ### Texte ###
        Text titre = new Text();
        Text sousTacheText = new Text("Sous-tâches");
        TextArea description = new TextArea();

        // ### Image ###
        ImageView image = new ImageView();
        ImageView imgSupp = new ImageView(new Image("file:icons/trash.png"));

        String archiverText = "";

        // ### Overlay background ###
        overlayBackground.getStyleClass().add("overlayBackground");
        overlayBackground.setPrefSize(
                modele.getStackPane().getWidth(),
                modele.getStackPane().getHeight());

        // ### Overlay ###
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.CENTER);
        overlay.setHgap(20);
        overlay.setVgap(20);
        overlay.setId("overlay");

        // ### Titre ###
        titre.setText(modele.getCurrentTache().getNom());
        titre.getStyleClass().add("titre");
        titre.setOnMouseClicked(new ControlChangerTitre(modele, modele.getCurrentTache()));

        // ### Quitter ###
        quitter.getStyleClass().add("quitter");
        // TODO: Faire un ControlQuitterTache qui enregistre les modifications
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modele.getCurrentTache().setDescription(modele.getCurrentTache().getDescription());
                if (image.getImage() != null) {
                    modele.getCurrentTache().setImage(image.getImage().getUrl());
                }
                modele.getStackPane().getChildren().remove(overlayBackground);
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        // ### Tags ###
        for (Tag tag : modele.getCurrentTache().getTags()) {
            HBox tagBox = new HBox();
            tagBox.setSpacing(5);
            tagBox.setPadding(new Insets(5));
            tagBox.setAlignment(Pos.CENTER);
            tagBox.setBackground(new Background(
                    new BackgroundFill(
                            tag.getCouleur(), new CornerRadii(10), Insets.EMPTY)));
            // ### Texte du tag ###
            Label label = new Label(tag.getNom());
            label.getStyleClass().add("tag");
            // ### Bouton supprimer tag ###
            Button btnSupprimerTag = new Button("x");
            btnSupprimerTag.getStyleClass().add("btnTag");
            tagBox.getChildren().addAll(label, btnSupprimerTag);
            // TODO: Faire un ControlSupprimerTag qui supprime le tag de la tache
            btnSupprimerTag.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    modele.getStackPane().getChildren().remove(overlayBackground);
                    modele.getCurrentTache().removeTag(tag);
                    modele.notifierObservateur();
                }
            });
            tagsGeneral.getChildren().add(tagBox);
        }

        // ### Bouton ajouter tag ###
        btnAjouterTag.getStyleClass().add("btn");
        btnAjouterTag.setOnAction(new ControlAjouterTag(modele, modele.getCurrentTache()));
        btnAjouterTag.setId(modele.getCurrentTache().getNom());

        // ### Conteneur des tags ###
        tagsGeneral.getChildren().add(btnAjouterTag);
        tagsGeneral.setSpacing(5);

        // ### Description ###
        description.setText(modele.getCurrentTache().getDescription());
        description.setWrapText(true);
        description.getStyleClass().add("description");

        // ### Image ###
        imageBox.setSpacing(10);
        imageBox.setAlignment(Pos.CENTER);
        GridPane.setHalignment(imageBox, HPos.CENTER);
        imageBox.getChildren().add(btnImage);
        // Si la tache a une image
        if (modele.getCurrentTache().getImage() != null) {
            image.setImage(new Image(modele.getCurrentTache().getImage()));
            image.setFitHeight(300);
            image.setFitWidth(300);
            image.setPreserveRatio(true);
            btnImage.setText("Changer l'image");
            imageBox.getChildren().add(image);
        } else {
            btnImage.setText("Ajouter une image");
        }

        // TODO: Faire un ControlAjouterImage qui ajoute une image a la tache
        btnImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            image.setImage(new Image(selectedFile.toURI().toString()));
            image.setFitHeight(300);
            image.setFitWidth(300);
            image.setPreserveRatio(true);
            modele.getStackPane().getChildren().remove(overlayBackground);
            modele.notifierObservateur();
        });

        // ### Bouton supprimer tache ###
        imgSupp.setFitHeight(10);
        imgSupp.setFitWidth(10);
        btnSupprimer.setGraphic(imgSupp);
        btnSupprimer.getStyleClass().add("quitter");
        // TODO: Faire un ControlSupprimerTache qui supprime la tache
        btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modele.getProjet().supprimerTache(modele.getCurrentTache().getNom());
                modele.getStackPane().getChildren().remove(overlayBackground);
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        // ### Bouton archiver tache ###
        // Si la tache est archivee, le bouton affiche "Desarchiver" sinon il affiche "Archiver"
        archiverText = modele.getCurrentTache().getEstArchive() ? "Désarchiver" : "Archiver";
        btnArchiver = new Button(archiverText);
        GridPane.setHalignment(btnArchiver, HPos.RIGHT);
        btnArchiver.getStyleClass().add("quitter");
        // TODO: Faire un ControlArchiverTache qui archive ou désarchive la tache
        btnArchiver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!modele.getCurrentTache().getEstArchive()) {
                    modele.getProjet().archiverTache(modele.getCurrentTache().getNom());
                } else {
                    modele.getProjet().desarchiverTache(modele.getCurrentTache().getNom());
                }
                modele.getStackPane().getChildren().remove(overlayBackground);
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        // ### Texte sous-taches ###
        sousTacheText.getStyleClass().add("titre");

        // ### Sous-taches ###
        vBoxSousTaches.getStyleClass().add("sousTaches");
        if (modele.getCurrentTache() instanceof Tache) {
            Tache tache = (Tache) modele.getCurrentTache();
            for (Composant sousTacheComposant : tache.getSousTaches()) {
                vBoxSousTaches.getChildren().add(sousTacheComposant.afficher(modele));
            }
        }

        // ### Bouton ajouter sous-tache ###
        btnAjouterSousTache.getStyleClass().add("btn");
        btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, modele.getCurrentTache()));
        btnAjouterSousTache.setId(modele.getCurrentTache().getNom());

        // ### ComboBox ###
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (!composant.getNom().equals(modele.getCurrentTache().getNom())) {
                    comboBox.getItems().add(composant.getNom());
                }
            }
        }

        // ### Ajout overlay background ###
        modele.getStackPane().getChildren().add(overlayBackground);

        // ### Ajout des elements au gridpane ###
        overlay.addRow(0, titre, new Label(), quitter);
        overlay.addRow(1, tagsGeneral);
        overlay.addRow(2, new Text("Description"));
        overlay.addRow(3, description, imageBox);
        overlay.addRow(4, sousTacheText);
        overlay.addRow(5, vBoxSousTaches);
        overlay.addRow(6, new Text("Dépendances"));
        overlay.addRow(7, comboBox);
        overlay.addRow(8, btnAjouterSousTache, btnArchiver, btnSupprimer);

        overlayBackground.getChildren().add(overlay);
    }
}
