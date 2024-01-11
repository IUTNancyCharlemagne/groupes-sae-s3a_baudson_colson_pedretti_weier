package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.Tag;

public class ControlSupprimerTag implements EventHandler<ActionEvent> {
    /**
     * Le modele
     */
    private final Modele modele;

    /**
     * Le tag
     */
    private final Tag tag;

    /**
     * Constructeur de ControlSupprimerTag
     * @param modele le modele
     * @param tag le tag
     */
    public ControlSupprimerTag(Modele modele, Tag tag){
        this.modele = modele;
        this.tag = tag;
    }

    /**
     * Methode qui permet de supprimer un tag
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        modele.getCurrentTache().removeTag(tag);
        modele.notifierObservateur();
    }

}
