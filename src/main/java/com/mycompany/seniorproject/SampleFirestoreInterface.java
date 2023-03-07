
package com.mycompany.seniorproject;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proof-of-concept demonstrating pulling data from Firestore.
 * @author ndars
 */
public class SampleFirestoreInterface {
    
    public SampleFirestoreInterface() {
        
    }
    
    public void storeResult(GameResult result) {
        try {
            var options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("key.json")))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Firestore db = FirestoreClient.getFirestore();
        db.collection("GameResults").add(result);
    }
    
    public GameResult getResult(UUID uuid) {
        try {
                var options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("key.json")))
                        .build();
                FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> future = db.collection("GameResults")
                    .whereEqualTo("sessionID", uuid.toString())
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            return docs.get(0).toObject(ScoreGameResult.class);
        } catch (InterruptedException | ExecutionException | IOException ex) {
            Logger.getLogger(SampleFirestoreInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
