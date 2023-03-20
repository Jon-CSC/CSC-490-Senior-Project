package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chriscanenguez
 */
public class ForgotPasswordPageController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        errorLabel.setVisible(false);
    }

    @FXML
    public void goToLoginPage() throws IOException {
        // Retrieves Loader for Login page.
        App.setRoot("LoginPage");
    } // End goToLoginPage.

    @FXML
    public void updatePassword() {
        errorLabel.setVisible(false);
        try {
            // Retrieves instance of the uid that was entered.
            UserRecord user = FirebaseAuth.getInstance().getUser(usernameField.getText());

            // Console print to show that user search was successful.
            if (user != null) {
                System.out.println("The following user was found: ");
                System.out.println(user);
            }

            // If user field is populated, checks if other fields are empty.
            if (newPasswordField.getText().trim().equals("") || confirmPasswordField.getText().trim().equals("")) {
                errorLabel.setText("All fields must be filled out.");
                errorLabel.setVisible(true);
                return;
            }

            // Check if both the newPasswordField and confirmPasswordField contents match.
            if (newPasswordField.getText().toString().trim().equals(confirmPasswordField.getText().toString().trim())) {
                System.out.println("Passwords Match!");

                UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(usernameField.getText()).setPassword(newPasswordField.getText());
                user = FirebaseAuth.getInstance().updateUser(request);
                
                
                clearFields();
                errorLabel.setVisible(true);
                errorLabel.setText("Password Changed.");

            } else {
                System.out.println("Passwords dont match!");
                errorLabel.setText("Passwords don't match, please enter correctly.");
                errorLabel.setVisible(true);
            }

        } catch (FirebaseAuthException ex) {
            // User doesn't exist in firebase.
            clearFields();
            errorLabel.setText("The user entered does not exist.");
            errorLabel.setVisible(true);

        } catch (IllegalArgumentException iae) {
            // User field was empty.
            clearFields();
            errorLabel.setText("All fields must be filled out.");
            errorLabel.setVisible(true);
        }
    }

    
    
    private void clearFields() {
        usernameField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

}
