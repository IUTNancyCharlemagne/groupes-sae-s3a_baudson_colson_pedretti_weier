package main.composite;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.Tag;
import main.controleurs.ControlAfficherTache;
import main.controleurs.ControlOnDragDetected;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Classe qui représente une tâche dans le projet <br>
 * Elle hérite de la classe Composant
 *
 * @see Composant
 */
public class Tache extends Composant {

    /**
     * Liste des sous-tâches de la tâche
     */
    protected List<Composant> sousTaches;

    /**
     * Date de début de la tâche
     * Format : yyyy-MM-dd
     */
    protected LocalDate dateDebut;

    /**
     * Date de fin de la tâche
     * Format : yyyy-MM-dd
     */
    protected LocalDate dateFin;

    /**
     * Durée de la tâche
     */
    protected int duree;

    /**
     * Constructeur de la classe Tache
     * @param nom nom de la tâche
     * @param image url de l'image de la tâche
     */
    public Tache(String nom, String image, int duree) {
        this.nom = nom;
        this.image = image;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.sousTaches = new ArrayList<Composant>();
        this.estTerminee = false;
        this.nbTags = 0;
    }

    /**
     * Constructeur avec dates de début et de fin.
     * Assigne les dates si et seulement si elles sont correctes.
     * @param nom
     * @param dateDebut
     * @param dateFin
     */
    public Tache(String nom, LocalDate dateDebut, LocalDate dateFin) {
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.sousTaches = new ArrayList<Composant>();
        this.estTerminee = false;
        this.nbTags = 0;
        if (dateValide(dateDebut) && dateValide(dateFin)) {
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }
    }

    /**
     * Méthode statique permettant de savoir si une date entrée est valide.
     *
     * @param date Date
     * @return true si elle est valide, false sinon
     */
    public static boolean dateValide(LocalDate date) {
        return date != null;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "enfants=" + sousTaches +
                ", dependances=" + dependances +
                ", estTerminee=" + estTerminee +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", estArchive=" + estArchive +
                ", tags=" + tags +
                ", dates=" + dateDebut + " - " + dateFin +
                '}';
    }

    @Override
    public VBox afficher(Modele modele) {

        // Création du Pane de la tâche
        VBox paneTache = new VBox();
        paneTache.setId(this.nom);
        paneTache.getStyleClass().add("paneTache");

        // Création du texte du nom de la tâche
        Text textNom = new Text(this.nom);
        paneTache.getChildren().add(textNom);

        paneTache.setOnMouseClicked(new ControlAfficherTache(modele));
        paneTache.setOnDragDetected(new ControlOnDragDetected(modele));
        return paneTache;
    }

    /**
     * Méthode qui permet de récupérer la liste dans laquelle se trouve la tâche
     *
     * @param modele Le modele
     * @return La liste dans laquelle se trouve la tâche
     */
    public Liste getCurentListe(Modele modele) {
        Liste liste = null;
        for (Liste l : modele.getProjet().getListeTaches()) {
            for (Composant c : l.getComposants()) {
                if (c.getNom().equals(this.nom)) {
                    liste = l;
                    break;
                }
            }
        }
        return liste;
    }

    /**
     * Méthode qui permet d'ajouter une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void ajouter(Composant c) {
        this.sousTaches.add(c);
    }

    /**
     * Méthode qui permet de retirer une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void retirer(Composant c) {
        this.sousTaches.remove(c);
    }

    /**
     * Méthode qui permet de voir si les dépendances d'une tâche sont terminées.
     *
     * @return vrai si toutes les dépendances sont terminées, faux sinon
     */
    public boolean dependancesOk() {
        boolean ok = true;
        for (Composant c : this.dependances) {
            ok = ok && c.getEstTerminee();
        }
        return ok;
    }

    /**
     * Méthode permettant de calculer la durée d'une tâche par rapport à ses attributs de dates.
     *
     * @return Durée en jours
     * @throws ParseException
     */
    public int calculerDureeTache() throws ParseException {
        if (this.dateDebut == null || this.dateFin == null) return -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(this.dateDebut.toString());
        Date date2 = sdf.parse(this.dateFin.toString());
        long diff = date2.getTime() - date1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Composant> getSousTaches() {
        return this.sousTaches;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
}
