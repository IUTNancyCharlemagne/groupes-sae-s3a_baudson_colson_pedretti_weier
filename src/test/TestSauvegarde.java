package test;

import main.Liste;
import main.Modele;
import main.composite.Tache;
import main.exceptions.ProjectNotFoundException;
import org.junit.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class TestSauvegarde {
    @Test
    public void testSauvegarde() throws ProjectNotFoundException, IOException, ClassNotFoundException {
        Modele m = new Modele();
        m.setNomProjet("ProjetTest");

        Liste liste1 = new Liste("Liste1");
        m.ajouterListeTaches(liste1);

        liste1.ajouterComposant(new Tache("Tache1"));
        liste1.ajouterComposant(new Tache("Tache2"));

        Liste liste2 = new Liste("Liste2");
        m.ajouterListeTaches(liste2);

        liste2.ajouterComposant(new Tache("Tache3"));

        try {
            m.sauvegarderProjet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Modele m2 = m.chargerProjet("./projects/" + m.getNomProjet() + ".trebo");

        assertEquals(m.toString(), m2.toString());
    }
}
