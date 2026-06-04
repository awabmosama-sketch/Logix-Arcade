package com.example.oopprojectjavafx.QuizGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class QuizGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Try Path A: Flat inside the main package
        URL fxmlLocation = QuizGame.class.getResource("/com/example/oopprojectjavafx/QUIZ.fxml");





        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/com/example/oopprojectjavafx/style.css").toExternalForm());
        stage.setTitle("Quiz");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/example/oopprojectjavafx/images/QLogo.jpg")).toExternalForm()));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}