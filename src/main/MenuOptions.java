package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.controleurs.ControlChangerFond;
import main.controleurs.ControlChangerVue;
import main.controleurs.ControlCharger;
import main.controleurs.ControlSauvegarde;
import main.exceptions.ProjectNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuOptions implements EventHandler<ActionEvent> {

    private Modele modele;
    private Stage primaryStage;

    private CheckMenuItem fullScreen;

    private BorderPane layout;

    private ControlChangerVue controlChangerVue;

    private ControlSauvegarde controlSauvegarde;
    private ControlCharger controlCharger;


    public MenuOptions(Modele modele, Stage stage, BorderPane layout, ControlChangerVue controlChangerVue, ControlSauvegarde controlSauvegarde, ControlCharger controlCharger) {
        this.modele = modele;
        this.primaryStage = stage;
        this.layout = layout;
        this.controlChangerVue = controlChangerVue;
        this.controlSauvegarde = controlSauvegarde;
        this.controlCharger = controlCharger;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Fichier");
        ImageView fileImage = new ImageView("file:icons/file.png");
        fileImage.setFitHeight(16);
        fileImage.setFitWidth(16);
        fileMenu.setGraphic(fileImage);
        MenuItem newMenuItem = new MenuItem("Nouveau Projet (Ctrl + N)");
        ImageView newImage = new ImageView("file:icons/new.png");
        newImage.setFitHeight(16);
        newImage.setFitWidth(16);
        newMenuItem.setGraphic(newImage);

        newMenuItem.setOnAction(e -> {
            primaryStage.setTitle("Nouveau Projet (Non Sauvegardé)");
            System.out.println("Nouveau Projet");
            modele.setProjet(new Projet(modele.getProjet().getNomProjet()));
            modele.notifierObservateur();
        });

        MenuItem openMenuItem = new MenuItem("Ouvrir Projet (Ctrl + O)");
        ImageView openImage = new ImageView("file:icons/open.png");
        openImage.setFitHeight(16);
        openImage.setFitWidth(16);
        openMenuItem.setGraphic(openImage);
        openMenuItem.setOnAction(this.controlCharger);

        MenuItem saveMenuItem = new MenuItem("Enregistrer Projet (Ctrl + S)");
        ImageView saveImage = new ImageView("file:icons/save.png");
        saveImage.setFitHeight(16);
        saveImage.setFitWidth(16);
        saveMenuItem.setGraphic(saveImage);
        saveMenuItem.setOnAction(this.controlSauvegarde);

        MenuItem exitMenuItem = new MenuItem("Fermer l'application (Alt+F4)");
        ImageView exitImage = new ImageView("file:icons/exit.png");
        exitImage.setFitHeight(16);
        exitImage.setFitWidth(16);
        exitMenuItem.setGraphic(exitImage);
        exitMenuItem.setOnAction(e -> {
            primaryStage.close();
        });
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().add(fileMenu);
        layout.setTop(menuBar);

        Menu displayMenu = new Menu("Affichage");
        ImageView displayImage = new ImageView("file:icons/display.png");
        displayImage.setFitHeight(16);
        displayImage.setFitWidth(16);
        displayMenu.setGraphic(displayImage);

        RadioMenuItem displayColumn = new RadioMenuItem("Affichage en colonnes");
        ImageView displayColumnImage = new ImageView("file:icons/collumn.png");
        displayColumnImage.setFitHeight(16);
        displayColumnImage.setFitWidth(16);
        displayColumn.setGraphic(displayColumnImage);
        displayColumn.setOnAction(controlChangerVue);

        RadioMenuItem displayRow = new RadioMenuItem("Affichage en lignes");
        ImageView displayRowImage = new ImageView("file:icons/row.png");
        displayRowImage.setFitHeight(16);
        displayRowImage.setFitWidth(16);
        displayRow.setGraphic(displayRowImage);
        displayRow.setOnAction(controlChangerVue);

        RadioMenuItem displayGantt = new RadioMenuItem("Afficher le Gantt");
        ImageView displayGanttImage = new ImageView("file:icons/gantt.png");
        displayGanttImage.setFitHeight(16);
        displayGanttImage.setFitWidth(16);
        displayGantt.setGraphic(displayGanttImage);
        displayGantt.setOnAction(controlChangerVue);

        RadioMenuItem displayArchives = new RadioMenuItem("Afficher les archives");
        ImageView displayArchivesImage = new ImageView("file:icons/archive.png");
        displayArchivesImage.setFitHeight(16);
        displayArchivesImage.setFitWidth(16);
        displayArchives.setGraphic(displayArchivesImage);
        displayArchives.setOnAction(controlChangerVue);

        EventHandler<ActionEvent> setFullScreen = e -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
            if (e.getSource() instanceof CheckMenuItem){
                if(primaryStage.isFullScreen()) ((CheckMenuItem) e.getSource()).setSelected(true);
                else ((CheckMenuItem) e.getSource()).setSelected(false);
            }
        };

        CheckMenuItem fullScreen = new CheckMenuItem("Plein écran (F5)");
        ImageView fullScreenImage = new ImageView("file:icons/fullscreen.png");
        fullScreenImage.setFitHeight(16);
        fullScreenImage.setFitWidth(16);
        fullScreen.setGraphic(fullScreenImage);
        fullScreen.setOnAction(setFullScreen);

        MenuItem changeBackground = new MenuItem("Changer le fond (Ctrl + B)");
        ImageView changeBackgroundImage = new ImageView("file:icons/background.png");
        changeBackgroundImage.setFitHeight(16);
        changeBackgroundImage.setFitWidth(16);
        changeBackground.setGraphic(changeBackgroundImage);

        EventHandler<ActionEvent> changeBackgroundEvent = e -> {
            new ControlChangerFond(modele, primaryStage, layout);
        };

        changeBackground.setOnAction(changeBackgroundEvent);

        ToggleGroup group = new ToggleGroup();
        displayColumn.setToggleGroup(group);
        displayColumn.setSelected(true);
        displayRow.setToggleGroup(group);
        displayGantt.setToggleGroup(group);
        displayArchives.setToggleGroup(group);

        displayMenu.getItems().addAll(displayColumn, displayRow, displayGantt, displayArchives, new SeparatorMenuItem(), fullScreen, changeBackground);
        menuBar.getMenus().add(displayMenu);
    }

}
