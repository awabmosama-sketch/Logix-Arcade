package com.example.oopprojectjavafx.FlappyBird;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BirdController implements Initializable {



    @FXML private AnchorPane gamePane;
    private List<Rectangle> pipes = new ArrayList<>();
    private int score = 0;
    private long lastScoreTime = 0;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label startLabel;
    @FXML
    private ImageView bird;
    @FXML
    private Rectangle plane;

    private AnimationTimer gameLoop;

    private boolean gameStarted = false;

    // Fields
    private int frameCount = 0;
    private double time = 0;
    private double velocityY = 0;
    private final double gravity = 0.45;
    private final double jumpStrength = -6.5; // (negative is up)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Make the bird capable of receiving focus
        bird.setFocusTraversable(true);
        bird.requestFocus();

        // 2. Add a global listener just in case the bird loses focus
        bird.setOnKeyPressed(event -> pressed(event));

        load();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }

    @FXML
    void pressed(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            fly();
        }
    }

    private void fly() {
        if (!gameStarted) {
            gameStarted = true; // Start the game on jump
            startLabel.setVisible(false);
            lastScoreTime = System.nanoTime();
        }
        velocityY = jumpStrength;
    }

    private void update() {
        if (gameStarted) {
            // Apply gravity to the velocity
            frameCount++;
            if (frameCount % 100 == 0) {
                spawnPipe();
            }
            velocityY += gravity;
            bird.setTranslateY(bird.getTranslateY() + velocityY);

            if (bird.getLayoutY() + bird.getTranslateY() < 0) {
                bird.setTranslateY(-bird.getLayoutY()); // Force it to stay at 0
                velocityY = 0;
            }

            if (System.nanoTime() - lastScoreTime > 1_000_000_000L) {
                score++;
                scoreLabel.setText("Score: " + score);
                lastScoreTime = System.nanoTime();
            }

            bird.setTranslateY(bird.getTranslateY() + velocityY);

            if (isBirdDead()) {
                gameOver();
            }
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle p = pipes.get(i);
                p.setX(p.getX() - 3);

                // Collision Detection:
                if (bird.getBoundsInParent().intersects(p.getBoundsInParent())) {
                    gameOver();
                }
            }
        }
    }

    private void load() {
        System.out.println("Game Started!");
    }

    private void moveBirdY(double position) {
        bird.setTranslateY(bird.getTranslateY() + position);
    }

    private boolean isBirdDead() {
        // Get the Y position of the bottom of the bird
        double birdBottom = bird.getLayoutY() + bird.getTranslateY() + bird.getFitHeight();

        // Get the top Y position of the green plane
        double planeTop = plane.getLayoutY();

        // Check if the bird's bottom has passed the plane's top
        return birdBottom >= planeTop;
    }

    private void resetBird() {
        bird.setTranslateY(0);
        time = 0;
    }

    private void gameOver() {
        gameLoop.stop();
        gameStarted = false;
        System.out.println("Game Over!");
    }

    private void spawnPipe() {
        double gap = 150;
        double minPipeHeight = 50;
        double pipeWidth = 50;

        // Generate random height for the top pipe
        double topPipeHeight = Math.random() * (250 - minPipeHeight) + minPipeHeight;

        Rectangle topPipe = new Rectangle(pipeWidth, topPipeHeight, javafx.scene.paint.Color.GREEN);
        topPipe.setX(600);
        topPipe.setY(0);

        Rectangle bottomPipe = new Rectangle(pipeWidth, 350 - topPipeHeight - gap, javafx.scene.paint.Color.GREEN);
        bottomPipe.setX(600);
        bottomPipe.setY(topPipeHeight + gap);

        gamePane.getChildren().addAll(topPipe, bottomPipe);
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }
}