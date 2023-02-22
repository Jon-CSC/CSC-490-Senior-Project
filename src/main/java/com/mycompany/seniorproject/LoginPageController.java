/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author canen
 */
public class LoginPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
    
}
