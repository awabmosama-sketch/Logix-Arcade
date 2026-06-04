package com.example.oopprojectjavafx.snake_game;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Scene_Controller {
    private Stage stage;
    private Scene scene;




    public void SwitchToScene1(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnakeGame.class.getResource("Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(),600,600);
        SnakeController controller = fxmlLoader.getController();
        scene.setOnKeyPressed(keyevent-> controller.keyPress(keyevent));
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }


    public void SwitchToScene3(javafx.scene.layout.Pane referencePane, String score) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnakeGame.class.getResource("Scene2.fxml"));
        Parent root=fxmlLoader.load();
        LeaderboardsController scenee = fxmlLoader.getController();
        scenee.setScore(score);
        stage = (Stage)referencePane.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }


}
