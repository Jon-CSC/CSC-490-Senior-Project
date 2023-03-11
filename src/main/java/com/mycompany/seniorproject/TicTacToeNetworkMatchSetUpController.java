package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Jonathan Espinal
 */
public class TicTacToeNetworkMatchSetUpController implements Initializable {
    
    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonConnect;

    @FXML
    private Button buttonHost;

    @FXML
    private Label labelHostIP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            labelHostIP.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
            labelHostIP.setText("Error getting IP address");
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        App.setRoot("TicTacToeMainMenu");
    }
    
    @FXML
    public void onHostButtonClick() throws IOException {
        // TODO: Start Tic-Tac-Toe game with this player as host/server
    }
    
       @FXML
    public void onConnectButtonClick() throws IOException {
        // TODO: Make connection and start game with this player as client
    }
}
