package com.example.oopprojectjavafx.RockPaperScisscors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region; // Matches any layout type (BorderPane, AnchorPane, etc.)

public class Controller {
    @FXML private ImageView computer;
    @FXML private Label computerScore;
    @FXML private Label playerScore;
    @FXML private Label result;
    @FXML private ImageView player;

    private Player humanPlayer = new HumanPlayer();
    private Player aiPlayer = new ComputerPlayer();

    @FXML
    public void initialize() {
        // 1. Reset text and clear out hand icons cleanly
        if (player != null) player.setImage(null);
        if (computer != null) computer.setImage(null);

        // 2. AUTOMATIC BACKGROUND FINDER (No FXML IDs required!)
        // This waits a fraction of a millisecond for the window to pop up,
        // then grabs the outermost layout window automatically.
        Platform.runLater(() -> {
            try {
                if (player != null && player.getScene() != null) {
                    Parent mainLayout = player.getScene().getRoot();

                    if (mainLayout instanceof Region) {
                        Region windowBackground = (Region) mainLayout;
                        java.net.URL bgUrl = null;

                        // Try locating the image using every possible path variation
                        bgUrl = getClass().getResource("images/bg.png");
                        if (bgUrl == null) bgUrl = getClass().getResource("images/bg-image.png");
                        if (bgUrl == null) bgUrl = getClass().getResource("/com/example/oopprojectjavafx/images/bg.png");
                        if (bgUrl == null) bgUrl = getClass().getResource("/com/example/oopprojectjavafx/images/bg-image.png");

                        if (bgUrl != null) {
                            windowBackground.setStyle(
                                    "-fx-background-image: url('" + bgUrl.toExternalForm() + "'); " +
                                            "-fx-background-size: cover; " +
                                            "-fx-background-repeat: no-repeat; " +
                                            "-fx-background-position: center center; " +
                                            "-fx-background-color: rgba(0, 0, 0, 0.5);" // 50% dark overlay tint
                            );
                        } else {
                            // Safe fallback: clear dark gray-blue background if files are missing
                            windowBackground.setStyle("-fx-background-color: #2c3e50;");
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Background configuration failed safely: " + e.getMessage());
            }
        });
    }

    @FXML
    private void playerturn(ActionEvent event) {
        String buttonId = ((Button) event.getSource()).getId();

        String playerChoice = humanPlayer.makeChoice(buttonId);
        String computerChoice = aiPlayer.makeChoice(null);

        updateImage(player, playerChoice);
        updateImage(computer, computerChoice);

        winner(playerChoice, computerChoice);
    }

    private void updateImage(ImageView view, String choice) {
        if (view == null) return;

        try {
            if (choice != null) {
                choice = choice.toLowerCase().trim();
            } else {
                choice = "";
            }

            // Explicitly only load actual valid move graphics into hand boxes
            if (choice.equals("rock") || choice.equals("paper") || choice.equals("scissors")) {
                String path = "/com/example/oopprojectjavafx/images/" + choice + ".png";
                java.net.URL imageUrl = getClass().getResource(path);

                if (imageUrl != null) {
                    view.setImage(new Image(imageUrl.toExternalForm()));
                } else {
                    view.setImage(null);
                }
            } else {
                view.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playerWin() {
        result.setText("You Win");
        humanPlayer.incrementScore();
        playerScore.setText(String.valueOf(humanPlayer.getScore()));
    }

    public void computerWin() {
        result.setText("You Lose");
        aiPlayer.incrementScore();
        computerScore.setText(String.valueOf(aiPlayer.getScore()));
    }

    public void draw(){
        result.setText("Draw");
    }

    private void winner(String playerChoice, String computerChoice) {
        if (playerChoice == null || computerChoice == null) return;

        if (playerChoice.equals(computerChoice)) {
            draw();
            return;
        }
        if (playerChoice.equals("rock")) {
            if (computerChoice.equals("paper")){
                computerWin();
            }
            else if (computerChoice.equals("scissors")) {
                playerWin();
            }
        }
        else if (playerChoice.equals("paper")) {
            if (computerChoice.equals("rock")) {
                playerWin();
            }
            else if (computerChoice.equals("scissors")) {
                computerWin();
            }
        }
        else if (playerChoice.equals("scissors")) {
            if (computerChoice.equals("rock")) {
                computerWin();
            }
            else if (computerChoice.equals("paper")) {
                playerWin();
            }
        }
    }
}