package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.Projet;
import main.Tag;
import main.composite.Tache;
import main.exceptions.ProjectNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ControlCharger implements EventHandler<ActionEvent> {
    private final Modele modele;
    private final Stage primaryStage;

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
            String nomProjet = (String) ois.readObject();
            Projet projet = new Projet(nomProjet);
            projet.setChemin(chemin);
            int nbListes = (int) ois.readObject();
            for (int i = 0; i < nbListes; i++) {
                Liste liste = (Liste) ois.readObject();
                for (int j = 0; j < liste.getNbTaches(); j++) {
                    Tache tache = (Tache) ois.readObject();
                    for (int k = 0; k < tache.getNbTags(); k++) {
                        tache.addTag((Tag) ois.readObject());
                    }
                }
                projet.ajouterListeTaches(liste);
            }
            return projet;
        }
    }

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
                modele.chargerProjet(selectedFile.getPath());
                primaryStage.setTitle(selectedFile.getName());
            }
            modele.notifierObservateur();
        } catch (IOException | ProjectNotFoundException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}