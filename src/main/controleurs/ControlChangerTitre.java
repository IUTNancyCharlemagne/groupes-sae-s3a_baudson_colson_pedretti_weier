package main.controleurs;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.Modele;
import main.objet.composite.Composant;

/**
 * Classe ControlChangerTitre qui permet de changer le titre d'un composant
 */
public class ControlChangerTitre implements EventHandler<MouseEvent> {

    /**
     * Le modele
     */
    private Modele modele;

    /**
     * Le composant a modifier
     */
    private Composant composant;

    /**
     * Constructeur de ControlChangerTitre
     * @param modele Le modele
     * @param composant Le composant a modifier
     */
    public ControlChangerTitre(Modele modele, Composant composant) {
        this.modele = modele;
        this.composant = composant;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Text titre = (Text) mouseEvent.getSource();
        GridPane parent = (GridPane) titre.getParent();

        // On cree un TextField avec le texte actuel du titre
        TextField tf = new TextField(titre.getText());
        tf.selectAll();
        // On cree un evenement pour valider le changement de titre
        tf.setOnAction(e2 -> {
            // On remet le titre a jour
            titre.setText(tf.getText());
            // On remplace le TextField par le titre
            parent.getChildren().remove(tf);
            parent.add(titre, 0, 0);
            // On met a jour le nom du composant et l'affichage
            composant.setNom(tf.getText());
            modele.notifierObservateur();
        });
        // On remplace le titre par le TextField
        parent.getChildren().remove(titre);
        parent.add(tf, 0, 0);
        tf.requestFocus();
    }
}
