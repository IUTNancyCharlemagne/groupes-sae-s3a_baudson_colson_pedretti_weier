package test;

import main.objet.Liste;
import main.objet.composite.Tache;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestListe {

    /**
     * Teste si on peut ajouter un composant à une liste
     */
    @Test
    public void test_ajouterComposantOK() {
        Liste l = new Liste("test");
        Liste l2 = new Liste("test");

        assertEquals(l, l2);

        l.ajouterComposant(new Tache("t1", "", 0));

        assertNotEquals(l, l2);
    }

    /**
     * Teste si on peut retirer un composant à une liste
     */
    @Test
    public void test_retirerComposantOK(){
        Liste l = new Liste("test");
        Tache t = new Tache("t1", "", 0);
        Liste l2 = new Liste("test");

        l.ajouterComposant(t);

        assertNotEquals(l, l2);

        l.retirerComposant(t);

        assertEquals(l, l2);
    }

    /**
     * Teste si on peut changer le nom d'une liste
     */
    @Test
    public void test_changerNomOK(){
        Liste l1 = new Liste("TEST");
        Liste l2 = new Liste("TEST2");

        assertNotEquals(l1, l2);

        l1.setNom("TEST2");

        assertEquals(l1, l2);
    }
}
