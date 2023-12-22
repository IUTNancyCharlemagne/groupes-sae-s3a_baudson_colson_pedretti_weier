package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
     * Le composant a afficher
     */
    private Composant composantAfficher;

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

        // ### Déclaration des variables ###
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        StackPane   overlayBackground = new StackPane();
        GridPane    overlay =           new GridPane();

        // ### Box ###
        HBox tagsGeneral =      new HBox();
        VBox imageBox =         new VBox();
        VBox vBoxSousTaches =   new VBox();
        ComboBox comboBox =     new ComboBox();

        // ### Boutons ###
        Button quitter =                new Button("X");
        Button btnAjouterTag =          new Button("+");
        Button btnImage =               new Button();
        Button btnSupprimer =           new Button();
        Button btnArchiver;
        Button btnAjouterSousTache =    new Button("Ajouter une sous-tâche");

        // ### Texte ###
        Text        titre =         new Text();
        Text        sousTacheText = new Text("Sous-tâches");
        TextArea    description =   new TextArea();

        // ### Image ###
        ImageView image =   new ImageView();
        ImageView imgSupp = new ImageView(new Image("file:icons/trash.png"));

        String archiverText = "";
        boolean trouve = false;

        // TODO: Réduire le morceau de code ci-dessous (c'est vraiment pas du tout opti)
        // Recherche de la tache
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                // Si la tache est trouvee
                if (composant.getNom().equals(nomTache) && !trouve) {
                    composantAfficher = composant;
                    trouve = true;
                } else if (!trouve) {
                    // Recherche dans les sous-taches
                    if (composant instanceof Tache) {
                        Tache tache = (Tache) composant;
                        for (Composant sousTache : tache.getSousTaches()) {
                            if (sousTache.getNom().equals(nomTache)) {
                                composantAfficher = sousTache;
                                trouve = true;
                            }
                        }
                    }
                }
            }
        }

        // Si la tache n'est pas trouvee (ne devrait pas arriver)
        if (!trouve) return;

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
        titre.setText(composantAfficher.getNom());
        titre.getStyleClass().add("titre");
        titre.setOnMouseClicked(new ControlChangerTitre(modele, composantAfficher));

        // ### Quitter ###
        quitter.getStyleClass().add("quitter");
        // TODO: Faire un ControlQuitterTache qui enregistre les modifications
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                composantAfficher.setDescription(description.getText());
                if (image.getImage() != null) {
                    composantAfficher.setImage(image.getImage().getUrl());
                }
                modele.getStackPane().getChildren().remove(overlayBackground);
            }
        });

        // ### Tags ###
        for (Tag tag : composantAfficher.getTags()) {
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
                    composantAfficher.removeTag(tag);
                    modele.getStackPane().getChildren().remove(overlayBackground);
                }
            });
            tagsGeneral.getChildren().add(tagBox);
        }

        // ### Bouton ajouter tag ###
        btnAjouterTag.getStyleClass().add("btn");
        btnAjouterTag.setOnAction(new ControlAjouterTag(modele, composantAfficher));
        btnAjouterTag.setId(composantAfficher.getNom());

        // ### Conteneur des tags ###
        tagsGeneral.getChildren().add(btnAjouterTag);
        tagsGeneral.setSpacing(5);

        // ### Description ###
        description.setText(composantAfficher.getDescription());
        description.setWrapText(true);
        description.getStyleClass().add("description");

        // ### Image ###
        imageBox.setSpacing(10);
        imageBox.setAlignment(Pos.CENTER);
        GridPane.setHalignment(imageBox, HPos.CENTER);
        imageBox.getChildren().add(btnImage);
        // Si la tache a une image
        if (composantAfficher.getImage() != null) {
            image.setImage(new Image(composantAfficher.getImage()));
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
            imageBox.getChildren().clear();
            imageBox.getChildren().addAll(image, btnImage);
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
                modele.getProjet().supprimerTache(composantAfficher.getNom());
                modele.notifierObservateur();
                modele.getStackPane().getChildren().remove(overlayBackground);
            }
        });

        // ### Bouton archiver tache ###
        // Si la tache est archivee, le bouton affiche "Desarchiver" sinon il affiche "Archiver"
        archiverText = composantAfficher.getEstArchive() ? "Désarchiver" : "Archiver";
        btnArchiver = new Button(archiverText);
        GridPane.setHalignment(btnArchiver, HPos.RIGHT);
        btnArchiver.getStyleClass().add("quitter");
        // TODO: Faire un ControlArchiverTache qui archive ou désarchive la tache
        btnArchiver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!composantAfficher.getEstArchive()) {
                    modele.getProjet().archiverTache(composantAfficher.getNom());
                } else {
                    modele.getProjet().desarchiverTache(composantAfficher.getNom());
                }
                modele.notifierObservateur();
                modele.getStackPane().getChildren().remove(overlayBackground);
            }
        });

        // ### Texte sous-taches ###
        sousTacheText.getStyleClass().add("titre");

        // ### Sous-taches ###
        vBoxSousTaches.getStyleClass().add("sousTaches");
        if (composantAfficher instanceof Tache) {
            Tache tache = (Tache) composantAfficher;
            for (Composant sousTacheComposant : tache.getSousTaches()) {
                vBoxSousTaches.getChildren().add(sousTacheComposant.afficher(modele));
            }
        }

        // ### Bouton ajouter sous-tache ###
        btnAjouterSousTache.getStyleClass().add("btn");
        btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, composantAfficher));
        btnAjouterSousTache.setId(composantAfficher.getNom());

        // ### ComboBox ###
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (!composant.getNom().equals(composantAfficher.getNom())) {
                    comboBox.getItems().add(composant.getNom());
                }
            }
        }

        // ### Ajout overlay background ###
        modele.getStackPane().getChildren().add(overlayBackground);

        // ### Ajout des elements au gridpane ###
//        overlay.setGridLinesVisible(true);
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
