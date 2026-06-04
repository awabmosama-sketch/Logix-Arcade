package com.example.oopprojectjavafx.snake_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class SnakeGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnakeGame.class.getResource("/com/example/oopprojectjavafx/snake_game/Scene0.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("logix Arcade Snake");
        stage.getIcons().add(new Image(getClass().getResource("/com/example/oopprojectjavafx/images/SnakeLogo.png").toExternalForm()));
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }



}
