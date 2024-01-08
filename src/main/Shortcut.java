package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlChangerFond;
import main.controleurs.ControlCharger;
import main.controleurs.ControlSauvegarde;
import main.exceptions.ProjectNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shortcut implements EventHandler<ActionEvent> {

    private final Modele modele;
    private final Stage primaryStage;

    private final BorderPane layout;

    public Shortcut(Modele modele, Stage primaryStage, BorderPane layout) {
        this.modele = modele;
        this.primaryStage = primaryStage;
        this.layout = layout;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // Raccourcis Clavier
        layout.setOnKeyPressed(e -> {

            // Mode Plein Ecran (F11)
            if (e.getCode().toString().equals("F11")) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }

            // Changer de fond d'écran (Ctrl + B)
            if (e.getCode().toString().equals("B") && e.isControlDown()) {
                ControlChangerFond ccf = new ControlChangerFond(modele, primaryStage, layout);
                ccf.handle(new ActionEvent());
            }

            // Nouveau Projet (Ctrl + N)
            if (e.getCode().toString().equals("N") && e.isControlDown()) {
                primaryStage.setTitle("Nouveau Projet (Non Sauvegardé)");
                System.out.println("Nouveau Projet");
                modele.setProjet(new Projet(modele.getProjet().getNomProjet()));
                modele.notifierObservateur();
            }

            // Sauvegarder (Ctrl + S)
            if (e.getCode().toString().equals("S") && e.isControlDown()) {
                System.out.println("Sauvegarder Projet");
                ControlSauvegarde cs = new ControlSauvegarde(modele,primaryStage);
                cs.handle(new ActionEvent());
            }

            // Ouvrir (Ctrl + O)
            if (e.getCode().toString().equals("O") && e.isControlDown()) {
                System.out.println("Ouvrir Projet");
                ControlCharger cc = new ControlCharger(modele,primaryStage);
                cc.handle(new ActionEvent());
            }

            // Créer une liste (Ctrl + L)
            if (e.getCode().toString().equals("L") && e.isControlDown()) {
                System.out.println("Créer une liste");
                ControlAjouterListe cal = new ControlAjouterListe(modele);
                cal.handle(new ActionEvent());
            }
        });
    }
}
