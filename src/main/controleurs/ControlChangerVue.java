package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;
import main.Modele;

/**
 * ControlChangerVue est le controleur qui permet de changer la vue courante
 */
public class ControlChangerVue implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlChangerVue
     * @param modele le modele
     */
    public ControlChangerVue(Modele modele) {
        this.modele = modele;
    }

    /**
     * Change la vue courante en fonction de l'item selectionne
     * @param e l'evenement
     */
    @Override
    public void handle(ActionEvent e) {
        RadioMenuItem radioMenuItem = (RadioMenuItem) e.getSource();

        switch (radioMenuItem.getText()) {
            case "Affichage en colonnes":
                modele.setVueCourante(Modele.COLONNE);
                break;
            case "Affichage en lignes" :
                modele.setVueCourante(Modele.LIGNE);
                break;
            case "Afficher le Gantt" :
                modele.setVueCourante(Modele.GANTT);
                break;
            case "Afficher les archives" :
                modele.setVueCourante(Modele.ARCHIVES);
                break;
            default:
        }

        System.out.println(modele.getVueCourante());
        modele.notifierObservateur();

    }

}
