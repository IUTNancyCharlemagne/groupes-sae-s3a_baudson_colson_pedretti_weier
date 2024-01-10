package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Liste;
import main.Modele;
import main.Projet;
import main.Tag;
import main.composite.Composant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ControlSauvegarde implements EventHandler<ActionEvent> {
    Modele modele;
    Stage primaryStage;

    public ControlSauvegarde(Modele modele, Stage primaryStage) {
        this.modele = modele;
        this.primaryStage = primaryStage;
    }

    /**
     * Crée un fichier de sauvegarde du bureau.
     * Le fichier est un fichier binaire de format '.trebbo'
     * Le nom du fichier est nomFichier + '.trebbo'
     * L'emplacement du fichier se trouve dans le dossier /projects/.
     *
     * @param chemin Nom du fichier voulu.
     *               Peut se terminer par '.trebbo' ou non, la conversion est faite.
     * @throws IOException
     */
    public void sauvegarderProjet(String chemin) throws IOException {
        if (!Projet.fichierTrebbo(chemin)) chemin += ".trebbo";
        if (modele.getProjet().getChemin() == null) modele.getProjet().setChemin(chemin);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modele.getProjet().getChemin()))) {
            oos.writeObject(modele.getProjet());
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            if (modele.getProjet().getChemin() != null) sauvegarderProjet(modele.getProjet().getChemin());
            else {
                if (!Files.exists(Paths.get("./projects/"))) Files.createDirectories(Paths.get("./projects"));
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save As");
                fileChooser.setInitialDirectory(new File("./projects/"));
                if (modele.getProjet().getNomProjet() != null)
                    fileChooser.setInitialFileName(modele.getProjet().getNomProjet() + ".trebbo");
                else fileChooser.setInitialFileName("untitled.trebbo");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Trebbo Files", "*.trebbo"));
                File selectedFile = fileChooser.showSaveDialog(this.primaryStage);
                if (selectedFile != null) {
                    sauvegarderProjet(selectedFile.getPath());
                    String nomProjet = selectedFile.getName().split("\\.")[0];
                    modele.getProjet().setNomProjet(nomProjet);
                    primaryStage.setTitle(nomProjet);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}