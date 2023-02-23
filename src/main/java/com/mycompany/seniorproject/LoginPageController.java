/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author canen & juan
 */
public class LoginPageController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        // Retrieves Loader for Register page.
        App.setRoot("RegisterPage");
    } // End goToRegisterPage.

    @FXML
    public void goToForgotPasswordPage() throws IOException {
        // Retrieves Loader for ForgotPassword page.
        App.setRoot("ForgotPasswordPage");
    } // End goToForgotPasswordPage.

    @FXML
    public void logIn() throws InterruptedException, ExecutionException {
        try {
            //Attempt to log in a user using what's typed in the usernameField
            UserRecord user = FirebaseAuth.getInstance().getUser(usernameField.getText());
            //If no exception has been caught, the password is then checked
            if (verifyPassword(usernameField.getText())) {
                //We would put code here to switch to whatever fxml file is considered the main page/library
                errorLabel.setText("Username and password match. We would switch to the main page now.");
                errorLabel.setVisible(true);
            } else if (passwordField.getText().equals("")) {
                errorLabel.setText("All fields must be filled out");
                errorLabel.setVisible(true);
            } else {
                errorLabel.setText("Password is incorrect");
                errorLabel.setVisible(true);
            }
        } catch (FirebaseAuthException ex) {
            if (passwordField.getText().equals("")) {
                errorLabel.setText("All fields must be filled out");
                errorLabel.setVisible(true);
            } else {
                errorLabel.setText("User does not exist");
                errorLabel.setVisible(true);
            }
        } catch (IllegalArgumentException iae) {
            errorLabel.setText("All fields must be filled out");
            errorLabel.setVisible(true);
        }
    }

    public boolean verifyPassword(String username) throws InterruptedException, ExecutionException {
        DocumentReference userDoc = App.fstore.collection("Users").document(username);
        ApiFuture<DocumentSnapshot> future = userDoc.get();
        DocumentSnapshot document = future.get();
        //We compare what's in the password field in the user's Firestore document vs. what's typed in the passwordField
        if (document.getData().get("Password").toString().equals(passwordField.getText())) {
            return true;
        }
        return false;
    }

}
