package com.example.oopprojectjavafx.snake_game;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnakeController extends Scene_Controller{
    @FXML private Label scorelabel;
    @FXML private Button startbutton ;
    @FXML private Pane gamePane;

    private final static double blockSize = 20.0;
    private enum Directions{Up,Down,Left,Right}
    private Directions currentDirection = Directions.Right;
    private final List<Rectangle> snake = new ArrayList<>();
    private Rectangle food;
    private int score;
    private boolean isGameRunning;
    private Timeline gameLoop;


    // Creating the Snake
    private void spawnSnake(){
        gamePane.getChildren().removeAll(snake);
        snake.clear();

        for(int i=1;i<=3;i++){
            Rectangle segment = new Rectangle(blockSize,blockSize);
            segment.setX(140.0 - (i*blockSize));
            segment.setY(200.0);

            segment.setFill(Color.web("#00ff66"));

            snake.add(segment);
            gamePane.getChildren().add(segment);
        }
        currentDirection = Directions.Right;
    }

    //Creating the FOOD
    private void createFood(){
        if (food!=null){
            gamePane.getChildren().remove(food);
        }

        double width = gamePane.getWidth() > 0 ? gamePane.getWidth() : 800;
        double height = gamePane.getHeight() > 0 ? gamePane.getHeight() : 600;
        int maxCol = (int)(width/blockSize);
        int maxRow = (int)(height/blockSize);

        int randomCol = (int)(Math.random()*maxCol);
        int randomRow = (int)(Math.random()*maxRow);

        double foodX = randomCol*blockSize;
        double foodY = randomRow*blockSize;

        food = new Rectangle(blockSize,blockSize);
        food.setX(foodX);
        food.setY(foodY);
        food.setFill(Color.web("#ff3333"));

        gamePane.getChildren().add(food);
    }

    //Start Button Click
    @FXML
    private void startButton()throws IOException{
        score = 0;
        scorelabel.setText("score: "+score);

        isGameRunning = true;

        startbutton.setDisable(true);

        gamePane.requestFocus();
        spawnSnake();
        createFood();

        KeyFrame frame = new KeyFrame(Duration.seconds(0.15),event -> {
            try {
                isRunning();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        gameLoop = new Timeline(frame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    // Running the game
    private void isRunning()throws IOException{
        if(!isGameRunning){
            return;
        }
        System.out.println("TICK MOVING");
        moveSnake();
        checkCollisions();
    }

    //Moving the Snake
    private void moveSnake(){

        for (int i=snake.size()-1;i>0;i--){
            Rectangle currentBlock = snake.get(i);
            Rectangle nextBlock = snake.get(i-1);

            currentBlock.setY(nextBlock.getY());
            currentBlock.setX(nextBlock.getX());
        }

        Rectangle head = snake.get(0);

        switch (currentDirection){
            case Up:
                head.setY(head.getY()-blockSize);
                break;
            case Down:
                head.setY(head.getY()+blockSize);
                break;
            case Left:
                head.setX(head.getX()-blockSize);
                break;
            case Right:
                head.setX(head.getX()+blockSize);
                break;
        }
    }

    //Preventing the snake from going through the walls & colliding with food
    private void checkCollisions()throws IOException{
        if (snake.isEmpty()){return;}

        Rectangle head = snake.getFirst();

        // FOOD EATING
        if (food!=null&&head.getBoundsInParent().intersects(food.getBoundsInParent())){
            score+=1;
            scorelabel.setText("Score: "+score);

            growSnake();
            createFood();
        }

        //WALLS COLLISION
        double width = gamePane.getWidth() > 0 ? gamePane.getWidth() : 800;
        double height = gamePane.getHeight() > 0 ? gamePane.getHeight() : 600;

        if(head.getX()<0||head.getX()>=width||head.getY()<0||head.getY()>=height){
            gameOver();
        }

        //SELF COLLISION
        for (int i = 1; i < snake.size(); i++) {
            if (head.getX() == snake.get(i).getX() && head.getY() == snake.get(i).getY()) {
                gameOver();
            }

        }
    }

    private void growSnake(){
        Rectangle lastBlock = snake.getLast();

        Rectangle newBlock = new Rectangle(blockSize,blockSize);

        newBlock.setX(lastBlock.getX());
        newBlock.setY(lastBlock.getY());
        newBlock.setFill(Color.web("#00ff66"));
        snake.add(newBlock);
        gamePane.getChildren().add(newBlock);
    }

    //GAME OVER!
    private void gameOver() throws IOException {
        isGameRunning=false;
        if(gameLoop!=null){
            gameLoop.stop();
        }
        SwitchToScene3(gamePane,""+score);


    }

    //Handling the pressing of the KEYS
    public void keyPress(KeyEvent event){
        switch (event.getCode()){
            case W:
            case UP:
                if(currentDirection!=Directions.Down){
                    currentDirection =Directions.Up;
                }
                break;
            case S:
            case DOWN:
                if (currentDirection!=Directions.Up){
                    currentDirection = Directions.Down;
                }
                break;
            case LEFT:
            case A:
                if (currentDirection!=Directions.Right){
                    currentDirection = Directions.Left;
                }
                break;
            case RIGHT:
            case D:
                if (currentDirection != Directions.Left){
                    currentDirection = Directions.Right;
                }
                break;
        }
    }


}
