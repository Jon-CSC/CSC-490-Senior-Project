package com.mycompany.seniorproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller Class
 *
 * @author juan
 */
public class SettingsController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }

    @FXML
    public void showInfo() {

    }

}
