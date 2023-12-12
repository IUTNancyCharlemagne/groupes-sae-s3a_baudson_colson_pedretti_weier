package main.observateur;

import main.Sujet;

public class VueListe implements Observateur {

    public VueListe() {
    }

    @Override
    public void actualiser(Sujet s) {
        System.out.println("Vue Liste actualisée");
    }
}
