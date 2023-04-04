package com.mycompany.seniorproject.games.tictactoe;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.PeerToPeer;
import com.mycompany.seniorproject.games.tictactoe.TicTacToeGameController;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
    private Label hostPortErrorMessage;

    @FXML
    private Label joinHostErrorMessage;

    @FXML
    private Label joinPortErrorMessage;

    @FXML
    private Label labelHostIP;

    @FXML
    private Label labelConnectionFailed;

    @FXML
    private TextField textFieldHostIP;

    @FXML
    private TextField textFieldPortNumClient;

    @FXML
    private TextField textFieldPortNumHost;

    @FXML
    private ProgressIndicator progressIndicatorConnecting;
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
            progressIndicatorConnecting.setVisible(false);
            connection = new PeerToPeer();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
            labelHostIP.setText("Error getting IP address");
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        App.setRoot("games/tictactoe/TicTacToeMainMenu");
    }

    @FXML
    public void onHostButtonClick() throws IOException, InterruptedException {
        // TODO: Start Tic-Tac-Toe game with this player as host/server
        String portString = textFieldPortNumHost.getText();
        Boolean validEntries = true;
        int port = -1;
        if (portString.compareTo("") != 0) {
            port = Integer.valueOf(portString);
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
                buttonConnect.setDisable(true);
                startHost(port);
            } catch (InterruptedException ex) {
                Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                /* if connection fails, reset relevant variables
                 */
                progressIndicatorConnecting.setVisible(false);
                labelConnectionFailed.setVisible(true);
                Thread.sleep(2000);
                labelConnectionFailed.setVisible(false);
                connection.closeConnection();
                buttonConnect.setDisable(false);
            }
        }
    }

    /**
     * Gets user inputted data to set up the host.
     *
     * @param port Host port specified by user.
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public void startHost(int port) throws IOException, InterruptedException {
        progressIndicatorConnecting.setVisible(true);
        Task task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                try {
                    connection.startHost(port);
                } catch (IOException ex) {
                    Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    /* if connection fails, reset relevant variables
                     */
                    connection.closeConnection();
                    progressIndicatorConnecting.setVisible(false);
                    labelConnectionFailed.setVisible(true);
                    Thread.sleep(2000);
                    labelConnectionFailed.setVisible(false);
                    buttonConnect.setDisable(false);
                    return null;
                }
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));
                    // Get current window
                    Stage stage = (Stage) buttonHost.getScene().getWindow();
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    TicTacToeGameController controller = loader.getController();
                    controller.initConnection(connection, true);
                    stage.show();
                });
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> System.out.println("Connected: \n" + connection.toString()));
        new Thread(task).start();
    }

    @FXML
    public void onConnectButtonClick() throws IOException, InterruptedException {
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
                buttonHost.setDisable(true);
                startConnection(hostIPString, port);
            } catch (InterruptedException ex) {
                Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                /* if connection fails, reset relevant variables
                 */
                connection.closeConnection();
                buttonHost.setDisable(false);
                progressIndicatorConnecting.setVisible(false);
                labelConnectionFailed.setVisible(true);
                Thread.sleep(2000);
                labelConnectionFailed.setVisible(false);
                buttonConnect.setDisable(false);

            }
        }
    }

    /**
     * Attempts to connect to the given host.
     * @param ip The host IP to connect to.
     * @param port The target port on the host
     * @throws IOException
     * @throws InterruptedException 
     */
    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        progressIndicatorConnecting.setVisible(true);
        Task task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                try {
                    connection.connectToHost(ip, port);
                } catch (IOException ex) {
                    Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    connection.closeConnection();
                    progressIndicatorConnecting.setVisible(false);
                    labelConnectionFailed.setVisible(true);
                    Thread.sleep(2000);
                    labelConnectionFailed.setVisible(false);
                    buttonHost.setDisable(false);
                    return null;
                }
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));
                    // Get current window
                    Stage stage = (Stage) buttonConnect.getScene().getWindow();
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    TicTacToeGameController controller = loader.getController();
                    controller.initConnection(connection, false);
                    stage.show();
                });
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> System.out.println("Connected: " + connection.toString()));
        new Thread(task).start();
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
                = "^(([2][5][0-5]|[2][0-4][0-9]|[1][0-9][0-9]|[1-9][0-9]|[0-9]){1}\\.{1})"
                + "(([2][5][0-5]|[2][0-4][0-9]|[1][0-9][0-9]|[1-9][0-9]|[0-9]){1}\\.{1})"
                + "(([2][5][0-5]|[2][0-4][0-9]|[1][0-9][0-9]|[1-9][0-9]|[0-9]){1}\\.{1})"
                + "(([2][5][0-5]|[2][0-4][0-9]|[1][0-9][0-9]|[1-9][0-9]|[0-9]){1})$";

        Pattern p = Pattern.compile(ipNumRegex);
        if (ip == null) {
            return false;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }
}
