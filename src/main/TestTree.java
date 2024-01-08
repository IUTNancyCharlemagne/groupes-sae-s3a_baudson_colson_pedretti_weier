package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;
import main.composite.Composant;
import main.composite.Tache;

public class TestTree {

    private static final int HEIGHT = 31;

    public static void addTreeAction(Modele modele, TreeView<Composant> treeView) {
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

                cell.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            // Click gauche

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
                            modele.notifierObservateur();

                            dragEvent.setDropCompleted(true);
                            dragEvent.consume();
                        } else {
                            dragEvent.setDropCompleted(false);
                            dragEvent.consume();
                        }
                    }
                });
                cell.setOnDragOver(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent dragEvent) {
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                        dragEvent.consume();
                    }
                });

                return cell;
            }
        });
    }
}
