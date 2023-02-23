package com.mycompany.seniorproject;

import java.util.UUID;

/**
 * Tracks the results of success-based games (like tic-tac-toe).
 * 
 * @author ndars
 */
public class WinLossGameResult extends GameResult {
    
    // whether or not this player won the game
    protected boolean isWinner;
    
    public WinLossGameResult() { }
    
    public WinLossGameResult(UUID sessionID, String userID, String game) {
        super(sessionID, userID, game);
        isWinner = false;
    }
    
    public boolean isWinner() {
        return isWinner;
    }
    
    public void setWin() {
        isWinner = true;
    }
    
    public void setLose() {
        isWinner = false;
    }
    
}
