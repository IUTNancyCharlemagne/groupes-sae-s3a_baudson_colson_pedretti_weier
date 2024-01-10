package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.composite.Composant;
import main.objet.composite.Tache;

import java.text.ParseException;

public class ControlQuitterTache implements EventHandler<ActionEvent> {

    private final Modele modele;

    public ControlQuitterTache(Modele modele){
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (modele.getCurrentTache() instanceof Tache && ((Tache) modele.getCurrentTache()).getParent() != null) {
            Tache tache = (Tache) modele.getCurrentTache().getParent();
            tache.fixDuree();
        }
        modele.setCurrentTache(null);
        for (Composant composant : modele.getProjet().getListeTouteTaches()) {
            try {
                composant.calcDateDebutDependance();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        modele.notifierObservateur();
    }
}
