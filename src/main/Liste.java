package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe Liste qui permet de créer une liste de tâches
 * @see Composant
 */
public class Liste implements Serializable {

    /**
     * Nom de la liste
     */
    private String nom;

    /**
     * Liste des composants de la liste
     */
    private final List<Composant> composants;


    /**
     * Constructeur de la classe Liste
     * @param nom Nom de la liste
     */
    public Liste(String nom) {
        this.nom = nom;
        composants = new ArrayList<Composant>();
    }

    /**
     * Ajoute un composant à la liste
     * @param c Composant à ajouter
     */
    public void ajouterComposant(Composant c) {
        this.composants.add(c);
    }

    /**
     * Retire un composant de la liste
     * @param c Composant à retirer
     */
    public void retirerComposant(Composant c) {
        this.composants.remove(c);
    }

    /**
     * Affiche la liste
     * @param modele Modèle de l'application
     * @return VBox contenant la liste
     */
    public VBox afficher(Modele modele) {

        // Liste de tâches
        VBox paneListe = new VBox();
        paneListe.setId(this.nom);

        // Informations de la liste
        HBox infosListe = new HBox();
        infosListe.setAlignment(Pos.CENTER);
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        infosListe.setPadding(new Insets(10, 10, 2, 10));

        Text textNom = new Text(this.nom);
        textNom.setStyle("" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;");
        Button more = new Button("...");
        more.getStyleClass().add("btnTransparent");
        more.setAlignment(Pos.CENTER);
        infosListe.getChildren().addAll(textNom, r, more);
        paneListe.getChildren().add(infosListe);

        for (Composant c : composants) {
            if(!c.getEstArchive()) {
                TreeView<Composant> treeView = new TreeView<>(c.afficher(modele));
                TreeViewActions.addTreeAction(modele, treeView);
                paneListe.getChildren().add(treeView);
            }
        }

        // Menu de Contexte de la liste
        ContextMenu contextMenu = new ContextMenu();

        MenuItem contextMenuRL = new MenuItem("Renommer la liste");
        ImageView contextMenuRLImage = new ImageView(new Image("file:icons/crayon.png"));
        contextMenuRLImage.setFitHeight(16);
        contextMenuRLImage.setFitWidth(16);
        contextMenuRL.setGraphic(contextMenuRLImage);

        contextMenuRL.setOnAction(e -> {
            textNom.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false,
                    false, false, false, false, false, false, null));

        });

        MenuItem contextMenuSL = new MenuItem("Supprimer la liste");
        ImageView contextMenuSLImage = new ImageView(new Image("file:icons/trashRed.png"));
        contextMenuSLImage.setFitHeight(16);
        contextMenuSLImage.setFitWidth(16);
        contextMenuSL.setGraphic(contextMenuSLImage);

        contextMenuSL.setOnAction(e -> {
            modele.getProjet().supprimerListeTaches(this);
            modele.notifierObservateur();
        });
        contextMenu.getItems().addAll(contextMenuRL, contextMenuSL);

        more.setOnMouseClicked(e -> {
            contextMenu.show(more, e.getScreenX(), e.getScreenY());
        });

        paneListe.setOnDragDropped(new ControlOnDragDroppedListe(modele));
        paneListe.setOnDragOver(new ControlOnDragOver(modele));

        textNom.setOnMouseClicked(e -> {
            ControlChangerTitreListe controlChangerTitreListe = new ControlChangerTitreListe(modele, this);
            controlChangerTitreListe.handle(e);
        });

        return paneListe;
    }

    public HBox afficherListe(Modele modele) {

        // Liste de tâches
        HBox paneListe = new HBox();
        paneListe.setId(this.nom);
        paneListe.setAlignment(Pos.CENTER_LEFT);

        // Informations de la liste
        HBox infosListe = new HBox();
        infosListe.setAlignment(Pos.CENTER);
        infosListe.setSpacing(10);

        Text textNom = new Text(this.nom);
        textNom.setStyle("" +
                "-fx-cursor: hand;" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;");
        Button more = new Button("...");
        more.getStyleClass().add("btnTransparent");
        more.setAlignment(Pos.CENTER);
        infosListe.getChildren().addAll(textNom, more);
        paneListe.getChildren().add(infosListe);

        for (Composant c : composants) {
            if(!c.getEstArchive()) {
                TreeView<Composant> treeView = new TreeView<>(c.afficher(modele));
                treeView.setMinWidth(300);
                TreeViewActions.addTreeAction(modele, treeView);

                paneListe.getChildren().add(treeView);
            }
        }

        // Menu de Contexte de la liste
        ContextMenu contextMenu = new ContextMenu();

        MenuItem contextMenuRL = new MenuItem("Renommer la liste");
        ImageView contextMenuRLImage = new ImageView(new Image("file:icons/crayon.png"));
        contextMenuRLImage.setFitHeight(16);
        contextMenuRLImage.setFitWidth(16);
        contextMenuRL.setGraphic(contextMenuRLImage);

        contextMenuRL.setOnAction(e -> {
            textNom.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false,
                    false, false, false, false, false, false, null));

        });

        MenuItem contextMenuSL = new MenuItem("Supprimer la liste");
        ImageView contextMenuSLImage = new ImageView(new Image("file:icons/trashRed.png"));
        contextMenuSLImage.setFitHeight(16);
        contextMenuSLImage.setFitWidth(16);
        contextMenuSL.setGraphic(contextMenuSLImage);

        contextMenuSL.setOnAction(e -> {
            modele.getProjet().supprimerListeTaches(this);
            modele.notifierObservateur();
        });
        contextMenu.getItems().addAll(contextMenuRL, contextMenuSL);

        more.setOnMouseClicked(e -> {
            contextMenu.show(more, e.getScreenX(), e.getScreenY());
        });

        paneListe.setOnDragDropped(new ControlOnDragDroppedListe(modele));
        paneListe.setOnDragOver(new ControlOnDragOver(modele));

        textNom.setOnMouseClicked(e -> {
            ControlChangerTitreListe controlChangerTitreListe = new ControlChangerTitreListe(modele, this);
            controlChangerTitreListe.handle(e);
        });

        return paneListe;
    }

    @Override
    public String toString() {
        return "Liste{" +
                "nom='" + nom + '\'' +
                ", composants=" + composants +
                '}';
    }

    // #########################
    // ### GETTERS & SETTERS ###
    // #########################

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Liste liste = (Liste) o;
        boolean res = true;
        for(int i = 0; i < Math.max(getComposants().size(), liste.getComposants().size()); i++) {
            try{
                res = res && getComposants().get(i).equals(liste.getComposants().get(i));
            }
            catch (IndexOutOfBoundsException e){
                return false;
            }
        }
        return Objects.equals(nom, liste.nom) && res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Composant> getComposants() {
        return this.composants;
    }
}
