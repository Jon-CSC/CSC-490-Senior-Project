package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.mycompany.seniorproject.games.TicTacToeGameController;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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

    private PeerToPeer connection;

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
        if (portString.compareTo("") != 0) {
            port = Integer.valueOf(portString);
            System.out.println(port);
            if (port < 0 || port > 65535) {
                hostPortErrorMessage.setVisible(true);
                validEntries = false;
            } else {
                hostPortErrorMessage.setVisible(false);
            }
        } else {
            hostPortErrorMessage.setVisible(true);
            validEntries = false;
        }
        if (validEntries) {
            // entries valid, use them to start host
            try {
                startHost(port);
            } catch (InterruptedException ex) {
                Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Gets user inputted data to set up the host.
     *
     * @param port Host port specified by user.
     * @return
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public Stage startHost(int port) throws IOException, InterruptedException {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                connection = new PeerToPeer();
                connection.startHost(port);
                Platform.runLater(() -> {
                    System.out.println("Connected: " + connection.toString());
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));
                    System.out.println("Setting stage");
                    // Get current window
                    Stage stage = (Stage) buttonHost.getScene().getWindow();
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Loading controller");
                    TicTacToeGameController controller = loader.getController();
                    controller.initConnection(connection);
                    System.out.println("Showing stage");
                    stage.show();
                });
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> System.out.println("Connected: \n" + connection.toString()));
        new Thread(task).start();
        return null;
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
        } else {
            joinHostErrorMessage.setVisible(false);
        }

        if (portString.compareTo("") != 0) {
            port = Integer.valueOf(portString);
            System.out.println(port);
            if (port < 0 || port > 65535) {
                joinPortErrorMessage.setVisible(true);
                validEntries = false;
            } else {
                joinPortErrorMessage.setVisible(false);
            }
        } else {
            joinPortErrorMessage.setVisible(true);
            validEntries = false;
        }
        if (validEntries) {
            try {
                startConnection(hostIPString, port);
            } catch (InterruptedException ex) {
                Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Stage startConnection(String ip, int port) throws IOException, InterruptedException {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                connection = new PeerToPeer();
                connection.connectToHost(ip, port);
                Platform.runLater(() -> {
                    System.out.println("Connected: " + connection.toString());
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));
                    System.out.println("Setting stage");
                    // Get current window
                    Stage stage = (Stage) buttonConnect.getScene().getWindow();
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Loading controller");
                    TicTacToeGameController controller = loader.getController();
                    controller.initConnection(connection);
                    System.out.println("Showing stage");
                    stage.show();
                });
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> System.out.println("Connected: " + connection.toString()));
        new Thread(task).start();
        return null;
    }

    /**
     * Check if IP is valid format ###.###.###.### where each number is between
     * 0 and 255 with no leading zeros.
     *
     * @param ip The IP to check
     * @return true if valid IP, false if not
     */
    public boolean isValidIPAddress(String ip) {
        String ipNumRegex
                = "(([2][5][0-5]|[2][0-4][0-9]|[1][0-9]{0,2}|[1-9]{0,2}|[0])\\."
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
