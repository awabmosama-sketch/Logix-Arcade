package com.example.oopprojectjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class Controller {

    @FXML
    private Circle MyCircle;

    private double x;
    private double y;

    @FXML
    public void initialize() {
        x = MyCircle.getCenterX();
        y = MyCircle.getCenterY();
    }

    public void up(ActionEvent event) {
        System.out.println("up");
        MyCircle.setCenterY(y -= 5);
    }

    public void down(ActionEvent event) {
        System.out.println("down");
        MyCircle.setCenterY(y += 5);
    }

    public void left(ActionEvent event) {
        System.out.println("left");
        MyCircle.setCenterX(x -= 5);
    }

    public void right(ActionEvent event) {
        System.out.println("right");
        MyCircle.setCenterX(x += 5);
    }
}
