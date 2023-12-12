package main.observateur;

import main.Sujet;

public class VueGantt implements Observateur {

    public VueGantt() {
    }

    @Override
    public void actualiser(Sujet s) {
        System.out.println("Vue Gantt actualisée");
    }
}
