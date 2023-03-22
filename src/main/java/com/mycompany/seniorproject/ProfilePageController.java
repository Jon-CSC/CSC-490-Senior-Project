package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author juan
 */
public class ProfilePageController implements Initializable {

    @FXML
    private ImageView profilePicture, gameThumbnail1, gameThumbnail2, gameThumbnail3, gameThumbnail4;
    Image profilePic = new Image(getClass().getResourceAsStream("ExampleProfilePicture.png"));
    
    @FXML
    private Label username, bio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        profilePicture.setImage(profilePic);
        gameThumbnail1.setImage(profilePic);
        gameThumbnail2.setImage(profilePic);
        gameThumbnail3.setImage(profilePic);
        gameThumbnail4.setImage(profilePic);
        fillInProfileData(LocalUserAccount.getInstance().getActiveUser().getUserID());
    }
    
    private void fillInProfileData(String userID) {
        UserAccount currentUser;
        if(LocalUserAccount.getInstance().getActiveUser().getUserID().equals(userID)) {
            currentUser = LocalUserAccount.getInstance().getActiveUser();
        } else {
            currentUser = UserAccount.downloadUser(userID, App.fstore);
        }
        if(null == currentUser) {
            return; // maybe show the user an error here?
        }
        username.setText(currentUser.getUserID());
        bio.setText(currentUser.getBiography());
        try {
            URL avatarURL = new URL(currentUser.getAvatarURL());
            profilePic = new Image(avatarURL.toString());
        } catch (IOException ex) {
            profilePic = new Image(getClass().getResourceAsStream("ExampleProfilePicture.png"));
        }
        profilePicture.setImage(profilePic);
    }
    
}
