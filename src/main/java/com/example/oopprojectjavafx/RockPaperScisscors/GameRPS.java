package com.example.oopprojectjavafx.RockPaperScisscors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameRPS extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Double-check this path matches where your RPS.fxml is physically located
        FXMLLoader fxmlLoader = new FXMLLoader(GameRPS.class.getResource("/com/example/oopprojectjavafx/RPS.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("StonePaperScissors");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/example/oopprojectjavafx/images/RPSLogo.jpg")).toExternalForm()));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        // It's best practice to pass the 'args' array to launch
        launch(args);
    }
}