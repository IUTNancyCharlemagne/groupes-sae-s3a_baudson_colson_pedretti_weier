package test;

import main.Tag;
import main.composite.SousTache;
import main.composite.Tache;
import org.junit.Test;

import java.text.ParseException;

public class TestTache {

    @Test
    public void testCreerTache() {
        Tache tache = new Tache("Tache",null,0);
        assert(tache.getNom().equals("Tache"));
    }

    @Test
    public void testModifierTache() {
        Tache tache = new Tache("Tache",null,0);
        tache.setNom("Tache2");
        assert(tache.getNom().equals("Tache2"));
    }

    @Test
    public void testAjouterTache() {
        Tache tache = new Tache("Tache",null,0);
        Tache tache1 = new Tache("Tache1",null,0);
        Tache tache2 = new Tache("Tache2",null,0);
        tache.ajouter(tache1);
        tache.ajouter(tache2);
        assert(tache.getSousTaches().contains(tache1));
        assert(tache.getSousTaches().contains(tache2));
    }

    @Test
    public void testSupprimerTache() {
        Tache tache = new Tache("Tache",null,0);
        Tache tache1 = new Tache("Tache1",null,0);
        Tache tache2 = new Tache("Tache2",null,0);
        tache.ajouter(tache1);
        tache.ajouter(tache2);
        tache.retirer(tache1);
        assert(!tache.getSousTaches().contains(tache1));
        assert(tache.getSousTaches().contains(tache2));
    }

    @Test
    public void testAjouterSousTache(){
        Tache tache = new Tache("Tache",null,0);
        SousTache sousTache1 = new SousTache("Tache1",null,0);
        SousTache sousTache2 = new SousTache("Tache2",null,0);
        tache.ajouter(sousTache1);
        tache.ajouter(sousTache2);
        assert(tache.getSousTaches().contains(sousTache1));
    }

    @Test
    public void testSupprimerSousTache(){
        Tache tache = new Tache("Tache",null,0);
        SousTache sousTache1 = new SousTache("Tache1",null,0);
        SousTache sousTache2 = new SousTache("Tache2",null,0);
        tache.ajouter(sousTache1);
        tache.ajouter(sousTache2);
        tache.retirer(sousTache1);
        assert(!tache.getSousTaches().contains(sousTache1));
    }

    @Test
    public void testAjouterDescription(){
        Tache tache = new Tache("Tache",null,0);
        tache.setDescription("Description");
        assert(tache.getDescription().equals("Description"));
    }

    @Test
    public void testAjouterDependance() throws ParseException {
        Tache tache = new Tache("Tache",null,0);
        Tache tache1 = new Tache("Tache1",null,0);
        tache.addDependance(tache1);
        assert(tache.getDependances().contains(tache1));
    }

    @Test
    public void testAjouterTag(){
        Tache tache = new Tache("Tache",null,0);
        Tag tag1 = new Tag("Tag1",null);
        Tag tag2 = new Tag("Tag2",null);
        tache.addTag(tag1);
        tache.addTag(tag2);
        assert(tache.getTags().contains(tag1));
        assert(tache.getTags().contains(tag2));
    }

    @Test
    public void testAjouterTacheEnArchive(){
        Tache tache = new Tache("Tache",null,0);
        tache.setEstArchive(true);
        assert(tache.getEstArchive());
    }

    @Test
    public void testAjouterImage(){
        Tache tache = new Tache("Tache",null,0);
        tache.setImage("/lienversimage");
        assert(tache.getImage().equals("/lienversimage"));
    }


}
