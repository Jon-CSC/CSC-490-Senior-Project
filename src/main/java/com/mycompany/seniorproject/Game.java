package com.mycompany.seniorproject;

/**
 *
 * @author ndars
 */
public enum Game {
    TICTACTOE,
    BATTLESHIP,
    CHECKERS,
    CHESS,
    SNAKE,
    JAVASTROIDS;
    
    @Override
    public String toString() {
        switch(this) {
            case TICTACTOE: return "tictactoe";
            case BATTLESHIP: return "battleship";
            case CHECKERS: return "checkers";
            case CHESS: return "chess";
            case SNAKE: return "snake";
            case JAVASTROIDS: return "javastroids";
            default: throw new IllegalArgumentException();
        }
    }
}
