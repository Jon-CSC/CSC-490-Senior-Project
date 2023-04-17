package com.mycompany.seniorproject;

/**
 *
 * @author ndars
 */
public enum Game {
    NOGAME,
    TICTACTOE,
    BATTLESHIP,
    CHECKERS,
    CHESS,
    SNAKE,
    JAVASTROIDS;
    
    @Override
    public String toString() {
        switch(this) {
            case NOGAME: return "nogame";
            case TICTACTOE: return "tictactoe";
            case BATTLESHIP: return "battleship";
            case CHECKERS: return "checkers";
            case CHESS: return "chess";
            case SNAKE: return "snake";
            case JAVASTROIDS: return "javastroids";
            default: throw new IllegalArgumentException();
        }
    }
    
    public String getTimeField() {
        return this.toString() + "_time";
    }
    
    public String getScoreField() {
        return this.toString() + "_hiscore";
    }

    public String getLastScoreField() {
        return this.toString() + "_lastscore";
    }
    
    public String getMatchesField() {
        return this.toString() + "_matches";
    }
    
    public String getWinsField() { 
        return this.toString() + "_wins";
    }
    
    public String getRatingField() {
        return this.toString() + "_rating";
    }
}
