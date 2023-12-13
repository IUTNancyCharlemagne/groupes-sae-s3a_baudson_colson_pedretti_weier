package main;

import main.composite.SousTache;
import main.composite.Tache;
import main.observateur.VueBureau;

public class Main {

    public static void main(String[] args) {

        Modele modele = new Modele();
        VueBureau vueBureau = new VueBureau();
        modele.enregistrerObservateur(vueBureau);

        Liste liste = new Liste("Liste 1");
        Tache tache1 = new Tache("Tache 1");
        Tache tache2 = new Tache("Tache 2");
        Tache tache21 = new Tache("Tache 2.1");
        SousTache tache22 = new SousTache("Tache 2.2");
        SousTache tache211 = new SousTache("Tache 2.1.1");
        tache1.ajouter(tache2);
        tache2.ajouter(tache21);
        tache2.ajouter(tache22);
        tache21.ajouter(tache211);
        liste.ajouterComposant(tache1);
    }
}
