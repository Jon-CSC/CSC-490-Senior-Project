
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games.snake;

import com.mycompany.seniorproject.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import com.mycompany.seniorproject.*;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.awt.Point;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
//import java.awt.event.KeyEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class SnakeGame {

    /*
    Create a board for background
    */
    
    private static final int WIDTH = 700;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] FOOD_IMAGE = new String[]{
        "ic_orange.png", 
        "ic_watermelon.png",
        "ic_apple.png" , 
        "ic_berry.png", 
        "ic_cherry.png", 
        "ic_coconut.png", 
        "ic_peach.png",
        "ic_pomegranate.png", 
        "ic_tomato.png"}; // String array to store the food images

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private GraphicsContext gc;
    private List<Point> snakeBody = new ArrayList();
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;
    private boolean paused = false;
    private Timeline gameplay;

    @FXML
    void exitGame() throws IOException {
        App.setRoot("games/snake/snakeMenu");
    }

    public SnakeGame(Canvas c) {
        
        setGraphicsContext(c);

        App.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (code == KeyCode.ESCAPE) {
                    if (paused) {
                        resumeGame();
                    } else {
                        gc.setTextAlign(TextAlignment.CENTER);
                        gc.setTextBaseline(VPos.CENTER);
                        gc.fillText("Paused", Math.round(c.getWidth() / 2), Math.round(c.getHeight() / 2));
                        pauseGame();
                    }
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++) { // for generate food
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);

        generateFood();

        startGame();
    }

    public void startGame() {
        gameplay = new Timeline(new KeyFrame(Duration.millis(130), e -> {
            try {
                run(gc);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }));
        gameplay.setCycleCount(Animation.INDEFINITE);
        gameplay.play();
    }

    private void pauseGame() {
        gameplay.pause();
        paused = true;
    }

    private void resumeGame() {
        gameplay.play();
        paused = false;
    }

    private void setGraphicsContext(Canvas c) {
        gc = c.getGraphicsContext2D();
    }

    private void run(GraphicsContext gc) throws IOException {
        if (paused) {
            pauseGame();
            return;
        }
        if (gameOver) {
            gameplay.stop();
            LocalUserAccount.getInstance().recordHiscore(Game.SNAKE, score);
            App.setRoot("games/snake/SnakeMainMenu");
            System.out.println("switched due to game over");
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }
        gameOver();
        eatFood();
    }

    /*
    draw the background using ROWS by COLUMNS that define on line 42,
    Other color combination would be 
    green - "AAS751", "A2d149" body: "4674E9"
    rn: "WHEAT", "SKYBLUE", body: "NAVY"
    */
    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("#b3d665"));
                } else {
                    gc.setFill(Color.web("#a1c456"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    /*
    Using Math.random to generate food on the board
    */
    private void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == foodY && snake.getY() == foodY) {
                    continue start;

                }
            }
            try {
                foodImage = new Image(getClass().getResourceAsStream(FOOD_IMAGE[(int) (Math.random() * (FOOD_IMAGE.length - 1))]));
            } catch(Exception e) {
                System.out.println("Food images are not loading properly.");
            }
            break;
        }

    }

    private void drawFood(GraphicsContext gc) {
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE - 1);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("#5275e5"));

        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE - 1, SQUARE_SIZE - 1,
                SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }

    // if snake touches boarder, kills itself
    public void gameOver() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH
                || snakeHead.y * SQUARE_SIZE >= HEIGHT) { // if snake touches border
            gameOver = true;
        }

        //destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }
    
    /*
    If the snake Head touches the food point, socre + 5
    */
    private void eatFood(){
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY){
            snakeBody.add(new Point (-1, -1));
            generateFood();
            score += 1;
        }
    }
    
    /*
    Text flowing on the board, displaying socre
    */
    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 36));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("" + score, 2, -5);
    }

}
