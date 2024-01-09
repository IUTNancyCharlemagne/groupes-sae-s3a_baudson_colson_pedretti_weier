package main.observateur;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

    static public int periodeSize = 50;
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

        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setHgap(10);
        grid.setVgap(10);


        HBox ganttHbox = new HBox();
        ganttHbox.setStyle("-fx-background-color: rgba(255,255,255,0.75);");

        VBox ganttInfoVbox = new VBox();
        ganttInfoVbox.setMinWidth(225);
        ganttInfoVbox.setStyle("-fx-background-color: rgba(255,255,255); -fx-border-color: black; -fx-border-width: 2px;");

        Label ganttInfoLabel = new Label("Diagramme de Gantt");
        ganttInfoLabel.setFont(new Font("Arial", 20));

        Label titreprojet = new Label("");

        if (modele.getProjet().getNomProjet() != null) {
            titreprojet.setText("Projet: " + modele.getProjet().getNomProjet());
        } else {
            titreprojet.setText("Mon Projet");
        }
        titreprojet.setFont(new Font("Arial", 16));

        ganttInfoVbox.getChildren().addAll(ganttInfoLabel, titreprojet);
        ganttInfoVbox.setPadding(new Insets(10));


        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.getStyleClass().add("scroll-pane");
        ganttHbox.getChildren().addAll(ganttInfoVbox, scrollPane);
        modele.getPaneBureau().setHgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().setVgrow(ganttHbox, Priority.ALWAYS);
        modele.getPaneBureau().getChildren().add(ganttHbox);

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();
        LocalDate finProjet = modele.getProjet().getDerniereDateFin();

        if (modele.getProjet().getToutesTaches().size() == 0) return;

        try {
            int dureeProjet = Composant.calculerDureeEntreDates(debutProjet, finProjet);
            LocalDate dateCourante = LocalDate.parse(debutProjet.toString());
            for (int i = 0; i <= dureeProjet; i += joursParColonne) {
                Label date = new Label(dateCourante.toString());
                dateCourante = dateCourante.plusDays(joursParColonne);
                grid.add(date, i, 0);
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
                    System.out.println(Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut()));
                    System.out.println(i + 1);
                    int duree = listeTaches.get(i).calculerDureeTache() / joursParColonne;
                    textePane.setPrefWidth(duree);
                    textePane.setPrefHeight(periodeSize);
                    grid.setHgap(0);

                    if (duree > 0) GridPane.setColumnSpan(textePane, duree);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}