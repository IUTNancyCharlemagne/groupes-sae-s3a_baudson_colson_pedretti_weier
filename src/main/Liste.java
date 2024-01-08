package main;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
     * Nombre de tâches dans la liste
     */
    private int nbTaches; // SERT POUR LA SAUVEGARDE


    /**
     * Constructeur de la classe Liste
     * @param nom Nom de la liste
     */
    public Liste(String nom) {
        this.nom = nom;
        composants = new ArrayList<Composant>();
        this.nbTaches = 0;
    }

    /**
     * Ajoute un composant à la liste
     * @param c Composant à ajouter
     */
    public void ajouterComposant(Composant c) {
        this.composants.add(c);
        this.nbTaches++;
    }

    /**
     * Retire un composant de la liste
     * @param c Composant à retirer
     */
    public void retirerComposant(Composant c) {
        this.composants.remove(c);
        this.nbTaches--;
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
        infosListe.setSpacing(10);

        Text textNom = new Text(this.nom);
        Button more = new Button();
        ImageView imgMore = new ImageView(new Image("file:icons/moreW.png"));
        imgMore.setFitHeight(20);
        imgMore.setFitWidth(20);
        more.setGraphic(imgMore);
        more.getStyleClass().add("button");
        infosListe.getChildren().addAll(textNom, more);
        paneListe.getChildren().add(infosListe);

        for (Composant c : composants) {
            if(!c.getEstArchive()) {
                TreeView<Composant> treeView = new TreeView<>(c.afficher(modele));

                // TODO : à faire dans une classe à part
                TestTree.addTreeAction(modele, treeView);

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
        ImageView contextMenuSLImage = new ImageView(new Image("file:icons/trash.png"));
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

        paneListe.setOnDragDropped(new ControlOnDragDropped(modele));
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

        // Informations de la liste
        HBox infosListe = new HBox();
        infosListe.setAlignment(Pos.CENTER);
        infosListe.setSpacing(10);

        Text textNom = new Text(this.nom);
        Button more = new Button();
        ImageView imgMore = new ImageView(new Image("file:icons/moreW.png"));
        imgMore.setFitHeight(20);
        imgMore.setFitWidth(20);
        more.setGraphic(imgMore);
        more.getStyleClass().add("button");
        infosListe.getChildren().addAll(textNom, more);
        paneListe.getChildren().add(infosListe);

        for (Composant c : composants) {
            if(!c.getEstArchive()) {
                TreeView<Composant> treeView = new TreeView<>(c.afficher(modele));

                // TODO : à faire dans une classe à part
                TestTree.addTreeAction(modele, treeView);

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
        ImageView contextMenuSLImage = new ImageView(new Image("file:icons/trash.png"));
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

        paneListe.setOnDragDropped(new ControlOnDragDropped(modele));
        paneListe.setOnDragOver(new ControlOnDragOver(modele));

        textNom.setOnMouseClicked(e -> {
            ControlChangerTitreListe controlChangerTitreListe = new ControlChangerTitreListe(modele, this);
            controlChangerTitreListe.handle(e);
        });

        return paneListe;
    }

    public boolean contient(Tache t){return this.getComposants().contains(t);}

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

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Composant> getComposants() {
        return this.composants;
    }

    public int getNbTaches(){
        return this.nbTaches;
    }
}
