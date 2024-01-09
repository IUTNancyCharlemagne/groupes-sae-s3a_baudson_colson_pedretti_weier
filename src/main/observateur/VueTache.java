package main.observateur;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.text.ParseException;

public class VueTache implements Observateur {

    Modele modele;

    public VueTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void actualiser(Sujet s) {

        if (!(s instanceof Modele modele)) return;

        // ### Déclaration des variables ###

        StackPane overlayBackground = new StackPane();
        GridPane overlay = new GridPane();

        // ### Box ###
        HBox tagsGeneral = new HBox();
        VBox imageBox = new VBox();

        // ### Boutons ###
        Button quitter = new Button("X");
        Button btnAjouterTag = new Button("+");
        Button btnImage = new Button();
        Button btnSupprimer = new Button();
        Button btnArchiver;
        Button btnAjouterSousTache = new Button("Ajouter une sous-tâche");

        // ### Texte ###
        Text titre = new Text();
        TextArea description = new TextArea();

        // ### Image ###
        ImageView image = new ImageView();
        ImageView imgSupp = new ImageView(new Image("file:icons/trashWhite.png"));

        String archiverText = "";

        // ### Reset ancienne vue ###
        if (modele.getStackPane().getChildren().size() > 1) {
            modele.getStackPane().getChildren().remove(1);
        }

        if (modele.getCurrentTache() == null) return;

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
                modele.setCurrentTache(null);
                for(Composant composant : modele.getProjet().getListeTouteTaches()){
                    try{
                        composant.CalcDateDebutDependance();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
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

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                modele.getCurrentTache().setDescription(description.getText());
                modele.notifierObservateur();
            }
        });

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
            if (selectedFile != null) {
                modele.getCurrentTache().setImage(selectedFile.toURI().toString());
                modele.notifierObservateur();
            }
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
                modele.getProjet().getListeTouteTaches().remove(modele.getCurrentTache());
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
        // TODO : Faire un ControlArchiverTache qui archive ou désarchive la tache
        btnArchiver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!modele.getCurrentTache().getEstArchive()) {
                    modele.getProjet().archiverTache(modele.getCurrentTache().getNom());
                } else {
                    modele.getProjet().desarchiverTache(modele.getCurrentTache().getNom());
                }
                modele.setCurrentTache(null);
                modele.notifierObservateur();
            }
        });

        // Dates
        HBox ganttBox = new HBox();
        VBox dateDebVBox = new VBox();
        VBox dureeVBox = new VBox();
        VBox dateFinVBox = new VBox();
        ganttBox.setSpacing(10);
        ganttBox.setAlignment(Pos.CENTER);
        dateDebVBox.setSpacing(10);
        dureeVBox.setSpacing(10);
        dateFinVBox.setSpacing(10);

        // Création des Text pour la date
        Text dateDebutText = new Text("Date de début");
        Text dureeText = new Text("Durée (Jours)");
        Text dateFinText = new Text("Date de fin");

        // Création des inputs de valeurs
        DatePicker dateDebutPicker = new DatePicker();
        TextField dureeTextField = new TextField();
        DatePicker dateFinPicker = new DatePicker();

        // On met les valeurs de la tache dans les inputs
        if (modele.getCurrentTache() instanceof Tache) {
            Tache tache = (Tache) modele.getCurrentTache();
            try{
                tache.CalcDateDebutDependance();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            dateDebutPicker.setValue(tache.getDateDebut());
            if (!modele.getCurrentTache().getDependances().isEmpty()) {
                dateDebutPicker.setDisable(true);
                dateDebutPicker.setTooltip(new Tooltip("La date de début est définie par les dépendances"));
            } else {
                dateDebutPicker.setDisable(false);
            }
            dureeTextField.setText(String.valueOf(tache.getDuree()));
            dateFinPicker.setValue(tache.getDateFin());

            // Quand la valeur de durée est changée, on met à jour la date de fin
            dureeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    dureeTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (!dureeTextField.getText().isEmpty()) {
                    tache.setDuree(Integer.parseInt(dureeTextField.getText()));
                    try {
                        tache.setDateFin(tache.calculerDateFin());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    dateFinPicker.setValue(tache.getDateFin());
                }

            });

            // Quand la valeur de la date de début est changée, on met à jour la date de fin
            dateDebutPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                tache.setDateDebut(newValue);
                try {
                    tache.setDateFin(tache.calculerDateFin());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                dateFinPicker.setValue(tache.getDateFin());
            });

            // Quand la valeur de la date de fin est changée, on met à jour la durée
            dateFinPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                tache.setDateFin(newValue);
                try {
                    tache.setDuree(tache.calculerDureeTache());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                dureeTextField.setText(String.valueOf(tache.getDuree()));
            });
        }

        // On ajoute les éléments aux conteneurs
        dateDebVBox.getChildren().addAll(dateDebutText, dateDebutPicker);
        dureeVBox.getChildren().addAll(dureeText, dureeTextField);
        dateFinVBox.getChildren().addAll(dateFinText, dateFinPicker);
        ganttBox.getChildren().addAll(dateDebVBox, dureeVBox, dateFinVBox);
        if (dateDebutPicker.getValue() != null) {
            overlay.addRow(4, ganttBox);
        }

        // ### Bouton ajouter sous-tache ###
        btnAjouterSousTache.getStyleClass().add("btn");
        btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, modele.getCurrentTache()));
        btnAjouterSousTache.setId(modele.getCurrentTache().getNom());

        // Dependence
        ComboBox comboBox = new ComboBox();
        HBox hBoxDependance = new HBox();
        Text DependenceText = new Text("Dépendances");
        // ### ComboBox ###
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (!composant.getNom().equals(modele.getCurrentTache().getNom())) {
                    comboBox.getItems().add(composant.getNom());
                }
            }
        }
        comboBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                Button btnDependance = null;
                if (comboBox.getValue() != null) {

                    for (Composant composant : modele.getProjet().getListeTouteTaches()) {
                        if (composant.getNom().equals(comboBox.getValue())) {
                            if (modele.getCurrentTache().getDependances().contains(composant)) {
                                btnDependance = new Button("Supprimer");
                            } else {
                                btnDependance = new Button("Ajouter");
                            }
                        }
                    }

                    btnDependance.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            if (comboBox.getValue() != null) {
                                for (Composant composant : modele.getProjet().getListeTouteTaches()) {
                                    if (composant.getNom().equals(comboBox.getValue())) {
                                        if (modele.getCurrentTache().getDependances().contains(composant)) {
                                            try {
                                                modele.getCurrentTache().removeDependance(composant);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }

                                        } else {
                                            try {
                                                modele.getCurrentTache().addDependance(composant);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                        modele.notifierObservateur();
                                    }
                                }
                            }
                        }
                    });
                    hBoxDependance.getChildren().add(btnDependance);
                }
            }
        });

        hBoxDependance.setSpacing(10);
        hBoxDependance.getChildren().add(comboBox);

        // ### Ajout overlay background ###
        modele.getStackPane().getChildren().add(overlayBackground);

        // ### Ajout des elements au gridpane ###
        overlay.addRow(0, titre, new Label(), quitter);
        overlay.addRow(1, tagsGeneral);
        overlay.addRow(2, new Text("Description"));
        overlay.addRow(3, description, imageBox);
        if (modele.getCurrentTache().getParent() == null) {
            overlay.addRow(7, DependenceText);
            overlay.addRow(8, hBoxDependance);
        }
        overlay.addRow(9, btnAjouterSousTache, btnArchiver, btnSupprimer);

        overlayBackground.getChildren().add(overlay);
    }
}
