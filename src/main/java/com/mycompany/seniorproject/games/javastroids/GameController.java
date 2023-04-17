/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.javastroids;

import com.mycompany.seniorproject.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

/**
 * Controller class for the active gameplay of Asteroid game. Initiates the 
 * game engine and scene to be rendered to.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class GameController {

    @FXML
    private Canvas canvasGame;
    
    /**
     * Method that is invoked upon this FXML scene becoming initialized.
     */
    @FXML
    public void initialize() {
        canvasGame.setHeight(App.getScene().getHeight());
        canvasGame.setWidth(App.getScene().getWidth());
        GameLoop loop = new GameLoop();
        loop.setGraphicsContext(canvasGame);
        loop.initializeGameScene(App.getScene());
        loop.start();
    }
}
