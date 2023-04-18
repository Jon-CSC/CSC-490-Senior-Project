package com.mycompany.seniorproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author juan, ndars
 */
public class ProfilePageController implements Initializable {
    
    enum Statistic {
        TIME,
        WINS,
        MATCHES,
        SCORE
    }
    
    static String userID;
    final int MAX_CHARS_BIO = 150;
    
    @FXML private Label username, characterCounter;
    @FXML private Rectangle profilePicRectangle, tictactoeRectangle, battleshipRectangle, snakeRectangle, javastroidsRectangle,
                chessRectangle, checkersRectangle;
    @FXML private Circle editButtonCircle, cancelEditCircle, profilePicEditButton;
    @FXML private TextArea bioTextArea, editAvatarField;
    @FXML private Label snakePlaytime, snakeHiscore, battleshipPlaytime, battleshipWins,
                 javastroidsPlaytime, javastroidsScore, tictactoePlaytime, tictactoeWins,
                 chessPlaytime, chessWins, checkersPlaytime, checkersWins, battleshipRatio,
                 checkersRatio, chessRatio, tictactoeRatio;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bioTextArea.setEditable(false);
        profilePicEditButton.setVisible(false);
        editButtonCircle.setVisible(false);
        cancelEditCircle.setVisible(false);
        characterCounter.setVisible(false);
        trackBioCharacterCount();
        try {
            fillInProfileData();
        } catch (Exception ex) {
            Logger.getLogger(ProfilePageController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not properly load all profile data. Sorry!");
        }
        loadImages();
    }

    @FXML
    public void goToGameLibrary() throws IOException {
        App.setRoot("gameLibrary");
    }
    
    // pass the controller the user whose profile you want to see
    static void configureUserProfile(String userID) {
        ProfilePageController.userID = userID;
    }
    
    private void fillInProfileData() throws Exception {
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
            editButtonCircle.setVisible(true);
            profilePicEditButton.setVisible(true);
        }
        
        // fill in their personal info
        username.setText(profileUser.getUserID());
        bioTextArea.setText(profileUser.getBiography());
        Image profilePic;
        try {
            URL avatarURL = new URL(profileUser.getAvatarURL());
            profilePic = new Image(avatarURL.toString());
            if (profilePic.isError()) {
                throw new Exception("Image URL does not contain direct image file. Cannot load!");
            }
            profilePicRectangle.setFill(new ImagePattern(profilePic));
        } catch (Exception e) {
//             trusty penguin fallback
            profilePic = new Image(getClass().getResourceAsStream("Images/penguin01.jpg"));
            profilePicRectangle.setFill(new ImagePattern(profilePic));
        }
        
        // fill in their stats
        HashMap<String, Object> gameData = profileUser.getGameData();
        long time;
        long matches;
        long wins;
        long score;
        
        // tictactoe stats
        time = getStat(Game.TICTACTOE, Statistic.TIME, gameData);
        tictactoePlaytime.setText(formatTime(time));
        wins = getStat(Game.TICTACTOE, Statistic.WINS, gameData);
        tictactoeWins.setText(Long.toString(wins));
        matches = getStat(Game.TICTACTOE, Statistic.MATCHES, gameData);
        if(matches != 0) {
            tictactoeRatio.setText(String.format("%.2f", (double)wins/matches));
        }
        
        // battleship stats
        time = getStat(Game.BATTLESHIP, Statistic.TIME, gameData);
        battleshipPlaytime.setText(formatTime(time));
        wins = getStat(Game.BATTLESHIP, Statistic.WINS, gameData);
        battleshipWins.setText(Long.toString(wins));
        matches = getStat(Game.BATTLESHIP, Statistic.MATCHES, gameData);
        if(matches != 0) {
            battleshipRatio.setText(String.format("%.2f", (double)wins/matches));
        }
        
        // snake stats
        time = getStat(Game.SNAKE, Statistic.TIME, gameData);
        snakePlaytime.setText(formatTime(time));
        score = getStat(Game.SNAKE, Statistic.SCORE, gameData);
        snakeHiscore.setText(Long.toString(score));
        
