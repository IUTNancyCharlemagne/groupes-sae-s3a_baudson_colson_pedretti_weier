package main;

import main.objet.composite.Composant;
import main.objet.composite.Tache;
import main.objet.Liste;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Classe représentant un projet
 */
public class Projet implements Serializable{

    /**
     * Liste des listes de tâches
     */
    private final List<Liste> listeTaches;

    /**
     * Liste de toutes les tâches sans arborescence
     */
    private final List<Composant> listeTouteTaches;

    /**
     * Nom du projet
     */
    private String nomProjet;
    /**
     * Chemin du fichier de sauvegarde
     */
    private String chemin;

    /**
     * Constructeur vide de Projet.
     * La liste de listes de tâches est initialisée.
     */
    public Projet() {
        this.listeTaches = new ArrayList<Liste>();
        this.listeTouteTaches = new ArrayList<Composant>();
    }

    /**
     * Constructeur de la classe Projet
     *
     * @param nomProjet Nom du projet
     */
    public Projet(String nomProjet) {
        this.nomProjet = nomProjet;
        this.listeTaches = new ArrayList<Liste>();
        this.listeTouteTaches = new ArrayList<Composant>();
    }

    /**
     * Indique si le fichier se finit par l'extension '.trebbo'
     *
     * @param fileName chemin du fichier
     * @return true si l'extension est '.trebbo', false sinon
     */
    public static boolean fichierTrebbo(String fileName) {
        return fileName.endsWith(".trebbo");
    }

    /**
     * Récupère une liste de tâches à partir du nom de la liste
     *
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
     * Méthode qui compare deux projets
     * @param o
     * @return true si les deux objets sont identiques, false sinon+
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projet projet = (Projet) o;
        boolean res = true;
        for(int i = 0; i < Math.max(listeTaches.size(), projet.listeTaches.size()); i++) {
            try{
                res = res && listeTaches.get(i).equals(projet.listeTaches.get(i));
            }
            catch (IndexOutOfBoundsException e){
                return false;
            }
        }

        return Objects.equals(nomProjet, projet.nomProjet) && Objects.equals(chemin, projet.chemin) && res;
    }

    /**
     * Méthode hashCode de la classe Projet
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(listeTaches, listeTouteTaches, nomProjet, chemin);
    }

    /**
     * Récupère une tâche à partir du nom de la tâche
     *
     * @param nomTache Nom de la tâche
     * @return Tâche
     */
    public Composant getTache(String nomTache) {
        for (Composant composant : this.listeTouteTaches) {
            if (composant.getNom().equals(nomTache)) {
                return composant;
            }
        }
        return null;
    }

    /**
     * Ajoute une liste de tâches à la liste des listes de tâches
     *
     * @param liste Liste de tâches à ajouter
     */
    public void ajouterListeTaches(Liste liste) {
        this.listeTaches.add(liste);
    }

    /**
     * Supprime une liste de tâches de la liste des listes de tâches
     *
     * @param liste Liste de tâches à supprimer
     */
    public void supprimerListeTaches(Liste liste) {
        this.listeTaches.remove(liste);
    }

    /**
     * Méthode permettant d'archiver une tâche et toutes ses sous-tâches
     * @param nomTache Nom de la tâche à archiver
     */
    public void archiverTache(String nomTache) {
        this.getTache(nomTache).setEstArchive(true);
        // Archiver toutes les sous-tâches
        if (this.getTache(nomTache) instanceof Tache) {
            for (Composant composant : ((Tache) this.getTache(nomTache)).getSousTaches()) {
                archiverTache(composant.getNom());
            }
        }
    }

    /**
     * Méthode permettant de désarchiver une tâche et toutes ses sous-tâches
     * @param nomTache Nom de la tâche à désarchiver
     */
    public void desarchiverTache(String nomTache){
        this.getTache(nomTache).setEstArchive(false);
    }

    /**
     * Méthode toString de la classe Projet
     * @return
     */
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

    /**
     * Méthode qui permet de récupérer la liste des listes de tâches
     * @return la liste des listes de tâches
     */
    public List<Liste> getListeTaches() {
        return this.listeTaches;
    }

    /**
     * Méthode qui permet de récupérer la liste de toutes les tâches
     * @return la liste de toutes les tâches
     */
    public List<Composant> getListeTouteTaches() {
        return this.listeTouteTaches;
    }

    /**
     * Méthode qui permet de récupérer le nom du projet
     * @return le nom du projet
     */
    public String getNomProjet() {
        return this.nomProjet;
    }

    /**
     * Méthode qui permet de modifier le nom du projet
     * @param nomProjet le nouveau nom du projet
     */
    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    /**
     * Méthode qui permet de récupérer le chemin du fichier de sauvegarde
     * @return le chemin du fichier de sauvegarde
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Méthode qui permet de modifier le chemin du fichier de sauvegarde
     * @param chemin le nouveau chemin du fichier de sauvegarde
     */
    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    /**
     * Méthode qui permet de récupérer la liste des tâches archivées
     * @return la liste des tâches archivées
     */
    public ArrayList<Composant> getArchives() {
        ArrayList<Composant> archives = new ArrayList<Composant>();
        for (Composant composant : this.listeTouteTaches) {
            if (composant.getEstArchive()) {
                archives.add(composant);
            }
        }
        return archives;
    }

    /**
     * Permet de supprimer une tâche du projet
     * @param nomTache
     */
    public void supprimerTache(String nomTache) {

        for (Liste liste : this.getListeTaches()) {
            Iterator<Composant> composantIterator = liste.getComposants().iterator();

            while (composantIterator.hasNext()) {
                Composant composant = composantIterator.next();

                if (composant.getNom().equals(nomTache)) {
                    composantIterator.remove();
                } else {
                    if (composant instanceof Tache) {

                        ((Tache) composant).getSousTaches().removeIf(sousTache -> sousTache.getNom().equals(nomTache));
                    }
                }
            }
        }
    }

    /**
     * Permet de récupérer toutes les tâches du projet
     * @return
     */
    public ArrayList<Tache> getToutesTaches(){
        ArrayList<Tache> taches = new ArrayList<Tache>();
        for(Liste l : this.getListeTaches()){
            for(Composant c : l.getComposants()){
                taches.add((Tache)c);
            }
        }
        return taches;
    }

    /**
     * Permet de récupérer la date de début la plus ancienne de toutes les tâches du projet
     * @return
     */
    public LocalDate getPremiereDateDebut(){
        ArrayList<Tache> taches = this.getToutesTaches();
        if(taches.size() == 0) return null;
        LocalDate minDate = taches.get(0).getDateDebut();

        for(Tache t : taches){
            if (t.getDateDebut().isBefore(minDate)) minDate = t.getDateDebut();
        }

        return minDate;
    }

    /**
     * Permet de récupérer la date de fin la plus récente de toutes les tâches du projet
     * @return
     */
    public LocalDate getDerniereDateFin(){
        ArrayList<Tache> taches = this.getToutesTaches();
        if(taches.size() == 0) return null;
        LocalDate maxDate = taches.get(0).getDateFin();

        for(Tache t : taches){
            if (t.getDateFin() != null && t.getDateFin().isAfter(maxDate)) maxDate = t.getDateFin();
        }

        return maxDate;
    }
}