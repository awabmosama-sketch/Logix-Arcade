package com.example.oopprojectjavafx.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GridPane chessBoard = new GridPane();
        ChessBoardController controller = new ChessBoardController();
        controller.chessBoard = chessBoard;
        controller.initialize();

        AnchorPane root = new AnchorPane(chessBoard);
        AnchorPane.setTopAnchor(chessBoard, 0.0);
        AnchorPane.setBottomAnchor(chessBoard, 0.0);
        AnchorPane.setLeftAnchor(chessBoard, 0.0);
        AnchorPane.setRightAnchor(chessBoard, 0.0);

        Scene scene = new Scene(root, 640, 640);
        stage.setTitle("JavaFX Chess");
        stage.getIcons().add(
                new Image(getClass().getResource("/com/example/oopprojectjavafx/images/chessLogo2.png").toExternalForm())
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}