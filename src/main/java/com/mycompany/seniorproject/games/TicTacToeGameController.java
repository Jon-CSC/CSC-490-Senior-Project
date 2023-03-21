/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games;


import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.PeerToPeer;
import java.io.IOException;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Controller class to manipulate GUI elements and animations. Also contains 
 * logic code for determining win states of any given game.
 * 
 * @author Justin Karp
 * @version 1.0
 * @since 11/8/2021
 */

public class TicTacToeGameController {
    
    private int[] gridArr = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
    
    /* 
    *   'gridArr' values are -1 when unchosen, 1 for player one, 2 for player two
    *
    *   Tic Tac Toe grid positions relate to 'gridArr' indexes as follows:
    *
    *   0|1|2
    *   3|4|5
    *   6|7|8
    *
    */
    
    private int playerTurn = 1; // player 1 is odd value, player 2 is even value
    
    private boolean gameOver;

    @FXML
    private GridPane gridBoard;
    
    @FXML
    private Pane paneBottomCenter;
    @FXML
    private Pane paneBottomLeft;
    @FXML
    private Pane paneBottomRight;
    @FXML
    private Pane paneMiddleCenter;
    @FXML
    private Pane paneMiddleLeft;
    @FXML
    private Pane paneMiddleRight;
    @FXML
    private Pane paneTopCenter;
    @FXML
    private Pane paneTopLeft;
    @FXML
    private Pane paneTopRight;
    
    @FXML
    private Circle indicatorCircle;
    @FXML
    private Rectangle indicatorRectangle;
    @FXML
    private Label labelP1;
    @FXML
    private Label labelP2;
    
    private PeerToPeer connection;
    /**
     * Method to fire when the JavaFX stage initializes on screen.
     */
    @FXML 
    void initialize() {
        changeActivePlayerIndicator();
        App.getStage().setWidth(420);
        App.getStage().setHeight(600);
        connection = new PeerToPeer();
    }
    
    // Use data received from network setup controller
    public void initConnection(PeerToPeer connection) {
        this.connection = connection;
    }
    /**
     * Method is called when the top left pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopLeft() { // index 0
        gridArr[0] = checkPlayerTurnEvent(gridArr[0], paneTopLeft);
        checkWinStatus();
    }
    
    /**
     * Method is called when the top center pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopCenter() { // index 1
        gridArr[1] = checkPlayerTurnEvent(gridArr[1], paneTopCenter);
        checkWinStatus();
    }
    
    /**
     * Method is called when the top right pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopRight() { // index 2
        gridArr[2] = checkPlayerTurnEvent(gridArr[2], paneTopRight);
        checkWinStatus();
    }

    /**
     * Method is called when the middle left pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleLeft() { // index 3
        gridArr[3] = checkPlayerTurnEvent(gridArr[3], paneMiddleLeft);
        checkWinStatus();
    }
    
    /**
     * Method is called when the middle center pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleCenter() { // index 4
        gridArr[4] = checkPlayerTurnEvent(gridArr[4], paneMiddleCenter);
        checkWinStatus();
    }

    /**
     * Method is called when the middle right pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleRight() { // index 5
        gridArr[5] = checkPlayerTurnEvent(gridArr[5], paneMiddleRight);
        checkWinStatus();
    }

    /**
     * Method is called when the bottom left pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomLeft() { // index 6
        gridArr[6] = checkPlayerTurnEvent(gridArr[6], paneBottomLeft);
        checkWinStatus();
    }
    
    /**
     * Method is called when the bottom center pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomCenter() { // index 7
        gridArr[7] = checkPlayerTurnEvent(gridArr[7], paneBottomCenter);
        checkWinStatus();
    }

    /**
     * Method is called when the bottom right pane within the 3x3 grid is clicked. 
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomRight() { // index 8
        gridArr[8] = checkPlayerTurnEvent(gridArr[8], paneBottomRight);
        checkWinStatus();
    }
    
    /**
     * Method clears the board of the current game and initiates a new game 
     * state.
     */
    @FXML
    void newGame() {
        ParallelTransition pt = new ParallelTransition();
        
        for (Object o: gridBoard.getChildren()) {
            Pane p = (Pane) o;
            if (p.getChildren().isEmpty()) {
                continue;
            }
            Object shape = p.getChildren().get(0);
            if (shape instanceof Rectangle) {
                Rectangle r = (Rectangle) shape;
                ScaleTransition st = new ScaleTransition(Duration.seconds(0.4), r);
                st.setToX(0);
                st.setToY(0);
                st.setCycleCount(1);
                pt.getChildren().add(st);
            } else if (shape instanceof Circle) {
                Circle c = (Circle) shape;
                ScaleTransition st = new ScaleTransition(Duration.seconds(0.4), c);
                st.setToX(0);
                st.setToY(0);
                st.setCycleCount(1);
                pt.getChildren().add(st);
            }
        }
        pt.play();
        pt.setOnFinished(e -> {
            gameOver = false;
            playerTurn = 1;
            gridArr = new int[] {-1, -1, -1, -1, -1, -1, -1, -1, -1};
            paneTopLeft.getChildren().clear();
            paneTopCenter.getChildren().clear();
            paneTopRight.getChildren().clear();
            paneMiddleLeft.getChildren().clear();
            paneMiddleCenter.getChildren().clear();
            paneMiddleRight.getChildren().clear();
            paneBottomLeft.getChildren().clear();
            paneBottomCenter.getChildren().clear();
            paneBottomRight.getChildren().clear();
            changeActivePlayerIndicator();
        });
        
    }
    
