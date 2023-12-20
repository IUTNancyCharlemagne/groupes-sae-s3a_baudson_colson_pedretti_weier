package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.controleurs.ControlChangerVue;
import main.controleurs.ControlCharger;
import main.controleurs.ControlSauvegarde;
import main.observateur.VueArchives;
import main.observateur.VueBureau;

public class Trebbo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Modele modele = new Modele(new Projet());
        BorderPane layout = new BorderPane();

        ControlChangerVue controlChangerVue = new ControlChangerVue(modele);
        ControlSauvegarde controlSauvegarde = new ControlSauvegarde(modele, primaryStage);
        ControlCharger controlCharger = new ControlCharger(modele, primaryStage);

        MenuOptions menuOptions = new MenuOptions(modele, primaryStage, layout, controlChangerVue, controlSauvegarde, controlCharger);

        MenuContext menuContext = new MenuContext(modele, layout,primaryStage);

        menuOptions.handle(new ActionEvent());
        menuContext.handle(new ActionEvent());

        Shortcut shortcut = new Shortcut(modele, primaryStage, layout);
        shortcut.handle(new ActionEvent());

        VueBureau vueBureau = new VueBureau();
        modele.enregistrerObservateur(vueBureau);

        VueArchives vueArchives = new VueArchives();
        modele.enregistrerObservateur(vueArchives);

        modele.notifierObservateur();
        layout.setCenter(modele.getPaneBureau());
        layout.setBackground(new Background(new BackgroundFill(new Color((double) 35 /255, (double) 38 /255, (double) 38 /255,1), CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setMaximized(true);

        modele.getStackPane().getChildren().add(layout);
        Scene scene = new Scene(modele.getStackPane(), 720, 576);

        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        primaryStage.getIcons().add(new Image("file:icons/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trebbo");
        primaryStage.show();
    }
}