package com.mycompany.seniorproject.games.javastroids;

import com.mycompany.seniorproject.App;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controller class for controlling leader board scene elements and accessing data.
 *
 * @author Justin Karp
 * @version 1.0
 * @since 12/8/2021
 */
public class LeaderboardController {

    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonExport;
    @FXML
    private Button buttonImport;

    @FXML
    private Canvas canvasOfScores;

    private Font hyperspaceFont;
    private double scrolledAmount = 0;
    private LinkedList<ScoreEntry> currentScoreList;

    /**
     * Method that is invoked upon this FXML scene being initialized.
     */
    @FXML
    public void initialize() {
        // Listeners for reactive buttons
        buttonBack.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonBack.setText("> BACK TO GAME <");
            } else {
                buttonBack.setText("BACK TO GAME");
            }
        });
        
        buttonExport.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonExport.setText("> EXPORT DATA <");
            } else {
                buttonExport.setText("EXPORT DATA");
            }
        });
        
        buttonImport.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                buttonImport.setText("> IMPORT DATA <");
            } else {
                buttonImport.setText("IMPORT DATA");
            }
        });

        // Pull in font file for score number rendering
        try {
            String fontFile = "HyperspaceBold.otf";
            InputStream fontStream = GameLoop.class.getResourceAsStream(fontFile);
            hyperspaceFont = Font.loadFont(fontStream, 30);
            fontStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        currentScoreList = ScoreDataManagement.pullEntriesFromDatabase();
        populateScoreInfo();
    }

    @FXML
    private void switchToMainMenu() throws IOException {
        System.out.println("Switching to main menu!");
        App.setRoot("games/javastroids/mainMenu");
    }

    @FXML
    private void scrollThroughScores(ScrollEvent event) {
        event.getDeltaY();
        scrolledAmount += event.getDeltaY() * 0.3;
        populateScoreInfo();
    }

    @FXML
    private void exportTheData() {
        // if below is true, display popup of success, otherwise display popup of failure
        ScoreDataManagement.exportToJSON();
    }
    
    @FXML
    private void importTheData() {
        // if below is true, display popup of success, otherwise display popup of failure
        ScoreDataManagement.importfromJSON();
    }
    
    private void populateScoreInfo() {
        GraphicsContext gc = canvasOfScores.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasOfScores.getWidth(), canvasOfScores.getHeight());
        gc.setFill(Color.WHITE);
        gc.setFont(hyperspaceFont);
        Iterator<ScoreEntry> entryIter = currentScoreList.iterator();
        int nextEntryVerticalOffset = 0;
        while (entryIter.hasNext()) {
            ScoreEntry nextEntry = entryIter.next();
            gc.fillText(nextEntry.getName(),
                    (gc.getCanvas().getWidth() / 2) - 200,
                    (gc.getCanvas().getHeight() / 5) + nextEntryVerticalOffset + scrolledAmount);
            gc.fillText("" + nextEntry.getScore(),
                    (gc.getCanvas().getWidth() / 2) + 100,
                    (gc.getCanvas().getHeight() / 5) + nextEntryVerticalOffset + scrolledAmount);
            nextEntryVerticalOffset += 40;
        }
    }
}
