package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.objet.Liste;
import main.Modele;
import main.objet.composite.Tache;

/**
 * Classe ControlOnDragDropped qui permet de gérer le drag and drop
 */
public class ControlOnDragDroppedListe implements EventHandler<DragEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlOnDragDropped
     * @param modele Le modele
     */
    public ControlOnDragDroppedListe(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui permet de gérer le drag and drop
     * @param dragEvent L'evenement de la souris
     */
    @Override
    public void handle(DragEvent dragEvent) {
        // Récupération de la tâche qui a été drag and drop
        Dragboard db = dragEvent.getDragboard();
        Tache tache = (Tache) modele.getProjet().getTache(db.getString());

        // Récupération de la Liste dans laquelle se trouvait la tâche avant le drag and drop
        Liste listeTachesPrecedente = modele.getProjet().getListeTaches(tache.getCurentListe(modele).getNom());

        // Suppression de la tâche de la liste de tâches précédente
        listeTachesPrecedente.retirerComposant(tache);

        // Récupération de la VBox dans laquelle on veut ajouter la tâche
        Node liste = null;
        if (modele.getVueCourante().equals(Modele.LISTE))
            liste = (HBox) dragEvent.getGestureTarget();
        else if (modele.getVueCourante().equals(Modele.COLONNE))
            liste = (VBox) dragEvent.getGestureTarget();

        // Récupération de la liste de tâches dans laquelle on veut ajouter la tâche
        Liste listeTaches = modele.getProjet().getListeTaches(liste.getId());

        // Ajout de la tâche à la liste de tâches
        listeTaches.ajouterComposant(tache);

        // Mise à jour de l'affichage
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
        modele.notifierObservateur();
    }
}
