package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

public class ControlAfficherTache implements EventHandler<MouseEvent> {

    private Modele modele;

    public ControlAfficherTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        for (Liste liste : modele.getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (composant.getNom().equals(nomTache)) {
                    // Overlay
                    BorderPane overlayBackground = new BorderPane();
                    overlayBackground.getStyleClass().add("overlayBackground");

                    overlayBackground.setMinSize(modele.stackPane.getWidth(), modele.stackPane.getHeight());

                    VBox overlay = new VBox();
                    overlay.getStyleClass().add("overlay");
                    overlay.setAlignment(Pos.TOP_LEFT);

                    // Quitter
                    Button quitter = new Button("X");
                    quitter.getStyleClass().add("quitter");
                    quitter.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            modele.stackPane.getChildren().remove(overlayBackground);
                        }
                    });
                    quitter.setAlignment(Pos.TOP_RIGHT);
                    overlay.getChildren().add(quitter);

                    Text titre = new Text(composant.getNom());
                    titre.getStyleClass().add("titre");
                    overlay.getChildren().add(titre);

                    TextArea description = new TextArea(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet nisi at mi imperdiet elementum. Maecenas et tellus vitae enim dapibus sagittis. Nulla interdum enim vitae eros hendrerit pellentesque. Sed pretium tortor sit amet vestibulum finibus. Donec tempus nisl gravida arcu porttitor, ut laoreet lacus fringilla. Curabitur dui est, varius ut consectetur id, accumsan vel tortor. Praesent laoreet accumsan magna eget tristique."
                    );
                    description.setWrapText(true);
                    description.getStyleClass().add("description");
                    overlay.getChildren().add(description);
//                    overlay.getChildren().add(new Text(composant.getDescription()));

                    modele.stackPane.getChildren().add(overlayBackground);
                    overlayBackground.setCenter(overlay);
                    BorderPane.setMargin(overlay, new Insets(50, 50, 50, 50));
                }
            }
        }
    }
}
