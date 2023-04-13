package com.mycompany.seniorproject;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Timer timer;
    private static Scene scene;
    public static Firestore fstore;
    private static Stage currentStage;

    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        fstore = firestore();
        scene = new Scene(loadFXML("LoginPage"));
        timer = null;
        stage.setTitle("Minigame App");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Method for getting the current-active scene of the Application.
     * 
     * @return Scene object of current stage.
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     * Method for setting what the current scene should be for the stage.
     * 
     * @param scene The desired current-active scene for the program's GUI.
     */
    public static void setScene(Scene scene) {
        App.scene = scene;
    }

    /**
     * Method to return current instance of UI stage.
     * 
     * @return Stage object of program GUI.
     */
    public static Stage getStage() {
        return currentStage;
    }
    
    /**
     * Method for setting the current FXML-based scene setup.
     * 
     * @param fxml The string name of the FXML file to be used, excluding filetype.
     * @throws IOException 
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    /**
     * Get the current timer.
     * @return the current timer. Can be null if never set.
     */
    public static Timer getTimer() {
        return timer;
    }
    
    /**
     * Set a new global timer.
     * @param timer the new timer
     */
    public static void setTimer(Timer newTimer) {
        timer = newTimer;
    }
    
    /**
     * Delete the current timer.
     */
    public static void clearTimer() {
        timer = null;
    }
    
    /**
     * Search for JSON key in resources and establishes connection to Firebase.
     *
     * @return Instance of FireStore connection.
     */
    public Firestore firestore() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("key.json")))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception ex) {
            // Uncomment line below for more detail. Good luck with Java exception tracing lol
            // ex.printStackTrace();
            System.out.println(">>> WARNING WARNING! Firebase key is incorrect or does not exist. "
                    + "Please check resource directory! <<<");
            return null;
        }
        return FirestoreClient.getFirestore();
    }

    /**
     * Main method to initiate the program.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch();
    }
    
    /**
     * Performs cleanup on program stop.
     */
    @Override
    public void stop() {
        // if a timer is running and we're logged in, still make sure to record the playtime
        if(null != timer && timer.isRunning() && LocalUserAccount.getInstance().isLoggedIn()) {
            timer.stop();
            LocalUserAccount.getInstance().recordTime(timer);
        }
    }
}
