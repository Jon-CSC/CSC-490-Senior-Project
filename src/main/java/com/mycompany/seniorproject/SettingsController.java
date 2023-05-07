package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * FXML Controller Class
 *
 * @author juan
 */
public class SettingsController implements Initializable {

    @FXML private VBox info;
    @FXML private Pane pane;
    @FXML private Label errorLabel;
    @FXML private TextField usernameField, emailField;
    @FXML private PasswordField passwordField;
    @FXML private Circle profilePictureCircle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pane.setVisible(false);
        info.setVisible(false);
        errorLabel.setVisible(false);

        // Set the fields on screen to the local user's info
        String userID = LocalUserAccount.getInstance().getUser().getUserID();
        usernameField.setText(userID);
        try {
            emailField.setText(FirebaseAuth.getInstance().getUser(userID).getEmail());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        // Getting the password and avatar URL from Firestore because of how we store passwords
        DocumentReference docRef = App.fstore.collection("Users").document(userID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        Image profilePic;
        try {
            document = future.get();
            passwordField.setText((String) document.getData().get("Password").toString());
            String avatarURL = document.getData().get("avatarURL").toString();
            profilePic = new Image(avatarURL);
            profilePictureCircle.setFill(new ImagePattern(profilePic));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the user back to the game library.
     * @throws IOException
     */
    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }

    /**
     * Shows the information on valid account credentials.
     */
    @FXML
    public void showInfo() {
        pane.setVisible(true);
        info.setVisible(true);
    }

    /**
     * Hides the information on valid account credentials.
     */
    @FXML
    public void hideInfo() {
        pane.setVisible(false);
        info.setVisible(false);
    }

    /**
     * Updates the user's account info using what the user has entered.
     */
    @FXML
    public void updateAccountInfo() {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().updateUser(createUpdateRequest());
            String userID = LocalUserAccount.getInstance().getUser().getUserID();
            DocumentReference docRef = App.fstore.collection("Users").document(userID);
            docRef.update("Password", passwordField.getText());
        } catch (FirebaseAuthException iae) {
            throw new RuntimeException();
        }
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #25be3e");
        errorLabel.setText("Account info updated");
    }

    /**
     * Creates a new user UpdateRequest using what the user has entered.
     * @return
     */
    UserRecord.UpdateRequest createUpdateRequest() {
        UserRecord.UpdateRequest request = null;
        try {
            request = new UserRecord.UpdateRequest(usernameField.getText())
                    .setEmail(emailField.getText().trim())
                    .setPassword(passwordField.getText().trim());
            return request;
        } catch (IllegalArgumentException iae) {
            if (passwordField.getText().length() < 6) {
                errorLabel.setVisible(true);
                errorLabel.setStyle("-fx-text-fill: #ff4242");
                errorLabel.setText("Password must be at least 6 characters long");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setStyle("-fx-text-fill: #ff4242");
                errorLabel.setText("Email format must be email@example.com");
            }
        }
        return null;
    }

}
