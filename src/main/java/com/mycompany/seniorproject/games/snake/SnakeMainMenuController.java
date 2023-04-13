package com.mycompany.seniorproject.games.snake;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.mycompany.seniorproject.App;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SnakeMainMenuController implements Initializable {

    private int BACKGROUND_HEIGHT = 700;
    private ParallelTransition checkerboardScrollTransition;
    @FXML private Rectangle snakeRectangle;
    @FXML private ImageView checkerboard;
    @FXML private ImageView checkerboard2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.getStage().setWidth(700);
        App.getStage().setHeight(700);
        App.getStage().centerOnScreen();
        App.getStage().setResizable(false);
        loadImages();
        startAnimation();
    }

    @FXML
    public void startGame() throws IOException {
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

    private void loadImages() {
        Image snakeImage;
        snakeImage = new Image(getClass().getResourceAsStream("../../Images/snake.png"));
        snakeRectangle.setFill(new ImagePattern(snakeImage));
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
