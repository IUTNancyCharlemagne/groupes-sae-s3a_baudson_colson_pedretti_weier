package test;

import main.Liste;
import main.Modele;
import main.Projet;
import main.composite.Tache;
import main.controleurs.ControlCharger;
import main.controleurs.ControlSauvegarde;
import main.exceptions.ProjectNotFoundException;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;

public class TestSauvegarde {
    @Test
    public void testSauvegarde_projetVide() {
        Modele test = new Modele(new Projet());
        Projet projet1 = test.getProjet();
        try {
            new ControlSauvegarde(test, null).sauvegarderProjet("./test.trebbo");
            Projet projet2 = new ControlCharger(test, null).chargerProjet("./test.trebbo");
            assertEquals(projet1, projet2);

            projet2.ajouterListeTaches(new Liste("TEST"));
            assertNotEquals(projet1, projet2);

        } catch (IOException | ProjectNotFoundException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSauvegarde_sauvegardeOK() {
        Projet p = new Projet();
        Liste l1 = new Liste("l1");
        l1.ajouterComposant(new Tache("t1", null, 0));
        l1.ajouterComposant(new Tache("t2", null, 0));
        Liste l2 = new Liste("l2");
        l2.ajouterComposant(new Tache("t3", "/chemin/", 15));
        p.ajouterListeTaches(l1);
        p.ajouterListeTaches(l2);
        Modele test = new Modele(p);

        try {
            new ControlSauvegarde(test, null).sauvegarderProjet("./test2.trebbo");
            Projet p2 = new ControlCharger(test, null).chargerProjet("./test2.trebbo");

            assertEquals(p, p2);

        } catch (ProjectNotFoundException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
