package main.composite;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Liste;
import main.Modele;
import main.Tag;
import main.controleurs.ControlAfficherTache;
import main.controleurs.ControlOnDragDetectedListe;
import main.observateur.VueGantt;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Classe abstraite représentant un composant (Tâche ou sous-tâche)
 *
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
     *
     * @see Tag
     */
    protected List<Tag> tags;

    /**
     * Image de description de la tâche
     */
    protected String image;

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

    /**
     * Constructeur de la classe Composant
     */
    public Composant(String nom, String image, int duree) {
        this.nom = nom;
        this.image = image;
        this.description = "";
        this.estArchive = false;
        this.tags = new ArrayList<Tag>();
        this.estTerminee = false;;
        this.dependances = new ArrayList<>();
        if(duree == 0) this.duree = 1;
        else this.duree = duree;
        this.dateDebut = LocalDate.now();
        this.dateFin = LocalDate.now().plusDays(duree);
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
        if (dateValide(dateDebut) && dateValide(dateFin)) {
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        } else {
            this.dateDebut = LocalDate.now();
            this.dateFin = LocalDate.now().plusDays(1);
        }
        this.duree = 1;
        this.dependances = new ArrayList<>();
    }

    public static int calculerDureeEntreDates(LocalDate date1, LocalDate date2) throws ParseException {
        if (date1 == null || date2 == null) return -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date debut = sdf.parse(date1.toString());
        Date fin = sdf.parse(date2.toString());
        long diff = debut.getTime() - fin.getTime();
        return (int) Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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
        paneTache.setOnDragDetected(new ControlOnDragDetectedListe(modele));
        return paneTache;
    }

    public Pane afficherGantt(Modele modele) throws ParseException {

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();

        Label texte;
        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        if (getDependances().isEmpty()) {
            texte = new Label(getNom());
        } else {
            texte = new Label(getNom() + "(dépendances: " + getDependances() + ")");
        }
        texte.setFont(new Font("Arial", (0.18 * VueGantt.periodeSize)));
        texte.setStyle("-fx-font-weight: bold;");
        texte.setWrapText(true);
        texte.prefHeight(VueGantt.periodeSize);
        VBox textePane = new VBox();

        HBox sousTaches = new HBox();

        textePane.getChildren().addAll(texte, sousTaches);

        StringBuffer tooltipText = new StringBuffer();

        if ((getDateDebut().isBefore(LocalDate.now()) || getDateDebut().equals(LocalDate.now())) && getDateFin().isAfter(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (" + (Composant.calculerDureeEntreDates(LocalDate.now(), getDateFin())) + " jours restant(s))");
        } else if (getDateDebut().isAfter(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (tâche pas encore commencée)");
        } else if (getDateFin().isBefore(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (tâche terminée)");
        }

        Tooltip tooltip = new Tooltip(tooltipText.toString());
        tooltip.setStyle("-fx-font-size: 14px;");
        Tooltip.install(textePane, tooltip);

        if (getDateFin() != null && (getEstTerminee() || estPassee(LocalDate.now()))) {
            textePane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (estDansIntervalle(LocalDate.now())) {
            textePane.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            textePane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        textePane.setStyle("-fx-border-color: BLACK; -fx-border-width: 1px;");

        return textePane;
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
     *
     * @param tag Tag à ajouter
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Méthode permettant de supprimer un tag de la tâche
     *
     * @param tag Tag à supprimer
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEstArchive() {
        return estArchive;
    }

    public void setEstArchive(boolean estArchive) {
        this.estArchive = estArchive;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean getEstTerminee() {
        return this.estTerminee;
    }

    public void setEstTerminee(boolean b) {
        this.estTerminee = b;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public boolean estDansIntervalle(LocalDate date) {
        if (date == null || this.dateDebut == null || this.dateFin == null) return false;
        return (date.isAfter(this.dateDebut) && date.isBefore(this.dateFin)) || date.isEqual(this.dateDebut) || date.isEqual(this.dateFin);
    }

    public boolean estPassee(LocalDate date) {
        if (this.dateFin == null) return false;
        return date.isAfter(this.dateFin);
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
        this.CalcDateDebutDependance();
    }

    public void removeDependance(Composant composant) throws ParseException {
        this.dependances.remove(composant);
        this.CalcDateDebutDependance();
    }

    public void CalcDateDebutDependance() throws ParseException {
        if (!this.dependances.isEmpty()) {
            this.dateDebut = this.dependances.get(0).getDateFin();
            for (Composant composant : this.dependances) {
                if (composant.getDateFin().isAfter(this.dateDebut)) {
                    this.dateDebut = composant.getDateFin();
                }
            }
        }
        this.dateFin = this.calculerDateFin();
    }
}
