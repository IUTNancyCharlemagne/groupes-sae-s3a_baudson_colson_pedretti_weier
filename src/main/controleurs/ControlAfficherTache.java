package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

        System.out.println(nomTache);

        for (Liste liste : modele.getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                if (composant.getNom().equals(nomTache)) {
                    System.out.println(composant.getNom());
                    return;
                }
            }
        }
    }
}
