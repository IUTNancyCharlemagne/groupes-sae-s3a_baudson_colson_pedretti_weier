package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Liste;
import main.Modele;

/**
 * ControlAjouterListe est la classe qui represente le controleur qui ajoute une liste au projet
 */
public class ControlAjouterListe implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Constructeur de ControlAjouterListe
     * @param modele le modele
     */
    public ControlAjouterListe(Modele modele) {
        this.modele = modele;
    }

    /**
     * Methode qui ajoute une liste au projet
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        String nbListes = String.valueOf(modele.getProjet().getListeTaches().size() + 1);
        modele.getProjet().ajouterListeTaches(new Liste("Liste" + nbListes));
        modele.notifierObservateur();
    }
}
