package com.example.oopprojectjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
        HelloApplication.class.getResource("/com/example/oopprojectjavafx/hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        String CSS = HelloApplication.class.getResource("/com/example/oopprojectjavafx/style.css").toExternalForm();
        scene.getStylesheets().add(CSS);
        stage.setTitle("Logix Arcade");
        stage.setScene(scene);
        stage.show();
    }
}
