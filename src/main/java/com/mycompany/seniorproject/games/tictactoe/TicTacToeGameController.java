/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games.tictactoe;

import com.google.cloud.firestore.FieldValue;
import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.Game;
import com.mycompany.seniorproject.LocalUserAccount;
import com.mycompany.seniorproject.PeerToPeer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Controller class to manipulate GUI elements and animations. Also contains
 * logic code for determining win states of any given game.
 *
 * @author Justin Karp, Jonathan Espinal
 * @version 1.0
 * @since 3/30/2023
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
    private VBox scorecardPane, scorecardPaneMultiplayer, rematchPane, drawPane;

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
    private Circle winnerCircle, winnerCircleMultiplayer, winnerCircleMultiplayerRematch;
    @FXML
    private Rectangle winnerRectangle, winnerRectangleMultiplayer, winnerRectangleMultiplayerRematch;

    @FXML
    private Label labelP1;
    @FXML
    private Label labelP2;

    @FXML
    private ProgressIndicator progressIndicatorRematchResponse;
    @FXML
    private Label labelRematchDeclined;
    @FXML
    private Label labelRequestRematch;

    // multiplayer variables
    private PeerToPeer connection;
    private boolean isHost;
    Pane[] paneArray;

    /**
     * Method to fire when the JavaFX stage initializes on screen.
     */
    @FXML
    void initialize() {
        App.getStage().setWidth(400);
        App.getStage().setHeight(600);
        changeActivePlayerIndicator();
        scorecardPane.setVisible(false);
        rematchPane.setVisible(false);
        scorecardPaneMultiplayer.setVisible(false);
        drawPane.setVisible(false);
        paneArray = new Pane[]{paneTopLeft, paneTopCenter, paneTopRight,
            paneMiddleLeft, paneMiddleCenter, paneMiddleRight,
            paneBottomLeft, paneBottomCenter, paneBottomRight};
    }

    /**
     * Initializes network-enabled multiplayer matches.
     *
     * @param connection The connection to use
     * @param isHost Is this player the match host?
     */
    public void initMultiplayer(PeerToPeer connection, boolean isHost) {
        this.connection = new PeerToPeer();
        this.connection = connection;
        this.isHost = isHost;

        /* Player 2 UI is blocked and forced to wait for player 1's actions 
        on turn 1*/
        if (connection != null && !isHost) {
            // disable pane inputs
            for (Pane paneObject : paneArray) {
                paneObject.setDisable(true);
            }
            // receive grid change from opponent, threaded to avoid UI lockups
            Task<Integer> task = new Task<Integer>() {
                @Override
                public Integer call() {
                    String response = connection.readPacket();
                    response = response.replaceAll(" END MESSAGE", "");
                    response = response.trim();
                    int gridTarget = Integer.valueOf(response);
                    return gridTarget;
                }
            };
            task.setOnSucceeded((WorkerStateEvent taskFinishEvent) -> {
                // update the grid with recieved change
                int gridTarget = task.getValue();
                updateGrid(gridTarget);
                // re-enable pane inputs
                for (Pane paneObject : paneArray) {
                    paneObject.setDisable(false);
                }
            });
            new Thread(task).start();
        }
    }

    /**
     * Method is called when the top left pane within the 3x3 grid is clicked.
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopLeft() { // index 0
        gridArr[0] = checkPlayerTurnEvent(gridArr[0], paneTopLeft, 0);
        checkWinStatus();
        //  transmitActionToOpponent();
    }

    /**
     * Method is called when the top center pane within the 3x3 grid is clicked.
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopCenter() { // index 1
        gridArr[1] = checkPlayerTurnEvent(gridArr[1], paneTopCenter, 1);
        checkWinStatus();
    }

    /**
     * Method is called when the top right pane within the 3x3 grid is clicked.
     * This will check the player turn and win status of the board.
     */
    @FXML
    void clickTopRight() { // index 2
        gridArr[2] = checkPlayerTurnEvent(gridArr[2], paneTopRight, 2);
        checkWinStatus();
    }

    /**
     * Method is called when the middle left pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleLeft() { // index 3
        gridArr[3] = checkPlayerTurnEvent(gridArr[3], paneMiddleLeft, 3);
        checkWinStatus();
    }

    /**
     * Method is called when the middle center pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleCenter() { // index 4
        gridArr[4] = checkPlayerTurnEvent(gridArr[4], paneMiddleCenter, 4);
        checkWinStatus();
    }

    /**
     * Method is called when the middle right pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickMiddleRight() { // index 5
        gridArr[5] = checkPlayerTurnEvent(gridArr[5], paneMiddleRight, 5);
        checkWinStatus();
    }

    /**
     * Method is called when the bottom left pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomLeft() { // index 6
        gridArr[6] = checkPlayerTurnEvent(gridArr[6], paneBottomLeft, 6);
        checkWinStatus();
    }

    /**
     * Method is called when the bottom center pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomCenter() { // index 7
        gridArr[7] = checkPlayerTurnEvent(gridArr[7], paneBottomCenter, 7);
        checkWinStatus();
    }

    /**
     * Method is called when the bottom right pane within the 3x3 grid is
     * clicked. This will check the player turn and win status of the board.
     */
    @FXML
    void clickBottomRight() { // index 8
        gridArr[8] = checkPlayerTurnEvent(gridArr[8], paneBottomRight, 8);
        checkWinStatus();
    }

    /**
     * Method clears the board of the current game and initiates a new game
     * state.
     */
    @FXML
    void newGame() {
        scorecardPane.setVisible(false);
        scorecardPaneMultiplayer.setVisible(false);
        rematchPane.setVisible(false);
        drawPane.setVisible(false);
        ParallelTransition pt = new ParallelTransition();

        for (Object o : gridBoard.getChildren()) {
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
            gridArr = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
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
        for (Pane paneObject : paneArray) {
            paneObject.setDisable(false);
        }
        // For multiplayer rematches. If multiplayer, init multiplayer match
        if (connection != null) {
            if (isHost) {
                initMultiplayer(connection, true);
            } else {
                initMultiplayer(connection, false);
            }
        }
    }

    /**
     * Method terminates the entire program.
     */
    @FXML
    void exitGame() throws IOException {
//        System.exit(0);
        // If it's a multiplayer game, close connections before changing root
        if (connection != null) {
            connection.closeConnection();
            connection = null;
        }
        App.setRoot("games/tictactoe/TicTacToeMainMenu");
    }

    /**
     * Method will check if selected Pane object has been clicked previously by
     * a player, and create the appropriate shape object (rectangle or circle)
     * corresponding to the active player.
     *
     * @param cellState The current state of a given Pane cell as an integer
     * value.
     * @param pane The relevant Pane object to attach a shape object to.
     * @return The new state of a given Pane cell as an integer value.
     */
    private int checkPlayerTurnEvent(int cellState, Pane pane, int gridLocation) {
        if (cellState == -1 && gameOver == false) {
            if ((playerTurn % 2) == 1) {    // player 1, generate square
                Rectangle r = new Rectangle(32, 32, 70, 70);
                pane.getChildren().add(r);
                cellState = 1;
                playerTurn++;
                changeActivePlayerIndicator();
                if (connection != null && isHost) {
                    // disable pane inputs
                    for (Pane paneObject : paneArray) {
                        paneObject.setDisable(true);
                    }
                    // transmit target grid change
                    connection.sendPacket(String.valueOf(gridLocation) + " END MESSAGE");
                    Task<Integer> task = new Task<Integer>() {
                        @Override
                        public Integer call() {
                            // receive grid change from opponent
                            String response = connection.readPacket();
                            response = response.replaceAll(" END MESSAGE", "");
                            response = response.trim();
                            int gridTarget = Integer.valueOf(response);
                            return gridTarget;
                        }
                    };
                    task.setOnSucceeded((WorkerStateEvent taskFinishEvent) -> {
                        int gridTarget = task.getValue();
                        updateGrid(gridTarget);
                        // re-enable pane inputs
                        for (Pane paneObject : paneArray) {
                            paneObject.setDisable(false);
                        }
                    });
                    new Thread(task).start();
                }
            } else {                        // player 2, generate circle
                Circle c = new Circle(67.5, 67.5, 35);
                pane.getChildren().add(c);
                cellState = 2;
                playerTurn++;
                changeActivePlayerIndicator();
                if (connection != null && !isHost) {
                    // disable pane inputs
                    for (Pane paneObject : paneArray) {
                        paneObject.setDisable(true);
                    }
                    // transmit target grid change
                    connection.sendPacket(String.valueOf(gridLocation) + " END MESSAGE");
                    Task<Integer> task = new Task<Integer>() {
                        @Override
                        public Integer call() {
                            // receive grid change from opponent
                            String response = connection.readPacket();
                            response = response.replaceAll(" END MESSAGE", "");
                            response = response.trim();
                            int gridTarget = Integer.valueOf(response);
                            return gridTarget;
                        }
                    };
                    task.setOnSucceeded((WorkerStateEvent taskFinishEvent) -> {
                        int gridTarget = task.getValue();
                        updateGrid(gridTarget);
                        // re-enable pane inputs
                        for (Pane paneObject : paneArray) {
                            paneObject.setDisable(false);
                        }
                    });
                    new Thread(task).start();
                }
            }
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
                updateScore(gridArr[0]);
            } else if (gridArr[3] == gridArr[4] && gridArr[4] == gridArr[5] && gridArr[5] != -1) {
                animateWinner(gridArr[3]);
                updateScore(gridArr[3]);
            } else if (gridArr[6] == gridArr[7] && gridArr[7] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[6]);
                updateScore(gridArr[6]);
            } // Vertical wins
            else if (gridArr[0] == gridArr[3] && gridArr[3] == gridArr[6] && gridArr[6] != -1) {
                animateWinner(gridArr[0]);
                updateScore(gridArr[0]);
            } else if (gridArr[1] == gridArr[4] && gridArr[4] == gridArr[7] && gridArr[7] != -1) {
                animateWinner(gridArr[1]);
                updateScore(gridArr[1]);
            } else if (gridArr[2] == gridArr[5] && gridArr[5] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[2]);
                updateScore(gridArr[2]);
            } // Diagonal wins
            else if (gridArr[0] == gridArr[4] && gridArr[4] == gridArr[8] && gridArr[8] != -1) {
                animateWinner(gridArr[0]);
                updateScore(gridArr[0]);
            } else if (gridArr[2] == gridArr[4] && gridArr[4] == gridArr[6] && gridArr[6] != -1) {
                animateWinner(gridArr[2]);
                updateScore(gridArr[2]);
            } // Draw state (no win)
            else if (playerTurn == 10) {
                gameOver = true;
                changeActivePlayerIndicator();
                drawPane.setVisible(true);
            }
        }
    }

    /**
     * Method conducts an animation of the winning player's shape objects
     * contained within the Tic-Tac-Toe board. Resulting animation is dependent
     * on which player wins.
     *
     * @param player The player that has won the game, as an integer (player one
     * = 1, player two = 2).
     */
    private void animateWinner(int player) {
        gameOver = true;

        ParallelTransition pt = new ParallelTransition();

        if (player == 1) { // rectangle animations
            for (Object o : gridBoard.getChildren()) {
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
            /* If this is player 2 animating player 1's victory, send one final 
            junk message to close player 1's listening thread */
            if (connection != null && !isHost) {
                connection.sendPacket(" END MESSAGE");
            }
        } else { // circle animations
            for (Object o : gridBoard.getChildren()) {
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
            /* If this is player 1 animating player 2's victory, send one final 
            junk message to close player 2's listening thread */
            if (connection != null && isHost) {
                connection.sendPacket(" END MESSAGE");
            }
        }
        pt.play();
        changeActivePlayerIndicator();
        scorecardAction(player);
        listenForRematch(player);
    }

    /**
     * Calls the appropriate function to mirror opponent player actions.
     *
     * @param gridLocation The target grid location to act upon.
     */
    private void updateGrid(int gridIndex) {
        switch (gridIndex) {
            case 0:
                clickTopLeft();
                break;
            case 1:
                clickTopCenter();
                break;
            case 2:
                clickTopRight();
                break;
            case 3:
                clickMiddleLeft();
                break;
            case 4:
                clickMiddleCenter();
                break;
            case 5:
                clickMiddleRight();
                break;
            case 6:
                clickBottomLeft();
                break;
            case 7:
                clickBottomCenter();
                break;
            case 8:
                clickBottomRight();
                break;
            default:
                break;
        }
    }

    /**
     * Method will make scorecard visible, and based on the winner, will show
     * the respective shape of the player.
     *
     * @param player
     */
    @FXML
    private void scorecardAction(int player) {
        if (connection == null) {
            scorecardPane.setVisible(true);
            if (1 == player) {
                winnerRectangle.setVisible(true);
                winnerCircle.setVisible(false);
            } else {
                winnerRectangle.setVisible(false);
                winnerCircle.setVisible(true);
            }
        } else {
            scorecardPaneMultiplayer.setVisible(true);
            if (1 == player) {
                winnerRectangleMultiplayer.setVisible(true);
                winnerCircleMultiplayer.setVisible(false);
            } else {
                winnerRectangleMultiplayer.setVisible(false);
                winnerCircleMultiplayer.setVisible(true);
            }
        }

    }

    /**
     * Change font of text when hovering over label.
     *
     * @param m
     */
    @FXML
    private void onMenuSelectionMouseEnter(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setStyle("-fx-font-weight: bold");
        label.setText("▶ " + label.getText() + " ◀");
    }

    /**
     * Change font of text when done hovering over label.
     *
     * @param m
     */
    @FXML
    private void onMenuSelectionMouseExit(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setStyle("-fx-font-weight: normal");
        label.setText(label.getText().substring(2, label.getText().length() - 2));

    }

    /**
     * Returns to main game library page.
     *
     * @throws IOException
     */
    @FXML
    private void onQuitMouseClick() throws IOException {
        if (connection != null) {
            connection.sendPacket("Rematch Declined END MESSAGE");
            connection.closeConnection();
            connection = null;
        }
        exitGame();

    }

    /**
     * Initiates a new local game.
     *
     * @throws IOException
     */
    @FXML
    private void onReplayMouseClick() throws IOException {
        newGame();
    }

    /**
     * Starts a request for a rematch in online games.
     *
     * @throws IOException
     */
    @FXML
    private void onRematchRequestMouseClick() throws IOException {
        if (connection != null) {
            // Send a rematch request
            connection.sendPacket("Rematch Requested END MESSAGE");
            labelRequestRematch.setVisible(false);
            progressIndicatorRematchResponse.setVisible(true);
            // Listen for the response
            Task<String> task = new Task<String>() {
                @Override
                public String call() {
                    String inboundRematchRequest = "";
                    if (connection != null) {
                        inboundRematchRequest = connection.readPacket();
                    }
                    return inboundRematchRequest;
                }
            };
            task.setOnSucceeded((WorkerStateEvent taskFinishEvent) -> {
                String inboundRematchRequest = task.getValue();
                System.out.println(inboundRematchRequest);
                if (inboundRematchRequest.contains("Rematch Accepted")) {
                    progressIndicatorRematchResponse.setVisible(false);
                    newGame();
                } else {
                    progressIndicatorRematchResponse.setVisible(false);
                    // Display declined, pause for 2 seconds, exit game
                    labelRematchDeclined.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> {
                        try {
                            labelRematchDeclined.setVisible(false);
                            exitGame();
                        } catch (IOException ex) {
                            Logger.getLogger(TicTacToeGameController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    pause.play();
                }
                labelRequestRematch.setVisible(true);
            });
            new Thread(task).start();
        }

    }

    /**
     * Method that updates the score of the winning player in multiplayer
     *
     * @param player The winning player
     */
    private void updateScore(int player) {
        if (connection != null) {
            // If this is host and player 1 won, tally win in database for this user
            if (isHost) {
                if (player == 1) {
                    LocalUserAccount.getInstance().recordMatch(Game.TICTACTOE, true);
                } else {
                    LocalUserAccount.getInstance().recordMatch(Game.TICTACTOE, false);
                }
            }
            // If this not host and player 2 won, then tally win in database for
            // this user
            if (!isHost) {
                if (player == 2) {
                    LocalUserAccount.getInstance().recordMatch(Game.TICTACTOE, true);
                } else {
                    LocalUserAccount.getInstance().recordMatch(Game.TICTACTOE, false);
                }
            }
        }
    }

    /**
     * Sets up a thread to listen for a rematch request
     */
    private void listenForRematch(int player) {
        // Detect incoming rematch request
        Task<String> task = new Task<String>() {
            @Override
            public String call() {
                String response = "";
                if (connection != null) {
                    response = connection.readPacket();
                }
                return response;
            }
        };
        task.setOnSucceeded((WorkerStateEvent taskFinishEvent) -> {
            String response = task.getValue();
            if (response.contains("Rematch Requested")) {
                // Switch to the rematch dialogue
                scorecardPaneMultiplayer.setVisible(false);
                rematchPane.setVisible(true);
                if (1 == player) {
                    winnerRectangleMultiplayerRematch.setVisible(true);
                    winnerCircleMultiplayerRematch.setVisible(false);
                } else {
                    winnerRectangleMultiplayerRematch.setVisible(false);
                    winnerCircleMultiplayerRematch.setVisible(true);
                }
            } else if (response.contains("Rematch Declined")) {
                try {
                    exitGame();
                } catch (IOException ex) {
                    Logger.getLogger(TicTacToeGameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        new Thread(task).start();
    }

    /**
     * Sends a message accepting the rematch and starts a new game.
     */
    @FXML
    private void onAcceptRematchClick() {
        // Junk string to feed the listenForRematch thread
        connection.sendPacket(" END MESSAGE");
        // actual message
        connection.sendPacket("Rematch Accepted END MESSAGE");
        newGame();
    }

    /**
     * Sends a message declining the rematch and exits the match.
     *
     * @throws IOException
     */
    @FXML
    private void onDeclineRematchClick() throws IOException {
        // Junk string to feed the listenForRematch thread
        connection.sendPacket(" END MESSAGE");
        // actual message
        connection.sendPacket("Rematch Declined END MESSAGE");
        exitGame();
    }
}
