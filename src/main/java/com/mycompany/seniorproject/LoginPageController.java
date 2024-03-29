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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

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
        if (App.fstore == null) {
            errorLabel.setVisible(true);
            errorLabel.setText("Connection failed. Check JSON key or network connection.");
        } else {
            errorLabel.setVisible(false);
        }
        App.getStage().setWidth(500);
        App.getStage().setHeight(500);
        App.getStage().setResizable(false);
        App.getStage().centerOnScreen();
    }

    @FXML
    void onEnter(KeyEvent event) throws IOException, ExecutionException, InterruptedException {
        if (event.getCode().toString().equalsIgnoreCase("ENTER")) {
            logIn();
        }
    }

    @FXML
    public void goToRegisterPage() throws IOException {
        // Retrieves Loader for Register page.
        App.setRoot("RegisterPage");
    } // End goToRegisterPage.

    @FXML
    public void goToGameLibraryPage() throws IOException {
        // Retrieves Loader for gameLibrary page.
        App.setRoot("gameLibrary");
    } // End goToGameLibraryPage.
    
    @FXML
    public void logIn() throws InterruptedException, ExecutionException, IOException {
        try {
            //Attempt to log in a user using what's typed in the usernameField
            UserRecord user = FirebaseAuth.getInstance().getUser(usernameField.getText());
            DocumentSnapshot userDoc = pullUserDocument(usernameField.getText());
            //UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(usernameField.getText());

            //If no exception has been caught, the password is then checked
            if (verifyPassword(userDoc)) {
                //We would put code here to switch to whatever fxml file is considered the main page/library
//                errorLabel.setText("Username and password match. We would switch to the main page now.");
//                errorLabel.setVisible(true);
                LocalUserAccount.getInstance().login(userDoc.toObject(UserAccount.class));
                goToGameLibraryPage();
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

    public boolean verifyPassword(DocumentSnapshot userDoc) {
        //We compare what's in the password field in the user's Firestore document vs. what's typed in the passwordField
        return userDoc.getData().get("Password").toString().equals(passwordField.getText());
    }
    
    private DocumentSnapshot pullUserDocument(String userID) throws InterruptedException, ExecutionException {
        DocumentReference userDoc = App.fstore.collection("Users").document(userID);
        ApiFuture<DocumentSnapshot> future = userDoc.get();
        DocumentSnapshot document = future.get();
        return document;
    }
    
}
