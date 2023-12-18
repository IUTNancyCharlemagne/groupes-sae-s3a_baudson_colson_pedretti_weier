package main;

import main.composite.Composant;
import main.composite.Tache;
import main.composite.Tag;
import main.exceptions.ProjectNotFoundException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Projet {
    private String nomProjet;

    /**
     * Liste des listes de tâches
     */
    private final List<Liste> listeTaches;

    public Projet(String nomProjet){
        this.nomProjet = nomProjet;
        this.listeTaches = new ArrayList<Liste>();
    }

    @Override
    public String toString() {
        return "Projet{" +
                "nomProjet='" + nomProjet + '\'' +
                ", listeTaches=" + listeTaches +
                '}';
    }

    public static boolean fichierTrebo(String fileName) {
        return fileName.endsWith(".trebo");
    }

    public List<Liste> getListeTaches() {
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

    public void ajouterListeTaches(Liste liste) {
        this.listeTaches.add(liste);
    }

    public void supprimerListeTaches(Liste liste) {
        this.listeTaches.remove(liste);
    }

    public Projet chargerProjet(String chemin) throws IOException, ProjectNotFoundException, ClassNotFoundException {
        if (!fichierTrebo(chemin)) chemin += ".trebo";
        if (!Files.exists(Paths.get(chemin))) throw new ProjectNotFoundException();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            String nomProjet = (String) ois.readObject();
            Projet projet = new Projet(nomProjet);
            int nbListes = (int) ois.readObject();
            for (int i = 0; i < nbListes; i++) {
                Liste liste = (Liste) ois.readObject();
                for (int j = 0; j < liste.getNbTaches(); j++) {
                    Tache tache = (Tache) ois.readObject();
                    for (int k = 0; k < tache.getNbTags(); k++) {
                        tache.addTag((Tag) ois.readObject());
                    }
                }
                projet.ajouterListeTaches(liste);
            }
            return projet;
        }
    }

    /**
     * Crée un fichier de sauvegarde du projet.
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(this.nomProjet);
            oos.writeObject(this.listeTaches.size());
            for (Liste l : this.listeTaches) {
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(this.nomProjet);
            oos.writeObject(this.listeTaches.size());
            for (Liste l : this.listeTaches) {
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