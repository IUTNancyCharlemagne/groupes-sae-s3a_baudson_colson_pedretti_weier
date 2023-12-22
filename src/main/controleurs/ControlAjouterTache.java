package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

        // Gestion de la durée
        HBox ganttBox = new HBox();
        VBox dateDebVBox = new VBox();
        VBox dureeVBox = new VBox();
        VBox dateFinVBox = new VBox();
        ganttBox.setSpacing(10);
        ganttBox.setAlignment(Pos.CENTER);
        dateDebVBox.setSpacing(10);
        dureeVBox.setSpacing(10);
        dateFinVBox.setSpacing(10);

        // Date de début
        Text dateDebText = new Text("Date de début");
        DatePicker dateDebPicker = new DatePicker();
        dateDebPicker.setValue(LocalDate.now());
        dateDebVBox.getChildren().addAll(dateDebText, dateDebPicker);

        // Durée
        Text dureeText = new Text("Durée (jours)");
        TextField duree = new TextField();
        duree.setText("0");
        dureeVBox.getChildren().addAll(dureeText, duree);

        // Date de fin
        Text dateFinText = new Text("Date de fin");
        DatePicker dateFinPicker = new DatePicker();
        dateFinPicker.setValue(LocalDate.now());
        dateFinVBox.getChildren().addAll(dateFinText, dateFinPicker);

        // Actions sur les dates

        // Quand on change la valeur de la date de début
        dateDebPicker.setOnAction(e -> {
            // Si la date de début est supérieure à la date de fin
            if (dateDebPicker.getValue().isAfter(dateFinPicker.getValue())) {
                // On met la date de fin à la date de début
                dateFinPicker.setValue(dateDebPicker.getValue());
            }

            // On met à jour la date de fin en fonction de la durée
            dateFinPicker.setValue(dateDebPicker.getValue().plusDays(Integer.parseInt(duree.getText())));
        });

        // Quand on modifie la durée
        duree.setOnAction(e -> {
            // Si la durée est négative
            if (Integer.parseInt(duree.getText()) < 0) {
                // On met la durée à 0
                duree.setText("0");
            }
            // On ajoute la durée à la date de début
            dateFinPicker.setValue(dateDebPicker.getValue().plusDays(Integer.parseInt(duree.getText())));
            duree.getParent().requestFocus();
        });

        // Quand on change la valeur de la date de fin
        dateFinPicker.setOnAction(e -> {
            // Si la date de fin est inférieure à la date de début
            if (dateFinPicker.getValue().isBefore(dateDebPicker.getValue())) {
                // On met la date de début à la date de fin
                dateDebPicker.setValue(dateFinPicker.getValue());
            }
            // On met la duree au nombre de jours entre la date de début et la date de fin (en comptant l'année et le mois)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = sdf.parse(dateDebPicker.getValue().toString());
                date2 = sdf.parse(dateFinPicker.getValue().toString());
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            long diff = date2.getTime() - date1.getTime();
            duree.setText(String.valueOf((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
        });



        // Ajout des éléments à la fenêtre
        ganttBox.getChildren().addAll(dateDebVBox, dureeVBox, dateFinVBox);

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
                    if (tacheImage.getImage() != null) {
                        imgUrl = tacheImage.getImage().getUrl();
                    }

                    Tache tache = new Tache(nom.getText(), imgUrl, Integer.parseInt(duree.getText()));

                    tache.setDateDebut(dateDebPicker.getValue());

                    modele.getProjet().getListeTaches(nomListe).ajouterComposant(tache);

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
        overlay.getChildren().add(ganttBox);
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
        stage.getIcons().add(new Image("file:icons/logo.png"));
        stage.setTitle("Ajouter une tâche");
        stage.setScene(scene);
        stage.show();
    }
}
