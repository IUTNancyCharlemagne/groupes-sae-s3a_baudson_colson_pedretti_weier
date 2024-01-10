package test;

import main.Liste;
import main.composite.Tache;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestListe {

    @Test
    public void test_ajouterComposantOK() {
        Liste l = new Liste("test");
        Liste l2 = new Liste("test");

        assertEquals(l, l2);

        l.ajouterComposant(new Tache("t1", "", 0));

        assertNotEquals(l, l2);
    }

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
}
