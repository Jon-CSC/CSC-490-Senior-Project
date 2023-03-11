/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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
        // TODO
    }

    @FXML
    public void onLocalMatchMouseEnter() {
        labelLocalMatch.setStyle("-fx-font-weight: bold");

    }

    @FXML
    public void onLocalMatchMouseExit() {
        labelLocalMatch.setStyle("-fx-font-weight: regular");

    }
    
    @FXML
    public void onLocalMatchMouseClick() throws IOException {
        App.setRoot("TicTacToeGame");

    }
    @FXML
    public void onNetworkMatchMouseEnter() {
        labelNetworkMatch.setStyle("-fx-font-weight: bold");

    }

    @FXML
    public void onNetworkMatchMouseExit() {
        labelNetworkMatch.setStyle("-fx-font-weight: regular");

    }
        @FXML
    public void onNetworkMatchMouseClick() throws IOException {
        App.setRoot("TicTacToeNetworkMatchSetup");

    }
    @FXML
    public void onOptionsMouseEnter() {
        labelOptions.setStyle("-fx-font-weight: bold");

    }

    @FXML
    public void onOptionsMouseExit() {
        labelOptions.setStyle("-fx-font-weight: regular");

    }
     @FXML
    public void onOptionsMouseClick() throws IOException {
        App.setRoot("TicTacToeOptions");
    }
    @FXML
    public void onQuitMouseEnter() {
        labelQuit.setStyle("-fx-font-weight: bold");

    }

    @FXML
    public void onQuitMouseExit() {
        labelQuit.setStyle("-fx-font-weight: regular");

    }
    
     @FXML
    public void onQuitMouseClick() throws IOException {
        App.setRoot("gameLibrary");

    }
}
