package com.mycompany.seniorproject.games.tictactoe;

import com.mycompany.seniorproject.App;
import com.mycompany.seniorproject.PeerToPeer;
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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jonathan Espinal, juan
 */
public class TicTacToeNetworkMatchSetUpController implements Initializable {


    @FXML private Button buttonConnect, buttonHost;
    @FXML private Label labelError;
    @FXML private TextField textFieldYourIP, textFieldHostIP, textFieldPortNumClient, textFieldPortNumHost;
    @FXML private ProgressIndicator progressIndicatorConnecting;
    @FXML private VBox pickMatchType, hostMatch, joinMatch;
    private PeerToPeer connection;
    final int MAX_CHARS_PORT = 5;
    final int MAX_CHARS_IP = 15;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getStage().setHeight(600);
        App.getStage().setWidth(400);
        App.getStage().centerOnScreen();
        try {
            textFieldYourIP.setText(InetAddress.getLocalHost().getHostAddress());
            labelError.setVisible(false);
            progressIndicatorConnecting.setVisible(false);
            hostMatch.setVisible(false);
            joinMatch.setVisible(false);
            trackCharacterCounts();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
            textFieldYourIP.setText("Error getting IP address");
        }
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        App.setRoot("games/tictactoe/TicTacToeMainMenu");
    }

    @FXML
    public void onBackToPickMatchTypeButtonClick() {
        pickMatchType.setVisible(true);
        hostMatch.setVisible(false);
        joinMatch.setVisible(false);
    }

    @FXML
    public void onHostMatchClick() {
        pickMatchType.setVisible(false);
        hostMatch.setVisible(true);
    }

    @FXML
    public void onJoinMatchClick() {
        pickMatchType.setVisible(false);
        joinMatch.setVisible(true);
    }

    @FXML
    public void onHostButtonClick() throws IOException {
        // TODO: Start Tic-Tac-Toe game with this player as host/server
        String portString = textFieldPortNumHost.getText();
        Boolean validEntries = true;
        int port = -1;
        if (portString.compareTo("") != 0) {
            port = Integer.valueOf(portString);
            if (port < 0 || port > 65535) {
                labelError.setVisible(true);
                labelError.setText("Invalid port number (must be 0-65535)");
                validEntries = false;
            } else {
                labelError.setVisible(false);
            }
        } else {
            labelError.setVisible(true);
            labelError.setText("All fields must be filled out");
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
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    private void startHost(int port) throws IOException, InterruptedException {
        progressIndicatorConnecting.setVisible(true);
        Task task = new Task<Void>() {
            @Override
            public Void call() throws IOException {
                connection = new PeerToPeer();
                connection.startHost(port);
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));
                    // Get current window
                    Stage stage = (Stage) buttonHost.getScene().getWindow();
                    try {
                        App.getStage().getScene().setRoot(loader.load());
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    TicTacToeGameController controller = loader.getController();
                    controller.initMultiplayer(connection,true);
                    stage.show();
                });
                return null;
            }
        };
        task.setOnSucceeded(taskFinishEvent -> System.out.println("Connected: \n" + connection.toString()));
        new Thread(task).start();
    }

    @FXML
    public void onConnectButtonClick() throws IOException {
        // TODO: Make connection and start game with this player as client
        String hostIPString = textFieldHostIP.getText();
        String portString = textFieldPortNumClient.getText();
        Boolean validEntries = true;
        int port = -1;
        if (!isValidIPAddress(hostIPString)) {
            labelError.setVisible(true);
            labelError.setText("Invalid IP address entered");
            validEntries = false;
        } else {
            labelError.setVisible(false);
        }

        if (portString.compareTo("") != 0) {
            port = Integer.valueOf(portString);
            if (port < 0 || port > 65535) {
                labelError.setVisible(true);
                labelError.setText("Invalid port number (must be 0-65535)");
                validEntries = false;
                return;
            } else {
                labelError.setVisible(false);
            }
        } else {
            labelError.setVisible(true);
            labelError.setText("All fields must be filled out");
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

    /**
     * Attempts to start a connection with the given IP on the given port.
     * @param ip The IP of the host
     * @param port The target port
     * @throws IOException
     * @throws InterruptedException 
     */
    private void startConnection(String ip, int port) throws IOException, InterruptedException {
        progressIndicatorConnecting.setVisible(true);
        Task task = new Task<Void>() {
            @Override
            public Void call() throws IOException {
                connection = new PeerToPeer();
                connection.connectToHost(ip, port);
                Platform.runLater(() -> {
                    FXMLLoader loader = new FXMLLoader(TicTacToeNetworkMatchSetUpController.this.getClass().getResource("TicTacToeGame.fxml"));   
                    try {
                        App.getStage().getScene().setRoot(loader.load());
                    } catch (IOException ex) {
                        Logger.getLogger(TicTacToeNetworkMatchSetUpController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    TicTacToeGameController controller = loader.getController();
                    controller.initMultiplayer(connection,false);
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
    private boolean isValidIPAddress(String ip) {
        String ipNumRegex =
                "^(([2][5][0-5]|[2][0-4][0-9]|[1][0-9][0-9]|[1-9][0-9]|[0-9]){1}\\.{1})"
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

    private void trackCharacterCounts() {
        textFieldPortNumHost.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().length() <= MAX_CHARS_PORT ? change : null));
        textFieldPortNumClient.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().length() <= MAX_CHARS_PORT ? change : null));
        textFieldHostIP.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().length() <= MAX_CHARS_IP ? change : null));
    }

}