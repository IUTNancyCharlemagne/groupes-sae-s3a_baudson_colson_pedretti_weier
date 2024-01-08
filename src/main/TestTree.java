package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Callback;
import main.composite.Composant;
import main.composite.Tache;
import main.controleurs.ControlAfficherTache;

public class TestTree {

    public static void addTreeAction(Modele modele, TreeView<Tache> treeView) {
        treeView.setPrefHeight(treeView.getExpandedItemCount() * 25);
        treeView.getRoot().expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                treeView.setPrefHeight(treeView.getExpandedItemCount() * 25);
            }
        });
        for (Object item : treeView.getRoot().getChildren()) {
            ((TreeItem) item).expandedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    treeView.setPrefHeight(treeView.getExpandedItemCount() * 25);
                }
            });
        }

        treeView.setCellFactory(new Callback<TreeView<Tache>, TreeCell<Tache>>() {
            @Override
            public TreeCell call(TreeView treeView) {
                TreeCell<Tache> cell = new TreeCell<Tache>() {
                    @Override
                    protected void updateItem(Tache tache, boolean empty) {
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
                        // Click droit
                        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                            // Création d'un menu contextuel
                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem item1 = new MenuItem("Ajouter sous-tâche");
                            item1.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    Tache tache = new Tache("Sous-tâche " + modele.getProjet().getListeTaches().size(), "", 0);
                                    TreeItem<Tache> treeItem = new TreeItem<>(tache);
                                    cell.getTreeItem().getChildren().add(treeItem);
                                    treeView.setPrefHeight(treeView.getExpandedItemCount() * 25);
                                }
                            });
                            contextMenu.getItems().add(item1);
                            cell.setContextMenu(contextMenu);
                        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                            // Click gauche

                            // Double click
                            if (mouseEvent.getClickCount() != 2) return;
                            Tache tache = cell.getTreeItem().getValue();
                            String nomTache = tache.getNom();
                            Composant composantAfficher = null;
                            boolean trouve = false;

                            // Recherche de la tache
                            for (Liste liste : modele.getProjet().getListeTaches()) {
                                for (Composant composant : liste.getComposants()) {
                                    // Si la tache est trouvee
                                    if (composant.getNom().equals(nomTache) && !trouve) {
                                        composantAfficher = composant;
                                        trouve = true;
                                    } else if (!trouve) {
                                        // Recherche dans les sous-taches
                                        if (composant instanceof Tache) {
                                            Tache t = (Tache) composant;
                                            for (Composant sousTache : t.getSousTaches()) {
                                                if (sousTache.getNom().equals(nomTache)) {
                                                    composantAfficher = sousTache;
                                                    trouve = true;
                                                }
                                            }
                                        }
                                    }
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
                        TreeItem<Tache> item = cell.getTreeItem();
                        // item = TreeItem qui a été drag and drop avec sous-tâches

                        if (item != null) {
                            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent content = new ClipboardContent();
                            System.out.println("----------------------------------------------------");
                            System.out.println("item : " + item.getValue().getNom());
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

                        Tache tache = modele.getProjet().getTache(db.getString());

                        // Affichage de la liste de tâches
                        System.out.println("listeTaches : " + modele.getProjet().getListeTaches());
                        System.out.println("sous-tache : " + tache.getSousTaches());

                        System.out.println("tache : " + tache);

                        TreeItem<Tache> draggedItem = new TreeItem<>(tache);

                        System.out.println("draggedItem : " + draggedItem);
                        System.out.println("Est une sous-tache : " + tache.estUneSousTache(modele));

                        // Si la tache est une sous-tache
                        if (tache.estUneSousTache(modele)) {
                            // Suppression de la tâche de la tache parent
                            // TODO

                            // Tache dans laquelle on veut ajouter la tâche
                            TreeItem<Tache> item = cell.getTreeItem();
                            Tache tacheCible = item.getValue();
//                            item.getChildren().add(draggedItem);
                            System.out.println("item cible : " + item);
                            System.out.println("tache cible : " + tacheCible);

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
