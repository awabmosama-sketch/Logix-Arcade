package com.example.oopprojectjavafx.Timer_Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;




    public void SwitchToScene1(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TimeGame.class.getResource("/com/example/oopprojectjavafx/Timer_Game/Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(),600,600);
        stage.setScene(scene);
        stage.show();

    }

    public void SwitchToScene2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TimeGame.class.getResource("/com/example/oopprojectjavafx/Timer_Game/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(),600,600);
        stage.setScene(scene);
        stage.show();

    }

    public void SwitchToScene3(ActionEvent event, String score) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TimeGame.class.getResource("/com/example/oopprojectjavafx/Timer_Game/Scene3.fxml"));
        Parent root=fxmlLoader.load();
        Scene3Controller scenee = fxmlLoader.getController();
        scenee.setScore(score);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,600);
        stage.setScene(scene);
        stage.show();

    }


}
