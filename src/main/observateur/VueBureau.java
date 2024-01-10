package main.observateur;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;


/**
 * Classe VueBureau qui affiche au format bureau (liste en vertical)
 */
public class VueBureau implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele)) return;

        Modele modele = (Modele) s;
        modele.getPaneBureau().getChildren().clear();

        modele.getPaneBureau().getStyleClass().add("paneBureau");

        int i = 0;

        // Affichage des listes
        for (Liste liste : modele.getProjet().getListeTaches()) {
            VBox pane = liste.afficher(modele);
            pane.getStyleClass().add("paneListe");

            Button btnAddTache = new Button("+ Ajouter tâche");
            btnAddTache.getStyleClass().add("btnTransparent");
            btnAddTache.setMaxWidth(Double.MAX_VALUE);
            btnAddTache.setAlignment(Pos.CENTER_LEFT);
            btnAddTache.setOnAction(new ControlAjouterTache(modele));
            btnAddTache.setId(liste.getNom());
            pane.getChildren().add(btnAddTache);
            modele.getPaneBureau().add(pane, i, 0);
            i++;
        }

        Button btnAddListe = new Button("+ Ajouter liste");
        btnAddListe.getStyleClass().add("btnAddListe");
        btnAddListe.setOnAction(new ControlAjouterListe(modele));
        GridPane.setValignment(btnAddListe, VPos.TOP);

        modele.getPaneBureau().setHgap(10);

        modele.getPaneBureau().add(btnAddListe, i, 0);

    }
}
