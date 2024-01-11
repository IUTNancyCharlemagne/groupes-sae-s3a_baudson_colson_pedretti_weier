package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.composite.Composant;

import java.text.ParseException;

public class ControlSupprimerTache implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private final Modele modele;

    /**
     * Constructeur de ControlQuitterTache
     * @param modele le modele
     */
    public ControlSupprimerTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui permet de quitter une tache
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        for (Composant c : modele.getProjet().getListeTouteTaches()) {
            if (!c.getEstArchive() && c.getDependances().contains(modele.getCurrentTache())) {
                try {
                    c.removeDependance(modele.getCurrentTache());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        modele.getProjet().supprimerTache(modele.getCurrentTache().getNom());
        modele.getProjet().getListeTouteTaches().remove(modele.getCurrentTache());
        modele.setCurrentTache(null);
        modele.notifierObservateur();
    }
}