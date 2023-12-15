package main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.composite.Composant;
import main.exceptions.ProjectNotFoundException;
import main.observateur.Observateur;

import java.io.*;
import java.util.ArrayList;

public class Modele implements Sujet{

    private ArrayList<Observateur> observateurs;
    private String vueCourante;
    private String nomProjet;
    public static final String BUREAU = "Bureau";
    public static final String LISTE = "Liste";
    public static final String COLONNE = "Colonne";
    public static final String LIGNE = "Ligne";
    public static final String GANTT = "Gantt";
    public static final String ARCHIVES = "Archives";

    /**
     * Liste des listes de tâches
     */
    private final ArrayList<Liste> listeTaches;
    public HBox paneBureau = new HBox();

    public StackPane stackPane = new StackPane();

    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.listeTaches = new ArrayList<Liste>();
        this.vueCourante = Modele.COLONNE;
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateur() {
        for (Observateur o : this.observateurs) {
            o.actualiser(this);
        }
    }

    public String getVueCourante() {
        return this.vueCourante;
    }

    public void setVueCourante(String vueCourante) {
        this.vueCourante = vueCourante;
    }

    public void ajouterListeTaches(Liste liste) {
        this.listeTaches.add(liste);
    }

    public void supprimerListeTaches(Liste liste) {
        this.listeTaches.remove(liste);
    }

    public ArrayList<Liste> getListeTaches() {
        return this.listeTaches;
    }

    public Liste getListeTaches(String nom) {
        for (Liste liste : this.listeTaches) {
            if (liste.getNom().equals(nom)) {
                return liste;
            }
        }
        return null;
    }

    public Modele chargerProjet(String nomProjet) throws FileNotFoundException, ProjectNotFoundException {
        String cheminProjet = "/" + nomProjet + "/";
        File repertoire = new File(cheminProjet);
        Modele m = new Modele();
        if(repertoire.isDirectory()){
            String[] nomsListes = repertoire.list();
            if (nomsListes == null) return m;
            for(String s : nomsListes){
                m.ajouterListeTaches(chargerListe(s));
            }
            return m;
        }
        else throw new ProjectNotFoundException();
    }

    public void sauvegarderProjet() throws FileNotFoundException {
        String chemin = "/" + this.nomProjet + "/";
        for(Liste l : this.getListeTaches()){
            String fichier = chemin + l.getNom() + ".trebo";
            sauvegarderListe(fichier, l);
        }
    }

    public void sauvegarderListe(String fichierSortie, Liste liste) throws FileNotFoundException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierSortie))){
            oos.writeObject(this);
            for(Composant c : liste.getComposants()){
                oos.writeObject(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Liste chargerListe(String fichierEntree) throws FileNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierEntree))) {
            Liste liste = (Liste)ois.readObject();
            Composant c = (Composant)ois.readObject();
            while(c != null){
                liste.ajouterComposant(c);
                c = (Composant)ois.readObject();
            }
            return liste;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
