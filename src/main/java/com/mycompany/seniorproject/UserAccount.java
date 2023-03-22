
package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Stores user account data. Designed to work with Firestore.
 * 
 * @author ndars
 */
@IgnoreExtraProperties
public class UserAccount {
    
    public static final String USER_DB_NAME = "Users";
    
    private String userID;
    public static final String USERID_FIELD = "userID";
    
    private String biography;
    public static final String BIOGRAPHY_FIELD = "biography";
    
    private String avatarURL;
    public static final String AVATAR_FIELD = "avatarURL";
    
    private HashMap<String, Object> gameData;
    public static final String GAMEDATA_FIELD = "gameData";
    
    // requisite default constructor for deserializing from Firestore
    public UserAccount() {}
    
    // make a default user account
    public UserAccount(String userID) {
        this.userID = userID;
        this.biography = "Hello, world!";
        this.avatarURL = "https://i.imgur.com/n96r5OU.png";
        this.gameData = new HashMap<>();
        initGameData();
    }

    // similarly requisite all-fields constructor
    public UserAccount(String userID, String biography, String avatarURL, HashMap<String, Object> gameData) {
        this.userID = userID;
        this.biography = biography;
        this.avatarURL = avatarURL;
        this.gameData = gameData;
    }
    
    /**
     * Retrieve this user's ID.
     * @return a user ID
     */
    public String getUserID() {
        return userID;
    }
    
    /**
     * Retrieve this user's biography.
     * @return a user bio
     */
    public String getBiography() {
        return biography;
    }
    
    /**
     * Retrieve this user's avatar URL.
     * @return a URL pointing to this user's avatar
     */
    public String getAvatarURL() {
        return avatarURL;
    }
    
    /**
     * Retrieve this user's game data.
     * @return a HashMap with key-value pairs of game data.
     */
    public HashMap<String, Object> getGameData() {
        return gameData;
    }
    
    private void initGameData() {
        // snake
        gameData.put("snake_time_mins", 0);
        gameData.put("snake_hiscore", 0);
        
        // battleship
        gameData.put("battleship_time_mins", 0);
        gameData.put("battleship_wins", 0);
        
        // checkers
        gameData.put("checkers_time_mins", 0);
        gameData.put("checkers_wins", 0);
        
        // tic tac toe
        gameData.put("tictactoe_time_mins", 0);
        gameData.put("tictactoe_wins", 0);
    }
    
    /**
     * Download a user account 
     * @param userID the userID of the data to download
     * @param fs a valid firestore instance
     * @return the stored UserAccount, or null if an error occurred
     */
    public static UserAccount downloadUser(String userID, Firestore fs) {
        try {
            DocumentReference userDocRef = App.fstore.collection("Users").document(userID);
            ApiFuture<DocumentSnapshot> future = userDocRef.get();
            DocumentSnapshot userDoc = future.get();
            return userDoc.toObject(UserAccount.class);
        } catch (InterruptedException | ExecutionException ex) {
            return null;
        }
    }
}
