package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.internal.DownloadAccountResponse;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * FXML Controller Class
 *
 * @author juan
 */
public class LeaderboardController implements Initializable {

    enum GameType {
        SINGLEPLAYER,
        MULTIPLAYER
    }

    /**
     * This class is used for storing users locally for performance reasons.
     */
    static class User {
        String userID, avatarURL;
        long stat;
        public User(String userID, String avatarURL, long stat) {
            this.userID = userID;
            this.avatarURL = avatarURL;
            this.stat = stat;
        }
        public long getStat() {
            return stat;
        }
        public String getUserID() {
            return userID;
        }
        public String getAvatarURL() {
            return avatarURL;
        }
    }

    @FXML private Button snakeButton, javastroidsButton, ticTacToeButton, battleshipButton, chessButton, checkersButton;
    @FXML private VBox leaderboard, info;
    @FXML private Pane pane;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statLabel, noScoresSubmitted;
    private ArrayList<User> listOfUsers = new ArrayList<User>();
    private User currentUser;
    private HashMap gameData;
    private String userID, avatarURL;
    private long stat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.getStage().setWidth(900);
        App.getStage().setHeight(640);
        App.getStage().centerOnScreen();
        progressIndicator.setVisible(false);
        info.setVisible(false);
        pane.setVisible(false);
        noScoresSubmitted.setVisible(false);
        Task task = new Task<Void>() {
            @Override public Void call() throws ExecutionException, InterruptedException {
                progressIndicator.setVisible(true);
                setFocusedGame(Game.SNAKE);
                progressIndicator.setVisible(false);
                return null;
            }
        };
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    /**
     * Retrieves and displays the scores on screen for one game.
     * @param game the desired game
     * @param gameType the type of game (singleplayer or multiplayer)
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void loadScores(Game game, GameType gameType) throws ExecutionException, InterruptedException {
        // Clear the leaderboard first
        leaderboard.getChildren().clear();
        listOfUsers.clear();
        noScoresSubmitted.setVisible(false);

        // Based on the GameType, the field String will be adjusted to what is needed for the Query
        String field;
        if (gameType == gameType.SINGLEPLAYER) {
            field = game.getScoreField();
        } else {
            field = game.getWinsField();
        }

        // Create a Query that sorts scores/wins in descending order
        CollectionReference users = App.fstore.collection("Users");
        Query query = users
                .whereGreaterThan("gameData." + field, 0)
                .orderBy("gameData." + field, Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        // Go through the documents in the QuerySnapshot and add them to a list of users
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            userID = document.getId();
            avatarURL = document.getData().get("avatarURL").toString();
            gameData = (HashMap) document.getData().get("gameData");
            stat = (long)gameData.getOrDefault(field, (long)0);
            currentUser = new User(userID, avatarURL, stat);
            listOfUsers.add(currentUser);
        }
        // Update the GUI with scores/wins
        Platform.runLater(() -> {
            for (int i = 0; i < listOfUsers.size(); i++) {
                createScoreCard(i + 1,
                        listOfUsers.get(i).getUserID(),
                        listOfUsers.get(i).getAvatarURL(),
                        listOfUsers.get(i).getStat());
            }
            // If there are no scores to show, display a message
            if (leaderboard.getChildren().isEmpty()) {
                noScoresSubmitted.setVisible(true);
                statLabel.setText("");
            }
        });
    }

    /**
     *
     * @param ranking where a user places on the leaderboard
     * @param userID the username of a given user
     * @param avatarURL the URL of the profile picture of a given user
     * @param stat the statistic of a given user (score or wins)
     */
    private void createScoreCard(int ranking, String userID, String avatarURL, long stat) {
        // Creating the card
        HBox scoreCard = new HBox();
        scoreCard.getStyleClass().add("score-card");
        scoreCard.setMinSize(480, 40);
        scoreCard.setPadding(new Insets(0, 20, 0, 20));

        // The left half of the card
        HBox leftHalf = new HBox();
        leftHalf.setMinSize(220, 40);
        leftHalf.setAlignment(Pos.CENTER_LEFT);
        leftHalf.setSpacing(10);

        // Create the rank, profile picture, and username and add styling
        Label rank = new Label();
        Label username = new Label();
        Circle profilePicture = new Circle();
        rank.getStyleClass().add("label-rank");
        username.getStyleClass().add("label");
        profilePicture.setRadius(13);

        // Put the user's information in
        rank.setText(String.valueOf(ranking));
        username.setText(userID);
        // To avoid a lag spike when loading profile pictures upon entering the leaderboard
        Task task = new Task<Void>() {
            @Override public Void call() {
                Image profilePic = new Image(avatarURL);
                profilePicture.setFill(new ImagePattern(profilePic));
                return null;
            }
        };
        new Thread(task).start();

        // Add rank, profile picture, and username to the left half of the card
        leftHalf.getChildren().addAll(rank, profilePicture, username);

        // The right half of the card
        HBox rightHalf = new HBox();
        rightHalf.setAlignment(Pos.CENTER_RIGHT);
        rightHalf.setMinSize(220, 40);

        // Create the high score/wins label and add styling
        Label score = new Label();
        score.getStyleClass().add("label");
        score.setText(String.valueOf(stat));

        // Add the high score/wins to the right half of the card
        rightHalf.getChildren().add(score);

        // Add both halves to create a complete card
        scoreCard.getChildren().addAll(leftHalf, rightHalf);

        // Add the scorecard to the leaderboard
        try {
            leaderboard.getChildren().add(scoreCard);
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Switches the focus of the leaderboard to a certain game.
     * @param game the desired game
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void setFocusedGame(Game game) throws ExecutionException, InterruptedException {
        resetButtonStyling();
        switch (game) {
            case SNAKE:
                snakeButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Score");
                loadScores(game, GameType.SINGLEPLAYER);
                return;
            case JAVASTROIDS:
                javastroidsButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Score");
                loadScores(game, GameType.SINGLEPLAYER);
                return;
            case TICTACTOE:
                ticTacToeButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Wins");
                loadScores(game, GameType.MULTIPLAYER);
                return;
            case BATTLESHIP:
                battleshipButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Wins");
                loadScores(game, GameType.MULTIPLAYER);
                return;
            case CHESS:
                chessButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Wins");
                loadScores(game, GameType.MULTIPLAYER);
                return;
            case CHECKERS:
                checkersButton.setStyle("-fx-background-color: #0866CC;");
                statLabel.setText("Wins");
                loadScores(game, GameType.MULTIPLAYER);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void resetButtonStyling() {
        snakeButton.setStyle("-fx-background-color: #404040;");
        snakeButton.setStyle("-fx-hover-color: #272727");
        javastroidsButton.setStyle("-fx-background-color: #404040;");
        javastroidsButton.setStyle("-fx-hover-color: #272727");
        ticTacToeButton.setStyle("-fx-background-color: #404040;");
        ticTacToeButton.setStyle("-fx-hover-color: #272727");
        battleshipButton.setStyle("-fx-background-color: #404040;");
        battleshipButton.setStyle("-fx-hover-color: #272727");
        checkersButton.setStyle("-fx-background-color: #404040;");
        checkersButton.setStyle("-fx-hover-color: #272727");
        chessButton.setStyle("-fx-background-color: #404040;");
        chessButton.setStyle("-fx-hover-color: #272727");
    }

    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }

    @FXML
    public void showInfo() {
        info.setVisible(true);
        pane.setVisible(true);
    }

    @FXML
    public void hideInfo() {
        info.setVisible(false);
        pane.setVisible(false);
    }

    @FXML
    public void onSnakeButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.SNAKE);
    }

    @FXML
    public void onJavastroidsButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.JAVASTROIDS);
    }

    @FXML
    public void onTicTacToeButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.TICTACTOE);
    }

    @FXML
    public void onBattleshipButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.BATTLESHIP);
    }

    @FXML
    public void onCheckersButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.CHECKERS);
    }

    @FXML
    public void onChessButtonClicked() throws ExecutionException, InterruptedException {
        setFocusedGame(Game.CHESS);
    }

}

