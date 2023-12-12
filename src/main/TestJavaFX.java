package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {

        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setStyle(
                "-fx-background-color: #333333;");
        pane.setPadding(new Insets(20, 40, 20, 40));

        Text text = new Text(20, 20, "Pedretti au tableau".toUpperCase());
        text.setStyle(
                "-fx-font-size: 25px;" +
                "-fx-font-family: Arial;" +
                "-fx-font-weight: bold;");
        text.setFill(Color.WHITE);

        pane.getChildren().add(text);

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Test JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
