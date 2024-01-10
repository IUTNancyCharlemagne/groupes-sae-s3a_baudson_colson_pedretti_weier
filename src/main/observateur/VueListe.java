package main.observateur;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;

/**
 * Classe VueBureau qui affiche au format bureau (liste en vertical)
 */
public class VueListe implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele)) return;

        Modele modele = (Modele) s;
        modele.getPaneBureau().getChildren().clear();

        modele.getPaneBureau().getStyleClass().add("paneBureau");

        int i = 0;

        // Affichage des listes
        for (Liste liste : modele.getProjet().getListeTaches()) {
            HBox pane = liste.afficherListe(modele);
            pane.getStyleClass().add("paneListe2");

            Button btnAddTache = new Button("+ Ajouter tâche");
            btnAddTache.getStyleClass().add("btnTransparent");
            btnAddTache.setMaxWidth(Double.MAX_VALUE);
            btnAddTache.setAlignment(Pos.CENTER_LEFT);
            btnAddTache.setOnAction(new ControlAjouterTache(modele));
            btnAddTache.setId(liste.getNom());
            pane.getChildren().add(btnAddTache);
            modele.getPaneBureau().add(pane, 0, i);
            i++;
        }

        Button btnAddListe = new Button("+ Ajouter liste");
        btnAddListe.setOnAction(new ControlAjouterListe(modele));

        modele.getPaneBureau().setVgap(10);

        modele.getPaneBureau().add(btnAddListe, 0, i);

    }
}
