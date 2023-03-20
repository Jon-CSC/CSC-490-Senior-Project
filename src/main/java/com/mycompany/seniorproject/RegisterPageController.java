package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chriscanenguez & juan
 */
public class RegisterPageController implements Initializable {

    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    public void goToLoginPage() throws IOException {
        // Retrieves Loader for Login page.
        App.setRoot("LoginPage");
    } // End goToLoginPage.

    @FXML
    public void register() throws IOException {
        createUser();
        createUserDocument();
    }

    public void createUser() throws IOException {
        try {
            //Make a new user record create request
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setUid(usernameField.getText().trim())
                    .setEmail(emailField.getText().trim())
                    .setPassword(passwordField.getText().trim())
                    .setDisplayName(usernameField.getText().trim());
            //Make a new UserRecord instance and use the data from the create request
            UserRecord newUser = FirebaseAuth.getInstance().createUser(request);
            //Use custom claims to set the new user's permissions to user and not admin
            Map<String, Object> claims = new HashMap<>();
            claims.put("user", true);
            FirebaseAuth.getInstance().setCustomUserClaims(newUser.getUid(), claims);
            //If registration is successful return to the log in page
            App.setRoot("LoginPage");
        } catch (FirebaseAuthException ex) {
            errorLabel.setText("Username is already in use");
            errorLabel.setVisible(true);
        } catch (IllegalArgumentException iae) {
            if (usernameField.getText().equals("") || passwordField.getText().equals("") || emailField.getText().equals("")) {
                errorLabel.setText("All fields must be filled out");
                errorLabel.setVisible(true);
            } else {
                errorLabel.setText("Password must be at least 6 characters long");
                errorLabel.setVisible(true);
            }
        }
    }

    public void createUserDocument() {
        //Create a new document using the username typed in the usernameField
        DocumentReference docRef = App.fstore.collection("Users").document(usernameField.getText());
        Map<String, Object> data = new HashMap<>();
        //Store the password in the new document to check later when logging in
        data.put("Password", passwordField.getText());
        ApiFuture<WriteResult> result = docRef.set(data);
    }

}
