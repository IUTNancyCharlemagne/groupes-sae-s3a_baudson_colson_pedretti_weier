package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import main.Modele;
import main.objet.composite.Composant;
import main.objet.composite.Tache;

/**
 * Classe ControlOnDragDroppedCell qui permet de gérer le drag and drop d'une cellule
 */
public class ControlOnDragDroppedCell implements EventHandler<DragEvent> {

    /**
     * La cellule
     */
    private TreeCell<Composant> cell;

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlOnDragDroppedCell
     * @param modele Le modele
     * @param cell La cellule
     */
    public ControlOnDragDroppedCell(Modele modele, TreeCell<Composant> cell) {
        this.modele = modele;
        this.cell = cell;
    }

    /**
     * Methode qui permet de gérer le drag and drop d'une cellule
     * @param dragEvent L'evenement de la souris
     */
    @Override
    public void handle(DragEvent dragEvent) {
        // Réupération de cellule qui a été drag and drop
        Dragboard db = dragEvent.getDragboard();

        Tache tache = (Tache) modele.getProjet().getTache(db.getString());

        // Si la tache est une sous-tache
        if (tache.estUneSousTache(modele)) {
            // Suppression de la tâche de la tache parent
            Tache tacheParent = (Tache) tache.getParent();
            tacheParent.retirer(tache);

            // Tache dans laquelle on veut ajouter la tâche
            TreeItem<Composant> item = cell.getTreeItem();
            Tache tacheCible = (Tache) item.getValue();
            // Ajout de la tâche dans la tâche cible
            tacheCible.ajouter(tache);
            tacheCible.fixDuree();
            modele.notifierObservateur();

            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        } else {
            dragEvent.setDropCompleted(false);
            dragEvent.consume();
        }
    }
}
