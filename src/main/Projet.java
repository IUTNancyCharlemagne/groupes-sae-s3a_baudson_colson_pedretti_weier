package main;

import main.composite.Composant;
import main.composite.Tache;
import main.exceptions.ProjectNotFoundException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe représentant un projet
 */
public class Projet {

    /**
     * Nom du projet
     */
    private String nomProjet;

    /**
     * Liste des listes de tâches
     */
    private final List<Liste> listeTaches;

    /**
     * Chemin du fichier de sauvegarde
     */
    private String chemin;

    /**
     * Constructeur vide de Projet.
     * La liste de listes de tâches est initialisée.
     */
    public Projet(){
        this.listeTaches = new ArrayList<Liste>();
    }

    /**
     * Constructeur de la classe Projet
     * @param nomProjet Nom du projet
     */
    public Projet(String nomProjet){
        this.nomProjet = nomProjet;
        this.listeTaches = new ArrayList<Liste>();
    }

    /**
     * Indique si le fichier se finit par l'extension '.trebo'
     * @param fileName chemin du fichier
     * @return true si l'extension est '.trebo', false sinon
     */
    public static boolean fichierTrebo(String fileName) {
        return fileName.endsWith(".trebo");
    }

    /**
     * Récupère une liste de tâches à partir du nom de la liste
     * @param nom Nom de la liste de tâches
     * @return Liste de tâches
     */
    public Liste getListeTaches(String nom) {
        for (Liste liste : this.listeTaches) {
            if (liste.getNom().equals(nom)) {
                return liste;
            }
        }
        return null;
    }

    /**
     * Récupère une tâche à partir du nom de la tâche
     * @param nomTache Nom de la tâche
     * @return Tâche
     */
    public Tache getTache(String nomTache) {
        for (Liste liste : this.listeTaches) {
            for (Composant composant : liste.getComposants()) {
                if (composant.getNom().equals(nomTache)) {
                    return (Tache) composant;
                }
            }
        }
        return null;
    }

    /**
     * Ajoute une liste de tâches à la liste des listes de tâches
     * @param liste Liste de tâches à ajouter
     */
    public void ajouterListeTaches(Liste liste) {
        this.listeTaches.add(liste);
    }

    /**
     * Supprime une liste de tâches de la liste des listes de tâches
     * @param liste Liste de tâches à supprimer
     */
    public void supprimerListeTaches(Liste liste) {
        this.listeTaches.remove(liste);
    }

    /**
     * Charge un projet à partir d'un fichier de sauvegarde
     * @param chemin Chemin du fichier de sauvegarde
     * @return Projet chargé
     * @throws IOException
     * @throws ProjectNotFoundException si le projet spécifié est introuvable.
     * @throws ClassNotFoundException
     */
    public Projet chargerProjet(String chemin) throws IOException, ProjectNotFoundException, ClassNotFoundException {
        if (!fichierTrebo(chemin)) chemin += ".trebo";
        if (!Files.exists(Paths.get(chemin))) throw new ProjectNotFoundException();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            String nomProjet = (String) ois.readObject();
            Projet projet = new Projet(nomProjet);
            projet.setChemin(chemin);
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
     * Crée un fichier de sauvegarde du bureau.
     * Le fichier est un fichier binaire de format '.trebo'
     * Le nom du fichier est nomFichier + '.trebo'
     * L'emplacement du fichier se trouve dans le dossier /projects/.
     *
     * @param chemin Nom du fichier voulu.
     *                   Peut se terminer par '.trebo' ou non, la conversion est faite.
     * @throws IOException
     */
    public void sauvegarderProjet(String chemin) throws IOException {
        if (!fichierTrebo(chemin)) chemin += ".trebo";
        if (this.chemin == null) this.chemin = chemin;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.chemin))) {
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

    public void archiverTache(String nomTache){
        this.getTache(nomTache).setEstArchive(true);
        for (Liste liste : this.listeTaches) {
            for (Composant composant : liste.getComposants()) {
                if (composant.getNom().equals(nomTache)) {
                    liste.retirerComposant(composant);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Projet{" +
                "nomProjet='" + nomProjet + '\'' +
                ", listeTaches=" + listeTaches +
                '}';
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Liste> getListeTaches() {
        return this.listeTaches;
    }

    public String getNomProjet() {
        return this.nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public ArrayList<Composant> getArchives() {
        ArrayList<Composant> archives = new ArrayList<Composant>();
        for(Liste l : this.getListeTaches()){
            for(Composant composant : l.getComposants()) if(composant.getEstArchive()) archives.add(composant);
        }
        return archives;
    }

    public void supprimerTache(String nomTache) {
        Iterator<Liste> listeIterator = this.getListeTaches().iterator();

        while (listeIterator.hasNext()) {
            Liste liste = listeIterator.next();
            Iterator<Composant> composantIterator = liste.getComposants().iterator();

            while (composantIterator.hasNext()) {
                Composant composant = composantIterator.next();

                if (composant.getNom().equals(nomTache)) {
                    composantIterator.remove();
                } else {
                    if (composant instanceof Tache) {
                        Iterator<Composant> sousTacheIterator = ((Tache) composant).getSousTaches().iterator();

                        while (sousTacheIterator.hasNext()) {
                            Composant sousTache = sousTacheIterator.next();

                            if (sousTache.getNom().equals(nomTache)) {
                                sousTacheIterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}