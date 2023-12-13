package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlAjouterTache;
import main.observateur.VueBureau;

public class TestJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {

        Modele modele = new Modele();

        VueBureau vueBureau = new VueBureau();
        modele.enregistrerObservateur(vueBureau);

        modele.notifierObservateur();

        Scene scene = new Scene(modele.paneBureau, 720, 576);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
