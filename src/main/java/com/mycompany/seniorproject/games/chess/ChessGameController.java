/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.chess;

import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.games.snake.SnakeGame;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 *
 * @author justi
 */
public class ChessGameController implements Initializable {
    
    @FXML
    public Pane GamePane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ChessGame gameInstance = new ChessGame(GamePane);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChessGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        App.getStage().setWidth(850);
        App.getStage().setHeight(877);
        App.getStage().centerOnScreen();
        App.getStage().setResizable(true);
    }
}
