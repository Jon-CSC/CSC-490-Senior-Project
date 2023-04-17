package com.mycompany.seniorproject.games.snake;

import com.google.firebase.auth.FirebaseAuth;
import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.Game;
import com.mycompany.seniorproject.LocalUserAccount;
import com.mycompany.seniorproject.UserAccount;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * FXML Controller Class
 *
 * @author juan
 */
public class SnakeGameOverController implements Initializable {

    private int BACKGROUND_HEIGHT = 700;
    private long lastScore, highScore;
    private ParallelTransition checkerboardScrollTransition;
    @FXML private ImageView checkerboard, checkerboard2;
    @FXML private Label labelLastScore, labelHighScore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Fill in last score and high score
        String userID = LocalUserAccount.getInstance().getUser().getUserID();
        UserAccount profileUser = UserAccount.downloadUser(userID, App.fstore);
        HashMap<String, Object> gameData = profileUser.getGameData();
        highScore = (long)gameData.getOrDefault(Game.SNAKE.getScoreField(), (long)0);
        lastScore = (long)gameData.getOrDefault(Game.SNAKE.getLastScoreField(), (long)0);
        labelLastScore.setText(Long.toString(lastScore));
        labelHighScore.setText(Long.toString(highScore));
        startAnimation();
    }

    @FXML
    public void playAgain() throws IOException {
        App.setRoot("games/snake/SnakeGame");
    }

    @FXML
    public void exit() throws IOException {
        App.setRoot("gameLibrary");
    }

    @FXML
    public void onMenuSelectionMouseEnter(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setText("▶ " + label.getText() + " ◀");
    }

    @FXML
    public void onMenuSelectionMouseExit(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setText(label.getText().substring(2, label.getText().length() - 2));
    }

    private void startAnimation() {
        TranslateTransition scrollTransition = new TranslateTransition(Duration.millis(20000), checkerboard);
        scrollTransition.setFromY(0);
        scrollTransition.setToY(-1 * BACKGROUND_HEIGHT);
        scrollTransition.setInterpolator(Interpolator.LINEAR);
        TranslateTransition scrollTransition2 = new TranslateTransition(Duration.millis(20000), checkerboard2);
        scrollTransition2.setFromY(0);
        scrollTransition2.setToY(-1 * BACKGROUND_HEIGHT);
        scrollTransition2.setInterpolator(Interpolator.LINEAR);
        checkerboardScrollTransition = new ParallelTransition(scrollTransition, scrollTransition2);
        checkerboardScrollTransition.setCycleCount(Animation.INDEFINITE);
        checkerboardScrollTransition.play();
    }

}
