/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author justi
 */
public class GameLibraryController implements Initializable {

    @FXML
    private BorderPane BorderPaneRoot;
    
    @FXML
    private Rectangle gameCard01;

    @FXML
    private Rectangle gameCard02;

    @FXML
    private Rectangle gameCard03;

    @FXML
    private Rectangle gameCard04;

    @FXML
    private Rectangle gameCard05;

    @FXML
    private Rectangle gameCard06;

    private Image gc01, gc02, gc03, gc04, gc05, gc06;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCardImages();
    } 
    private void loadCardImages() {
        // Loads images from \Image\ directory in the default package for use as thumbnails
        // Spits out error if it couldn't find them
        try {
            gc01 = new Image(getClass().getResourceAsStream("\\Images\\snake_thumbnail.png"));
            gc02 = new Image(getClass().getResourceAsStream("\\Images\\battleship_thumbnail.png"));
            gameCard01.setFill(new ImagePattern(gc01));
            gameCard02.setFill(new ImagePattern(gc02));
        } catch(Exception e) {
            System.out.println("Thumbnail images were not loaded correctly. "
                    + "Check that paths are correct and images exist.");
        }
    }
        
    @FXML
    void clickedGameCard01(MouseEvent event) throws IOException {
//        playSnake();
        
        // Below is some test animations for scaling thumbnail cards
        ParallelTransition plt = new ParallelTransition();
        
//        ScaleTransition scaleX01 = new ScaleTransition(Duration.seconds(0.4), gameCard01);
//        scaleX01.setToX(0);
//        scaleX01.setToY(2.0);
//        scaleX01.setCycleCount(1);
//        plt.getChildren().add(scaleX01);
        
        Path path01 = new Path();
        path01.getElements().add(new MoveTo(gameCard01.getX(), gameCard01.getY()));
//        path01.getElements().add(new CubicCurveTo(180, 60, 250, 340, 420, 240));
        path01.getElements().add(new LineTo(600, 200));
        BorderPaneRoot.getChildren().addAll(path01);
        
        PathTransition pt01 = new PathTransition();
        pt01.setDuration(Duration.seconds(2));
        pt01.setPath(path01);
        pt01.setNode(gameCard01);
        pt01.setCycleCount(2);
        pt01.play();
        
        
//        plt.play();
    }

    @FXML
    void clickedGameCard02(MouseEvent event) throws IOException {
        playBattleship();
    }

    @FXML
    void clickedGameCard03(MouseEvent event) {

    }

    @FXML
    void clickedGameCard04(MouseEvent event) {

    }

    @FXML
    void clickedGameCard05(MouseEvent event) {

    }

    @FXML
    void clickedGameCard06(MouseEvent event) {

    }
    
    @FXML
    void keyboardCommand(KeyEvent event) throws IOException {
        // Responds to keyboard input when scene is up
        // Currently only supports [Esc] to logout
        if (event.getCode().toString().equalsIgnoreCase("ESCAPE")) {
            App.setRoot("LoginPage");
        }
    }
    
    // Below are functions to switch to the respective game scene.
    // These are placeholders for now, and may only switch to a blank scene.
    public void playSnake() throws IOException {
        App.setRoot("SnakeGame");
    }
    
    public void playBattleship() throws IOException {
        App.setRoot("BattleshipGame");
    }
    
    public void playCheckers() throws IOException {
//        App.setRoot("");
    }
    
    public void playTicTacToe() throws IOException {
//        App.setRoot("");
    }
    
    public void playChess() throws IOException {
//        App.setRoot("");
    }
}
