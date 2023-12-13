package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import main.Modele;

public class ControlChangerVu implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControlChangerVu() {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent e) {
        ComboBox<String> c = (ComboBox<String>)e.getSource();
        System.out.println("c.getValue()");

        switch (c.getValue()) {
            case "Bureau":
                modele.setVueCourante(Modele.BUREAU);
                break;
            case "Liste" :
                modele.setVueCourante(Modele.LISTE);
                break;
            case "Archive" :
                modele.setVueCourante(Modele.ARCHIVES);
                break;
        }

    }

}
