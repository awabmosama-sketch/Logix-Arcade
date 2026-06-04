package com.example.oopprojectjavafx.Timer_Game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Random;


public class TimerController extends SceneController {
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;


    private Timeline timeline;
    private Random random = new Random();
    private int time_passed = 0;
    private int timenum;


    @FXML public void initializeTimer(){startTimer();}

    public void newNumber(){
        this.timenum = this.random.nextInt(0,6001);
        int seconds = (this.timenum/100)%60;
        int minutes = (this.timenum/6000)%60;
        int centiseconds = this.timenum%100;
        this.label2.setText(String.format( "%02d:%02d:%02d",minutes,seconds,centiseconds));
    }

    public void startTimer(){
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        KeyFrame frame = new KeyFrame(Duration.millis(10), event ->{
            this.time_passed++;

            int seconds = (this.time_passed/100)%60;
            int minutes = (this.time_passed/6000)%60;
            int centiseconds = this.time_passed%100;

            this.label1.setText(String.format( "%02d:%02d:%02d",minutes,seconds,centiseconds));
        });

        this.timeline = new Timeline(frame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public void resetTime(){
        time_passed = 0;
    }


    public void calculateTime(ActionEvent event) throws IOException {

        int seconds = (this.time_passed-timenum/100)%60;
        int centiseconds = this.time_passed-timenum%100;
//        0.05
        int limit=(int) (this.timenum*0.05);
        int upperLimit = this.timenum+limit;
        int lowerLimit = this.timenum-limit;
        String scoreText;
        if(time_passed>lowerLimit&&time_passed<upperLimit){

            System.out.println("Congrats you stopped in the range of the time");
            //write the name of the player and add it to a file for note

            if (time_passed>timenum){
                scoreText = "+"+(this.time_passed-this.timenum);
                System.out.println("+"+(this.time_passed-this.timenum));
                System.out.println("GREATER");
                System.out.println(scoreText);
            }
            else{
                scoreText = " "+(this.time_passed-this.timenum);
                    System.out.println(""+(this.time_passed-this.timenum));
                    System.out.println("LESSER");
                System.out.println(scoreText);
            }

            SwitchToScene3(event,scoreText);


        }

    }


    public void stopTimer(){
        if(this.timeline !=null){
            this.timeline.stop();
        }
    }
}
