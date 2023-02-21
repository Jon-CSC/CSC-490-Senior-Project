package com.mycompany.seniorproject;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    public void goToLoginPage() throws IOException {
        // Retrieves Loader for Login page.
        App.setRoot("LoginPage");
    } // End goToLoginPage.
}
