package main.observateur;

import javafx.scene.control.Button;
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

        // Affichage des listes
        for (Liste liste : modele.getProjet().getListeTaches()) {
            VBox pane = liste.afficher(modele);
            pane.getStyleClass().add("paneListe");

            Button btnAddTache = new Button("Ajouter tâche");
            btnAddTache.setOnAction(new ControlAjouterTache(modele));
            btnAddTache.setId(liste.getNom());

            pane.getChildren().add(btnAddTache);

            modele.getPaneBureau().getChildren().add(pane);
        }

        Button btnAddListe = new Button("Ajouter liste");
        btnAddListe.setOnAction(new ControlAjouterListe(modele));

        modele.getPaneBureau().getChildren().add(btnAddListe);

    }
}
