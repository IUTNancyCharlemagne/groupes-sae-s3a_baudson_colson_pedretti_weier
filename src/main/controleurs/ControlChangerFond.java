package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Modele;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ControlChangerFond implements EventHandler<ActionEvent> {

    /**
     * Le modele
     */
    Modele modele;

    /**
     * Le stage
     */
    Stage primaryStage;

    /**
     * Le layout principal
     */
    Pane layout;

    /**
     * Constructeur de ControlChangerFond
     *
     * @param modele       le modele
     * @param primaryStage le stage
     * @param layout       le layout principal
     */
    public ControlChangerFond(Modele modele, Stage primaryStage, Pane layout) {
        this.modele = modele;
        this.primaryStage = primaryStage;
        this.layout = layout;
    }

    /**
     * Methode qui permet de changer le fond de la fenetre
     *
     * @param actionEvent l'evenement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.setInitialDirectory(new File("./backgrounds/"));
        if (Files.exists(Paths.get("./params/background.txt"))) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("./params/background.txt"))) {
                String cheminFond = bufferedReader.readLine();
                if (cheminFond != null && Files.exists(Paths.get(cheminFond))) {
                    File chemin = new File(cheminFond);
                    while (!chemin.isDirectory()) chemin = new File(chemin.getParent());
                    fileChooser.setInitialDirectory(chemin);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                changerFond(selectedFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changerFond(String pathString) throws IOException {
        Image image = new Image(pathString);
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        // si l'image est plus grande que la fenêtre, on la redimensionne
        if (image.getWidth() > layout.getWidth() || image.getHeight() > layout.getHeight()) {
            backgroundImage = new BackgroundImage(image, null, null, null, new BackgroundSize(1, 1, false, false, true, true));
        }
        layout.setBackground(new Background(backgroundImage));
        changeParam(pathString);
    }

    public void changeParam(String stringPath) throws IOException {
        Path path = Paths.get("./params");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        PrintWriter out = new PrintWriter("./params/background.txt");
        out.println(stringPath);
        out.close();
    }
}
