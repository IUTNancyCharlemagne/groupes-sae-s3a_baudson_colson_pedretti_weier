package main;

import main.observateur.Observateur;

/**
 * Interface Sujet pour le patron de conception Observateur
 */
public interface Sujet {

    /**
     * Enregistre un observateur
     * @param o Observateur à enregistrer
     */
    public void enregistrerObservateur(Observateur o);

    /**
     * Supprime un observateur
     * @param o Observateur à supprimer
     */
    public void supprimerObservateur(Observateur o);

    /**
     * Notifie les observateurs
     */
    public void notifierObservateur();
}
