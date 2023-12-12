package main.observateur;

import main.Sujet;

public class VueArchive implements Observateur {

    public VueArchive() {
    }

    @Override
    public void actualiser(Sujet s) {
        System.out.println("Vue Archive actualisée");
    }
}
