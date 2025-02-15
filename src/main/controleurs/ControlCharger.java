package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Modele;
import main.Projet;
import main.exceptions.ProjectNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ControlCharger implements EventHandler<ActionEvent> {
    /**
     * Le modele
     */
    private final Modele modele;

    /**
     * Le stage
     */
    private final Stage primaryStage;

    /**
     * Constructeur de ControlCharger
     * @param modele le modele
     * @param primaryStage le stage
     */
    public ControlCharger(Modele modele, Stage primaryStage){
        this.modele = modele;
        this.primaryStage = primaryStage;
    }

    /**
     * Charge un projet à partir d'un fichier de sauvegarde
     *
     * @param chemin Chemin du fichier de sauvegarde
     * @return Projet chargé
     * @throws IOException
     * @throws ProjectNotFoundException si le projet spécifié est introuvable.
     * @throws ClassNotFoundException
     */
    public Projet chargerProjet(String chemin) throws IOException, ProjectNotFoundException, ClassNotFoundException {
        if (!Projet.fichierTrebbo(chemin)) chemin += ".trebbo";
        if (!Files.exists(Paths.get(chemin))) throw new ProjectNotFoundException();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chemin))) {
            return (Projet) ois.readObject();
        }
    }

    /**
     * Methode qui permet de charger un projet
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            if(!Files.exists(Paths.get("./projects/"))) Files.createDirectories(Paths.get("./projects"));
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Project File");
            fileChooser.setInitialDirectory(new File("./projects"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Trebbo Files", "*.trebbo"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile != null){
                modele.setProjet(chargerProjet(selectedFile.getPath()));
                String nomProjet = selectedFile.getName().split("\\.")[0];
                modele.getProjet().setNomProjet(nomProjet);
                primaryStage.setTitle(nomProjet);
            }
            modele.notifierObservateur();
        } catch (IOException | ProjectNotFoundException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}