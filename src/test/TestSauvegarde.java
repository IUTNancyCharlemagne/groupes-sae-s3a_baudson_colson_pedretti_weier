package test;

import main.Liste;
import main.Modele;
import main.Projet;
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
        Modele m = new Modele(new Projet("ProjetTest"));
        m.getProjet().setNomProjet("ProjetTest");

        Liste liste1 = new Liste("Liste1");
        m.getProjet().ajouterListeTaches(liste1);

        liste1.ajouterComposant(new Tache("Tache1",null, null));
        liste1.ajouterComposant(new Tache("Tache2",null, null));

        Liste liste2 = new Liste("Liste2");
        m.getProjet().ajouterListeTaches(liste2);

        liste2.ajouterComposant(new Tache("Tache3",null, null));

        try {
            m.getProjet().sauvegarderProjet("./projects/ProjectTest.trebo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Modele m2 = new Modele(m.getProjet().chargerProjet("./projects/" + m.getProjet().getNomProjet() + ".trebo"));

        assertEquals(m.getProjet().toString(), m2.getProjet().toString());
    }
}
