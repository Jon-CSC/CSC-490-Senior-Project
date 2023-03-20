/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    private Button buttonProfile;
    
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
    
    @FXML
    private ImageView profileImgViewer;
    
    @FXML
    private Text textGameDesc;
    
    @FXML
    private Text textGameTitle;
    
    @FXML
    private VBox vBoxCardText;

    private Image gc01, gc02, gc03, gc04, gc05, gc06;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCardImages();
        App.getStage().setWidth(800);
        App.getStage().setHeight(600);
        buttonProfile.setText(App.getCurrentUser());
        vBoxCardText.toBack();
        textGameTitle.setVisible(false);
        textGameDesc.setVisible(false);
    } 
    
    @FXML
    void clickedGameCard01(MouseEvent event) throws IOException {
        cardAnimation(gameCard01);
//        playSnake();
    }

    @FXML
    void clickedGameCard02(MouseEvent event) throws IOException {
        cardAnimation(gameCard02);
//        playBattleship();
    }

    @FXML
    void clickedGameCard03(MouseEvent event) {
        cardAnimation(gameCard03);
    }

    @FXML
    void clickedGameCard04(MouseEvent event) {
        cardAnimation(gameCard04);
    }

    @FXML
    void clickedGameCard05(MouseEvent event) throws IOException {
        cardAnimation(gameCard05);
//        playTicTacToe();
    }

    @FXML
    void clickedGameCard06(MouseEvent event) {
        cardAnimation(gameCard06);
//        playNetworkTest();
    }
    
    @FXML
    void keyboardCommand(KeyEvent event) throws IOException {
        // Responds to keyboard input when scene is up
        // Currently only supports [Esc] to logout
        if (event.getCode().toString().equalsIgnoreCase("ESCAPE")) {
            App.setRoot("LoginPage");
            App.setCurrentUser(null);
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
        App.setRoot("TicTacToeGame");
    }
    
    public void playChess() throws IOException {
//        App.setRoot("");
    }
    
    public void playNetworkTest() {
//        App.setRoot("NetworkTest");
    }
    
    private void loadCardImages() {
        // Loads images from \Image\ directory in the default package for use as thumbnails
        // Spits out error if it couldn't find them
        try {
            gc01 = new Image(getClass().getResourceAsStream("\\Images\\snake.png"));
            gc02 = new Image(getClass().getResourceAsStream("\\Images\\battleship.png"));
            gc03 = new Image(getClass().getResourceAsStream("\\Images\\checkers.png"));
            gc04 = new Image(getClass().getResourceAsStream("\\Images\\chess.png"));
            gc05 = new Image(getClass().getResourceAsStream("\\Images\\tictactoe.png"));
            gc06 = new Image(getClass().getResourceAsStream("\\Images\\comingsoon.png"));
            gameCard01.setFill(new ImagePattern(gc01));
            gameCard02.setFill(new ImagePattern(gc02));
            gameCard03.setFill(new ImagePattern(gc03));
            gameCard04.setFill(new ImagePattern(gc04));
            gameCard05.setFill(new ImagePattern(gc05));
            gameCard06.setFill(new ImagePattern(gc06));
        } catch(Exception e) {
            System.out.println("Thumbnail images were not loaded correctly. "
                    + "Check that paths are correct and images exist.");
        }
    }
        
    private void cardAnimation(Rectangle card) {
        // Draws card on top of other elements
        card.getParent().toFront();
        
        ParallelTransition parentTransition = new ParallelTransition();
        
        SequentialTransition scaleCardTransition = new SequentialTransition();
        
        // Card flip animation using two scaling animations stretching and shrinking card
        ScaleTransition scale01 = new ScaleTransition(Duration.seconds(0.25), card);
        scale01.setToX(0);
        scale01.setToY(2.0);
        scale01.setCycleCount(1);
        scaleCardTransition.getChildren().add(scale01);
        scale01.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                card.setFill(rgb(58,58,58));
            }
        });
        
        ScaleTransition scale02 = new ScaleTransition(Duration.seconds(0.5), card);
        scale02.setToX(4.1);
        scale02.setToY(2.8);
        scale02.setCycleCount(1);
        scaleCardTransition.getChildren().add(scale02);
        
        parentTransition.getChildren().add(scaleCardTransition);

        // Movement animation to position card in center of scene
        SequentialTransition moveCardTransition = new SequentialTransition();
        
        double xPosMid = (App.getStage().getWidth() / 4.0) - 50;
        double yPosMid = (App.getStage().getHeight() / 4.0) - 50;
        double xPosFinal = (App.getStage().getWidth() / 2.0) - 142;
        double yPosFinal = (App.getStage().getHeight() / 2.0) - 142;
        
        Path path01 = new Path();
        path01.getElements().add(new MoveToAbs(card));
        path01.getElements().add(new LineToAbs(card, xPosMid, yPosMid));
        Path path02 = new Path();
        path02.getElements().add(new MoveToAbs(card, xPosMid, yPosMid));
        path02.getElements().add(new LineToAbs(card, xPosFinal, yPosFinal));
//        BorderPaneRoot.getChildren().addAll(path01);
//        BorderPaneRoot.getChildren().addAll(path02);
        
        
        PathTransition pt01 = new PathTransition();
        pt01.setNode(card);
        pt01.setDuration(Duration.seconds(0.25));
        pt01.setPath(path01);
        pt01.setCycleCount(1);
        moveCardTransition.getChildren().add(pt01);
        
        PathTransition pt02 = new PathTransition();
        pt02.setNode(card);
        pt02.setDuration(Duration.seconds(0.5));
        pt02.setPath(path02);
        pt02.setCycleCount(1);
        moveCardTransition.getChildren().add(pt02);
        
        parentTransition.getChildren().add(moveCardTransition);
        parentTransition.setRate(1.5);
        parentTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vBoxCardText.toFront();
                textGameTitle.setVisible(true);
                textGameDesc.setVisible(true);
            }
        }
        );
        parentTransition.play();
    }
    
    public static class MoveToAbs extends MoveTo {

        public MoveToAbs(Node node) {
            super(node.getLayoutBounds().getWidth() / 2, node.getLayoutBounds().getHeight() / 2);
        }

        public MoveToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

    public static class LineToAbs extends LineTo {

        public LineToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }
}
