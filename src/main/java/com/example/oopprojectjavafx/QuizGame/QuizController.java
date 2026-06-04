package com.example.oopprojectjavafx.QuizGame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class QuizController {
    @FXML private Label questionLabel;
    @FXML private Button A;
    @FXML private Button B;
    @FXML private Button C;
    @FXML private Button D;
    @FXML private Label scoreLabel;

    String[] questions = {
            "When was the first telephone invented?",
            "Who is the founder of the computer?",
            "What was Java originally called?",
            "What is the only organ in the human body capable of completely regenerating itself?",
            "What is the only type of bird capable of flying backward?",
            "What is the name of the largest moon in our solar system?",
            "Which type of memory is volatile and loses its data when the computer is powered off?",
            "Which of the following is the largest ocean on Earth?",
            "What is the product of 15 and 12?",
            "Solve for x in the equation: 2x + 5 = 15",
            "What is the primary purpose of a Compiler?",
            "If you have a sequence 2, 4, 8, 16, ..., what is the next number?",
            "What is the standard format for representing colors on the web?",
            "Which country is known as the \"Land of the Rising Sun\"?",
            "How many continents are there on Earth?",
            "What is the capital city of France?",
            "How many bones are there in an adult human body?",
            "What does the \"IP\" in IP address stand for?",
            "What is the result of 15 x 4 + 10?",
            "Which logic gate outputs '1' only if both inputs are '1'?"
    };

    String[][] options = {
            {"1865", "1876", "1893", "1901"},
            {"Alan Turing", "Charles Babbage", "John von Neumann", "Ada Lovelace"},
            {"Oak", "Green", "Lattee", "C++"},
            {"The Lungs", "The Heart", "The Kidneys", "The Liver"},
            {"Hummingbird", "Peregrine Falcon", "Eagles", "Parrot"},
            {"Titan", "Europa", "Ganymede", "Triton"},
            {"ROM", "HDD", "RAM", "SSD"},
            {"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"},
            {"150", "160", "180", "190"},
            {"2", "5", "7", "10"},
            {"Execute Code", "Debug Code", "Translate Code", "Store Code"},
            {"20", "24", "32", "64"},
            {"Hex Code", "Decimal", "Integer", "Boolean"},
            {"China", "Thailand", "Japan", "South Korea"},
            {"5", "6", "7", "8"},
            {"Berlin", "Madrid", "Paris", "Rome"},
            {"186", "206", "256", "306"},
            {"Internal Protocol", "Internet Protocol", "Input Port", "Integrated Program"},
            {"50", "60", "70", "80"},
            {"OR", "AND", "NOT", "XOR"}
    };

    char[] answers = {'B', 'B', 'A', 'D', 'A', 'C', 'C', 'D', 'C', 'B', 'C', 'C', 'A', 'C', 'C', 'C', 'B', 'B', 'C', 'B'};

    int index = 0;
    int correct_guess = 0;
    int total_questions = questions.length;

    @FXML
    public void initialize() {
        nextQuestion();
    }

    public void nextQuestion() {
        if (index >= total_questions){
            results();
        }
        else{
            questionLabel.setText(questions[index]);
            A.setText(options[index][0]);
            B.setText(options[index][1]);
            C.setText(options[index][2]);
            D.setText(options[index][3]);
            scoreLabel.setText(String.valueOf(correct_guess));
        }
    }

    @FXML
    public void answerClick(ActionEvent event) {
        Object source = event.getSource();
        String playerGuess = " ";
        if(source == A){
            playerGuess = "A";
        }
        else if (source == B) {
            playerGuess = "B";
        }
        else if(source == C){
            playerGuess = "C";
        }
        else if(source == D){
            playerGuess = "D";
        }

        String correctAnswer = String.valueOf(answers[index]);
        if(playerGuess.equals(correctAnswer)){
            correct_guess++;
            scoreLabel.setText(String.valueOf(correct_guess));
        }
        displayAnswer(playerGuess);
    }

    public void displayAnswer(String playerGuess) {
        A.setDisable(true);
        B.setDisable(true);
        C.setDisable(true);
        D.setDisable(true);
        String correctAnswer = String.valueOf(answers[index]);

        if (correctAnswer.equals("A")){
            A.getStyleClass().add("correct");
        }
        if (correctAnswer.equals("B")){
            B.getStyleClass().add("correct");
        }
        if (correctAnswer.equals("C")) {
            C.getStyleClass().add("correct");
        }
        if (correctAnswer.equals("D")){
            D.getStyleClass().add("correct");
        }

        if (!playerGuess.equals(correctAnswer)) {
            if (playerGuess.equals("A")) {
                A.getStyleClass().add("wrong");
            }
            if (playerGuess.equals("B")) {
                B.getStyleClass().add("wrong");
            }
            if (playerGuess.equals("C")){
                C.getStyleClass().add("wrong");
            }
            if (playerGuess.equals("D")){
                D.getStyleClass().add("wrong");
            }
        }

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));

        pause.setOnFinished(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                A.getStyleClass().remove("correct");
                A.getStyleClass().remove("wrong");
                B.getStyleClass().remove("correct");
                B.getStyleClass().remove("wrong");
                C.getStyleClass().remove("correct");
                C.getStyleClass().remove("wrong");
                D.getStyleClass().remove("correct");
                D.getStyleClass().remove("wrong");
                A.setDisable(false);
                B.setDisable(false);
                C.setDisable(false);
                D.setDisable(false);
                index++;
                nextQuestion();
            }
        });
        pause.play();

    }

    public void results() {
        A.setDisable(true);
        B.setDisable(true);
        C.setDisable(true);
        D.setDisable(true);
        questionLabel.setText("Quiz Completed!");
        int percentage = (int) (((double) correct_guess / total_questions) * 100);
        A.setText("Total Questions: " + total_questions);
        B.setText("Correct: " + correct_guess);
        C.setText("Wrong: " + (total_questions - correct_guess));
        D.setText("Final Score: " + percentage + "%");
    }
}
