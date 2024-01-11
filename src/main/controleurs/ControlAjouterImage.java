package main.controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import main.Modele;

import java.io.File;

public class ControlAjouterImage implements EventHandler<ActionEvent> {

    /*
     * modele
     */
    private final Modele modele;

    /**
     * Constructeur de ControlAjouterImage
     *
     * @param modele modele
     */
    public ControlAjouterImage(Modele modele){
        this.modele = modele;
    }

    /**
     * Methode qui permet d'ajouter une image a une tache
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            modele.getCurrentTache().setImage(selectedFile.toURI().toString());
            modele.notifierObservateur();
        }
    }
}