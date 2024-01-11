package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Modele;
import main.objet.composite.Composant;
import main.objet.composite.Tache;

import java.text.ParseException;

public class ControlQuitterTache implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private final Modele modele;

    /**
     * Constructeur de ControlQuitterTache
     * @param modele le modele
     */
    public ControlQuitterTache(Modele modele){
        this.modele = modele;
    }

    /**
     * Methode qui permet de quitter une tache
     * @param actionEvent l'evenement
     */
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
