package com.example.oopprojectjavafx.FlappyBird;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Game extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oopprojectjavafx/FlappyBird.fxml"));
            Parent root = loader.load();


            Image icon = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/BirdLogo.jpeg").toExternalForm());
            primaryStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Flappy Bird");
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("Press ESC to exit full screen");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}