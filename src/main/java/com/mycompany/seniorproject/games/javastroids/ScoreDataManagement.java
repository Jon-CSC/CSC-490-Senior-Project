package com.mycompany.seniorproject.games.javastroids;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains code for accessing and modifying the leaderboard scoring database.
 * Also contains methods for importing and exporting the data in the form of JSON.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class ScoreDataManagement {

    private static String databaseURL;
    private static Connection conn = null;
    private static String tableName = "PlayerScores";
    private static boolean connectionInitialized = false;

    /**
     * Method for initializing the Application's connection to the MS Access SQL database.
     */
    public static void initializeDatabaseConnection() {
        // Data retrieval
        if (!connectionInitialized) {
            databaseURL = "jdbc:ucanaccess://.//src\\main\\resources\\com\\karp\\javastroids_v2\\javastroids_scores_db.accdb";
            try {
                conn = DriverManager.getConnection(databaseURL);
                System.out.println("Successfully connected to DB!");
                connectionInitialized = true;
            } catch (SQLException ex) {
                System.out.println("Error connecting to DB...");
                java.util.logging.Logger.getLogger(
                        ScoreDataManagement.class.getName()).log(
                                java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Method for adding a score entry to the leaderboard database.
     * 
     * @param name Name of the player as a String.
     * @param score Score from gameplay as an integer.
     */
    public static void addEntryToDatabase(String name, int score) {
        try {
            String sql = "INSERT INTO " + tableName + " (PlayerName, Score) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, score);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println("New score added to DB!");
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(
                    ScoreDataManagement.class.getName()).log(
                            java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Method for retrieving the current database state as a linked list.
     * @return LinkedList of type ScoreEntry.
     */
    public static LinkedList<ScoreEntry> pullEntriesFromDatabase() {
        LinkedList<ScoreEntry> resultingScoreList = new LinkedList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);
            
            while (result.next()) {
                ScoreEntry newEntry = new ScoreEntry(result.getString("PlayerName"), result.getInt("Score"));
                resultingScoreList.add(newEntry);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Collections.sort(resultingScoreList);
        Collections.reverse(resultingScoreList);
        return resultingScoreList;
    }
    
    /**
     * Method for locating and importing JSON data into the SQL database, overwriting 
     * the existing data.
     */
    public static void importfromJSON() {
        LinkedList<ScoreEntry> javaStroidsScores = new LinkedList<>();
        String fileName = "javastroidsDBexport";
        try {
            String fileInput = fileName + ".json";
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(fileInput));    
            javaStroidsScores = new LinkedList<>(Arrays.asList(gson.fromJson(reader, ScoreEntry[].class)));
            DeleteAllScoreEntriesFromDB();
        } catch (IOException ex) {
            System.out.println("File does not exist, please try another file name...");
        }
        
        for (ScoreEntry entry: javaStroidsScores) {
            addEntryToDatabase(entry.getName(), entry.getScore());
        }
    }
    
    /**
     * Method for exporting the current state of the SQL database to a JSON file.
     */
    public static void exportToJSON() {
        Writer writer = null;
        String fileName = "javastroidsDBexport";
        try {
            LinkedList<ScoreEntry> javaStroidsScores = pullEntriesFromDatabase();
            String fileInput = fileName + ".json";
            Gson gson = new Gson();
            writer = Files.newBufferedWriter(Paths.get(fileInput));
            gson.toJson(javaStroidsScores, writer);
            System.out.println("Successfully saved DB to JSON file!");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ScoreDataManagement.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exporting to JSON failed...");
        }
    }
    
    /**
     * Method for clearing all score entries from the SQL database.
     */
    public static void DeleteAllScoreEntriesFromDB() {
        try {
            String sql = "DELETE FROM PlayerScores";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int deleteRows = preparedStatement.executeUpdate();
            if (deleteRows > 0) {
                System.out.println("All score entries have been removed...");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScoreDataManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
