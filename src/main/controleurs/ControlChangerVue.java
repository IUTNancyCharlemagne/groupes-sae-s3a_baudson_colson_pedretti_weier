package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;
import main.Modele;

public class ControlChangerVue implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControlChangerVue(Modele modele) {
        this.modele = modele;
    }

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

    }

}
