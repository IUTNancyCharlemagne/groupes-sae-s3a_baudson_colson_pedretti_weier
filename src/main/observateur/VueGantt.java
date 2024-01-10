package main.observateur;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import main.menu.MenuOptions;
import main.Modele;
import main.Sujet;
import main.objet.composite.Composant;
import main.objet.composite.Tache;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class VueGantt implements Observateur {
    static public int joursParColonne = 1;

    static public int periodeSizeW = 75;
    static public int periodeSizeH = 75;

    static public int ypos = 1;
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

        Label paramLabel = new Label("Paramètres");
        paramLabel.setFont(new Font("Arial", 16));
        paramLabel.setStyle("-fx-font-weight: bold;");
        paramLabel.setPadding(new Insets(10, 0, 10, 0));
        Label longueurLabel = new Label("Longueur des cases");
        Slider longueurCase = new Slider();
        Label hauteurLabel = new Label("Hauteur des cases");
        Slider hauteurCase = new Slider();

        longueurCase.setMin(75);
        longueurCase.setMax(500);
        longueurCase.setValue(periodeSizeW);
        longueurCase.setOrientation(Orientation.HORIZONTAL);
        longueurCase.setShowTickMarks(true);
        longueurCase.setShowTickLabels(true);
        longueurCase.setMajorTickUnit(20);
        longueurCase.setPrefSize(20, 500);
        longueurCase.setSnapToTicks(true);

        hauteurCase.setMin(75);
        hauteurCase.setMax(500);
        hauteurCase.setValue(periodeSizeH);
        hauteurCase.setOrientation(Orientation.HORIZONTAL);
        hauteurCase.setShowTickMarks(true);
        hauteurCase.setShowTickLabels(true);
        hauteurCase.setMajorTickUnit(20);
        hauteurCase.setPrefSize(20, 500);
        hauteurCase.setSnapToTicks(true);

        HBox proportionHbox = new HBox();
        Label proportionLabel = new Label("Garder les proportions");
        CheckBox proportionCheckBox = new CheckBox();
        proportionCheckBox.setPadding(new Insets(0, 10, 0, 10));
        proportionHbox.setPadding(new Insets(10, 0, 10, 0));
        proportionHbox.getChildren().addAll(proportionLabel, proportionCheckBox);

        proportionCheckBox.setOnAction(e -> {
            if(proportionCheckBox.isSelected()){
                hauteurCase.setValue(longueurCase.getValue());
                hauteurCase.setDisable(true);
            } else {
                hauteurCase.setDisable(false);
            }
        });

        longueurCase.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(proportionCheckBox.isSelected()){
                hauteurCase.setValue(newValue.intValue());
            }
//            System.out.println("px par case (Longueur): " + periodeSizeW);
            periodeSizeW = newValue.intValue();
            chargerGANTT(grid);
        });

        hauteurCase.valueProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("px par case (Hauteur): " + periodeSizeH);
            periodeSizeH = newValue.intValue();
            chargerGANTT(grid);
        });

        Label nbJoursLabel = new Label("Nombre de jours par colonne");
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
        joursColonne.setOrientation(Orientation.HORIZONTAL);
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

        // Prendre une Capture d'écran
        Button screenshotButton = new Button("Capture d'écran");

        screenshotButton.setOnAction(e -> {
            MenuOptions.menuBar.setManaged(false);
            MenuOptions.menuBar.setStyle("-fx-opacity: 0;");
            ganttInfoVbox.setStyle("-fx-opacity: 0;");
            ganttInfoVbox.setManaged(false);
            WritableImage screenshot = new WritableImage((int) modele.getPaneBureau().getWidth(), (int) modele.getPaneBureau().getHeight());
            modele.getPaneBureau().getScene().snapshot(screenshot);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la capture d'écran");
            if(!new File("./screenshots/").exists()){
                new File("./screenshots/").mkdir();
            }
            fileChooser.setInitialDirectory(new File("./screenshots/"));
            if (modele.getProjet().getNomProjet() != null)
                fileChooser.setInitialFileName(modele.getProjet().getNomProjet() + ".png");
            else fileChooser.setInitialFileName("untitled.png");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File selectedFile = fileChooser.showSaveDialog(modele.getPaneBureau().getScene().getWindow());
            if (selectedFile != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", selectedFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            ganttInfoVbox.setManaged(true);
            MenuOptions.menuBar.setManaged(true);
            MenuOptions.menuBar.setStyle("-fx-opacity: 1;");
            ganttInfoVbox.setStyle("-fx-opacity: 1;");
            ganttInfoVbox.setStyle("-fx-background-color: rgba(255,255,255); -fx-border-color: black; -fx-border-width: 2px;");
        });

        VBox sliderVbox = new VBox();
        sliderVbox.setPrefHeight(250);
        sliderVbox.getChildren().addAll(proportionHbox,longueurLabel, longueurCase, hauteurLabel, hauteurCase, nbJoursLabel, joursColonne);
        ganttInfoVbox.getChildren().addAll(ganttInfoLabel, titreprojet, paramLabel, sliderVbox,screenshotButton);
        ganttInfoVbox.setPadding(new Insets(10));
        ganttHbox.setHgrow(scrollPane, Priority.ALWAYS);
        chargerGANTT(grid);

    }

    public void chargerGANTT(GridPane grid) {
        VueGantt.ypos = 1;
        grid.getChildren().clear();

        grid.setHgap(0);

        LocalDate debutProjet = modele.getProjet().getPremiereDateDebut();
        LocalDate finProjet = modele.getProjet().getDerniereDateFin();

        if (modele.getProjet().getToutesTaches().isEmpty()) return;

        try {
            int dureeProjet = Composant.calculerDureeEntreDates(debutProjet, finProjet);
            LocalDate dateCourante = LocalDate.parse(debutProjet.toString());

            List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
            int ypos = 1;
            for (int i = 0; i < modele.getProjet().getListeTouteTaches().size(); i++) {
                if (!listeTaches.get(i).getEstArchive() && listeTaches.get(i).getParent() == null) {
                    for (int j = 0; j <= dureeProjet; j += joursParColonne) {
                        Rectangle rect = new Rectangle(periodeSizeW, periodeSizeH);
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
                date.setFont(new Font("Arial", (0.18 * periodeSizeH)));
                date.setStyle("-fx-font-weight: bold;");
                date.setAlignment(Pos.CENTER);
                datePane.getChildren().add(date);
                date.setPrefHeight((int) (periodeSizeH / 2));
                date.setPrefWidth(periodeSizeW);
                datePane.setPrefHeight((int) (periodeSizeH / 2));
                datePane.setPrefWidth(periodeSizeW);
                dateCourante = dateCourante.plusDays(joursParColonne);
                Rectangle rect = new Rectangle(periodeSizeW, (int) (periodeSizeH / 2));
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                grid.add(rect, i, 0);
                grid.add(datePane, i, 0);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Composant> listeTaches = modele.getProjet().getListeTouteTaches();
        for (int i = 0; i < listeTaches.size(); i++) {
            if (!listeTaches.get(i).getEstArchive() && listeTaches.get(i).getParent() == null) {
                afficherTacheGantt(listeTaches.get(i),grid,debutProjet,false);
                VueGantt.ypos++;
            }
        }
    }

    /**
     * Affiche une tâche dans le diagramme de Gantt et ses dépandances
     * @param composant Tâche renseignée
     * @param grid grille du diagramme de Gantt
     * @param debutProjet date de début du projet
     * @param estTacheDep true si la tâche dépend de la tâche précédente
     */
    public void afficherTacheGantt(Composant composant, GridPane grid, LocalDate debutProjet, boolean estTacheDep) {
        if(composant instanceof Tache) {
            Tache tache = (Tache) composant;
            try {
                Pane textePane = tache.afficherGantt(modele);
                int xPos = Composant.calculerDureeEntreDates(debutProjet, tache.getDateDebut());
                if (xPos >= 0) {
                    if (tache.getDependances().isEmpty()) {
                        grid.add(textePane, xPos, VueGantt.ypos);
                        int duree = Math.round(tache.calculerDureeTache() / (float) VueGantt.joursParColonne);
                        textePane.setPrefWidth(duree * VueGantt.periodeSizeW);
                        textePane.setPrefHeight(VueGantt.periodeSizeH);

                        if (duree > 0) GridPane.setColumnSpan(textePane, duree * VueGantt.joursParColonne);
                        if(!tache.getTachesDependantes(modele).isEmpty()){
                            for (Composant composant1 : tache.getTachesDependantes(modele)) {
                                VueGantt.ypos++;
                                afficherTacheGantt(composant1, grid, debutProjet, true);
                                VueGantt.ypos++;
                            }
                        }
                    } else {
                        if(estTacheDep){
                            ImageView img = new ImageView();
                            img.setImage(new Image("file:./images/dependance.png"));
                            img.setFitHeight(periodeSizeH);
                            img.setFitWidth(periodeSizeW);
                            grid.add(img, xPos-1, VueGantt.ypos);
                            grid.add(textePane, xPos, VueGantt.ypos);
                            int duree = Math.round(tache.calculerDureeTache() / (float) VueGantt.joursParColonne);
                            textePane.setPrefWidth(duree * VueGantt.periodeSizeW);
                            textePane.setPrefHeight(VueGantt.periodeSizeH);

                            if (duree > 0) GridPane.setColumnSpan(textePane, duree * VueGantt.joursParColonne);
                            if(!tache.getTachesDependantes(modele).isEmpty()){
                                for (Composant composant1 : tache.getTachesDependantes(modele)) {
                                    VueGantt.ypos++;
                                    afficherTacheGantt(composant1, grid, debutProjet, true);
                                    VueGantt.ypos++;
                                }
                            }
                        }
                        VueGantt.ypos--;
                    }

                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}