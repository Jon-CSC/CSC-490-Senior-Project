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

    private static Scene scene;
    public static Firestore fstore;
    private static Stage currentStage;
    private static String currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        fstore = firestore();
        scene = new Scene(loadFXML("LoginPage"));
        stage.setTitle("Minigame App");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return currentStage;
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

    public static void main(String[] args) {
        launch();
    }
}
