package com.example.oopprojectjavafx.Timer_Game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;


public class Scene3Controller {
    @FXML Label label3;
    @FXML ListView listView;
    @FXML
    TextField textField;
    private String score;
    private final String fileName= "score.txt";

    @FXML
    public void initialize(){
        refreshLeaderboard();
    }



    public void saveToFile(String score){
        String dataToSave = score + System.lineSeparator();

        try{
            Files.write(Path.of(fileName),dataToSave.getBytes(), StandardOpenOption.WRITE,StandardOpenOption.APPEND);
            System.out.println("File written successfully");
        } catch (IOException e) {
            System.out.println("Could not write the file \n Error\n "+ e);

        }


    }

    public List<String> readFromFile(){
        try{
            if (Files.exists(Path.of(fileName))){
                System.out.println("File read Successfully");
                return Files.readAllLines(Path.of(fileName));

            }
            else{
                System.out.println("Reading Failed: File is missing");
            }
        } catch (Exception e) {
            System.out.println("Error:\n"+e);
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    private void refreshLeaderboard() {
        List<String> records = readFromFile();

        Collections.sort(records,new Comparator<String>(){
            @Override
            public int compare(String record1,String record2){
                try{
                    int score1 = parseScoreValue(record1);
                    int score2 = parseScoreValue(record2);

                    return Integer.compare(Math.abs(score1),Math.abs(score2));

                } catch (Exception e) {
                    return record1.compareTo(record2);
                }
            }
            private int parseScoreValue(String record) {
                String[] parts = record.split(":");
                if (parts.length < 2) return 99999; // Send bad format items to the bottom

                // Strip out any accidental hidden spaces or explicit '+' signs
                String scoreStr = parts[1].trim().replace("+", "");
                return Integer.parseInt(scoreStr);
            }

        });
        listView.getItems().clear();
        listView.getItems().addAll(records);
    }

    public void setScore(String score){
        if (label3 !=null){
            label3.setText(score);
            this.score = score;
        }

    }

    public void handleSubmit(javafx.event.ActionEvent event) {
        String playerName = textField.getText().trim();

        if(playerName.isEmpty()){
            playerName = "Anonymous";
        }

        String finalRecord = playerName + ":" +score;

        saveToFile(finalRecord);

        refreshLeaderboard();
    }

    
}
