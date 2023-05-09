/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation.Status;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
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
    private Button buttonPlayGame;
    
    @FXML
    private StackPane centerStackPane;
    
    @FXML
    private Rectangle gameCard01, gameCard02, gameCard03, gameCard04, gameCard05, gameCard06;
    
    @FXML
    private Rectangle ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5;
    
    @FXML
    private HBox starBox1, starBox2, starBox3, starBox4, starBox5;

    @FXML
    private Circle profilePicCircle;

    @FXML
    private Button username;

    @FXML
    private VBox dropDownMenu;
    
    @FXML
    private Text textGameDesc;
    
    @FXML
    private Text textGameTitle;

    @FXML
    private Label buttonCloseExpandedCard;
    
    @FXML
    private GridPane gridContainerGameCards;
    
    @FXML
    private GridPane gridPaneCardText;

    private Image gc01, gc02, gc03, gc04, gc05, gc06;
    
    private Image starFilled, starHollow;
    
    private ImagePattern starFilledIP, starHollowIP;
    
    private boolean cardExpanded = false;
    
    private float selectedGameRating = 1.8f;
    
    private Game selectedGame;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCardImages();
        App.getStage().setWidth(900);
        App.getStage().setHeight(640);
        App.getStage().centerOnScreen();
        UserAccount currentUser = LocalUserAccount.getInstance().getUser();
        username.setText(currentUser.getUserID());
        
        Image profilePic;
        try {
            URL avatarURL = new URL(currentUser.getAvatarURL());
            profilePic = new Image(avatarURL.toString());
            if (profilePic.isError()) {
                throw new Exception("Image URL does not contain direct image file. Cannot load!");
            }
            profilePicCircle.setFill(new ImagePattern(profilePic));
        } catch (Exception e) {
//             trusty penguin fallback
            profilePic = new Image(getClass().getResourceAsStream("Images/penguin01.jpg"));
            profilePicCircle.setFill(new ImagePattern(profilePic));
        }
        
        gridPaneCardText.toBack();
        textGameTitle.setVisible(false);
        textGameDesc.setVisible(false);
        ratingStar1.setVisible(false);
        ratingStar2.setVisible(false);
        ratingStar3.setVisible(false);
        ratingStar4.setVisible(false);
        ratingStar5.setVisible(false);
        buttonPlayGame.setVisible(false);
        buttonCloseExpandedCard.setVisible(false);
        dropDownMenu.setVisible(false);
        
        // Rating star click events
        ratingStar1.setOnMouseClicked(e -> {
            boolean successful = LocalUserAccount.getInstance().recordRating(selectedGame, 1);
            Alert noGameAlert = new Alert(AlertType.INFORMATION);
            noGameAlert.setContentText("You rated the game 1 star!");
            noGameAlert.show();
        });
        ratingStar2.setOnMouseClicked(e -> {
            boolean successful = LocalUserAccount.getInstance().recordRating(selectedGame, 2);
            Alert noGameAlert = new Alert(AlertType.INFORMATION);
            noGameAlert.setContentText("You rated the game 2 stars!");
            noGameAlert.show();
        });
        ratingStar3.setOnMouseClicked(e -> {
            boolean successful = LocalUserAccount.getInstance().recordRating(selectedGame, 3);
            Alert noGameAlert = new Alert(AlertType.INFORMATION);
            noGameAlert.setContentText("You rated the game 3 stars!");
            noGameAlert.show();
        });
        ratingStar4.setOnMouseClicked(e -> {
            boolean successful = LocalUserAccount.getInstance().recordRating(selectedGame, 4);
            Alert noGameAlert = new Alert(AlertType.INFORMATION);
            noGameAlert.setContentText("You rated the game 4 stars!");
            noGameAlert.show();
        });
        ratingStar5.setOnMouseClicked(e -> {
            boolean successful = LocalUserAccount.getInstance().recordRating(selectedGame, 5);
            Alert noGameAlert = new Alert(AlertType.INFORMATION);
            noGameAlert.setContentText("You rated the game 5 stars!");
            noGameAlert.show();
        });
        
        
        setUpRatingStars();
        
        // stop any existing timers and push their results online
        Timer timer = App.getTimer();
        if(null != timer) {
            if(timer.isRunning() && LocalUserAccount.getInstance().isLoggedIn()) {
                timer.stop();
                LocalUserAccount.getInstance().recordTime(timer);
            }
            App.clearTimer();
        }
    } 
    
    private void setUpRatingStars() {
        // Rating star hover events
        starBox1.setOnMouseEntered(e -> { setVisibleRating(1); });
        starBox2.setOnMouseEntered(e -> { setVisibleRating(2); });
        starBox3.setOnMouseEntered(e -> { setVisibleRating(3); });
        starBox4.setOnMouseEntered(e -> { setVisibleRating(4); });
        starBox5.setOnMouseEntered(e -> { setVisibleRating(5); });

        // Reset to game's average rating when mouse moves off any star
        starBox1.setOnMouseExited(e -> { setVisibleRating((int)Math.round(selectedGameRating)); });
        starBox2.setOnMouseExited(e -> { setVisibleRating((int)Math.round(selectedGameRating)); });
        starBox3.setOnMouseExited(e -> { setVisibleRating((int)Math.round(selectedGameRating)); });
        starBox4.setOnMouseExited(e -> { setVisibleRating((int)Math.round(selectedGameRating)); });
        starBox5.setOnMouseExited(e -> { setVisibleRating((int)Math.round(selectedGameRating)); });
    }
    
    private void setVisibleRating(int rating) {
        ratingStar1.setFill((rating>=1) ? (starFilledIP) : (starHollowIP));
        ratingStar2.setFill((rating>=2) ? (starFilledIP) : (starHollowIP));
        ratingStar3.setFill((rating>=3) ? (starFilledIP) : (starHollowIP));
        ratingStar4.setFill((rating>=4) ? (starFilledIP) : (starHollowIP));
        ratingStar5.setFill((rating>=5) ? (starFilledIP) : (starHollowIP));
    }
    
    @FXML
    void clickedGameCard01(MouseEvent event) throws IOException {
        selectedGame = Game.SNAKE;
        cardAnimation(gameCard01);
        populateCardDetails();
        initializeGameCardAverageRating();
    }

    @FXML
    void clickedGameCard02(MouseEvent event) throws IOException {
        selectedGame = Game.BATTLESHIP;
        cardAnimation(gameCard02);
        populateCardDetails();
        initializeGameCardAverageRating();
    }

    @FXML
    void clickedGameCard03(MouseEvent event) {
        selectedGame = Game.CHECKERS;
        cardAnimation(gameCard03);
        populateCardDetails();
        initializeGameCardAverageRating();
    }

    @FXML
    void clickedGameCard04(MouseEvent event) {
        selectedGame = Game.CHESS;
        cardAnimation(gameCard04);
        populateCardDetails();
        initializeGameCardAverageRating();
    }

    @FXML
    void clickedGameCard05(MouseEvent event) throws IOException {
        selectedGame = Game.TICTACTOE;
        cardAnimation(gameCard05);
        populateCardDetails();
        initializeGameCardAverageRating();
    }

    @FXML
    void clickedGameCard06(MouseEvent event) {
        selectedGame = Game.JAVASTROIDS;
        cardAnimation(gameCard06);
        populateCardDetails();
        initializeGameCardAverageRating();
    }
    
    @FXML
    void keyboardCommand(KeyEvent event) throws IOException {
        // Responds to keyboard input when scene is up
        // Currently only supports [Esc] to logout
        if (event.getCode().toString().equalsIgnoreCase("ESCAPE")) {
            logOut();
        }
    }

    @FXML
    void logOut() throws IOException {
        App.setRoot("LoginPage");
        LocalUserAccount.getInstance().logout();
    }

    @FXML
    void goToLeaderboard() throws IOException {
        App.setRoot("Leaderboard");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("Settings");
    }
    
    @FXML
    void launchSelectedGame(ActionEvent event) throws IOException {
        switch (selectedGame) {
            default:
                displayNoGameDialogBoxError();
                break;
            case NOGAME:
                displayNoGameDialogBoxError();
                break;
            case SNAKE:
                playSnake();
                break;
            case BATTLESHIP:
                playBattleship();
                break;
            case CHECKERS:
                playCheckers();
                break;
            case CHESS:
                playChess();
                break;
            case TICTACTOE:
                playTicTacToe();
                break;
            case JAVASTROIDS:
                playJavaStroids();
                break;
        }
    }
    
    private void initializeGameCardAverageRating() {
        // ToDo:
        // Query firestore for GameResults collection
        // Find document of currently selectedGame
        // Pull average-rating and rating-count
        // some other calculate-y stuff....
        setVisibleRating((int)Math.round(selectedGameRating));
    }
    
    private void populateCardDetails() {
        String gameTitle, gameDesc;
        switch (selectedGame) {
            default:
                gameTitle = "No Title";
                gameDesc = "No description available.";
                break;
            case NOGAME:
                gameTitle = "No Title";
                gameDesc = "No description available.";
                break;
            case SNAKE:
                gameTitle = "Snake";
                gameDesc = "•1 Player"
                        + "\n•Local Play"
                        + "\n\nControl a snake with an unrelenting apetite."
                        + " Gather as much food as you can by controlling"
                        + " the snake's direction. The snake grows as he eats, so watch out to"
                        + " not collide with himself or the walls around him.";
                break;
            case BATTLESHIP:
                gameTitle = "Battleship";
                gameDesc = "•2 Players"
                        + "\n•Online Play"
                        + "\n\nReady your ships, captain! In this game, each player will"
                        + " take aim and fire somewhere on the grid-map in hopes of hitting"
                        + " the other player's ship. Each ship will be oriented and sized"
                        + " differently, so think before you aim!";
                break;
            case CHECKERS:
                gameTitle = "Checkers";
                gameDesc = "•2 Players"
                        + "\n•Local & Online Play"
                        + "\n\nThe classic board game we all know and love. Try to beat"
                        + " your opponent by eliminating as many of their checkers as possible.";
                break;
            case CHESS:
                gameTitle = "Chess";
                gameDesc = "•2 Players"
                        + "\n•Local & Online Play"
                        + "\n\nThe most well-known tactical board game in the world."
                        + " Try to outwit your opponent based on the rules of the game."
                        + " Each game piece has a unique moveset. Your goal is to"
                        + " checkmate the opponent and defeat their king.";
                break;
            case TICTACTOE:
                gameTitle = "Tic-Tac-Toe";
                gameDesc = "•2 Players"
                        + "\n•Local & Online Play"
                        + "\n\nTest your skills in a 3x3 grid. Attempt to connect"
                        + " three of your shapes in a row before your opponent can"
                        + " block you to win the game.";
                break;
            case JAVASTROIDS:
                gameTitle = "JavaStroids";
                gameDesc = "•1 Player"
                        + "\n•Local Play"
                        + "\n\nThe classic vector-graphics game, reimagined in Java! "
                        + "Based on the original game 'Asteroids' for arcade systems, fight your "
                        + "way through perilous danger as you cross an asteroid field. Shoot down "
                        + "those rocks to survive as long as you can.";
                break;
        }
        textGameTitle.setText(gameTitle);
        textGameDesc.setText(gameDesc);
    } 
    
    private void playSnake() throws IOException {
        Timer snakeTimer = new Timer(Game.SNAKE);
        snakeTimer.start();
        App.setTimer(snakeTimer);
        App.setRoot("games/snake/SnakeMainMenu");
    }
    
    private void playBattleship() throws IOException {
//        App.setRoot("games/battleship/BattleshipGame");
//        Timer battleshipTimer = new Timer(Game.BATTLESHIP);
//        battleshipTimer.start();
//        App.setTimer(battleshipTimer);
        displayNoGameDialogBoxError();
    }
    
    private void playCheckers() throws IOException {
        //App.setRoot("");
//        Timer checkersTimer = new Timer(Game.CHECKERS);
//        checkersTimer.start();
//        App.setTimer(checkersTimer);
        displayNoGameDialogBoxError();
    }
    
    private void playTicTacToe() throws IOException {
        Timer tictactoeTimer = new Timer(Game.TICTACTOE);
        tictactoeTimer.start();
        App.setTimer(tictactoeTimer);
        App.setRoot("games/tictactoe/TicTacToeMainMenu");
    }
    
    private void playChess() throws IOException {
        //App.setRoot("");
//        Timer chessTimer = new Timer(Game.CHESS);
//        chessTimer.start();
//        App.setTimer(chessTimer);
        displayNoGameDialogBoxError();
    }
    
    private void playJavaStroids() throws IOException {
        Timer javastroidsTimer = new Timer(Game.JAVASTROIDS);
        javastroidsTimer.start();
        App.setTimer(javastroidsTimer);
        App.setRoot("games/javastroids/mainMenu");
    }
    
    @FXML
    private void goToProfilePage() throws IOException {
        ProfilePageController.configureUserProfile(LocalUserAccount.getInstance().getUser().getUserID());
        App.setRoot("ProfilePage");
    }
    
    private void playNoGame() {
        displayNoGameDialogBoxError();
    }
    
    private void displayNoGameDialogBoxError() {
        Alert noGameAlert = new Alert(AlertType.ERROR);
        noGameAlert.setContentText("No game here yet, sorry!");
        noGameAlert.show();
    }
    
    private void loadCardImages() {
        // Loads images from \Image\ directory in the default package for use as thumbnails
        // Spits out error if it cannot find them
        try {
            gc01 = new Image(getClass().getResourceAsStream("Images/snake.png"));
            gc02 = new Image(getClass().getResourceAsStream("Images/battleship.png"));
            gc03 = new Image(getClass().getResourceAsStream("Images/checkers.png"));
            gc04 = new Image(getClass().getResourceAsStream("Images/chess.png"));
            gc05 = new Image(getClass().getResourceAsStream("Images/tictactoe.png"));
            gc06 = new Image(getClass().getResourceAsStream("Images/javastroids.png"));
            
            starFilled = new Image(getClass().getResourceAsStream("Images/star_yellow.png"));
            starHollow = new Image(getClass().getResourceAsStream("Images/star_hollow.png"));
            
            starFilledIP = new ImagePattern(starFilled);
            starHollowIP = new ImagePattern(starHollow);
            
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
        
        ratingStar1.setFill(starFilledIP);
        ratingStar2.setFill(starFilledIP);
        ratingStar3.setFill(starFilledIP);
        ratingStar4.setFill(starFilledIP);
        ratingStar5.setFill(starFilledIP);
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
            card.setFill(rgb(32,32,32));
//            card.setFill(rgb(16,38,58));
            card.setArcWidth(5);
            card.setArcHeight(5);
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
        
        double xPosFinal = (App.getStage().getScene().getWidth() / 2.0) - cardPosX + 55;
        double yPosFinal = (App.getStage().getScene().getHeight() / 2.0) - cardPosY + 80;
        
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
            gridPaneCardText.toFront();
            textGameTitle.setVisible(true);
            textGameDesc.setVisible(true);
            ratingStar1.setVisible(true);
            ratingStar2.setVisible(true);
            ratingStar3.setVisible(true);
            ratingStar4.setVisible(true);
            ratingStar5.setVisible(true);
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
                gridPaneCardText.toBack();
                textGameTitle.setVisible(false);
                textGameDesc.setVisible(false);
                ratingStar1.setVisible(false);
                ratingStar2.setVisible(false);
                ratingStar3.setVisible(false);
                ratingStar4.setVisible(false);
                ratingStar5.setVisible(false);
                buttonPlayGame.setVisible(false);
                buttonCloseExpandedCard.setVisible(false);
            }
        });
    }

    @FXML
    public void clickedProfilePic() {
        if (dropDownMenu.isVisible()) {
            dropDownMenu.setVisible(false);
        } else {
            dropDownMenu.setVisible(true);
        }
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
