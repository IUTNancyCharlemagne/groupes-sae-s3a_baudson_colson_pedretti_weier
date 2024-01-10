package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import main.Modele;
import main.objet.composite.Composant;

/**
 * ControlAfficherTache est la classe qui represente le controleur qui affiche une tache en detail
 */
public class ControlAfficherTache implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Le composant a afficher
     */
    private Composant composantAfficher;

    /**
     * Constructeur de ControlAfficherTache
     *
     * @param modele Le modele
     */
    public ControlAfficherTache(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui affiche une tache en detail
     *
     * @param mouseEvent L'evenement de la souris
     */
    @Override
    public void handle(MouseEvent mouseEvent) {

        // ### Déclaration des variables ###
        Pane pane = (Pane) mouseEvent.getSource();
        String nomTache;
        if(pane.getChildren().get(0) instanceof Label){
            nomTache = ((Label) pane.getChildren().get(0)).getText();
        }
        else nomTache = pane.getChildren().get(0).getAccessibleText();

        composantAfficher = modele.getProjet().getTache(nomTache);

        if (composantAfficher != null) {
            modele.setCurrentTache(composantAfficher);
            modele.notifierObservateur();
        }
    }
}