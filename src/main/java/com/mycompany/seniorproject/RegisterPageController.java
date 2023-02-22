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
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chriscanenguez
 */
public class RegisterPageController implements Initializable {

    @FXML
    private TextField usernameField, passwordField;
    
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
    public void register() throws IOException {
        createUser();
        createUserDocument();
    }
    
    public void createUser() throws IOException {
        try {
            // Make a new user record create request
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setUid(usernameField.getText().trim())
                    .setPassword(passwordField.getText().trim());
            // Make a new userRecord instance and use the data from the create request
            UserRecord newUser = FirebaseAuth.getInstance().createUser(request);
            Map<String, Object> claims = new HashMap<>();
            claims.put("user", true);
            FirebaseAuth.getInstance().setCustomUserClaims(newUser.getUid(), claims);
            App.setRoot("LoginPage");
        } catch (FirebaseAuthException ex) {
            System.out.println("FirebaseAuthException");
        } catch (IllegalArgumentException iae) {
            System.out.println("IllegalArgumentException");
        }
    }
    
    public void createUserDocument() {
        DocumentReference docRef = App.fstore.collection("Users").document(usernameField.getText());
        Map<String, Object> data = new HashMap<>();
        data.put("Password", passwordField.getText());
        ApiFuture<WriteResult> result = docRef.set(data);
    }    
    
}
