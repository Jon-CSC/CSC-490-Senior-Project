package com.mycompany.seniorproject;

import java.util.UUID;

/**
 * Abstract representation of the results of a game session.
 * 
 * Note: one instance of a GameResult corresponds to one player's results.
 * This means that multiplayer games should generate multiple results: one for each player.
 * A query needs to be run over all GameResults with the same sessionID to get the full picture of a game.
 * 
 * @author ndars
 */
public abstract class GameResult {
    
    // a unique ID corresponding to a particular game session
    // NOTE: we're storing this in a string to make Firestore happy, but it really represents a UUID!
    protected String sessionID;
    // the userID of the player that recorded this result
    protected String userID;
    // a string naming which game this result is associated with
    protected String game;
    
    // serializing objects from firestore requires a default constructor
    public GameResult() { }

    public GameResult(UUID sessionID, String userID, String game) {
        this.sessionID = sessionID.toString();
        this.userID = userID;
        this.game = game;
    }
    
    public String getSessionID() {
        return sessionID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public String getGame() {
        return game;
    }
}
