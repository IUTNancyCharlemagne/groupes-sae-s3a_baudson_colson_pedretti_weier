package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.controleurs.ControlAjouterListe;
import main.controleurs.ControlChangerFond;

public class MenuContext implements EventHandler<ActionEvent> {

    /**
     * Modele
     */
    private Modele modele;
    /**
     * Layout
     */
    private BorderPane layout;
    /**
     * Stage
     */
    private Stage primaryStage;

    /**
     * Constructeur
     * @param modele Modele
     * @param layout Layout
     * @param primaryStage Stage
     */
    public MenuContext(Modele modele, BorderPane layout, Stage primaryStage) {
        this.modele = modele;
        this.layout = layout;
        this.primaryStage = primaryStage;
    }

    /**
     * Gestion du menu contextuel
     * @param actionEvent ActionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem contextMenuItem = new MenuItem("Ajouter une liste (Ctrl + L)");
        ImageView contextImage = new ImageView("file:icons/plus.png");
        contextImage.setFitHeight(16);
        contextImage.setFitWidth(16);
        contextMenuItem.setGraphic(contextImage);

        EventHandler<ActionEvent> changeBackgroundEvent = e -> {
            ControlChangerFond cf = new ControlChangerFond(modele, primaryStage, layout);
            cf.handle(new ActionEvent());
        };

        MenuItem contextMenuItem2 = new MenuItem("Changer le fond (Ctrl + B)");
        ImageView changeBackgroundImage = new ImageView("file:icons/background.png");
        changeBackgroundImage.setFitHeight(16);
        changeBackgroundImage.setFitWidth(16);
        contextMenuItem2.setGraphic(changeBackgroundImage);
        contextMenuItem2.setOnAction(changeBackgroundEvent);

        contextMenu.getItems().add(contextMenuItem);
        contextMenu.getItems().add(contextMenuItem2);

        layout.setOnContextMenuRequested(e -> {
            Node Source = (Node) e.getSource();

            contextMenu.show(Source, e.getScreenX(), e.getScreenY());

        });

        layout.setOnMouseClicked(e -> {
            contextMenu.hide();
        });

        contextMenuItem.setOnAction(new ControlAjouterListe(modele));
    }
}
