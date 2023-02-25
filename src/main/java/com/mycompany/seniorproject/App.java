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
import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Firestore fstore;
    
    @Override
    public void start(Stage stage) throws IOException {
        fstore = firestore();
        scene = new Scene(loadFXML("LoginPage"), 640, 480);
        stage.setScene(scene);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setTitle("MiniGame App");
        
        // Icon code below, for whenever we get an icon for the app
        // Image icon = new Image(getClass().getResourceAsStream("javastroids_icon.png"));
        // stage.getIcons().add(icon);

        stage.show();
        
        App.getStage().setOnHiding(e -> {
            Platform.exit();
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static Scene getScene() {
        return scene;
    }
    
    public static void setScene(Scene scene) {
        App.scene = scene;
    }
    
    public static Stage getStage() {
        return currentStage;
    }

    public static void main(String[] args) {
        launch();
    }
    
    public Firestore firestore() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("key.json")))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return FirestoreClient.getFirestore();
    }

}