        // javastroids stats
        time = getStat(Game.JAVASTROIDS, Statistic.TIME, gameData);
        javastroidsPlaytime.setText(formatTime(time));
        score = getStat(Game.JAVASTROIDS, Statistic.SCORE, gameData);
        javastroidsScore.setText(Long.toString(score));
        
        // chess stats
        time = getStat(Game.CHESS, Statistic.TIME, gameData);
        chessPlaytime.setText(formatTime(time));
        wins = getStat(Game.CHESS, Statistic.WINS, gameData);
        chessWins.setText(Long.toString(wins));
        matches = getStat(Game.CHESS, Statistic.MATCHES, gameData);
        if(matches != 0) {
            chessRatio.setText(String.format("%.2f", (double)wins/matches));
        }
        
        // checkers stats
        time = getStat(Game.CHECKERS, Statistic.TIME, gameData);
        checkersPlaytime.setText(formatTime(time));
        wins = getStat(Game.CHECKERS, Statistic.WINS, gameData);
        checkersWins.setText(Long.toString(wins));
        matches = getStat(Game.CHECKERS, Statistic.MATCHES, gameData);
        if(matches != 0) {
            checkersRatio.setText(String.format("%.2f", (double)wins/matches));
        }
        
    }
    
    private long getStat(Game game, Statistic stat, HashMap<String, Object> gameData) {
        switch(stat) {
            case TIME:
                return (long)gameData.getOrDefault(game.getTimeField(), (long)0);
            case WINS:
                return (long)gameData.getOrDefault(game.getWinsField(), (long)0);
            case MATCHES:
                return (long)gameData.getOrDefault(game.getMatchesField(), (long)0);
            case SCORE:
                return (long)gameData.getOrDefault(game.getScoreField(), (long)0);
            default:
                throw new IllegalArgumentException();
        }
    }
    
    private String formatTime(long timeInSeconds) {
        Duration time = Duration.ofSeconds(timeInSeconds);
        String timeString = time.toString().toLowerCase().substring(2);
        return timeString;
    }

    /**
     * Loads all the images on a user's profile page.
     */
    private void loadImages() {
        Image tictactoePic, battleshipPic, snakePic, javastroidsPic, editButton, profilePicEditButtonImage, cancelEditButton,
                chessPic, checkersPic;
        tictactoePic = new Image(getClass().getResourceAsStream("Images/tictactoe.png"));
        battleshipPic = new Image(getClass().getResourceAsStream("Images/battleship.png"));
        snakePic = new Image(getClass().getResourceAsStream("Images/snake.png"));
        javastroidsPic = new Image(getClass().getResourceAsStream("Images/javastroids.png"));
        chessPic = new Image(getClass().getResourceAsStream("Images/chess.png"));
        checkersPic = new Image(getClass().getResourceAsStream("Images/checkers.png"));
        editButton = new Image(getClass().getResourceAsStream("Images/edit.png"));
        profilePicEditButtonImage = new Image(getClass().getResourceAsStream("Images/edit.png"));
        cancelEditButton = new Image(getClass().getResourceAsStream("Images/cancel.png"));
        tictactoeRectangle.setFill(new ImagePattern(tictactoePic));
        battleshipRectangle.setFill(new ImagePattern(battleshipPic));
        snakeRectangle.setFill(new ImagePattern(snakePic));
        javastroidsRectangle.setFill(new ImagePattern(javastroidsPic));
        chessRectangle.setFill(new ImagePattern(chessPic));
        checkersRectangle.setFill(new ImagePattern(checkersPic));
        editButtonCircle.setFill(new ImagePattern(editButton));
        profilePicEditButton.setFill(new ImagePattern(profilePicEditButtonImage));
        cancelEditCircle.setFill(new ImagePattern(cancelEditButton));
    }

    /**
     * Allows the user to edit their profile.
     */
    @FXML
    public void editProfile() {
        Image editButton;
        //If the user is not already editing and wants to make changes using the pencil icon
        if (!bioTextArea.isEditable()) {
            bioTextArea.setEditable(true);
            bioTextArea.requestFocus();
            bioTextArea.positionCaret(bioTextArea.getText().length());
            //This is to change the content of the text area
            Region content = (Region) bioTextArea.lookup(".content");
            content.setStyle("-fx-background-color: #353535");
            content.setStyle("-fx-cursor: text");
            cancelEditCircle.setVisible(true);
            characterCounter.setVisible(true);
            editButton = new Image(getClass().getResourceAsStream("Images/checkmark.png"));
            editButtonCircle.setFill(new ImagePattern(editButton));
        } else {
            //If the user was already editing and saved their changes using the checkmark icon
            bioTextArea.setEditable(false);
            cancelEditCircle.setVisible(false);
            characterCounter.setVisible(false);
            //This is to change the content of the text area
            Region content = (Region) bioTextArea.lookup(".content");
            content.setStyle("-fx-background-color: #232323");
            content.setStyle("-fx-cursor: default");
            editButton = new Image(getClass().getResourceAsStream("Images/edit.png"));
            editButtonCircle.setFill(new ImagePattern(editButton));

            // code to take what's in the text area and update the user's bio in firestore
            String newBio = bioTextArea.getText();
            LocalUserAccount.getInstance().updateBiography(newBio);
        }
    }
    
    /**
     * Spawns a window to edit the user's avatar.
     */
    @FXML
    public void editAvatar() {
        if(!editAvatarField.isVisible()) {
            editAvatarField.setVisible(true);
            editAvatarField.setText(LocalUserAccount.getInstance().getUser().getAvatarURL());
            editAvatarField.requestFocus();
            editAvatarField.positionCaret(editAvatarField.getText().length());
            editAvatarField.setEditable(true);
            
            Image checkmark = new Image(getClass().getResourceAsStream("Images/checkmark.png"));
            profilePicEditButton.setFill(new ImagePattern(checkmark));
        } else {
            editAvatarField.setEditable(false);
            editAvatarField.setVisible(false);
            Image editButton = new Image(getClass().getResourceAsStream("Images/edit.png"));
            profilePicEditButton.setFill(new ImagePattern(editButton));
            
            // upload to firestore
            String newAvatar = editAvatarField.getText();
            Image profilePic;
            try {
                URL avatarURL = new URL(newAvatar);
                profilePic = new Image(avatarURL.toString());
                if(null != profilePic) {
                    profilePicRectangle.setFill(new ImagePattern(profilePic));
                    LocalUserAccount.getInstance().updateAvatar(avatarURL.toString());
                }
            } catch (IOException | IllegalArgumentException ex) {
                
            }
        }
    }

    /**
     * Cancels any changes made while editing the user's profile.
     */
    @FXML
    public void cancelEditProfile() {
        Image editButton;
        bioTextArea.setEditable(false);
        cancelEditCircle.setVisible(false);
        characterCounter.setVisible(false);
        //This is to change the content of the text area
        Region content = (Region) bioTextArea.lookup(".content");
        content.setStyle("-fx-background-color: #232323");
        content.setStyle("-fx-cursor: default");
        editButton = new Image(getClass().getResourceAsStream("Images/edit.png"));
        editButtonCircle.setFill(new ImagePattern(editButton));

        // cancel the changes and get the user's bio (it'll be the same bio it was before they began editing)
        bioTextArea.setText(LocalUserAccount.getInstance().getUser().getBiography());
    }

    /**
     * Tracks the amount of characters a user has in their bio.
     */
    private void trackBioCharacterCount() {
        bioTextArea.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().length() <= MAX_CHARS_BIO ? change : null));
        characterCounter.textProperty().bind(bioTextArea.textProperty().length().asString("%d"));
        characterCounter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                //If the user hits the 150 char limit the counter will turn red
                if (characterCounter.getText().equals("150")) {
                    characterCounter.setStyle("-fx-text-fill: #de4a3b");
                } else {
                    characterCounter.setStyle("-fx-text-fill: white");
                }
            }
        });
    }

}
