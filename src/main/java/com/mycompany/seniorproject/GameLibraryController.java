/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.ImagePattern;
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
    private Button buttonPlayGame;
    
    @FXML
    private Button buttonCloseExpandedCard;
    
    @FXML
    private StackPane centerStackPane;
    
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
    private Rectangle ratingCard;
    
    @FXML
    private ImageView profileImgViewer;
    
    @FXML
    private Text textGameDesc;
    
    @FXML
    private Text textGameTitle;
    
    @FXML
    private VBox vBoxCardText;
    
    @FXML
    private GridPane gridContainerGameCards;

    private Image gc01, gc02, gc03, gc04, gc05, gc06;
    
    private boolean cardExpanded = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCardImages();
        App.getStage().setWidth(1000);
        App.getStage().setHeight(625);
        buttonProfile.setText(App.getCurrentUser());
        vBoxCardText.toBack();
        textGameTitle.setVisible(false);
        textGameDesc.setVisible(false);
        ratingCard.setVisible(false);
        buttonPlayGame.setVisible(false);
        buttonCloseExpandedCard.setVisible(false);
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
        // Spits out error if it cannot find them
        try {
            gc01 = new Image(getClass().getResourceAsStream("\\Images\\snake.png"));
            gc02 = new Image(getClass().getResourceAsStream("\\Images\\battleship.png"));
            gc03 = new Image(getClass().getResourceAsStream("\\Images\\checkers.png"));
            gc04 = new Image(getClass().getResourceAsStream("\\Images\\chess.png"));
            gc05 = new Image(getClass().getResourceAsStream("\\Images\\tictactoe.png"));
            gc06 = new Image(getClass().getResourceAsStream("\\Images\\comingsoon.png"));
            populateCardImages();
        } catch(Exception e) {
            System.out.println("Thumbnail images were not loaded correctly. "
                    + "Check that paths are correct and images exist.");
        }
    }
    
    private void populateCardImages() {
        gameCard01.setFill(new ImagePattern(gc01));
        gameCard02.setFill(new ImagePattern(gc02));
        gameCard03.setFill(new ImagePattern(gc03));
        gameCard04.setFill(new ImagePattern(gc04));
        gameCard05.setFill(new ImagePattern(gc05));
        gameCard06.setFill(new ImagePattern(gc06));
    }
        
    private void cardAnimation(Rectangle card) {
        
        // Get position relative to current scene
        
        Bounds cardBoundsInScene = card.localToScene(card.getBoundsInLocal());
        double cardPosX = cardBoundsInScene.getMinX() + (card.getWidth()/2);
        double cardPosY = cardBoundsInScene.getMinY() + (card.getHeight()/2);
        
        
        // Draws card on top of other elements, then instantiate animations
        
        card.getParent().toFront();
        
        ParallelTransition parentTransition = new ParallelTransition();
        SequentialTransition scaleCardTransition = new SequentialTransition();
        
        
        // Card flip animation using two scaling animations shrinking and stretching card
        
        ScaleTransition scale01 = new ScaleTransition(Duration.seconds(0.25), card);
        scale01.setToX(0);
        scale01.setToY(2.0);
        scale01.setCycleCount(1);
        scaleCardTransition.getChildren().add(scale01);
        scale01.setOnFinished(e -> {
            card.setFill(rgb(58,58,58));
            card.setArcWidth(10);
            card.setArcHeight(15);
        });
        ScaleTransition scale02 = new ScaleTransition(Duration.seconds(0.5), card);
        scale02.setToX(4.7);
        scale02.setToY(3.0);
        scale02.setCycleCount(1);
        scale02.setOnFinished(e -> {
            if (cardExpanded) {
                populateCardImages();
                card.setArcWidth(15);
                card.setArcHeight(15);
            }
        });
        scaleCardTransition.getChildren().add(scale02);
        
        parentTransition.getChildren().add(scaleCardTransition);

        
        // Movement animation to position card in center of scene
        
        SequentialTransition moveCardTransition = new SequentialTransition();
        
        double xPosFinal = (App.getStage().getScene().getWidth() / 2.0) - cardPosX + 45;
        double yPosFinal = (App.getStage().getScene().getHeight() / 2.0) - cardPosY + 65;
        
        Path path01 = new Path();
        path01.getElements().add(new MoveToAbs(card));
        path01.getElements().add(new LineToAbs(card, xPosFinal, yPosFinal));
        PathTransition pathTrace01 = new PathTransition();
        pathTrace01.setNode(card);
        pathTrace01.setDuration(Duration.seconds(0.75));
        pathTrace01.setPath(path01);
        pathTrace01.setCycleCount(1);
        pathTrace01.setOnFinished(e -> {
            parentTransition.pause();
            vBoxCardText.toFront();
            textGameTitle.setVisible(true);
            textGameDesc.setVisible(true);
            ratingCard.setVisible(true);
            buttonPlayGame.setVisible(true);
            buttonCloseExpandedCard.setVisible(true);
        });
        
        moveCardTransition.getChildren().add(pathTrace01);
        
        parentTransition.getChildren().add(moveCardTransition);
        parentTransition.setAutoReverse(true);
        parentTransition.setCycleCount(2);
        parentTransition.setRate(1.5);
        parentTransition.setOnFinished(e -> {
            cardExpanded = false;
            // Manually setting scale back to 1, fixes bug with animation 
            // scaling not returning game card rectangles to original size.
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            System.out.println("FINISHED");
        });
        parentTransition.playFromStart();
        
        buttonCloseExpandedCard.setOnMouseClicked(e -> {
            if(parentTransition.getStatus().equals(Status.PAUSED)) {
                cardExpanded = true;
                parentTransition.play();
                vBoxCardText.toBack();
                textGameTitle.setVisible(false);
                textGameDesc.setVisible(false);
                ratingCard.setVisible(false);
                buttonPlayGame.setVisible(false);
                buttonCloseExpandedCard.setVisible(false);
            }
        });
    }
    
    public static class MoveToAbs extends MoveTo {

        public MoveToAbs(Node node) {
            super(node.getLayoutBounds().getWidth() / 2, 
                    node.getLayoutBounds().getHeight() / 2);
        }

        public MoveToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, 
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }

    public static class LineToAbs extends LineTo {

        public LineToAbs(Node node, double x, double y) {
            super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2, 
                    y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
        }
    }
}
