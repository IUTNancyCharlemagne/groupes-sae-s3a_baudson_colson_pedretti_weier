package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.composite.Composant;
import main.controleurs.ControlAfficherTacheCell;
import main.controleurs.ControlOnDragDetectedCell;
import main.controleurs.ControlOnDragDroppedCell;
import main.controleurs.ControlOnDragOver;

/**
 * Classe TreeViewActions qui ajoute les actions au TreeView
 */
public class TreeViewActions {

    /**
     * Hauteur d'une cellule
     */
    private static final double HEIGHT = 29.5;

    /**
     * Méthode qui permet au TreeView d'adapter sa taille en fonction du nombre de cellules
     * @param treeView TreeView à modifier
     */
    private static void heightAdapt(TreeView<Composant> treeView) {
        treeView.setPrefHeight(treeView.getExpandedItemCount() * HEIGHT);
        treeView.getRoot().expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                treeView.setPrefHeight(treeView.getExpandedItemCount() * HEIGHT);
            }
        });
        for (Object item : treeView.getRoot().getChildren()) {
            ((TreeItem) item).expandedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    treeView.setPrefHeight(treeView.getExpandedItemCount() * HEIGHT);
                }
            });
        }
    }

    /**
     * Méthode qui ajoute les actions au TreeView
     * <ul>
     *     <li>Adapte la taille du TreeView en fonction du nombre de cellules</li>
     *     <li>Double click sur une tâche pour l'afficher</li>
     *     <li>Drag and drop d'une tâche dans une autre tâche</li>
     * </ul>
     *
     * @param modele   Modèle de l'application
     * @param treeView TreeView à modifier
     */
    public static void addTreeAction(Modele modele, TreeView<Composant> treeView) {
        // ### Adaptation de la taille du TreeView ###
        heightAdapt(treeView);

        // Ajout des actions sur les cellules
        treeView.setCellFactory(new Callback<TreeView<Composant>, TreeCell<Composant>>() {
            @Override
            public TreeCell call(TreeView treeView) {
                TreeCell<Composant> cell = new TreeCell<Composant>() {
                    @Override
                    protected void updateItem(Composant tache, boolean empty) {
                        super.updateItem(tache, empty);
                        if (tache != null) {
                            setText(tache.toString());
                            setId(tache.getNom());
                        } else {
                            setText("");
                        }
                    }
                };

                // ### Double click pour afficher une tâche ###
                cell.setOnMousePressed(new ControlAfficherTacheCell(modele, cell));

                // ### Drag and Drop ###
                cell.setOnDragDetected(new ControlOnDragDetectedCell(cell));
                cell.setOnDragDropped(new ControlOnDragDroppedCell(modele, cell));
                cell.setOnDragOver(new ControlOnDragOver(modele));

                return cell;
            }
        });
    }
}
