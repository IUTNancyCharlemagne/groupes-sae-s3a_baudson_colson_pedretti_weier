package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.composite.Composant;

/**
 * Classe ControlChangerTitre qui permet de changer le titre d'un composant
 */
public class ControlChangerTitreListe implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Le composant a modifier
     */
    private Liste liste;

    /**
     * Constructeur de ControlChangerTitre
     * @param modele Le modele
     * @param liste Le composant a modifier
     */
    public ControlChangerTitreListe(Modele modele, Liste liste) {
        this.modele = modele;
        this.liste = liste;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Text titre = (Text) mouseEvent.getSource();
        HBox parent = (HBox) titre.getParent();

        // On cree un TextField avec le texte actuel du titre
        TextField tf = new TextField(titre.getText());
        tf.selectAll();
        // On cree un evenement pour valider le changement de titre
        tf.setOnAction(e2 -> {
            // On remet le titre a jour
            titre.setText(tf.getText());
            // On remplace le TextField par le titre
            parent.getChildren().remove(tf);
            parent.getChildren().add(0,titre);
            // On met a jour le nom du composant et l'affichage
            this.liste.setNom(tf.getText());
            modele.notifierObservateur();
        });
        // On remplace le titre par le TextField
        parent.getChildren().remove(titre);
        parent.getChildren().add( 0, tf);
        tf.requestFocus();
    }

}
