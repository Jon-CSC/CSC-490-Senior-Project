

package com.mycompany.seniorproject.games.snake;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class SnakeGameController implements Initializable {

    @FXML
    public Canvas gameCanvas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameCanvas.setWidth(700);
        gameCanvas.setHeight(700);
        SnakeGame gameInstance = new SnakeGame(gameCanvas);
        App.getStage().setWidth(700);
        App.getStage().setHeight(727);
        App.getStage().centerOnScreen();
        App.getStage().setResizable(false);

    }

    public Canvas getCanvas() {
        return this.gameCanvas;
    }

}