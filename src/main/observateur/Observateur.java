package main.observateur;

import main.Sujet;

/**
 * Interface Observateur
 *
 * <p>Interface pour les observateurs</p>
 */
public interface Observateur {

    /**
     * Méthode actualiser
     * <p>Actualise l'observateur</p>
     * @param s Sujet
     */
    public void actualiser(Sujet s);
}
