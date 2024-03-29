package com.mycompany.seniorproject.games.chess;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.seniorproject.App;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jonathan Espinal, juan
 */
public class ChessMainMenuController implements Initializable {

    @FXML private Button buttonLocalMatch, buttonNetworkMatch, buttonQuit;
    @FXML private Rectangle gameLogoRectangle;

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
    public void onLocalMatchMouseClick() throws IOException {
         FXMLLoader loader = new FXMLLoader(ChessMainMenuController.this.getClass().getResource("ChessGame.fxml"));
                    // Get current window
                    Stage stage = (Stage) buttonLocalMatch.getScene().getWindow();
                    try {
                        App.getStage().getScene().setRoot(loader.load());
                    } catch (IOException ex) {
                        Logger.getLogger(ChessNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ChessGameController controller = loader.getController();
                    controller.initLocalMatch();
                    stage.show();
    }

    @FXML
    public void onNetworkMatchMouseClick() throws IOException {
        App.setRoot("games/chess/ChessNetworkMatchSetup");
    }

    @FXML
    public void onQuitMouseClick() throws IOException {
        App.setRoot("gameLibrary");
    }

    private void loadImages() {
        Image gameImage;
        gameImage = new Image(getClass().getResourceAsStream("../../Images/chess.png"));
        gameLogoRectangle.setFill(new ImagePattern(gameImage));
    }

}
