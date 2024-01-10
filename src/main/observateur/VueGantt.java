package main.observateur;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import main.Modele;
import main.Sujet;
import main.composite.Composant;
import main.composite.SousTache;
import main.composite.Tache;
import main.controleurs.ControlAfficherTache;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class VueGantt implements Observateur {
    static public int joursParColonne = 1;

    static public int periodeSize = 75;
    Modele modele;
    Timeline mainTimeLine;

    public VueGantt(Modele modele) {
        this.modele = modele;
        this.mainTimeLine = new Timeline();
    }

    @Override
    public void actualiser(Sujet s) {

        if (!(s instanceof Modele modele)) return;
        modele.getPaneBureau().getChildren().clear();
        modele.getPaneBureau().getStyleClass().clear();

        HBox ganttHbox = new HBox();

        VBox ganttInfoVbox = new VBox();
        ganttInfoVbox.setMinWidth(225);
        ganttInfoVbox.setStyle("-fx-background-color: rgba(255,255,255); -fx-border-color: black; -fx-border-width: 2px;");

        modele.getPaneBureau().setHgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().setVgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().getChildren().add(ganttHbox);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: rgba(255,255,255); -fx-border-color: black; -fx-border-width: 2px;");

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.getStyleClass().add("scroll-pane");
        ganttHbox.getChildren().addAll(ganttInfoVbox, scrollPane);

        Label ganttInfoLabel = new Label("Diagramme de Gantt");
        ganttInfoLabel.setFont(new Font("Arial", 20));

        Label titreprojet = new Label("");

        if (modele.getProjet().getNomProjet() != null) {
            titreprojet.setText("Projet: " + modele.getProjet().getNomProjet());
        } else {
            titreprojet.setText("Mon Projet");
        }
        titreprojet.setFont(new Font("Arial", 16));

        HBox sliderBox = new HBox();

        Slider tailleCase = new Slider();

        tailleCase.setMin(75);
        tailleCase.setMax(500);
        tailleCase.setValue(periodeSize);
        tailleCase.setOrientation(Orientation.VERTICAL);
        tailleCase.setShowTickMarks(true);
        tailleCase.setShowTickLabels(true);
        tailleCase.setMajorTickUnit(20);
        tailleCase.setPrefSize(20, 500);
        tailleCase.setSnapToTicks(true);

        tailleCase.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("px par case: " + periodeSize);
            periodeSize = newValue.intValue();
            chargerGANTT(grid);
        });

        Slider joursColonne = new Slider();

        joursColonne.setMin(1);
        int dureeProjet;
        if (modele.getProjet().getPremiereDateDebut() != null && modele.getProjet().getDerniereDateFin() != null) {
            try {
                dureeProjet = Composant.calculerDureeEntreDates(modele.getProjet().getPremiereDateDebut(), modele.getProjet().getDerniereDateFin());
                joursColonne.setMax(dureeProjet);
            } catch (ParseException e) {
                joursColonne.setMax(300);
                throw new RuntimeException(e);
            }
        } else {
            joursColonne.setMax(300);
        }
        joursColonne.setValue(joursParColonne);
        joursColonne.setOrientation(Orientation.VERTICAL);
        joursColonne.setShowTickMarks(true);
        joursColonne.setShowTickLabels(true);
        joursColonne.setMajorTickUnit(5);
        joursColonne.setPrefSize(20, 500);
        joursColonne.setSnapToTicks(true);

        joursColonne.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("jours par colonne: " + joursParColonne);
            joursParColonne = newValue.intValue();
            chargerGANTT(grid);
        });

        sliderBox.getChildren().addAll(tailleCase, joursColonne);
        ganttInfoVbox.getChildren().addAll(ganttInfoLabel, titreprojet, sliderBox);
        ganttInfoVbox.setPadding(new Insets(10));

        chargerGANTT(grid);

    }

    public void chargerGANTT(GridPane grid) {

        grid.getChildren().clear();

        grid.setHgap(0);

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();
        LocalDate finProjet = modele.getProjet().getDerniereDateFin();

        if (modele.getProjet().getToutesTaches().size() == 0) return;

        try {
            int dureeProjet = Composant.calculerDureeEntreDates(debutProjet, finProjet);
            LocalDate dateCourante = LocalDate.parse(debutProjet.toString());

            List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
            int ypos = 1;
            for (int i = 0; i < modele.getProjet().getListeTouteTaches().size(); i++) {
                if (!listeTaches.get(i).getEstArchive() && listeTaches.get(i).getParent() == null) {
                    for (int j = 0; j <= dureeProjet; j += joursParColonne) {
                        Rectangle rect = new Rectangle(periodeSize, periodeSize);
                        rect.setFill(Color.WHITE);
                        rect.setStroke(Color.BLACK);
                        grid.add(rect, j, ypos);
                    }
                    ypos++;
                }
            }

            for (int i = 0; i <= dureeProjet; i += joursParColonne) {
                Pane datePane = new Pane();
                Label date = new Label(dateCourante.toString());
                date.setFont(new Font("Arial", (0.18 * periodeSize)));
                date.setStyle("-fx-font-weight: bold;");
                date.setAlignment(Pos.CENTER);
                datePane.getChildren().add(date);
                date.setPrefHeight((int) (periodeSize / 2));
                date.setPrefWidth(periodeSize);
                datePane.setPrefHeight((int) (periodeSize / 2));
                datePane.setPrefWidth(periodeSize);
                dateCourante = dateCourante.plusDays(joursParColonne);
                Rectangle rect = new Rectangle(periodeSize, (int) (periodeSize / 2));
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                grid.add(rect, i, 0);
                grid.add(datePane, i, 0);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        int ypos = 1;
        for (int i = 0; i < listeTaches.size(); i++) {
            if (!listeTaches.get(i).getEstArchive() && listeTaches.get(i).getParent() == null) {
                try {
                    Pane textePane = listeTaches.get(i).afficherGantt(modele);
                    int xPos = Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut());
                    if (xPos >= 0) {
                        grid.add(textePane, xPos, ypos);

                        if (!listeTaches.get(i).getDependances().isEmpty()) {
                            ImageView image = new ImageView();
                            image.setImage(new Image("file:icons/rightArrow.png"));
                            image.setFitHeight(VueGantt.periodeSize);
                            image.setFitWidth(VueGantt.periodeSize);
                            grid.add(image, xPos - 1, ypos);
                        }
                        ypos++;
                        int duree = Math.round(listeTaches.get(i).calculerDureeTache() / (float) VueGantt.joursParColonne);
                        textePane.setPrefWidth(duree * VueGantt.periodeSize);
                        textePane.setPrefHeight(VueGantt.periodeSize);

                        if (duree > 0) GridPane.setColumnSpan(textePane, duree * VueGantt.joursParColonne);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}