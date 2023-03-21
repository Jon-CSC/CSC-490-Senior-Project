
package com.mycompany.seniorproject;

import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import java.util.HashMap;

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
}
