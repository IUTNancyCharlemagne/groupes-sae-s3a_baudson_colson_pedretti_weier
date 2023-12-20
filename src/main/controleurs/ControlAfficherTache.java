package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Liste;
import main.Modele;
import main.Tag;
import main.composite.Composant;
import main.composite.Tache;

import java.io.File;

/**
 * ControlAfficherTache est la classe qui represente le controleur qui affiche une tache en detail
 */
public class ControlAfficherTache implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlAfficherTache
     *
     * @param modele Le modele
     */
    public ControlAfficherTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui affiche une tache en detail
     *
     * @param mouseEvent L'evenement de la souris
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        boolean trouve = false;
        Composant composantAfficher = null;
        Liste listeAfficher = null;
        // Recherche de la tache
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                // Si la tache est trouvee
                if (composant.getNom().equals(nomTache) && !trouve) {
                    listeAfficher = liste;
                    composantAfficher = composant;
                    trouve = true;
                } else if (!trouve) {
                    // Recherche dans les sous-taches
                    if (composant instanceof Tache) {
                        Tache tache = (Tache) composant;
                        for (Composant sousTache : tache.getSousTaches()) {
                            if (sousTache.getNom().equals(nomTache)) {
                                listeAfficher = liste;
                                composantAfficher = sousTache;
                                trouve = true;
                            }
                        }
                    }
                }
            }
        }

        if (trouve) {
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
            Text titre = new Text(composantAfficher.getNom());
            titre.getStyleClass().add("titre");
            overlay.getChildren().add(titre);

            // Tags
            VBox tagsGeneral = new VBox();
            Button btnAjouterTag = new Button("Ajouter un tag");
            btnAjouterTag.getStyleClass().add("btn");
            btnAjouterTag.setOnAction(new ControlAjouterTag(modele, composantAfficher));
            btnAjouterTag.setId(composantAfficher.getNom());
            tagsGeneral.getChildren().add(btnAjouterTag);

            HBox tagsBox = new HBox();
            tagsBox.setAlignment(Pos.CENTER);
            tagsBox.setSpacing(10);
            for (Tag tag : composantAfficher.getTags()) {
                Label label = new Label(tag.getNom());
                label.getStyleClass().add("tag");
                label.setBackground(new Background(new BackgroundFill(tag.getCouleur(), CornerRadii.EMPTY, Insets.EMPTY)));
                tagsBox.getChildren().add(label);
                Button btnSupprimerTag = new Button("X");
                btnSupprimerTag.getStyleClass().add("btn");

                Composant finalComposantAfficher = composantAfficher;
                btnSupprimerTag.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        finalComposantAfficher.removeTag(tag);
                        modele.getStackPane().getChildren().remove(overlayBackground);
                    }
                });
                tagsBox.getChildren().add(btnSupprimerTag);
            }

            tagsGeneral.getChildren().add(tagsBox);
            overlay.getChildren().add(tagsGeneral);

            // Description
            HBox detailsBox = new HBox();

            TextArea description = new TextArea();
            if (composantAfficher.getDescription() == null) {
                description.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet nisi at mi imperdiet elementum. Maecenas et tellus vitae enim dapibus sagittis. Nulla interdum enim vitae eros hendrerit pellentesque. Sed pretium tortor sit amet vestibulum finibus. Donec tempus nisl gravida arcu porttitor, ut laoreet lacus fringilla. Curabitur dui est, varius ut consectetur id, accumsan vel tortor. Praesent laoreet accumsan magna eget tristique.");
            } else {
                description.setText(composantAfficher.getDescription());
            }
            description.setWrapText(true);
            description.getStyleClass().add("description");
            detailsBox.getChildren().add(description);
//                    overlay.getChildren().add(new Text(composant.getDescription()));

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
            if (composantAfficher.getImage() != null) {
                image.setImage(new Image(composantAfficher.getImage()));
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
            Liste finalListeAfficher = listeAfficher;
            Composant finalComposantAfficher = composantAfficher;
            btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    modele.getProjet().supprimerTache(finalComposantAfficher.getNom());
                    modele.notifierObservateur();
                    modele.getStackPane().getChildren().remove(overlayBackground);
                }
            });

            if (finalComposantAfficher.getEstArchive()) archiverText = "Désarchiver";
            else archiverText = "Archiver";
            Button btnArchiver = new Button(archiverText);
            btnArchiver.getStyleClass().add("quitter");

            btnArchiver.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!finalComposantAfficher.getEstArchive()) {
                        modele.getProjet().archiverTache(finalComposantAfficher.getNom());
                    } else {
                        modele.getProjet().desarchiverTache(finalComposantAfficher.getNom());
                    }
                    modele.notifierObservateur();
                    modele.getStackPane().getChildren().remove(overlayBackground);
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
            if (composantAfficher instanceof Tache) {
                Tache tache = (Tache) composantAfficher;
                for (Composant sousTacheComposant : tache.getSousTaches()) {
                    vBoxSousTaches.getChildren().add(sousTacheComposant.afficher(modele));
                }
            }
            overlay.getChildren().add(vBoxSousTaches);

            Button btnAjouterSousTache = new Button("Ajouter une sous-tâche");
            btnAjouterSousTache.getStyleClass().add("btn");
            btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, composantAfficher));
            btnAjouterSousTache.setId(composantAfficher.getNom());
            overlay.getChildren().add(btnAjouterSousTache);

            Composant finalComposantAfficher1 = composantAfficher;
            quitter.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    finalComposantAfficher1.setDescription(description.getText());
                    if (image.getImage() != null) {
                        finalComposantAfficher1.setImage(image.getImage().getUrl());
                    }
                    modele.getStackPane().getChildren().remove(overlayBackground);
                }
            });

            modele.getStackPane().getChildren().add(overlayBackground);
            overlay.setAlignment(Pos.CENTER);
            overlayBackground.setCenter(overlay);
            BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
        }
    }
}
