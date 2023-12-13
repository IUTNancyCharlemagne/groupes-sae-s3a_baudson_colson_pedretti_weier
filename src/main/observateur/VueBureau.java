package main.observateur;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;

public class VueBureau implements Observateur {

    @Override
    public void actualiser(Sujet s) {
        if (s instanceof Modele) {
            Modele modele = (Modele) s;
            modele.paneBureau.getChildren().clear();

            for (Liste liste : modele.getListesTaches()) {
                VBox pane = liste.afficher();

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
}
