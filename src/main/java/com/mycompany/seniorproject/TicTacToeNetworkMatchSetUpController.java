package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.mycompany.seniorproject.games.TicTacToeGameController;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private HBox hboxParent;

    @FXML
    private Label hostPortErrorMessage;

    @FXML
    private Label joinHostErrorMessage;

    @FXML
    private Label joinPortErrorMessage;

    @FXML
    private Label labelHostIP;

    @FXML
    private TextField textFieldHostIP;

    @FXML
    private TextField textFieldPortNumClient;

    @FXML
    private TextField textFieldPortNumHost;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            labelHostIP.setText(InetAddress.getLocalHost().getHostAddress());
            hostPortErrorMessage.setVisible(false);
            joinHostErrorMessage.setVisible(false);
            joinPortErrorMessage.setVisible(false);
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
        String portString = textFieldPortNumHost.getText();
        Boolean validEntries = true;
        int port = -1;
         if (portString.compareTo("") != 0){
            port = Integer.valueOf(portString);
            System.out.println(port);
            if (port < 0 || port > 65535) {
                hostPortErrorMessage.setVisible(true);
                validEntries = false;
            }else{
                hostPortErrorMessage.setVisible(false);
            }
        }else{
            hostPortErrorMessage.setVisible(true);
            validEntries = false;
        }
        if (validEntries){
            // entries valid, use them to start host
        }
    }

    /**
     * Gets user inputted data to set up the host.
     *
     * @param port Host port specified by user.
     * @return
     * @throws IOException
     */
    public Stage startHost(int port) throws IOException {
        System.out.println("Received port: " + port);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TicTacToeGame.fxml"));
        System.out.println("Setting stage");
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene((Pane) loader.load()));
        //Stage current = (Stage) buttonHost.getScene().getWindow();
        //stage.initOwner(current);
        System.out.println("Loading controller");
        TicTacToeGameController controller = loader.getController();
        System.out.println("Setting init");
        //stage.show();
        controller.initData(port);
        BorderPane pane = loader.getRoot();
        System.out.println("Showing stage");
        hboxParent.getChildren().setAll(pane);
        return stage;
    }

    @FXML
    public void onConnectButtonClick() throws IOException {
        // TODO: Make connection and start game with this player as client
        String hostIPString = textFieldHostIP.getText();
        String portString = textFieldPortNumClient.getText();
        Boolean validEntries = true;
        int port = -1;
        if (!isValidIPAddress(hostIPString)) {
            joinHostErrorMessage.setVisible(true);
            validEntries = false;
        } else{
            joinHostErrorMessage.setVisible(false);
        }
        
        if (portString.compareTo("") != 0){
            port = Integer.valueOf(portString);
            System.out.println(port);
            if (port < 0 || port > 65535) {
                joinPortErrorMessage.setVisible(true);
                validEntries = false;
            }else{
                joinPortErrorMessage.setVisible(false);
            }
        }else{
            joinPortErrorMessage.setVisible(true);
            validEntries = false;
        }
        if (validEntries){
           // entries valid, use them to connect to the host
        }
    }
    
    /**
     * Check if IP is valid format ###.###.###.### where each number is between
     * 0 and 255 with no leading zeros.
     * @param ip The IP to check
     * @return true if valid IP, false if not
     */
    public boolean isValidIPAddress(String ip) {
        String ipNumRegex =
        "(([2][5][0-5]|[2][0-4][0-9]|[1][0-9]{0,2}|[1-9]{0,2}|[0])\\."
        + "([2][5][0-5]|[2][0-4][0-9]|[1][0-9]{0,2}|[1-9]{0,2}|[0])\\."
        + "([2][5][0-5]|[2][0-4][0-9]|[1][0-9]{0,2}|[1-9]{0,2}|[0])\\."
        + "([2][5][0-5]|[2][0-4][0-9]|[1][0-9]{0,2}|[1-9][0]{1,2}|[0]))";

        Pattern p = Pattern.compile(ipNumRegex);
        if (ip == null) {
            return false;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }
}
