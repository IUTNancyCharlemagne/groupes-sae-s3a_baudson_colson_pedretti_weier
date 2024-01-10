package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.Tag;

public class ControlSupprimerTag implements EventHandler<ActionEvent> {

    private final Modele modele;
    private final Tag tag;

    public ControlSupprimerTag(Modele modele, Tag tag){
        this.modele = modele;
        this.tag = tag;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        modele.getCurrentTache().removeTag(tag);
        modele.notifierObservateur();
    }

}
