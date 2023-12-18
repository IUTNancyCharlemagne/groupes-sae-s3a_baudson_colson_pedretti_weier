package main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.composite.Composant;
import main.composite.Tache;
import main.composite.Tag;
import main.exceptions.ProjectNotFoundException;
import main.observateur.Observateur;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Modele implements Sujet, Serializable {

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
    private ArrayList<Observateur> observateurs;
    private String vueCourante;
    private String nomProjet;

    public Modele() {
        this.observateurs = new ArrayList<Observateur>();
        this.listeTaches = new ArrayList<Liste>();
        this.vueCourante = Modele.COLONNE;
    }

    public static boolean fichierTrebo(String fileName) {
        return fileName.endsWith(".trebo");
    }

    @Override
    public String toString() {
        return "Modele{" +
                "observateurs=" + observateurs +
                ", vueCourante='" + vueCourante + '\'' +
                ", nomProjet='" + nomProjet + '\'' +
                ", listeTaches=" + listeTaches +
                '}';
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

    public Modele chargerProjet(String chemin) throws IOException, ProjectNotFoundException, ClassNotFoundException {
        if (!fichierTrebo(chemin)) chemin += ".trebo";
        if (!Files.exists(Paths.get(chemin))) throw new ProjectNotFoundException();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            Modele m = new Modele();
            m.setNomProjet((String) ois.readObject());
            int nbListes = (int) ois.readObject();
            for (int i = 0; i < nbListes; i++) {
                Liste liste = (Liste) ois.readObject();
                for (int j = 0; j < liste.getNbTaches(); j++) {
                    Tache tache = (Tache) ois.readObject();
                    for (int k = 0; k < tache.getNbTags(); k++) {
                        tache.addTag((Tag) ois.readObject());
                    }
                }
                m.ajouterListeTaches(liste);
            }
            return m;
        }
    }

    /**
     * Crée un fichier de sauvegarde du bureau.
     * Le fichier est un fichier binaire de format '.trebo'
     * Le nom du fichier est le nom du projet + '.trebo'
     * L'emplacement du fichier se trouve dans le dossier /projects/.
     *
     * @throws IOException
     */
    public void sauvegarderProjet() throws IOException {
        String chemin = "./projects/";
        String fichier = chemin + this.nomProjet + ".trebo";
        Path path = Paths.get(chemin);
        if (!Files.exists(path)) Files.createDirectories(path);
        System.out.println(this.getListeTaches());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(this.nomProjet);
            oos.writeObject(this.listeTaches.size());
            for (Liste l : this.getListeTaches()) {
                oos.writeObject(l);
                for (Composant c : l.getComposants()) {
                    oos.writeObject(c);
                    if (c.getTags().size() > 0) {
                        for (Tag t : c.getTags()) oos.writeObject(t);
                    }
                }
            }
        }
    }

    /**
     * Crée un fichier de sauvegarde du bureau.
     * Le fichier est un fichier binaire de format '.trebo'
     * Le nom du fichier est nomFichier + '.trebo'
     * L'emplacement du fichier se trouve dans le dossier /projects/.
     *
     * @param nomFichier Nom du fichier voulu.
     *                   Peut se terminer par '.trebo' ou non, la conversion est faite.
     * @throws IOException
     */
    public void sauvegarderProjet(String nomFichier) throws IOException {
        String chemin = "./projects/";
        String fichier;
        if (fichierTrebo(nomFichier)) fichier = chemin + this.nomProjet;
        else fichier = chemin + this.nomProjet + ".trebo";
        Path path = Paths.get(chemin);
        if (!Files.exists(path)) Files.createDirectories(path);
        System.out.println(this.getListeTaches());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(this.nomProjet);
            oos.writeObject(this.listeTaches.size());
            for (Liste l : this.getListeTaches()) {
                oos.writeObject(l);
                for (Composant c : l.getComposants()) {
                    oos.writeObject(c);
                    if (c.getTags().size() > 0) {
                        for (Tag t : c.getTags()) oos.writeObject(t);
                    }
                }
            }
        }
    }

    public String getNomProjet() {
        return this.nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }
}
