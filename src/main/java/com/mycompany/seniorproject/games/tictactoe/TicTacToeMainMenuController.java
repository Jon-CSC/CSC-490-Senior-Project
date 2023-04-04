/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games.tictactoe;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Jonathan Espinal
 */
public class TicTacToeMainMenuController implements Initializable {

    @FXML
    private Label labelLocalMatch;

    @FXML
    private Label labelNetworkMatch;
    @FXML
    private Label labelOptions;
    @FXML
    private Label labelQuit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getStage().setWidth(420);
        App.getStage().setHeight(600);
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
        App.setRoot("games/tictactoe/TicTacToeGame");

    }

    @FXML
    public void onNetworkMatchMouseClick() throws IOException {
        App.setRoot("games/tictactoe/TicTacToeNetworkMatchSetup");

    }

    @FXML
    public void onOptionsMouseClick() throws IOException {
        App.setRoot("games/tictactoe/TicTacToeOptions");
    }

    @FXML
    public void onQuitMouseClick() throws IOException {
        App.setRoot("GameLibrary");

    }
}
