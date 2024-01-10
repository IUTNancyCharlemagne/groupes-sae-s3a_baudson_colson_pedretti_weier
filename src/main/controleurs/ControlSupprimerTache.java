package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;

public class ControlSupprimerTache implements EventHandler<ActionEvent> {

    private final Modele modele;

    public ControlSupprimerTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        modele.getProjet().getListeTouteTaches().remove(modele.getCurrentTache());
        modele.setCurrentTache(null);
        modele.notifierObservateur();
        System.out.println(modele.getProjet().getListeTouteTaches());
    }
}