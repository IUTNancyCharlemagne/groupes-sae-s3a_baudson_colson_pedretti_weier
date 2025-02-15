package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import main.Modele;

/**
 * Classe ControlOnDragDetected qui permet de gérer le drag and drop
 */
public class ControlOnDragDetectedListe implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlOnDragDetected
     * @param modele Le modele
     */
    public ControlOnDragDetectedListe(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui permet de détecter la liste
     * @param mouseEvent L'evenement de la souris
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage(node.snapshot(new SnapshotParameters(), null));
        content.putString(node.getId());
        db.setContent(content);
        mouseEvent.consume();
    }
}
