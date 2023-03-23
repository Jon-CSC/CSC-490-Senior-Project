package com.mycompany.seniorproject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author juan, ndars
 */
public class ProfilePageController implements Initializable {
    
    static String userID;
    
    @FXML private ImageView profilePicture;
    @FXML private Label username, bio;
    @FXML private Hyperlink bioEditLink, avatarEditLink;

    @FXML private Label snakePlaytime, snakeHiscore, battleshipPlaytime, battleshipWins,
                 checkersPlaytime, checkersWins, tictactoePlaytime, tictactoeWins;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // in future, it'd be a good idea to check OS and dynamically update game images instead
        // for now, i just set them in the FXML
        fillInProfileData();
    }
    
    // pass the controller the user whose profile you want to see
    static void configureUserProfile(String userID) {
        ProfilePageController.userID = userID;
    }
    
    private void fillInProfileData() {
        // find the right account
        if(null == userID) {
            // fall back to the local account if there's a misconfiguration
            userID = LocalUserAccount.getInstance().getUser().getUserID();
        }
        UserAccount profileUser = UserAccount.downloadUser(userID, App.fstore);
        if(null == profileUser) {
            return; // maybe show the user an error here?
        }
        
        // if it's the local account, make the edit links visible and add their logic
        if(LocalUserAccount.getInstance().getUser().getUserID().equals(profileUser.getUserID())) {
            bioEditLink.setOnMouseClicked(e -> {
                System.out.println("editing bio :D");
            });
            avatarEditLink.setOnMouseClicked(e -> {
                System.out.println("editing avatar :D");
            });
            bioEditLink.setVisible(true);
            avatarEditLink.setVisible(true);
        }
        
        // fill in their personal info
        username.setText(profileUser.getUserID());
        bio.setText(profileUser.getBiography());
        Image profilePic;
        try {
            URL avatarURL = new URL(profileUser.getAvatarURL());
            profilePic = new Image(avatarURL.toString());
        } catch (IOException ex) {
            profilePic = new Image(getClass().getResourceAsStream("Images\\penguin01.jpg"));
        }
        profilePicture.setImage(profilePic);
        
        // fill in their stats
        HashMap<String, Object> gameData = profileUser.getGameData();
        snakePlaytime.setText(gameData.getOrDefault(UserAccount.SNAKE_TIME, 0).toString());
        snakeHiscore.setText(gameData.getOrDefault(UserAccount.SNAKE_HISCORE, 0).toString());
        battleshipPlaytime.setText(gameData.getOrDefault(UserAccount.BATTLESHIP_TIME, 0).toString());
        battleshipWins.setText(gameData.getOrDefault(UserAccount.BATTLESHIP_WINS, 0).toString());
        checkersPlaytime.setText(gameData.getOrDefault(UserAccount.CHECKERS_TIME, 0).toString());
        checkersWins.setText(gameData.getOrDefault(UserAccount.CHECKERS_WINS, 0).toString());
        tictactoePlaytime.setText(gameData.getOrDefault(UserAccount.TICTACTOE_TIME, 0).toString());
        tictactoeWins.setText(gameData.getOrDefault(UserAccount.TICTACTOE_WINS, 0).toString());
    }
}
