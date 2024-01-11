package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.controleurs.ControlChangerFond;
import main.controleurs.ControlChangerVue;
import main.controleurs.ControlCharger;
import main.controleurs.ControlSauvegarde;
import main.menu.MenuContext;
import main.menu.MenuOptions;
import main.observateur.VueArchives;
import main.observateur.VueBureau;
import main.observateur.VueGantt;
import main.observateur.VueListe;
import main.observateur.VueTache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Trebbo extends Application {

    /**
     * Méthode main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode qui lance l'application.
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Modele modele = new Modele(new Projet());
        BorderPane layout = new BorderPane();

        Path path = Paths.get("./params");
        if (!Files.exists(path)) Files.createDirectories(path);

        ControlChangerVue controlChangerVue = new ControlChangerVue(modele);
        ControlSauvegarde controlSauvegarde = new ControlSauvegarde(modele, primaryStage);
        ControlCharger controlCharger = new ControlCharger(modele, primaryStage);
        ControlChangerFond controlChangerFond = new ControlChangerFond(modele, primaryStage, layout);

        MenuOptions menuOptions = new MenuOptions(modele, primaryStage, layout, controlChangerVue, controlSauvegarde, controlCharger);

        MenuContext menuContext = new MenuContext(modele, layout, primaryStage);

        menuOptions.handle(new ActionEvent());
        menuContext.handle(new ActionEvent());

        Shortcut shortcut = new Shortcut(modele, primaryStage, layout);
        shortcut.handle(new ActionEvent());

        VueBureau vueBureau = new VueBureau();
        modele.enregistrerObservateur(vueBureau);

        VueGantt vueGantt = new VueGantt(modele);
        modele.enregistrerObservateur(vueGantt);

        VueArchives vueArchives = new VueArchives();
        modele.enregistrerObservateur(vueArchives);

        VueListe vueListe = new VueListe();
        modele.enregistrerObservateur(vueListe);

        VueTache vueTache = new VueTache(modele);
        modele.enregistrerObservateur(vueTache);

        modele.notifierObservateur();
        layout.setCenter(modele.getPaneBureau());
        primaryStage.setMaximized(true);

        modele.getStackPane().getChildren().add(layout);
        Scene scene = new Scene(modele.getStackPane(), 720, 576);

        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        primaryStage.getIcons().add(new Image("file:icons/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trebbo");

        primaryStage.show();

        //set background image
        String fondParamFile = "./params/background.txt";
        if (Files.exists(Paths.get(fondParamFile))) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fondParamFile));
            String cheminFond = bufferedReader.readLine();
            if (!Objects.equals(cheminFond, "") && Files.exists(Paths.get(cheminFond))) {
                controlChangerFond.changerFond(cheminFond);
            }
        } else
            layout.setBackground(new Background(new BackgroundImage(new Image("file:backgrounds/base.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));
    }
}