    /**
     * Method terminates the entire program.
     */
    @FXML
    void exitGame() throws IOException {
//        System.exit(0);
        App.setRoot("TicTacToeMainMenu");
    }
    
    /**
     * Method will check if selected Pane object has been clicked previously
     * by a player, and create the appropriate shape object (rectangle or circle)
     * corresponding to the active player.
     * 
     * @param cellState The current state of a given Pane cell as an integer value.
     * @param pane The relevant Pane object to attach a shape object to.
     * @return The new state of a given Pane cell as an integer value.
     */
    private int checkPlayerTurnEvent(int cellState, Pane pane) {
        if (cellState == -1 && gameOver == false) {
            if ((playerTurn % 2) == 1) {    // player 1, generate square
                Rectangle r = new Rectangle(32,32,70,70);
                pane.getChildren().add(r);
                cellState = 1;

            } else {                        // player 2, generate circle
                Circle c = new Circle(67.5,67.5,35);
                pane.getChildren().add(c);
                cellState = 2;
            }
            playerTurn++;
            changeActivePlayerIndicator();
            return cellState;
        } else {
            return cellState;
        }
    }
    
    /**
     * Method controls and animates the player indicator (shape and player name
     * at bottom of GUI) corresponding to the current active player when called.
     */
    private void changeActivePlayerIndicator() {   
        if (!gameOver) {
            if ((playerTurn % 2) == 1) {    // player 1
                ScaleTransition st1 = new ScaleTransition(Duration.seconds(0.3), indicatorRectangle);
                st1.setToX(1.1);
                st1.setToY(1.1);
                st1.setCycleCount(1);
                ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.3), indicatorCircle);
                st2.setToX(0.8);
                st2.setToY(0.8);
                st2.setCycleCount(1);
                ParallelTransition pt = new ParallelTransition();
                pt.getChildren().addAll(st1, st2);
                pt.play();
                labelP1.setStyle("-fx-text-fill: #ffffff");
                labelP2.setStyle("-fx-text-fill: #434343");
            } else {                        // player 2
                ScaleTransition st1 = new ScaleTransition(Duration.seconds(0.3), indicatorRectangle);
                st1.setToX(0.8);
                st1.setToY(0.8);
                st1.setCycleCount(1);
                ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.3), indicatorCircle);
                st2.setToX(1.1);
                st2.setToY(1.1);
                st2.setCycleCount(1);
                ParallelTransition pt = new ParallelTransition();
                pt.getChildren().addAll(st1, st2);
                pt.play();
                labelP2.setStyle("-fx-text-fill: #ffffff");
                labelP1.setStyle("-fx-text-fill: #434343");
            }
        } else {
            ScaleTransition st1 = new ScaleTransition(Duration.seconds(0.3), indicatorRectangle);
                st1.setToX(1);
                st1.setToY(1);
                st1.setCycleCount(1);
                ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.3), indicatorCircle);
                st2.setToX(1);
                st2.setToY(1);
                st2.setCycleCount(1);
                ParallelTransition pt = new ParallelTransition();
                pt.getChildren().addAll(st1, st2);
                pt.play();
                labelP2.setStyle("-fx-text-fill: #ffffff");
                labelP1.setStyle("-fx-text-fill: #ffffff");
        }
    }
    
    /**
     * Method runs logic checks to determine if either player has one yet, 
     * including a draw state.
     */
    private void checkWinStatus() {
        if (gameOver == false) {
            

            // Horizontal wins
            if (gridArr[0] == gridArr[1] && gridArr[1] == gridArr[2] && gridArr[2] != -1) {
                animateWinner(gridArr[0]);
            } else if (gridArr[3] == gridArr[4] && gridArr[4] == gridArr[5] && gridArr[5] != -1) {
                animateWinner(gridArr[3]);
            } else if (gridArr[6] == gridArr[7] && gridArr[7] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[6]);
            }

            // Vertical wins
            else if (gridArr[0] == gridArr[3] && gridArr[3] == gridArr[6] && gridArr[6] != -1) {
                animateWinner(gridArr[0]);
            } else if (gridArr[1] == gridArr[4] && gridArr[4] == gridArr[7] && gridArr[7] != -1) {
                animateWinner(gridArr[1]);
            } else if (gridArr[2] == gridArr[5] && gridArr[5] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[2]);
            }

            // Diagonal wins
            else if (gridArr[0] == gridArr[4] && gridArr[4] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[0]);
            } else if (gridArr[2] == gridArr[4] && gridArr[4] == gridArr[6] && gridArr[6] != -1) {
                animateWinner(gridArr[2]);
            } 

            // Draw state (no win)
            else if (playerTurn == 10) {
                gameOver = true;
                changeActivePlayerIndicator();
                System.out.println("Draw!");
            }
        }
    }
    
    /**
     * Method conducts an animation of the winning player's shape objects 
     * contained within the Tic-Tac-Toe board. Resulting animation is dependent
     * on which player wins.
     * 
     * @param player The player that has won the game, as an integer (player one
     *  = 1, player two = 2).
     */
    private void animateWinner(int player) {
        gameOver = true;
        
        ParallelTransition pt = new ParallelTransition();
        
        if (player == 1) { // rectangle animations
            for (Object o: gridBoard.getChildren()) {
                Pane p = (Pane) o;
                if (p.getChildren().isEmpty()) {
                    continue;
                }
                Object shape = p.getChildren().get(0);
                if (shape instanceof Rectangle) {
                    Rectangle r = (Rectangle) shape;
                    RotateTransition rt = new RotateTransition(Duration.seconds(0.9), r);
                    rt.setFromAngle(0.0);
                    rt.setToAngle(300.0);
                    rt.setCycleCount(6);
                    rt.setAutoReverse(true);
                    pt.getChildren().add(rt);
                    
                    ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), r);
                    st.setByX(0.35);
                    st.setByY(0.35);
                    st.setCycleCount(1);
                    pt.getChildren().add(st);
                }
            }
        } else { // circle animations
            for (Object o: gridBoard.getChildren()) {
                Pane p = (Pane) o;
                if (p.getChildren().isEmpty()) {
                    continue;
                }
                Object shape = p.getChildren().get(0);
                if (shape instanceof Circle) {
                    Circle c = (Circle) shape;
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.9), c);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.setCycleCount(6);
                    ft.setAutoReverse(true);
                    pt.getChildren().add(ft);
                    
                    ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), c);
                    st.setByX(0.35);
                    st.setByY(0.35);
                    st.setCycleCount(1);
                    pt.getChildren().add(st);
                }
            }
        }
        pt.play();
        changeActivePlayerIndicator();
    }
}

