package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.composite.Composant;

import java.text.ParseException;

public class ControlArchiverTache implements EventHandler<ActionEvent> {

    /**
     * modele
     */
    private final Modele modele;

    /**
     * Constructeur de ControlArchiverTache
     * @param modele modele
     */
    public ControlArchiverTache(Modele modele){
        this.modele = modele;
    }

    /**
     * Methode qui permet d'archiver une tache
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if (!modele.getCurrentTache().getEstArchive()) {
            modele.getProjet().archiverTache(modele.getCurrentTache().getNom());
            for (Composant c : modele.getProjet().getListeTouteTaches()) {
                if (!c.getEstArchive() && c.getDependances().contains(modele.getCurrentTache())) {
                    try {
                        c.removeDependance(modele.getCurrentTache());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            modele.getProjet().desarchiverTache(modele.getCurrentTache().getNom());
        }
        modele.setCurrentTache(null);
        modele.notifierObservateur();
    }
}
