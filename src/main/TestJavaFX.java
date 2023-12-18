package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlChangerVue;
import main.observateur.VueBureau;

import java.io.File;

public class TestJavaFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Modele modele = new Modele(new Projet("Projet 1"));
        BorderPane layout = new BorderPane();
        MenuBar menuBar = new MenuBar();

        ControlChangerVue controlChangerVue = new ControlChangerVue(modele);

        ImageView background = new ImageView();

        Menu fileMenu = new Menu("File");
        ImageView fileImage = new ImageView("file:icons/file.png");
        fileImage.setFitHeight(16);
        fileImage.setFitWidth(16);
        fileMenu.setGraphic(fileImage);
        MenuItem newMenuItem = new MenuItem("New Project");
        ImageView newImage = new ImageView("file:icons/new.png");
        newImage.setFitHeight(16);
        newImage.setFitWidth(16);
        newMenuItem.setGraphic(newImage);

        newMenuItem.setOnAction(e -> {
            System.out.println("Nouveau Projet");
        });

        MenuItem openMenuItem = new MenuItem("Open");
        ImageView openImage = new ImageView("file:icons/open.png");
        openImage.setFitHeight(16);
        openImage.setFitWidth(16);
        openMenuItem.setGraphic(openImage);
        openMenuItem.setOnAction(e -> {
            System.out.println("Ouvrir Projet");
        });

        MenuItem saveMenuItem = new MenuItem("Save");
        ImageView saveImage = new ImageView("file:icons/save.png");
        saveImage.setFitHeight(16);
        saveImage.setFitWidth(16);
        saveMenuItem.setGraphic(saveImage);
        saveMenuItem.setOnAction(e -> {
            System.out.println("Sauvegarder Projet");
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        ImageView exitImage = new ImageView("file:icons/exit.png");
        exitImage.setFitHeight(16);
        exitImage.setFitWidth(16);
        exitMenuItem.setGraphic(exitImage);
        exitMenuItem.setOnAction(e -> {
            primaryStage.close();
        });
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        Menu displayMenu = new Menu("Display");
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
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image File");
            fileChooser.setInitialDirectory(new File("C:\\"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if(selectedFile != null) {
                Image image = new Image(selectedFile.getPath());
                layout.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
            }
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

        ContextMenu contextMenu = new ContextMenu();
        MenuItem contextMenuItem = new MenuItem("Ajouter une liste");
        ImageView contextImage = new ImageView("file:icons/plus.png");
        contextImage.setFitHeight(16);
        contextImage.setFitWidth(16);
        contextMenuItem.setGraphic(contextImage);

        MenuItem contextMenuItem2 = new MenuItem("Changer le fond (Ctrl + B)");
        contextMenuItem2.setGraphic(changeBackgroundImage);
        contextMenuItem2.setOnAction(changeBackgroundEvent);

        contextMenu.getItems().add(contextMenuItem);
        contextMenu.getItems().add(contextMenuItem2);
        layout.setOnContextMenuRequested(e -> {
            contextMenu.show(layout, e.getScreenX(), e.getScreenY());
        });
        contextMenuItem.setOnAction(new ControlAjouterListe(modele));


        // Raccourcis Clavier
            // Mode Plein Ecran (F5)
            layout.setOnKeyPressed(e -> {
                if(e.getCode().toString().equals("F5")) {
                    if(fullScreen.isSelected()) {
                        fullScreen.setSelected(false);
                    } else {
                        fullScreen.setSelected(true);
                    }
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
                }
            });

            // Changer de fon d'écran (Ctrl + B)
            layout.setOnKeyPressed(e -> {
                if(e.getCode().toString().equals("B") && e.isControlDown()) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Image File");
                    fileChooser.setInitialDirectory(new File("C:\\"));
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                    if(selectedFile != null) {
                        Image image = new Image(selectedFile.getPath());
                        layout.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
                    }
                }
            });

        layout.setTop(menuBar);

        VueBureau vueBureau = new VueBureau();
        modele.enregistrerObservateur(vueBureau);

        modele.notifierObservateur();
        layout.setCenter(modele.getPaneBureau());
        layout.setBackground(new Background(new BackgroundFill(new Color((double) 35 /255, (double) 38 /255, (double) 38 /255,1), CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setMaximized(true);

        modele.getStackPane().getChildren().add(layout);

        Scene scene = new Scene(modele.getStackPane(), 720, 576);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test JavaFX");
        primaryStage.show();
    }
}
