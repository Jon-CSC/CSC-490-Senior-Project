package com.mycompany.seniorproject;

import java.util.UUID;

/**
 * Tracks the results of point-based games (like Snake).
 * 
 * @author ndars
 */
public class ScoreGameResult extends GameResult {
    
    // the player's score in the recorded game session
    protected int score;
    
    public ScoreGameResult() { }
    
    public ScoreGameResult(UUID sessionID, String userID, String game) {
        super(sessionID, userID, game);
        score = 0;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int addScore(int amount) {
        score += amount;
        return score;
    }
    
    public int subtractScore(int amount) {
        score -= amount;
        return score;
    }

}
