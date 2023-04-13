

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


public class SnakeGameController implements Initializable {

    @FXML
    private Canvas gameCanvas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SnakeGame gameInstance = new SnakeGame(gameCanvas);
        App.getStage().setWidth(700);
        App.getStage().setHeight(727);
        App.getStage().centerOnScreen();
        App.getStage().setResizable(false);
        gameCanvas.setWidth(700);
        gameCanvas.setHeight(700);
    }
    
    @FXML
    void testClick(MouseEvent event) throws IOException {
       System.out.println("SnakeController works!");
      App.setRoot("gameLibrary");
    }
}