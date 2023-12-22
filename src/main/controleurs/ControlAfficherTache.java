package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.composite.Composant;
import main.composite.Tache;

import java.io.File;

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
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        boolean trouve = false;

        // TODO: Réduire le morceau de code ci-dessous (c'est vraiment pas du tout opti)
        // Recherche de la tache
        for (Liste liste : modele.getProjet().getListeTaches()) {
            for (Composant composant : liste.getComposants()) {
                // Si la tache est trouvee
                if (composant.getNom().equals(nomTache) && !trouve) {
                    composantAfficher = composant;
                    trouve = true;
                } else if (!trouve) {
                    // Recherche dans les sous-taches
                    if (composant instanceof Tache) {
                        Tache tache = (Tache) composant;
                        for (Composant sousTache : tache.getSousTaches()) {
                            if (sousTache.getNom().equals(nomTache)) {
                                composantAfficher = sousTache;
                                trouve = true;
                            }
                        }
                    }
                }
            }
        }

        if (trouve) {
            modele.setCurrentTache(composantAfficher);
            modele.notifierObservateur();
        }
    }
}
