/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author justi
 */
public class GameRatings {

    public static final String GAME_RATING_DB = "GameRatings";
    
    public GameRatings() {}
    
    // This is unfinished and I have no idea what I'm doing here, firestore eludes me
//    public static double getAvgRating(String game, Firestore fs) {
//        if(null == game || null == fs) {
//            return -1.0;
//        }
//        try {
//            DocumentReference gameRatingDoc = fs.collection(GameRatings.GAME_RATING_DB).document(game);
//            ApiFuture<DocumentSnapshot> future = gameRatingDoc.get();
//            DocumentSnapshot ratingDocSnap = future.get();
//            GameRatingDocument ratingDocObj =  ratingDocSnap.toObject(GameRatingDocument.class);
//            return ratingDocObj.getRating();
//            
//        } catch (InterruptedException | ExecutionException ex) {
//            return -1.0;
//        }
//    }
    
    public static double computeAverageNewRating(double avgRating, double totalRatings, double newRating) {
        double newAvg = ( avgRating * (totalRatings / (totalRatings+1)) ) 
                + (newRating * (1 / (totalRatings+1)) );
        return newAvg;
    }
    
    public static double computeAverageChangeRating(double avgRating, double totalRatings, double newRating, double oldRating) {
        // First calculate new average by removing old rating
        totalRatings--;
        double tempAvg = ((avgRating * totalRatings) - oldRating) / (totalRatings);
        // Then, calculate new average by adding the new rating
        double newAvg = computeAverageNewRating(tempAvg, totalRatings, newRating);
        return newAvg;
    }
}

// class for taking in firestore GameRatings document as an object
class GameRatingDocument {
    
    private String gameTitle;
    static final String GAMETITLE = "GameTitle";
    
    private double rating;
    static final String RATING = "Rating";
    
    private double totalRatingCount;
    static final String TOTALRATINGCOUNT = "TotalRatingCount";
    
    GameRatingDocument() {}
    
    GameRatingDocument(String gameTitle) {
        this.gameTitle = gameTitle;
        this.rating = 0;
        this.totalRatingCount = 0;
    }
    
    GameRatingDocument(String gameTitle, double rating, double totalRatingCount) {
        this.gameTitle = gameTitle;
        this.rating = rating;
        this.totalRatingCount = totalRatingCount;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public double getRating() {
        return rating;
    }

    public double getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTotalRatingCount(double totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }
    
    
}
