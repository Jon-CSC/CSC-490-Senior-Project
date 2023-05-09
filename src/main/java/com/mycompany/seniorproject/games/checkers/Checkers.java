/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.checkers;

import com.mycompany.seniorproject.App;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Checkers extends Application {

    @FXML
    void initialize() {

        App.getStage().setWidth(500);
        App.getStage().setHeight(500);

        start(App.getStage());

    }

    /**
     * Draws a game status message.
     *
     * @see BoardLogic#message()
     * @param gc a given GraphicsContext object
     * @param str a string
     * @param x text start position x-coordinate, pixels
     * @param y text start position y-coordinate, pixels
     * @param size text size, pixels
     */
    private void drawMessage(GraphicsContext gc, String str,
            double x, double y, double size) {
        gc.setFont(new Font("Arial", size));
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillText(str, x, y);
    }

    /**
     * @param primaryStage JavaFX primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");

        Group root = new Group();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);

        Canvas canvas = new Canvas(500, 500);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        BoardLogic board = new BoardLogic(50.0, 50.0, 400.0, 0.1, 8, 3);
        AIPlayer aiPlayer = new AIPlayer(); // not functional yet!
        board.draw(gc);
        drawMessage(gc, "Click C-computer or H-human opponent",
                50.0, 40.0, 22.0);

        primaryScene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                if (!board.isOpponentSet()) {
                    return;
                }
                if (board.isGameOverDelayed()) {
                    board.reset();
                }

                if (board.turn() && aiPlayer.isActive()) {
                    aiPlayer.runTurn(board);
                } else if (board.someLegalPos()) {
                    board.attemptMove(board.decodeMouse(e.getX(), e.getY()));
                } else {
                    board.highlightMoves(board.decodeMouse(e.getX(), e.getY()));
                }

                // drawing stuff
                gc.clearRect(0, 0, gc.getCanvas().getWidth(),
                        gc.getCanvas().getHeight());
                board.draw(gc);
                if (board.isOpponentSet()) {
                    drawMessage(gc, board.message(), 50.0, 40.0, 22.0);
                } else {
                    drawMessage(gc, "Click C-computer or H-human player",
                            50.0, 40.0, 22.0);
                }
            }
        });

        primaryScene.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!board.isOpponentSet() && event.getText().equals("h")) {
                    board.setOpponent();
                    aiPlayer.setInactive();
                } else if (!board.isOpponentSet() && event.getText().equals("c")) {
                    board.setOpponent();
                    aiPlayer.setActive();
                }

                gc.clearRect(0, 0, gc.getCanvas().getWidth(),
                        gc.getCanvas().getHeight());
                board.draw(gc);
                if (board.isOpponentSet()) {
                    drawMessage(gc, board.message(), 50.0, 40.0, 22.0);
                }
            }
        });

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
