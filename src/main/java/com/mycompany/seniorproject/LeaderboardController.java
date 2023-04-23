package com.mycompany.seniorproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller Class
 *
 * @author juan
 */
public class LeaderboardController implements Initializable {

    @FXML private Button snakeButton, javastroidsButton, ticTacToeButton, battleshipButton, chessButtons, checkersButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.getStage().setWidth(900);
        App.getStage().setHeight(640);
        App.getStage().centerOnScreen();
    }

    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }


}
