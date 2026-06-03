package com.example.oopprojectjavafx.MainMenu;

import com.example.oopprojectjavafx.FlappyBird.Game;
import com.example.oopprojectjavafx.HangMan.HangGame;
import com.example.oopprojectjavafx.TicTacToe.TicTacToe;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ArcadeLauncher extends Application {

    private Stage primaryStage;
    private Stage gameSelectionStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Image icon = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/LArLogo.jpeg").toExternalForm());
        primaryStage.getIcons().add(icon);

        // Title
        Label title = new Label("Logix Arcade");
        title.setStyle("-fx-font-size: 48; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Start button
        Button startBtn = new Button("Start");
        startBtn.setStyle("-fx-font-size: 24; -fx-padding: 10 30;");
        startBtn.setOnAction(e -> showGameSelection());

        // Quit button
        Button quitBtn = new Button("Quit");
        quitBtn.setStyle("-fx-font-size: 24; -fx-padding: 10 30;");
        quitBtn.setOnAction(e -> primaryStage.close());

        VBox root = new VBox(30, title, startBtn, quitBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #bdc3c7, #ecf0f1); -fx-padding: 50;");

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Logix Arcade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showGameSelection() {
        // Close the main menu (optional) or keep it in background
        // We'll open a new window for game selection
        gameSelectionStage = new Stage();
        gameSelectionStage.setTitle("Choose a Game");
        Image icon = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/LArLogo.jpeg").toExternalForm());
        gameSelectionStage.getIcons().add(icon);

        Label instruction = new Label("Select a game to play:");
        instruction.setStyle("-fx-font-size: 24; -fx-padding: 20;");

        Button hangmanBtn = new Button("Hangman");
        hangmanBtn.setPrefSize(200, 50);
        hangmanBtn.setStyle("-fx-font-size: 18;");
        hangmanBtn.setOnAction(e -> launchGame(new HangGame()));

        Button flappyBtn = new Button("Flappy Bird");
        flappyBtn.setPrefSize(200, 50);
        flappyBtn.setStyle("-fx-font-size: 18;");
        flappyBtn.setOnAction(e -> launchGame(new Game()));  // Game is the FlappyBird main class

        Button tictactoeBtn = new Button("Tic Tac Toe");
        tictactoeBtn.setPrefSize(200, 50);
        tictactoeBtn.setStyle("-fx-font-size: 18;");
        tictactoeBtn.setOnAction(e -> launchGame(new TicTacToe()));

        Button backBtn = new Button("Back to Main Menu");
        backBtn.setPrefSize(200, 50);
        backBtn.setStyle("-fx-font-size: 18;");
        backBtn.setOnAction(e -> gameSelectionStage.close());

        VBox selectionBox = new VBox(20, instruction, hangmanBtn, flappyBtn, tictactoeBtn, backBtn);
        selectionBox.setAlignment(Pos.CENTER);
        selectionBox.setStyle("-fx-background-color: #34495e; -fx-padding: 40;");

        Scene scene = new Scene(selectionBox, 400, 400);
        gameSelectionStage.setScene(scene);
        gameSelectionStage.show();
    }

    private void launchGame(Application gameApp) {
        // Close the selection window
        gameSelectionStage.close();
        // Launch the selected game in a new stage
        Stage gameStage = new Stage();
        try {
            gameApp.start(gameStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}