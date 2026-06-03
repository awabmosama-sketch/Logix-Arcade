package com.example.oopprojectjavafx.FlappyBird;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BirdController implements Initializable {

    @FXML private AnchorPane gamePane;

    private Node bird;               // can be ImageView or Rectangle
    private Rectangle ground;
    private Label scoreLabel;
    private Label startLabel;
    private List<Rectangle> pipes = new ArrayList<>();

    private AnimationTimer gameLoop;
    private boolean gameStarted = false;
    private boolean gameOverFlag = false;

    private int score = 0;
    private long lastScoreTime = 0;
    private int frameCount = 0;
    private double velocityY = 0;

    private double screenWidth, screenHeight;
    private double birdSize, birdX, groundY, pipeWidth, pipeGap, pipeSpeed, gravity, jumpStrength;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gamePane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                gamePane.prefWidthProperty().bind(newScene.widthProperty());
                gamePane.prefHeightProperty().bind(newScene.heightProperty());
                newScene.widthProperty().addListener((w, oldW, newW) -> recreateGame());
                newScene.heightProperty().addListener((h, oldH, newH) -> recreateGame());
                javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(50));
                delay.setOnFinished(e -> recreateGame());
                delay.play();
            }
        });
    }

    private void recreateGame() {
        screenWidth = gamePane.getWidth();
        screenHeight = gamePane.getHeight();
        if (screenWidth <= 0 || screenHeight <= 0) return;

        birdSize = screenHeight * 0.08;
        birdX = screenWidth * 0.2;
        groundY = screenHeight * 0.85;
        pipeWidth = screenWidth * 0.08;
        pipeGap = screenHeight * 0.25;
        pipeSpeed = screenWidth * 0.005;
        double scale = screenHeight / 400.0;
        gravity = 0.45 * scale;
        jumpStrength = -5.5 * scale;

        gamePane.getChildren().clear();
        pipes.clear();

        // Ground
        ground = new Rectangle(0, groundY, screenWidth, screenHeight - groundY);
        ground.setFill(Color.GREEN);
        gamePane.getChildren().add(ground);

        // Bird – try to load image, fallback to rectangle
        try {
            ImageView imageView = new ImageView();
            Image birdImg = new Image(getClass().getResourceAsStream("/com/example/oopprojectjavafx/images/Bird2.png"));
            if (birdImg != null) {
                imageView.setImage(birdImg);
                imageView.setFitWidth(birdSize);
                imageView.setFitHeight(birdSize);
                bird = imageView;
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            // Fallback to yellow rectangle
            Rectangle rect = new Rectangle(birdSize, birdSize, Color.YELLOW);
            bird = rect;
        }
        bird.setLayoutX(birdX);
        bird.setLayoutY(screenHeight / 2 - birdSize / 2);
        gamePane.getChildren().add(bird);

        // Score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Arial", screenHeight * 0.05));
        scoreLabel.setLayoutX(screenWidth * 0.02);
        scoreLabel.setLayoutY(screenHeight * 0.05);
        scoreLabel.setTextFill(Color.WHITE);
        gamePane.getChildren().add(scoreLabel);

        // Start label
        startLabel = new Label("Press SPACE to Start");
        startLabel.setFont(Font.font("Arial", screenHeight * 0.04));
        Text temp = new Text(startLabel.getText());
        temp.setFont(startLabel.getFont());
        double textWidth = temp.getLayoutBounds().getWidth();
        startLabel.setLayoutX((screenWidth - textWidth) / 2);
        startLabel.setLayoutY(screenHeight / 2);
        startLabel.setTextFill(Color.BLACK);
        gamePane.getChildren().add(startLabel);

        gameStarted = false;
        gameOverFlag = false;
        startLabel.setVisible(true);
        score = 0;
        scoreLabel.setText("Score: 0");
        frameCount = 0;
        velocityY = 0;

        if (gameLoop == null) {
            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    update();
                }
            };
            gameLoop.start();
        }

        if (gamePane.getScene() != null) {
            gamePane.getScene().setOnKeyPressed(event -> {
                if (event.getCode().toString().equals("SPACE")) {
                    if (gameOverFlag) restartGame();
                    else if (!gameStarted) startGame();
                    else fly();
                }
            });
        }
    }

    private void startGame() {
        gameStarted = true;
        gameOverFlag = false;
        startLabel.setVisible(false);
        lastScoreTime = System.nanoTime();
        velocityY = 0;
    }

    private void fly() {
        if (gameStarted && !gameOverFlag) velocityY = jumpStrength;
    }

    private void update() {
        if (gameStarted && !gameOverFlag) {
            frameCount++;
            if (frameCount % 100 == 0) spawnPipe();

            velocityY += gravity;
            bird.setLayoutY(bird.getLayoutY() + velocityY);

            if (bird.getLayoutY() < 0) {
                bird.setLayoutY(0);
                velocityY = 0;
            }

            if (System.nanoTime() - lastScoreTime > 1_000_000_000L) {
                score++;
                scoreLabel.setText("Score: " + score);
                lastScoreTime = System.nanoTime();
            }

            if (bird.getLayoutY() + birdSize >= groundY) gameOver();

            for (Rectangle p : pipes) {
                p.setLayoutX(p.getLayoutX() - pipeSpeed);
                if (bird.getBoundsInParent().intersects(p.getBoundsInParent())) gameOver();
            }
            pipes.removeIf(p -> p.getLayoutX() + pipeWidth < 0);
        }
    }

    private void spawnPipe() {
        double minPipeHeight = screenHeight * 0.15;
        double maxTopHeight = groundY - pipeGap - minPipeHeight;
        double topHeight = Math.random() * (maxTopHeight - minPipeHeight) + minPipeHeight;

        Rectangle topPipe = new Rectangle(pipeWidth, topHeight, Color.GREEN);
        topPipe.setLayoutX(screenWidth);
        topPipe.setLayoutY(0);

        Rectangle bottomPipe = new Rectangle(pipeWidth, groundY - topHeight - pipeGap, Color.GREEN);
        bottomPipe.setLayoutX(screenWidth);
        bottomPipe.setLayoutY(topHeight + pipeGap);

        gamePane.getChildren().addAll(topPipe, bottomPipe);
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    private void gameOver() {
        gameStarted = false;
        gameOverFlag = true;
        System.out.println("Game Over! Press SPACE to restart.");
    }

    private void restartGame() {
        for (Rectangle p : pipes) gamePane.getChildren().remove(p);
        pipes.clear();
        bird.setLayoutY(screenHeight / 2 - birdSize / 2);
        velocityY = 0;
        score = 0;
        scoreLabel.setText("Score: 0");
        frameCount = 0;
        lastScoreTime = System.nanoTime();
        gameStarted = true;
        gameOverFlag = false;
        startLabel.setVisible(false);
    }

    public void pressed(javafx.scene.input.KeyEvent event) {}
}