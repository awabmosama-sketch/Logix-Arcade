package com.example.oopprojectjavafx.HangMan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class HangGame extends Application {
    private ArrayList<String> wordList;
    private String secretWords;
    private HashSet<Character> guessedCharacters;
    private int wrongGuesses;

    private Label wordLabel;
    private Label guessedLabel;
    private Label attempsLabel;
    private Label messageLabel;
    private TextField guessWord;
    private Button guessButton;
    private Button restartButton;
    private Button fullScreenButton;
    private Canvas hangmanCanvas;

    @Override
    public void start(Stage stage) {
        stage.setTitle("JavaFX Hang Man");
        stage.getIcons().add(
                new Image(getClass().getResource("/com/example/oopprojectjavafx/images/HangLogo.jpg").toExternalForm())
        );
        loadWordsFromFile();
        startNewGame();
        buildUI(stage);
    }

    private void loadWordsFromFile() {
        wordList = new ArrayList<>();
        try {
            File file = new File("src/main/java/com/example/oopprojectjavafx/HangMan/words.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String word = sc.nextLine().trim().toUpperCase();
                if (!word.isEmpty()) {
                    wordList.add(word);
                }
            }
            sc.close();
            if (wordList.isEmpty()) {
                wordList.add("JAVA");
                wordList.add("PROGRAM");
                wordList.add("COMPUTER");
            }
        } catch (Exception e) {
            System.out.println("words.txt not found. Using default words.");
            wordList.add("JAVA");
            wordList.add("PYTHON");
            wordList.add("HANGMAN");
            wordList.add("COMPUTER");
            wordList.add("GAME");
        }
    }

    private void startNewGame() {
        Random rand = new Random();
        int index = rand.nextInt(wordList.size());
        secretWords = wordList.get(index);
        guessedCharacters = new HashSet<>();
        wrongGuesses = 0;
    }

    private void makeGuess() {
        if (wrongGuesses >= 6) {
            messageLabel.setText("GAME OVER");
            return;
        }
        if (isWordGuessed()) {
            messageLabel.setText("YOU WON! Press Restart to play again.");
            return;
        }
        String input = guessWord.getText().trim();
        guessWord.clear();
        if (input.isEmpty()) {
            messageLabel.setText("Please enter a letter");
            return;
        }
        char letter = Character.toUpperCase(input.charAt(0));
        if (!Character.isLetter(letter)) {
            messageLabel.setText("Only letters A-Z are allowed");
            return;
        }
        if (guessedCharacters.contains(letter)) {
            messageLabel.setText("You already guessed '" + letter + "'. Try a different letter.");
            return;
        }
        guessedCharacters.add(letter);
        if (secretWords.indexOf(letter) >= 0) {
            messageLabel.setText("Good guess! '" + letter + "' is in the word.");
        } else {
            wrongGuesses++;
            messageLabel.setText("Wrong! '" + letter + "' is not in the word. " + (6 - wrongGuesses) + " attempts left.");
        }
        updateDisplay();
        if (isWordGuessed()) {
            messageLabel.setText("CONGRATULATIONS! YOU WIN! The word was " + secretWords + ".");
            guessButton.setDisable(true);
            guessWord.setDisable(true);
        }
        if (wrongGuesses >= 6) {
            messageLabel.setText("GAME OVER! The word was: " + secretWords + ".");
            guessButton.setDisable(true);
            guessWord.setDisable(true);
        }
    }

    private boolean isWordGuessed() {
        for (int i = 0; i < secretWords.length(); i++) {
            char c = secretWords.charAt(i);
            if (!guessedCharacters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private void updateDisplay() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < secretWords.length(); i++) {
            char c = secretWords.charAt(i);
            if (guessedCharacters.contains(c)) {
                display.append(c).append(" ");
            } else {
                display.append("_ ");
            }
        }
        wordLabel.setText(display.toString().trim());
        StringBuilder guessedStr = new StringBuilder("Guessed Letters: ");
        for (char ch : guessedCharacters) {
            guessedStr.append(ch).append(" ");
        }
        guessedLabel.setText(guessedStr.toString().trim());
        int attemptsLeft = 6 - wrongGuesses;
        attempsLabel.setText("Attempts left: " + attemptsLeft);
        drawHangman();
    }

    private void drawHangman() {
        GraphicsContext gc = hangmanCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, hangmanCanvas.getWidth(), hangmanCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        // gallows
        gc.strokeLine(40, 180, 160, 180);
        gc.strokeLine(100, 180, 100, 20);
        gc.strokeLine(100, 20, 150, 20);
        gc.strokeLine(150, 20, 150, 40);
        // body parts
        if (wrongGuesses >= 1) gc.strokeOval(140, 40, 20, 20);
        if (wrongGuesses >= 2) gc.strokeLine(150, 60, 150, 110);
        if (wrongGuesses >= 3) gc.strokeLine(150, 70, 130, 90);
        if (wrongGuesses >= 4) gc.strokeLine(150, 70, 170, 90);
        if (wrongGuesses >= 5) gc.strokeLine(150, 110, 130, 140);
        if (wrongGuesses >= 6) gc.strokeLine(150, 110, 170, 140);
    }

    private void restartGame() {
        startNewGame();
        guessedCharacters.clear();
        wrongGuesses = 0;
        guessButton.setDisable(false);
        guessWord.setDisable(false);
        guessWord.clear();
        messageLabel.setText("");
        updateDisplay();
    }

    private void toggleFullScreen(Stage stage) {
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void buildUI(Stage stage) {
        wordLabel = new Label();
        wordLabel.setFont(Font.font("Courier New", 28));
        wordLabel.setStyle("-fx-border-color: black; -fx-padding: 10;");

        guessedLabel = new Label("Guessed letters: ");
        attempsLabel = new Label("Attempts left: 6");
        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        guessWord = new TextField();
        guessWord.setPromptText("Enter a letter");
        guessWord.setPrefWidth(100);
        guessButton = new Button("Guess");
        restartButton = new Button("Restart");
        fullScreenButton = new Button("Full Screen");

        guessButton.setOnAction(e -> makeGuess());
        guessWord.setOnAction(e -> makeGuess());
        restartButton.setOnAction(e -> restartGame());
        fullScreenButton.setOnAction(e -> toggleFullScreen(stage));

        hangmanCanvas = new Canvas(200, 200);
        drawHangman();

        VBox centerBox = new VBox(15, wordLabel, hangmanCanvas);
        centerBox.setStyle("-fx-alignment: center;");

        HBox controlBox = new HBox(10, guessWord, guessButton, restartButton, fullScreenButton);
        controlBox.setStyle("-fx-alignment: center;");

        VBox bottomBox = new VBox(10, guessedLabel, attempsLabel, controlBox, messageLabel);
        bottomBox.setStyle("-fx-alignment: center; -fx-padding: 10;");

        VBox mainContent = new VBox(20, centerBox, bottomBox);
        mainContent.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Wrap in a BorderPane to keep content centered when window is resized
        BorderPane root = new BorderPane();
        root.setCenter(mainContent);
        root.setStyle("-fx-background-color: #f0f0f0;");

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();

        // Press F11 to toggle full screen
        scene.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("F11")) {
                toggleFullScreen(stage);
            }
        });

        updateDisplay();
    }

    public static void main(String[] args) {
        launch(args);
    }
}