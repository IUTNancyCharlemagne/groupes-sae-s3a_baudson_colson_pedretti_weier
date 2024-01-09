package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.ControlOnDragOver;

/**
 * Classe TreeViewActions qui ajoute les actions au TreeView
 */
public class TreeViewActions {

    /**
     * Hauteur d'une cellule
     */
    private static final int HEIGHT = 31;

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
                cell.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        // Click gauche
                        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            // Double click
                            if (mouseEvent.getClickCount() != 2) return;
                            Composant tache = cell.getTreeItem().getValue();
                            String nomTache = tache.getNom();
                            Composant composantAfficher = null;
                            boolean trouve = false;

                            // Recherche de la tache
                            for (Composant composant : modele.getProjet().getListeTouteTaches()) {
                                if (composant.getNom().equals(nomTache)) {
                                    composantAfficher = composant;
                                    trouve = true;
                                    break;
                                }
                            }

                            if (trouve) {
                                modele.setCurrentTache(composantAfficher);
                                modele.notifierObservateur();
                            }
                        }
                    }
                });

                // ### Drag and Drop ###
                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        TreeItem<Composant> item = cell.getTreeItem();
                        // item = TreeItem qui a été drag and drop avec sous-tâches

                        if (item != null) {
                            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent content = new ClipboardContent();
                            content.putString(item.getValue().getNom());
                            db.setContent(content);
                            mouseEvent.consume();
                        }
                    }
                });
                cell.setOnDragDropped(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        // Réupération de cellule qui a été drag and drop
                        Dragboard db = dragEvent.getDragboard();

                        Tache tache = (Tache) modele.getProjet().getTache(db.getString());

                        // Si la tache est une sous-tache
                        if (tache.estUneSousTache(modele)) {
                            // Suppression de la tâche de la tache parent
                            Tache tacheParent = (Tache) tache.getParent();
                            tacheParent.retirer(tache);

                            // Tache dans laquelle on veut ajouter la tâche
                            TreeItem<Composant> item = cell.getTreeItem();
                            Tache tacheCible = (Tache) item.getValue();
                            // Ajout de la tâche dans la tâche cible
                            tacheCible.ajouter(tache);
                            tacheCible.fixDuree();
                            modele.notifierObservateur();

                            dragEvent.setDropCompleted(true);
                            dragEvent.consume();
                        } else {
                            dragEvent.setDropCompleted(false);
                            dragEvent.consume();
                        }
                    }
                });
                cell.setOnDragOver(new ControlOnDragOver(modele));

                return cell;
            }
        });
    }
}
