package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlChangerFond;

import java.io.File;

public class MenuContext implements EventHandler<ActionEvent> {

    private Modele modele;
    private EventHandler<ActionEvent> changeBackgroundEvent;
    private BorderPane layout;

    private Stage primaryStage;

    public MenuContext(Modele modele, BorderPane layout, Stage primaryStage) {
        this.modele = modele;
        this.layout = layout;
        this.primaryStage = primaryStage;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem contextMenuItem = new MenuItem("Ajouter une liste (Ctrl + L)");
        ImageView contextImage = new ImageView("file:icons/plus.png");
        contextImage.setFitHeight(16);
        contextImage.setFitWidth(16);
        contextMenuItem.setGraphic(contextImage);

        EventHandler<ActionEvent> changeBackgroundEvent = e -> {
            ControlChangerFond ccf = new ControlChangerFond(modele, primaryStage, layout);
        };

        MenuItem contextMenuItem2 = new MenuItem("Changer le fond (Ctrl + B)");
        ImageView changeBackgroundImage = new ImageView("file:icons/background.png");
        changeBackgroundImage.setFitHeight(16);
        changeBackgroundImage.setFitWidth(16);
        contextMenuItem2.setGraphic(changeBackgroundImage);
        contextMenuItem2.setOnAction(changeBackgroundEvent);

        contextMenu.getItems().add(contextMenuItem);
        contextMenu.getItems().add(contextMenuItem2);

        ContextMenu listContextMenu = new ContextMenu();
        MenuItem listContextMenuItem = new MenuItem("Ajouter une tâche");
        ImageView listContextImage = new ImageView(new Image("file:icons/plus.png"));
        listContextImage.setFitHeight(16);
        listContextImage.setFitWidth(16);
        listContextMenuItem.setGraphic(listContextImage);

        MenuItem listModifier = new MenuItem("Modifier la liste");
        ImageView listModifierImage = new ImageView(new Image("file:icons/edit.png"));
        listModifierImage.setFitHeight(16);
        listModifierImage.setFitWidth(16);
        listModifier.setGraphic(listModifierImage);

        MenuItem listSupprimer = new MenuItem("Supprimer la liste");
        ImageView listSupprimerImage = new ImageView(new Image("file:icons/trash.png"));
        listSupprimerImage.setFitHeight(16);
        listSupprimerImage.setFitWidth(16);
        listSupprimer.setGraphic(listSupprimerImage);

        listContextMenu.getItems().addAll(listModifier, listSupprimer,listContextMenuItem);


        layout.setOnContextMenuRequested(e -> {
            Node Source = (Node) e.getSource();

            contextMenu.show(Source, e.getScreenX(), e.getScreenY());

        });


        contextMenuItem.setOnAction(new ControlAjouterListe(modele));
    }
}
