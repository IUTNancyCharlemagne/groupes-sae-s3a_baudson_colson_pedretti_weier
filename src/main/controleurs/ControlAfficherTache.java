package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

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
     * @param modele Le modele
     */
    public ControlAfficherTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui affiche une tache en detail
     * @param mouseEvent L'evenement de la souris
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        // Recherche de la tache
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                // Si la tache est trouvee
                if (composant.getNom().equals(nomTache)) {
                    // Background de l'overlay
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
                    quitter.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            modele.getStackPane().getChildren().remove(overlayBackground);
                        }
                    });
                    quitter.setAlignment(Pos.TOP_RIGHT);
                    overlay.getChildren().add(quitter);

                    // Titre
                    Text titre = new Text(composant.getNom());
                    titre.getStyleClass().add("titre");
                    overlay.getChildren().add(titre);

                    // Description
                    TextArea description = new TextArea(
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet nisi at mi imperdiet elementum. Maecenas et tellus vitae enim dapibus sagittis. Nulla interdum enim vitae eros hendrerit pellentesque. Sed pretium tortor sit amet vestibulum finibus. Donec tempus nisl gravida arcu porttitor, ut laoreet lacus fringilla. Curabitur dui est, varius ut consectetur id, accumsan vel tortor. Praesent laoreet accumsan magna eget tristique."
                    );
                    description.setWrapText(true);
                    description.getStyleClass().add("description");
                    overlay.getChildren().add(description);
//                    overlay.getChildren().add(new Text(composant.getDescription()));

                    Button btnSupprimer = new Button("Supprimer");
                    btnSupprimer.getStyleClass().add("quitter");
                    btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            liste.retirerComposant(composant);
                            modele.notifierObservateur();
                            modele.getStackPane().getChildren().remove(overlayBackground);
                        }
                    });
                    btnSupprimer.setAlignment(Pos.TOP_RIGHT);
                    overlay.getChildren().add(btnSupprimer);

                    Text sousTache = new Text("Sous-tâches");
                    sousTache.getStyleClass().add("titre");
                    overlay.getChildren().add(sousTache);

                    VBox vBoxSousTaches = new VBox();
                    vBoxSousTaches.getStyleClass().add("sousTaches");
                    if (composant instanceof Tache) {
                        Tache tache = (Tache) composant;
                        for (Composant sousTacheComposant : tache.getSousTaches()) {
                            vBoxSousTaches.getChildren().add(sousTacheComposant.afficher(modele));
                        }
                    }
                    overlay.getChildren().add(vBoxSousTaches);

                    Button btnAjouterSousTache = new Button("Ajouter une sous-tâche");
                    btnAjouterSousTache.getStyleClass().add("btn");
                    btnAjouterSousTache.setOnAction(new ControlAjouterSousTache(modele, composant));
                    btnAjouterSousTache.setId(composant.getNom());
                    overlay.getChildren().add(btnAjouterSousTache);

                    modele.getStackPane().getChildren().add(overlayBackground);
                    overlayBackground.setCenter(overlay);
                    BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
                }
            }
        }
    }
}
