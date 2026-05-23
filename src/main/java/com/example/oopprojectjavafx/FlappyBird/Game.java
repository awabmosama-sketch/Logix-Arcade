package com.example.oopprojectjavafx.FlappyBird;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;


public class Game extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oopprojectjavafx/FlappyBird.fxml"));
            Parent root = loader.load();

            // Obtain the controller instance from the FXML loader
            BirdController controller = loader.getController();

            Scene scene = new Scene(root);


            URL iconUrl = getClass().getResource("/com/example/oopprojectjavafx/images/BirdLogo.jpeg");
            if (iconUrl != null) {
                primaryStage.getIcons().add(new Image(iconUrl.toExternalForm()));
            } else {
                System.out.println("Warning: Icon file not found at the specified path.");
            }
            // Global Key Listener
            scene.setOnKeyPressed(event -> controller.pressed(event));

            primaryStage.setTitle("JavaFX Flappy Bird");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}