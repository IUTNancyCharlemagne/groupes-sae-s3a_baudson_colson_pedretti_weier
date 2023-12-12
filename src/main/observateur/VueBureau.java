package main.observateur;

import main.Liste;
import main.Modele;
import main.Sujet;

public class VueBureau implements Observateur {

    public VueBureau() {
    }

    @Override
    public void actualiser(Sujet s) {
        if (s instanceof Modele) {
            Modele modele = (Modele) s;
            System.out.println("Listes de tâches :");
            for (Liste liste : modele.getListesTaches()) {
                System.out.println(liste.getNom());
            }

        }
    }
}
