package com.example.oopprojectjavafx.TicTacToe;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TicTacToe extends Application {
    private char currentPlayer = 'X';
    private char[][] board = new char[3][3];
    private Button[][] buttons = new Button[3][3];
    private Label status;

    @Override
    public void start(Stage stage) {
        Image icon = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/TTTlogo.png").toExternalForm());
        stage.getIcons().add(icon);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = new Button(" ");
                btn.setPrefSize(120, 120);
                btn.setStyle("-fx-font-size: 48; -fx-font-weight: bold;");
                int row = i, col = j;
                btn.setOnAction(e -> move(row, col));
                grid.add(btn, j, i);
                buttons[i][j] = btn;
                board[i][j] = ' ';
            }
        }

        status = new Label("Player X's turn");
        status.setStyle("-fx-font-size: 24; -fx-padding: 20;");

        Button resetBtn = new Button("Restart");
        resetBtn.setStyle("-fx-font-size: 16; -fx-padding: 8 16;");
        resetBtn.setOnAction(e -> resetGame());

        VBox content = new VBox(20, status, grid, resetBtn);
        content.setAlignment(Pos.CENTER);

        // Center everything on a dark background
        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: #2c3e50;");

        Scene scene = new Scene(root);
        stage.setTitle("Tic Tac Toe");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("Press ESC to exit full screen");
        stage.setScene(scene);
        stage.show();
    }

    private void move(int row, int col) {
        if (board[row][col] != ' ') return;

        board[row][col] = currentPlayer;
        buttons[row][col].setText(String.valueOf(currentPlayer));

        if (checkWin()) {
            status.setText("Player " + currentPlayer + " wins!");
            disableBoard();
        } else if (isDraw()) {
            status.setText("Draw!");
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            status.setText("Player " + currentPlayer + "'s turn");
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) return true;
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) return true;
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) return true;
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) return true;
        return false;
    }

    private boolean isDraw() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') return false;
        return true;
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setDisable(true);
    }

    private void resetGame() {
        currentPlayer = 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText(" ");
                buttons[i][j].setDisable(false);
            }
        }
        status.setText("Player X's turn");
    }

    public static void main(String[] args) {
        launch(args);
    }
}