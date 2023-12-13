package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Liste;
import main.Modele;

public class ControlAjouterListe implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControlAjouterListe(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String nbListes = String.valueOf(modele.getListesTaches().size() + 1);
        modele.ajouterListeTaches(new Liste("Liste" + nbListes));
        modele.notifierObservateur();
    }
}
