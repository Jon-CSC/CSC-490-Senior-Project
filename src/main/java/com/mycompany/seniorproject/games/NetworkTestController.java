/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject.games;

import com.mycompany.seniorproject.PeerToPeer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author justi
 */
public class NetworkTestController implements Initializable {

    private PeerToPeer connection;
    
    @FXML
    private TextField hostPort;
    @FXML
    private TextField connectToIP;
    @FXML
    private TextField connectToPort;
    @FXML
    private TextField hostMsg;
    @FXML
    private TextField clientMsg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connection = new PeerToPeer();
    }    
    
    public void startAsHost() {
        
    }
    
    public void connectAsClient() {
        
    }
    
}
