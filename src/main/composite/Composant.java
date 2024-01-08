package main.composite;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.Tag;
import main.controleurs.ControlAfficherTache;
import main.controleurs.ControlOnDragDetected;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Classe abstraite représentant un composant (Tâche ou sous-tâche)
 * @see Tache
 * @see SousTache
 */
public abstract class Composant implements Serializable {

    /**
     * Nom de la tâche
     */
    protected String nom;

    /**
     * Description de la tâche
     */
    protected String description;

    /**
     * Booléen indiquant si la tâche est archivée ou non
     */
    protected boolean estArchive;

    /**
     * Liste des tags de la tâche
     * @see Tag
     */
    protected List<Tag> tags;

    /**
     * Image de description de la tâche
     */
    protected String image;

    /**
     * Nombre de tags de la tâche
     */
    protected int nbTags; // SERT POUR LA SAUVEGARDE

    /**
     * Booléen qui indique si la tâche est terminée ou non
     */
    protected boolean estTerminee;

    /**
     * Liste des dépendances de la tâche
     */
    protected List<Composant> dependances;

    /**
     * Parent du composant
     */
    protected Composant parent;

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

    public static int calculerDureeEntreDates(LocalDate date1, LocalDate date2) throws ParseException {
        if (date1 == null || date2 == null) return -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = sdf.parse(date1.toString());
        Date fin = sdf.parse(date2.toString());
        long diff = debut.getTime() - fin.getTime();
        return (int) Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    /**
     * Constructeur de la classe Composant
     */
    public Composant(String nom, String image, int duree) {
        this.nom = nom;
        this.image = image;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.estTerminee = false;
        this.nbTags = 0;
        this.duree = duree;
        this.dependances = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Composant
     */
    public Composant(String nom, String image, LocalDate dateDebut, LocalDate dateFin) {
        this.nom = nom;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
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

    // ### Test d'affichage ###
    public VBox afficherArchive(Modele modele) {
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
     * Méthode permettant de savoir si une tâche est une sous-tâche ou non.
     *
     * @param modele Modèle de l'application
     * @return true si la tâche est une sous-tâche, false sinon
     */
    public boolean estUneSousTache(Modele modele) {
        return this.getParent() != null;
    }

    /**
     * Méthode permettant d'ajouter un tag à la tâche
     * @param tag Tag à ajouter
     */
    public void addTag(Tag tag){
        tags.add(tag);
        this.nbTags++;
    }

    /**
     * Méthode permettant de supprimer un tag de la tâche
     * @param tag Tag à supprimer
     */
    public void removeTag(Tag tag){
        tags.remove(tag);
        this.nbTags--;
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public String getNom() {
        return nom;
    }
    public String getDescription() {
        return description;
    }
    public boolean getEstArchive() {
        return estArchive;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEstArchive(boolean estArchive) {
        this.estArchive = estArchive;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public int getNbTags(){
        return this.nbTags;
    }
    public boolean getEstTerminee(){return this.estTerminee;}
    public void setEstTerminee(boolean b){
        this.estTerminee = b;
    }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public List<Composant> getDependances() {
        return dependances;
    }

    public void setDependances(List<Composant> dependances) {
        this.dependances = dependances;
    }

    public Composant getParent() {
        return parent;
    }

    public void setParent(Composant parent) {
        this.parent = parent;
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
        long jours = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return (int) jours;
    }

    public LocalDate calculerDateFin() throws ParseException {
        if (this.dateDebut == null || this.duree == 0) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(this.dateDebut.toString());
        Date date2 = new Date(date1.getTime() + TimeUnit.DAYS.toMillis(this.duree));
        return LocalDate.parse(sdf.format(date2));
    }

    public boolean estDansIntervalle(LocalDate date){
        return (date.isAfter(this.dateDebut) && date.isBefore(this.dateFin)) || date.isEqual(this.dateDebut) || date.isEqual(this.dateFin);
    }

    public boolean estPassee(LocalDate date){
        return date.isAfter(this.dateFin);
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
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

    public String toString() {
        return nom;
    }

    public abstract TreeItem<Composant> afficher(Modele modele);

    public void addDependance(Composant composant) throws ParseException {
        this.dependances.add(composant);
        for (Composant c : this.dependances) {
            if (c.getDateFin().isAfter(this.dateDebut)) {
                this.dateDebut = c.getDateFin();
            }
        }
        this.dateFin = this.calculerDateFin();
    }
    public void removeDependance(Composant composant) {
        this.dependances.remove(composant);
    }
}
