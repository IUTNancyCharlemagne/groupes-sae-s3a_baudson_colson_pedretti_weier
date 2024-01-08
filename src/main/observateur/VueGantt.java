package main.observateur;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import main.Liste;
import main.Modele;
import main.Sujet;
import main.composite.Composant;
import main.composite.Tache;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

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
        modele.getPaneBureau().getStyleClass().add("paneGantt");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ScrollPane scrollPane = new ScrollPane(grid);
        modele.getPaneBureau().setVgrow(scrollPane, Priority.ALWAYS);
        modele.getPaneBureau().setHgrow(scrollPane, Priority.ALWAYS);
        modele.getPaneBureau().getChildren().add(scrollPane);

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();
        LocalDate finProjet = modele.getProjet().getDerniereDateFin();

        try {
            int dureeProjet = Composant.calculerDureeEntreDates(debutProjet, finProjet);
            LocalDate dateCourante = LocalDate.parse(debutProjet.toString());
            for(int i = 0; i <= dureeProjet; i+=joursParColonne){
                Label date = new Label(dateCourante.toString());
                dateCourante = dateCourante.plusDays(joursParColonne);
                grid.add(date, i, 0);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        for (int i = 0; i < listeTaches.size(); i++) {
            try {
                Label texte;
                if(listeTaches.get(i).getDependances().size() == 0) {
                    texte = new Label(listeTaches.get(i).getNom());
                }
                else{
                    texte = new Label(listeTaches.get(i).getNom() + "(dépendances: " + listeTaches.get(i).getDependances() + ")");
                }
                texte.setFont(new Font("Arial", 14));
                texte.setStyle("-fx-font-weight: bold;");
                texte.setWrapText(true);
                texte.prefHeight(periodeSize);
                Pane textePane = new Pane(texte);

                if(listeTaches.get(i).getEstTerminee() || listeTaches.get(i).estPassee(LocalDate.now())){
                    textePane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                } else if(listeTaches.get(i).estDansIntervalle(LocalDate.now())){
                    textePane.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else{
                    textePane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                }

                grid.add(textePane, Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut()), i + 1);
                System.out.println(Composant.calculerDureeEntreDates(debutProjet, listeTaches.get(i).getDateDebut()));
                System.out.println(i+1);
                int duree = listeTaches.get(i).calculerDureeTache()/joursParColonne;
                textePane.setPrefWidth(duree);
                textePane.setPrefHeight(periodeSize);
                grid.setHgap(0);
                grid.setGridLinesVisible(true);
                if(duree > 0) GridPane.setColumnSpan(textePane, duree);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}