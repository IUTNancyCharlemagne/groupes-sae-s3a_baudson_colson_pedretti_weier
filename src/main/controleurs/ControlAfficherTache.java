package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Liste;
import main.Modele;
import main.Tag;
import main.composite.Composant;
import main.composite.Tache;
import main.observateur.VueTache;

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
        VBox vBox = (VBox) mouseEvent.getSource();
        Text text = (Text) vBox.getChildren().get(0);
        String nomTache = text.getText();

        boolean trouve = false;
        Composant composantAfficher = null;
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
