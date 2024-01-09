package main.observateur;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Modele;
import main.Sujet;
import main.composite.Composant;

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
        ganttHbox.setStyle("-fx-background-color: rgba(255,255,255,0.75);");

        VBox ganttInfoVbox = new VBox();
        ganttInfoVbox.setMinWidth(225);
        ganttInfoVbox.setStyle("-fx-background-color: rgba(255,255,255); -fx-border-color: black; -fx-border-width: 2px;");

        modele.getPaneBureau().setHgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().setVgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().getChildren().add(ganttHbox);

        GridPane grid = new GridPane();

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
        try {
            dureeProjet = Composant.calculerDureeEntreDates(modele.getProjet().getPremiereDateDebut(), modele.getProjet().getDerniereDateFin());
            joursColonne.setMax(dureeProjet);
        } catch (ParseException e) {
            joursColonne.setMax(300);
            throw new RuntimeException(e);
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

    public void chargerGANTT(GridPane grid){

        grid.getChildren().clear();
        grid.setGridLinesVisible(true);
        grid.setHgap(0);
        grid.setVgap((int)(0.1*periodeSize));

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();
        LocalDate finProjet = modele.getProjet().getDerniereDateFin();

        if (modele.getProjet().getToutesTaches().size() == 0) return;

        try {
            int dureeProjet = Composant.calculerDureeEntreDates(debutProjet, finProjet);
            LocalDate dateCourante = LocalDate.parse(debutProjet.toString());
            for (int i = 0; i <= dureeProjet; i += joursParColonne) {
                Pane datePane = new Pane();
                Label date = new Label(dateCourante.toString());
                datePane.getChildren().add(date);
                datePane.setPrefHeight(periodeSize);
                datePane.setPrefWidth(periodeSize);
                datePane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                dateCourante = dateCourante.plusDays(joursParColonne);
                grid.add(datePane, i, 0);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        for (int i = 0; i < listeTaches.size(); i++) {
            if (!listeTaches.get(i).getEstArchive()) {
                try {
                    Label texte;
                    if (listeTaches.get(i).getDependances().size() == 0) {
                        texte = new Label(listeTaches.get(i).getNom());
                    } else {
                        texte = new Label(listeTaches.get(i).getNom() + "(dépendances: " + listeTaches.get(i).getDependances() + ")");
                    }
                    texte.setFont(new Font("Arial", 14));
                    texte.setStyle("-fx-font-weight: bold;");
                    texte.setWrapText(true);
                    texte.prefHeight(periodeSize);
                    texte.prefHeight(periodeSize);
                    Pane textePane = new Pane(texte);

                    if (listeTaches.get(i).getEstTerminee() || listeTaches.get(i).estPassee(LocalDate.now())) {
                        textePane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else if (listeTaches.get(i).estDansIntervalle(LocalDate.now())) {
                        textePane.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        textePane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    }

                    grid.add(textePane, Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut()), i + 1);
                    if (!listeTaches.get(i).getDependances().isEmpty()) {
                        ImageView image = new ImageView();
                        image.setImage(new Image("file:icons/rightArrow.png"));
                        image.setFitHeight(periodeSize);
                        image.setFitWidth(periodeSize);
                        grid.add(image, Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut()) - 1, i + 1);
                    }
                    int duree = Math.round(listeTaches.get(i).calculerDureeTache() / (float)joursParColonne);
                    textePane.setPrefWidth(duree * periodeSize);
                    textePane.setPrefHeight(periodeSize);

                    if (duree > 0) GridPane.setColumnSpan(textePane, duree*joursParColonne);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}