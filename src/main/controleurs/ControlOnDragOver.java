package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import main.Modele;

/**
 * Classe ControlOnDragOver qui permet de gérer le drag and drop
 */
public class ControlOnDragOver implements EventHandler<DragEvent> {


    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlOnDragOver
     * @param modele Le modele
     */
    public ControlOnDragOver(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui permet de gerer le drag and drop
     * @param dragEvent L'evenement de drag and drop
     */
    @Override
    public void handle(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);
        dragEvent.consume();
    }
}
