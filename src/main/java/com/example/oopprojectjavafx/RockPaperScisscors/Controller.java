package com.example.oopprojectjavafx.RockPaperScisscors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.net.URL;

public class Controller {

    private static final String PAPER = "paper";
    private static final String ROCK = "rock";
    private static final String SCISSORS = "scissors";
    private Image image;

    @FXML private ImageView computer;
    @FXML private Label computerScore;
    @FXML private Button paperButton;
    @FXML private ImageView player;
    @FXML private Label playerScore;
    @FXML private Label result;
    @FXML private Button rockButton;
    @FXML private Button scissorsButton;

    @FXML
    public void initialize() {
        // Run later ensures the scene is fully attached to the window before we query it
        Platform.runLater(() -> {
            try {
                if (result.getScene() != null && result.getScene().getRoot() instanceof BorderPane) {
                    BorderPane root = (BorderPane) result.getScene().getRoot();

                    // Correct way to load resources from Maven/Gradle structures
                    URL bgUrl = getClass().getResource("/com/example/oopprojectjavafx/images/bg.png");

                    if (bgUrl != null) {
                        String bgPath = bgUrl.toExternalForm();
                        root.setStyle("-fx-background-image: url('" + bgPath + "'); " +
                                "-fx-background-size: cover; " +
                                "-fx-background-position: center; " +
                                "-fx-background-color: rgba(0,0,0,0.5);");
                        System.out.println("Background loaded successfully");
                    } else {
                        System.out.println("Background image not found. Using fallback.");
                        root.setStyle("-fx-background-color: green;");
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load background: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void playerturn(ActionEvent event) {
        String playerChoice = null;
        String buttonId = ((Button) event.getSource()).getId();

        try {
            switch (buttonId) {
                case "paperButton":
                    image = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/paper.png").toExternalForm());
                    playerChoice = PAPER;
                    break;
                case "rockButton":
                    image = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/rock.png").toExternalForm());
                    playerChoice = ROCK;
                    break;
                case "scissorsButton":
                    image = new Image(getClass().getResource("/com/example/oopprojectjavafx/images/scissors.png").toExternalForm());
                    playerChoice = SCISSORS;
                    break;
            }

            if (player != null && image != null) {
                player.setImage(image);
                player.setFitWidth(220);
                player.setFitHeight(299);
                player.setPreserveRatio(true);
            }

        } catch (Exception e) {
            System.out.println("Error loading player image: " + e.getMessage());
            e.printStackTrace();
        }
        winner(playerChoice, computerTurn());
    }

    @FXML
    private String computerTurn() {
        String computerChoice = null;
        int index = (int) (Math.random() * 3);
        String resourcePath = "";

        switch (index) {
            case 0:
                resourcePath = "/com/example/oopprojectjavafx/images/rock.png";
                computerChoice = ROCK;
                break;
            case 1:
                resourcePath = "/com/example/oopprojectjavafx/images/paper.png";
                computerChoice = PAPER;
                break;
            case 2:
                resourcePath = "/com/example/oopprojectjavafx/images/scissors.png";
                computerChoice = SCISSORS;
                break;
        }

        try {
            URL imgUrl = getClass().getResource(resourcePath);
            if (imgUrl != null) {
                computer.setImage(new Image(imgUrl.toExternalForm()));
                computer.setFitWidth(220);
                computer.setFitHeight(299);
                computer.setPreserveRatio(true);
            }
        } catch (Exception e) {
            System.out.println("Error loading computer image: " + e.getMessage());
        }
        return computerChoice;
    }

    public void playerWin(){
        result.setText("You Win");
        playerScore.setText(String.valueOf(Integer.parseInt(playerScore.getText()) + 1));
    }

    public void computerWin(){
        result.setText("You Lose");
        computerScore.setText(String.valueOf(Integer.parseInt(computerScore.getText()) + 1));
    }

    public void draw(){
        result.setText("Draw");
    }

    private void winner(String playerChoice, String computerChoice){
        if(playerChoice == null || computerChoice == null) return;
        if(playerChoice.equals(computerChoice)){
            draw();
            return;
        }
        if (playerChoice.equals(ROCK)){
            if(computerChoice.equals(PAPER)){
                computerWin();
            } else if(computerChoice.equals(SCISSORS)){
                playerWin();
            }
        } else if(playerChoice.equals(PAPER)){
            if(computerChoice.equals(ROCK)){
                playerWin();
            } else if(computerChoice.equals(SCISSORS)){
                computerWin();
            }
        } else { // SCISSORS
            if(computerChoice.equals(ROCK)){
                computerWin();
            } else if(computerChoice.equals(PAPER)){
                playerWin();
            }
        }
    }
}