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
     * Liste des dépendances de la tâche
     */
    protected List<Tache> dependances;

    /**
     * Date de début de la tâche
     * Format : yyyy-MM-dd
     */
    protected String dateDebut;

    /**
     * Date de fin de la tâche
     * Format : yyyy-MM-dd
     */
    protected String dateFin;

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
        this.dependances = new ArrayList<Tache>();
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
    public Tache(String nom, String dateDebut, String dateFin) {
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.sousTaches = new ArrayList<Composant>();
        this.dependances = new ArrayList<Tache>();
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
    public static boolean dateValide(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            Date d = dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
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
        for (Tache t : this.dependances) {
            ok = ok && t.getEstTerminee();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateD = dateFormat.parse(this.dateDebut);
        Date dateF = dateFormat.parse(this.dateFin);
        long durationInMillis = dateD.getTime() - dateF.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(durationInMillis);
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Composant> getSousTaches() {
        return this.sousTaches;
    }

    public List<Tache> getDependances() {
        return dependances;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
}
