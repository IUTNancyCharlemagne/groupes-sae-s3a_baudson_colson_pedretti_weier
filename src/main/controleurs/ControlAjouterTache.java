package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
     * @param modele le modele
     */
    public ControlAjouterTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Méthode qui ajoute une tâche à une liste.
     * @param actionEvent l'action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        // Récupère le nom de la liste
        String nomListe = btn.getId();

        VBox overlay = new VBox();
        overlay.getStyleClass().add("overlay");
        overlay.setAlignment(Pos.TOP_LEFT);

        TextArea nom = new TextArea(
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
            if (nom.getText().isEmpty()) {
                System.out.println("Le nom de la tâche ne peut pas être vide.");
            } else {
                boolean trouve = false;
                for (Liste liste : modele.getProjet().getListeTaches()){
                    for (Composant tache : liste.getComposants()){
                        if (tache.getNom().equals(nom.getText())){
                            trouve = true;
                        }
                    }
                }
                if (trouve){
                    System.out.println("La tâche existe déjà.");
                } else {
                    if (tacheImage.getImage() == null){
                        modele.getProjet().getListeTaches(nomListe).ajouterComposant(new Tache(nom.getText(), null));
                    } else{
                        modele.getProjet().getListeTaches(nomListe).ajouterComposant(new Tache(nom.getText(), tacheImage.getImage().getUrl()));
                    }

                    modele.notifierObservateur();
                }
                modele.getStackPane().getChildren().remove(overlay);
            }
        });

        imageSelection.setAlignment(Pos.CENTER);
        imageSelection.setSpacing(10);

        nom.setWrapText(true);
        nom.getStyleClass().add("description");
        overlay.getChildren().add(nom);
        overlay.getChildren().add(imageSelection);
        overlay.getChildren().add(btnValider);
        overlay.setAlignment(Pos.CENTER);

        modele.getStackPane().getChildren().add(overlay);
        BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
    }
}
