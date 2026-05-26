package com.example.oopprojectjavafx.HangMan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;   // <-- needed for that line
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import java.awt.*;
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
    private Canvas hangmanCanvas;

    @Override
    public void start(Stage stage){
        stage.setTitle("JavaFX Hang Man");
        stage.getIcons().add(
                new Image(getClass().getResource("/com/example/oopprojectjavafx/images/HangLogo.jpg").toExternalForm())
        );
        loadWordsFromFile();

        startNewGame();

        buildUI(stage);
    }
    private void loadWordsFromFile(){
        wordList = new ArrayList<>();
        try{
            File file = new File("src/main/java/com/example/oopprojectjavafx/HangMan/words.txt");
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String word = sc.nextLine().trim().toUpperCase();
                if(!word.isEmpty()){
                    wordList.add(word);
                }
            }
            sc.close();

            if (wordList.isEmpty()){
                wordList.add("JAVA");
                wordList.add("PROGRAM");
                wordList.add("COMPUTER");
            }
        }catch (Exception e){
            System.out.println("words.txt not found. Using default words.");
            wordList.add("JAVA");
            wordList.add("PYTHON");
            wordList.add("HANGMAN");
            wordList.add("COMPUTER");
            wordList.add("GAME");
        }
    }

    private void startNewGame(){
        Random rand = new Random();
        int index =  rand.nextInt(wordList.size());
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
            messageLabel.setText("YOU WON ! , Press Any Button To Restart");
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
            messageLabel.setText("Wrong! '" + letter + "' is not the word." + (6-wrongGuesses) + " attempts left");
        }
        updateDisplay();

        if(isWordGuessed()){
            messageLabel.setText(" CONGRATULATIONS! YOU WIN! , The word was " + secretWords + ".");
            guessButton.setDisable(true);   // disable guessing after win
            guessWord.setDisable(true);
        }
        if (wrongGuesses >= 6) {
            messageLabel.setText(" GAME OVER!  The word was: " + secretWords + ".");
            guessButton.setDisable(true);
            guessWord.setDisable(true);
        }
    }
    private boolean isWordGuessed(){
        for (int i = 0; i < secretWords.length(); i++) {
            char c = secretWords.charAt(i);
            if (!guessedCharacters.contains(c)) {
                return false;
            }
        }
        return true;
    }
    private void updateDisplay(){
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < secretWords.length(); i++) {
            char c = secretWords.charAt(i);
            if (guessedCharacters.contains(c)) {
                display.append(c).append(" ");
            }else {
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

        // Draw the gallows (always visible)
        gc.strokeLine(40, 180, 160, 180);  // base
        gc.strokeLine(100, 180, 100, 20);  // pole
        gc.strokeLine(100, 20, 150, 20);   // top bar
        gc.strokeLine(150, 20, 150, 40);   // rope

        // Draw body parts based on wrongGuesses
        if (wrongGuesses >= 1) { // head
            gc.strokeOval(140, 40, 20, 20);
        }
        if (wrongGuesses >= 2) { // body
            gc.strokeLine(150, 60, 150, 110);
        }
        if (wrongGuesses >= 3) { // left arm
            gc.strokeLine(150, 70, 130, 90);
        }
        if (wrongGuesses >= 4) { // right arm
            gc.strokeLine(150, 70, 170, 90);
        }
        if (wrongGuesses >= 5) { // left leg
            gc.strokeLine(150, 110, 130, 140);
        }
        if (wrongGuesses >= 6) { // right leg
            gc.strokeLine(150, 110, 170, 140);
        }
    }
    private void restartGame(){
        startNewGame();
        guessedCharacters.clear();
        wrongGuesses = 0;

        guessButton.setDisable(false);
        guessWord.setDisable(false);
        guessWord.clear();

        messageLabel.setText("");
        updateDisplay();
    }
    private void buildUI(Stage stage){
        wordLabel = new Label();
        wordLabel.setFont(Font.font("Courier New", 28));
        wordLabel.setStyle("-fx-border-color: black; -fx-padding: 10;");

        guessedLabel = new Label("Guessed letters: ");
        attempsLabel = new Label("Attempts left: 6");
        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        // Create text field and buttons
        guessWord = new TextField();
        guessWord.setPromptText("Enter a letter");
        guessWord.setPrefWidth(100);

        guessButton = new Button("Guess");
        restartButton = new Button("Restart");

        // What happens when you click Guess
        guessButton.setOnAction(e -> makeGuess());
        // Pressing Enter in the text field does the same
        guessWord.setOnAction(e -> makeGuess());
        // Restart button resets the game
        restartButton.setOnAction(e -> restartGame());

        // Canvas for drawing hangman
        hangmanCanvas = new Canvas(200, 200);
        drawHangman();  // initial drawing (just gallows)

        // Layout: center = word label + canvas, bottom = controls
        VBox centerBox = new VBox(15, wordLabel, hangmanCanvas);
        centerBox.setStyle("-fx-alignment: center;");

        HBox controlBox = new HBox(10, guessWord, guessButton, restartButton);
        controlBox.setStyle("-fx-alignment: center;");

        VBox bottomBox = new VBox(10, guessedLabel, attempsLabel, controlBox, messageLabel);
        bottomBox.setStyle("-fx-alignment: center; -fx-padding: 10;");

        VBox root = new VBox(20, centerBox, bottomBox);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Hangman Game");
        stage.setScene(scene);
        stage.show();

        // Initial update to show underscores for the new word
        updateDisplay();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
