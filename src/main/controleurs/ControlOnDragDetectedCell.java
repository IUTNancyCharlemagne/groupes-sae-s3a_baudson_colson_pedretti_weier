package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import main.objet.composite.Composant;

/**
 * Classe ControlOnDragDetectedCell qui permet de gérer le drag and drop d'une cellule
 */
public class ControlOnDragDetectedCell implements EventHandler<MouseEvent> {

    /**
     * La cellule
     */
    private TreeCell<Composant> cell;

    /**
     * Constructeur de ControlOnDragDetectedCell
     * @param cell La cellule
     */
    public ControlOnDragDetectedCell(TreeCell<Composant> cell) {
        this.cell = cell;
    }

    /**
     * Methode qui permet de gérer le drag and drop d'une cellule
     * @param mouseEvent L'evenement de la souris
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        TreeItem<Composant> item = cell.getTreeItem();
        // item = TreeItem qui a été drag and drop avec sous-tâches

        if (item != null) {
            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(item.getValue().getNom());
            db.setContent(content);
            mouseEvent.consume();
        }
    }
}
