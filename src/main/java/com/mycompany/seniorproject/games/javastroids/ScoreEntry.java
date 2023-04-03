/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject.games.javastroids;

/**
 * Contains code for instantiating and managing a basic Score Entry object, which 
 * holds a name and score.
 * 
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class ScoreEntry implements Comparable {

    private String name;
    private int score;
    
    /**
     * Method to create a new ScoreEntry object with a specified name and score.
     * 
     * @param name Name of player as a String.
     * @param score Score of player as an integer.
     */
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Method for retrieving the name of a given entry.
     * 
     * @return Name in entry as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Method for retrieving the score of a given entry.
     * 
     * @return Score in entry as integer.
     */
    public int getScore() {
        return score;
    }

    /**
     * Method to set the name in an entry.
     * 
     * @param name Name as a String.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to set the score in an entry.
     * 
     * @param score Score an an integer.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /** 
     * Method for comparing two ScoreEntry objects based on their integer score.
     * 
     * @param o ScoreEntry object to be compared to this object.
     * @return Result of comparison as integer; -1 means other object is greater 
     * than this one, 1 means this object is greater than other object, 0 means 
     * equivalent score values.
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ScoreEntry)) {
            return 0;
        }
        
        ScoreEntry otherScore = (ScoreEntry) o;

        if (this.score > otherScore.getScore()) {
            return 1;
        } else if (this.score < otherScore.getScore()) {
            return -1;
        }
        return 0;
    }
}
