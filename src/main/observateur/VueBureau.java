package main.observateur;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;

import java.util.Objects;

public class VueBureau implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (!(s instanceof Modele)) return;

        Modele modele = (Modele) s;
        modele.paneBureau.getChildren().clear();

        modele.paneBureau.getStyleClass().add("paneBureau");

        for (Liste liste : modele.getListeTaches()) {
            VBox pane = liste.afficher(modele);
            pane.getStyleClass().add("paneListe");

            Button btnAddTache = new Button("Ajouter tâche");
            btnAddTache.setOnAction(new ControlAjouterTache(modele));
            btnAddTache.setId(liste.getNom());

            pane.getChildren().add(btnAddTache);

            modele.paneBureau.getChildren().add(pane);
        }

        Button btnAddListe = new Button("Ajouter liste");
        btnAddListe.setOnAction(new ControlAjouterListe(modele));

        modele.paneBureau.getChildren().add(btnAddListe);

    }
}
