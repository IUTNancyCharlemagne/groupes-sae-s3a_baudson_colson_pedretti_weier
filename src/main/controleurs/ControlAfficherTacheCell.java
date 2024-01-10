package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import main.Modele;
import main.objet.composite.Composant;

/**
 * ControlAfficherTacheCell est la classe qui represente le controleur qui affiche une tache en detail (cellule)
 */
public class ControlAfficherTacheCell implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * La cellule
     */
    private TreeCell<Composant> cell;

    public ControlAfficherTacheCell(Modele modele, TreeCell<Composant> cell) {
        this.modele = modele;
        this.cell = cell;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        // Click gauche
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // Double click
            if (mouseEvent.getClickCount() != 2) return;
            Composant tache = cell.getTreeItem().getValue();
            String nomTache = tache.getNom();
            Composant composantAfficher = null;
            boolean trouve = false;

            // Recherche de la tache
            for (Composant composant : modele.getProjet().getListeTouteTaches()) {
                if (composant.getNom().equals(nomTache)) {
                    composantAfficher = composant;
                    trouve = true;
                    break;
                }
            }

            if (trouve) {
                modele.setCurrentTache(composantAfficher);
                modele.notifierObservateur();
            }
        }
    }
}
