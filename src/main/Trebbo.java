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
import main.observateur.VueTache;

import java.io.IOException;

public class Trebbo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode qui lance l'application.
     * @param primaryStage
     */
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

        VueTache vueTache = new VueTache(modele);
        modele.enregistrerObservateur(vueTache);

        // Sauvegarde automatique à la fermeture de l'application, ne sauvegarde pas si le projet est vierge ou s'il n'existe pas
        primaryStage.setOnCloseRequest(event -> {
            if (!modele.getProjet().getListeTaches().isEmpty() || modele.getProjet().getChemin() != null) {
                controlSauvegarde.handle(new ActionEvent());
            }
        });

        modele.notifierObservateur();
        layout.setCenter(modele.getPaneBureau());
        //set background image
        layout.setBackground(new Background(new BackgroundImage(new Image("file:backgrounds/base.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));
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