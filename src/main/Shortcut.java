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
import main.exceptions.ProjectNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shortcut implements EventHandler<ActionEvent> {

    private Modele modele;
    private Stage primaryStage;

    private BorderPane layout;

    public Shortcut(Modele modele, Stage primaryStage, BorderPane layout) {
        this.modele = modele;
        this.primaryStage = primaryStage;
        this.layout = layout;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // Raccourcis Clavier
        layout.setOnKeyPressed(e -> {

            // Mode Plein Ecran (F5)
            if(e.getCode().toString().equals("F5")) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }

            // Changer de fond d'écran (Ctrl + B)
            if(e.getCode().toString().equals("B") && e.isControlDown()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                fileChooser.setInitialDirectory(new File("./backgrounds/"));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if(selectedFile != null) {
                    Image image = new Image(selectedFile.getPath());
                    BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
                    // si l'image est plus grande que la fenêtre, on la redimensionne
                    if(image.getWidth() > layout.getWidth()|| image.getHeight() > layout.getHeight()) {
                        backgroundImage = new BackgroundImage(image, null, null, null, new BackgroundSize(1,1,false,false,true,true));
                    }
                    layout.setBackground(new Background(backgroundImage));
                }
            }

            // Nouveau Projet (Ctrl + N)
            if(e.getCode().toString().equals("N") && e.isControlDown()) {
                primaryStage.setTitle("Nouveau Projet (Non Sauvegardé)");
                System.out.println("Nouveau Projet");
                modele.setProjet(new Projet(modele.getProjet().getNomProjet()));
                modele.notifierObservateur();
            }

            // Sauvegarder (Ctrl + S)
            if(e.getCode().toString().equals("S") && e.isControlDown()) {
                System.out.println("Sauvegarder Projet");
                try {
                    if(modele.getProjet().getChemin() != null) modele.sauvegarderProjet(modele.getProjet().getChemin());
                    else{
                        if(!Files.exists(Paths.get("./projects/"))) Files.createDirectories(Paths.get("./projects"));
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save As");
                        fileChooser.setInitialDirectory(new File("./projects/"));
                        if(modele.getProjet().getNomProjet() != null){
                            fileChooser.setInitialFileName(modele.getProjet().getNomProjet() + ".trebo");
                        }
                        else fileChooser.setInitialFileName("untitled.trebo");
                        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Trebo Files", "*.trebo"));
                        File selectedFile = fileChooser.showSaveDialog(primaryStage);
                        if(selectedFile != null){
                            modele.sauvegarderProjet(selectedFile.getPath());
                            primaryStage.setTitle(selectedFile.getName());
                        }
                    }
                    modele.notifierObservateur();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            // Ouvrir (Ctrl + O)
            if(e.getCode().toString().equals("O") && e.isControlDown()) {
                System.out.println("Ouvrir Projet");
                try {
                    if(!Files.exists(Paths.get("./projects/"))) Files.createDirectories(Paths.get("./projects"));
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Project File");
                    fileChooser.setInitialDirectory(new File("./projects"));
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Trebo Files", "*.trebo"));
                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                    if(selectedFile != null){
                        modele.chargerProjet(selectedFile.getPath());
                        primaryStage.setTitle(selectedFile.getName());
                    }
                    modele.notifierObservateur();
                } catch (IOException | ProjectNotFoundException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            // Créer une liste (Ctrl + L)
            if(e.getCode().toString().equals("L") && e.isControlDown()) {
                System.out.println("Créer une liste");
                ControlAjouterListe cal = new ControlAjouterListe(modele);
                cal.handle(new ActionEvent());
            }

        });
    }
}
