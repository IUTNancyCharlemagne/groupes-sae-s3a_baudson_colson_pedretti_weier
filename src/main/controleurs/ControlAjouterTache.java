package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.Modele;
import main.composite.Tache;

public class ControlAjouterTache implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControlAjouterTache(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        String nomListe = btn.getId();

        if (modele.getListeTaches().isEmpty()) {
            System.out.println("Aucune liste de tâches n'a été créée.");
        } else {
            modele.getListeTaches(nomListe).ajouterComposant(new Tache("Nouvelle tâche"));
            modele.notifierObservateur();
        }
    }
}
