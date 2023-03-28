package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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

    // JavaFX items.
    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label successLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setLabelsNotVisible();
    }

    /**
     * Set page to LoginPage.
     * @throws IOException 
     */
    @FXML
    public void goToLoginPage() throws IOException {
        // Retrieves Loader for Login page.
        App.setRoot("LoginPage");
    } // End goToLoginPage.

    /**
     * Method that will send an 'email' to admin in order to reset the user's password.
     * @throws IOException - if field is empty.
     */
    @FXML
    public void sendPasswordRestEmail() throws IOException{
        setLabelsNotVisible();
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(emailField.getText());
            successLabel.setVisible(true);
            clearFields();

        } catch (IllegalArgumentException iae) {
            errorLabel.setVisible(true);
            errorLabel.setText("Fields cannot be empty");
            return;
        } catch (FirebaseAuthException ex) {
            errorLabel.setVisible(true);
            errorLabel.setText("Email doesn't exist within system");
            clearFields();
        }

    }

    /**
     * Clear all the text fields.
     */
    private void clearFields() {
        emailField.clear();
    }

    /**
     * Turns all the fields to not visible.
     */
    private void setLabelsNotVisible() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

}
