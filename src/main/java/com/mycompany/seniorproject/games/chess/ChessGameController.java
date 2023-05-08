/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.chess;

import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.PeerToPeer;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

/**
 *
 * @author justi
 */
public class ChessGameController implements Initializable {
    
    @FXML
    public Pane GamePane;
    public PeerToPeer connection;
    public boolean isHost;
    ChessGame gameInstance;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getStage().setWidth(850);
        App.getStage().setHeight(877);
        App.getStage().centerOnScreen();
        App.getStage().setResizable(true);

        // Sometimes the application will keep sockets bound after game window
        // is closed. This ensures everything is released properly.
        App.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                if ( connection != null){
                    connection.closeConnection();
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
    
    public void initLocalMatch(){
        try {
            ChessGame gameInstance = new ChessGame(GamePane);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChessGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void initMultiplayer(PeerToPeer connection, boolean isHost){
        try {
            ChessGame gameInstance = new ChessGame(GamePane);
            gameInstance.connection = connection;
            gameInstance.isHost = isHost;
            if (connection != null) {
                gameInstance.runOpponentsAction();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChessGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
