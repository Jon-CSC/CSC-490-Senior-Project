/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games.checkers;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class CheckersMainMenuController implements Initializable {

    @FXML
    private Rectangle checkersRectangle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getStage().setWidth(400);
        App.getStage().setHeight(600);
        App.getStage().centerOnScreen();
        loadImages();
    }

    @FXML
    public void onLocalMatchMouseClick() throws IOException {
        App.setRoot("games/checkers/checkers");
    }

    @FXML
    public void onMenuSelectionMouseEnter(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setStyle("-fx-font-weight: bold");
        label.setText("▶ " + label.getText() + " ◀");
    }

    @FXML
    public void onMenuSelectionMouseExit(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setStyle("-fx-font-weight: normal");
        label.setText(label.getText().substring(2, label.getText().length() - 2));

    }

    @FXML
    public void onQuitMouseClick() throws IOException {
        App.setRoot("gameLibrary");
    }

    private void loadImages() {
        Image ticTacToeImage;
        ticTacToeImage = new Image(getClass().getResourceAsStream("../../Images/checkers.png"));
        checkersRectangle.setFill(new ImagePattern(ticTacToeImage));
    }
}
