package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.internal.DownloadAccountResponse;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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

    class User {
        String username, avatarURL;
        long score;
        public User(String username, String avatarURL, long score) {
            this.username = username;
            this.avatarURL = avatarURL;
            this.score = score;
        }
        public long getScore() {
            return score;
        }
        public String getUsername() {
            return username;
        }
        public String getAvatarURL() {
            return avatarURL;
        }
    }

    @FXML private Button snakeButton, javastroidsButton, ticTacToeButton, battleshipButton, chessButton, checkersButton;
    @FXML private VBox leaderboard;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statLabel;
    private ArrayList<User> listOfUsers = new ArrayList<User>();
    private User currentUser;
    private HashMap<String, Object> gameData;
    private String userID, avatarURL;
    private long score;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.getStage().setWidth(900);
        App.getStage().setHeight(640);
        App.getStage().centerOnScreen();
        progressIndicator.setVisible(false);
        setFocusedGame(Game.SNAKE);
        Task task = new Task<Void>() {
            @Override public Void call() throws ExecutionException, InterruptedException {
                progressIndicator.setVisible(true);
                loadScores(Game.SNAKE, GameType.SINGLEPLAYER);
                progressIndicator.setVisible(false);
                return null;
            }
        };
        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }



    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }

    private void loadScores(Game game, GameType gameType) throws ExecutionException, InterruptedException {
        CollectionReference users = App.fstore.collection("Users");
        Query query = users.orderBy("gameData." + game.getScoreField(), Query.Direction.DESCENDING);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            userID = document.getId();
            avatarURL = document.getData().get("avatarURL").toString();
            gameData = (HashMap) document.getData().get("gameData");
            score = (long)gameData.getOrDefault(game.getScoreField(), (long)0);
            currentUser = new User(userID, avatarURL, score);
            listOfUsers.add(currentUser);
        }
        Platform.runLater(() -> {
            for (int i = 0; i < listOfUsers.size(); i++) {
                generateScoreCard(i + 1,
                        listOfUsers.get(i).getUsername(),
                        listOfUsers.get(i).getAvatarURL(),
                        listOfUsers.get(i).getScore());
            }
        });

    }

    private void generateScoreCard(int ranking, String userID, String avatarURL, long highScore) {
        // The main part of the card
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

        // Using a Task to avoid a lag spike when entering the leaderboard
        Task task = new Task<Void>() {
            @Override public Void call() {
                Image profilePic = new Image(avatarURL);
                profilePicture.setFill(new ImagePattern(profilePic));
                return null;
            }
        };
        // Put the user's information in
        rank.setText(String.valueOf(ranking));
        username.setText(userID);
        new Thread(task).start();
        // Add rank, profile picture, and username to the left half of the card
        leftHalf.getChildren().addAll(rank, profilePicture, username);

        // The right half of the card
        HBox rightHalf = new HBox();
        rightHalf.setAlignment(Pos.CENTER_RIGHT);
        rightHalf.setMinSize(220, 40);
        // Create the high score label and add styling
        Label score = new Label();
        score.getStyleClass().add("label");
        score.setText(String.valueOf(highScore));
        rightHalf.getChildren().add(score);


        // Add both halves to create a complete card
        scoreCard.getChildren().addAll(leftHalf, rightHalf);
        scoreCard.setFocusTraversable(false);
        // Add the scorecard to the leaderboard
        try {
            leaderboard.getChildren().add(scoreCard);
        } catch (IllegalArgumentException illegalArgumentException) {
        }
    }

    @FXML
    public void onSnakeButtonClicked() {
        setFocusedGame(Game.SNAKE);
    }

    @FXML
    public void onJavastroidsButtonClicked() {
        setFocusedGame(Game.JAVASTROIDS);
    }

    @FXML
    public void onTicTacToeButtonClicked() {
        setFocusedGame(Game.TICTACTOE);
    }

    @FXML
    public void onBattleshipButtonClicked() {
        setFocusedGame(Game.BATTLESHIP);
    }

    @FXML
    public void onCheckersButtonClicked() {
        setFocusedGame(Game.CHECKERS);
    }

    @FXML
    public void onChessButtonClicked() {
        setFocusedGame(Game.CHESS);
    }

    private void setFocusedGame(Game game) {
        resetButtonFocus();
        if (game.equals(Game.SNAKE)) {
            resetButtonFocus();
            snakeButton.getStyleClass().add("button-selected");
            statLabel.setText("Score");
        } else if (game.equals(Game.JAVASTROIDS)) {
            resetButtonFocus();
            javastroidsButton.getStyleClass().add("button-selected");
            statLabel.setText("Score");
        } else if (game.equals(Game.TICTACTOE)) {
            resetButtonFocus();
            ticTacToeButton.getStyleClass().add("button-selected");
            statLabel.setText("Wins");
        } else if (game.equals(Game.BATTLESHIP)) {
            battleshipButton.getStyleClass().add("button-selected");
            statLabel.setText("Wins");
        } else if (game.equals(Game.CHECKERS)) {
            checkersButton.getStyleClass().add("button-selected");
            statLabel.setText("Wins");
        } else if (game.equals(Game.CHESS)) {
            chessButton.getStyleClass().add("button-selected");
            statLabel.setText("Wins");
        }
    }

    private void resetButtonFocus() {
        snakeButton.getStyleClass().add("button-unselected");
        javastroidsButton.getStyleClass().add("button-unselected");
        ticTacToeButton.getStyleClass().add("button-unselected");
        battleshipButton.getStyleClass().add("button-game");
        checkersButton.getStyleClass().add("button-game");
        chessButton.getStyleClass().add("button-game");
    }

}

