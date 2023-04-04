package com.mycompany.seniorproject.games.tictactoe;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.seniorproject.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Jonathan Espinal, juan
 */
public class TicTacToeOptionsController implements Initializable {

    @FXML private Label labelBackToMenu;
    @FXML private Rectangle ticTacToeRectangle;

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
    public void onMenuSelectionMouseEnter(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setText("▶ " +label.getText() + " ◀");
    }

    @FXML
    public void onMenuSelectionMouseExit(MouseEvent m) {
        Label label = (Label) m.getSource();
        label.setText(label.getText().substring(2,label.getText().length()-2));
    }

    @FXML
    public void onQuitMouseClick() throws IOException {
        App.setRoot("games/tictactoe/TicTacToeMainMenu");
    }

    private void loadImages() {
        Image ticTacToeImage;
        ticTacToeImage = new Image(getClass().getResourceAsStream("Images/tictactoe.png"));
        ticTacToeRectangle.setFill(new ImagePattern(ticTacToeImage));
    }
    
}
