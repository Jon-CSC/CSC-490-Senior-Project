package com.mycompany.seniorproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void goToLoginPage() throws IOException {
        // Retrieves Loader for Login page.
        App.setRoot("LoginPage");
    } // End goToLoginPage.
    
    @FXML
    public void updatePassword(){
        try {
            // Retrieves instance of the uid that was entered.
            UserRecord user = FirebaseAuth.getInstance().getUser(usernameField.getText());
            
            if (user != null){
                System.out.println("The following user was found: ");
                System.out.println(user);
            }
            
        } catch (FirebaseAuthException ex) {
            // User doesn't exist in firebase.
            System.out.println("User doesnt exist in the database.");
        } catch (IllegalArgumentException iae) {
            // User field was empty.
            System.out.println("User field cannot be empty.");
        }
    }

}
