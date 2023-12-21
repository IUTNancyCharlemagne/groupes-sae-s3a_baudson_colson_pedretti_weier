package main.observateur;

import javafx.scene.layout.VBox;
import main.Modele;
import main.Sujet;

public class VueTache extends VBox implements Observateur {

        @Override
        public void actualiser(Sujet s) {
            if (!(s instanceof main.Modele)) return;

            Modele modele = (Modele) s;





        }
}
