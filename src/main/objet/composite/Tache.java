package main.objet.composite;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Modele;
import main.controleurs.ControlAfficherTache;
import main.observateur.VueGantt;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public Tache(String nom, String image, int duree) {
        super(nom, image, duree);
        this.sousTaches = new ArrayList<>();
    }

    public Tache(String nom, String image, LocalDate dateDebut, LocalDate dateFin) {
        super(nom, image, dateDebut, dateFin);
        this.sousTaches = new ArrayList<>();
    }

    @Override
    public TreeItem<Composant> afficher(Modele modele) {
        TreeItem<Composant> treeItem = new TreeItem<>(this);
        treeItem.setExpanded(true);
        for (Composant c : this.sousTaches) {
            treeItem.getChildren().add(c.afficher(modele));
        }
        return treeItem;
    }


    /**
     * Méthode qui permet d'ajouter une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void ajouter(Composant c) {
        this.sousTaches.add(c);
        c.setParent(this);
    }

    /**
     * Méthode qui permet de retirer une sous-tâche à la tâche
     *
     * @param c sous-tâche
     */
    public void retirer(Composant c) {
        this.duree -= c.getDuree();
        this.sousTaches.remove(c);
        c.setParent(null);
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    public List<Composant> getSousTaches() {
        return this.sousTaches;
    }

    public void fixDuree() {
        int duree = 0;
        for (Composant composant : this.sousTaches) {
            duree += composant.getDuree();
        }
        if (this.duree < duree) {
            this.duree = duree;
        }
        try {
            this.dateFin = this.calculerDateFin();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ;
        if (this.getParent() != null && this.getParent() instanceof Tache)
            ((Tache) this.getParent()).fixDuree();
    }

    @Override
    public Pane afficherGantt(Modele modele) throws ParseException {

        if(this.getDateDebut() == null) return null;
        calcDateDebutDependance();
        fixDuree();

        Label texte;
        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        if (getDependances().isEmpty()) {
            texte = new Label(getNom());
        } else {
            texte = new Label(getNom() + "→(dépendances: " + getDependances() + ")");
        }
        texte.setFont(new Font("Arial", (0.18 * VueGantt.periodeSizeH)));
        texte.setStyle("-fx-font-weight: bold;");
        texte.setWrapText(true);
        texte.prefHeight(VueGantt.periodeSizeH);
        VBox textePane = new VBox();

        HBox sousTaches = new HBox();
        if (!getSousTaches().isEmpty()) {
            StringBuffer sousTachesTexte = new StringBuffer("Sous-tâches: ");
            for (Composant sousTache : getSousTaches()) {
                Tache sousTacheRef = (Tache) sousTache;
                sousTachesTexte.append(sousTacheRef.getNom() + " (" + sousTacheRef.getDuree() + " jour(s))");
                if (getSousTaches().indexOf(sousTache) != getSousTaches().size() - 1) {
                    sousTachesTexte.append(", ");
                }
            }
            Label stLabel = new Label(sousTachesTexte.toString());
            stLabel.setFont(new Font("Arial", (0.15 * VueGantt.periodeSizeH)));
            sousTaches.getChildren().add(stLabel);
        }

        textePane.getChildren().addAll(texte, sousTaches);

        StringBuffer tooltipText = new StringBuffer();

        if ((this.dateDebut.isBefore(LocalDate.now()) || this.dateDebut.equals(LocalDate.now())) && this.dateFin.isAfter(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (" + (Composant.calculerDureeEntreDates(LocalDate.now(), getDateFin())) + " jours restant(s))");
            textePane.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (this.dateDebut.isAfter(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (tâche pas encore commencée)");
            textePane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (this.dateFin.isBefore(LocalDate.now())) {
            tooltipText.append("Durée : " + getDuree() + " jours (tâche terminée)");
            textePane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        Tooltip tooltip = new Tooltip(tooltipText.toString());
        tooltip.setStyle("-fx-font-size: 14px;");
        Tooltip.install(textePane, tooltip);

        textePane.setStyle("-fx-border-color: BLACK; -fx-border-width: 1px;");

        textePane.setOnMouseClicked(new ControlAfficherTache(modele));


        return textePane;
    }

    public List<Composant> getTachesDependantes(Modele modele) {
        List<Composant> dependances = new ArrayList<>();
        for (Composant composant : modele.getProjet().getListeTouteTaches()) {
            if (composant instanceof Tache) {
                Tache tache = (Tache) composant;
                for (Composant tacheDep : tache.getDependances()) {
                    if (tacheDep.equals(this)) {
                        dependances.add(tache);
                    }
                }
            }
        }
        return dependances;
    }
}
