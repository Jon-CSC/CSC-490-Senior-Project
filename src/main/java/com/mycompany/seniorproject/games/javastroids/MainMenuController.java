package com.mycompany.seniorproject.games.javastroids;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller class for designating actions within the Main Menu FXML scene.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class MainMenuController {

    @FXML
    private Button buttonHighScores;

    @FXML
    private Button buttonPlayGame;
    
    /**
     * Method that is invoked upon this FXML scene being initialized.
     */
    @FXML
    public void initialize() {
        App.getStage().setWidth(800);
        App.getStage().setHeight(640);
        App.getStage().setOnHiding(e -> {
            GameLoop.stopAllThreads();
            Platform.exit();
        });
        
        buttonHighScores.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonHighScores.setText("> EXIT <");
            } else {
                buttonHighScores.setText("EXIT");
            }
        });
        buttonPlayGame.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonPlayGame.setText("> PLAY GAME <");
            } else {
                buttonPlayGame.setText("PLAY GAME");
            }
        });
    }
    
    @FXML
    private void switchToLeaderboard() throws IOException {
        System.out.println("Switching to high scores!");
        App.setRoot("gameLibrary");
    }
    
    @FXML
    void switchToGame(ActionEvent event) throws IOException {
        System.out.println("Switching to game!");
        App.setRoot("games/javastroids/game");
    }
}
