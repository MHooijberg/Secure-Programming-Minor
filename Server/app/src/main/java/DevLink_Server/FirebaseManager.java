package devlink_server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseManager {
    private DataverseManager dataverseManager;
    private Firestore firestoreInstance;
    private FirebaseMessaging firebaseMessagingInstance;
    private GitHubManager gitHubManager;
    private StackExchangeManager stackExchangeManager;

    public FirebaseManager(){
        dataverseManager = new DataverseManager();
        gitHubManager = new GitHubManager();
        stackExchangeManager = new StackExchangeManager();
    }

    public void InitializeFirebase()
    {
        // TODO: Create a method to initialize and connect to the different Firebase Services.
        // Set the environment variable with the following command in the console: $env:GOOGLE_APPLICATION_CREDENTIALS="path/to/the/service-account-file.json"
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://devlink-89dfe.firebaseio.com/")
                .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        FirebaseApp.initializeApp(options);
        FirebaseApp.getInstance();
        firestoreInstance = FirestoreClient.getFirestore();
        firebaseMessagingInstance = FirebaseMessaging.getInstance();
    }


    public void FCMDirectExample(String registrationToken){
        // This registration token comes from the client FCM SDKs.
        //String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // See documentation on defining a message payload.
        Message message = Message.builder()
            .putData("Message", "This is some custom message content")
            .putData("Recipient", "Georgie is the recipient")
            .setToken(registrationToken)
            .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try {
            response = firebaseMessagingInstance.send(message);
        } catch (FirebaseMessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }

    public void FCMMulticastExample(List<String> registrationTokens){
        MulticastMessage message = MulticastMessage.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .addAllTokens(registrationTokens)
            .build();
        BatchResponse response = null;
        try {
            response = firebaseMessagingInstance.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // See the BatchResponse reference documentation
        // for the contents of response.
        if (response != null)
            System.out.println(response.getSuccessCount() + " messages were sent successfully");

        // if (response.getFailureCount() > 0) {
        //     List<SendResponse> responses = response.getResponses();
        //     List<String> failedTokens = new ArrayList<>();
        //     for (int i = 0; i < responses.size(); i++) {
        //         if (!responses.get(i).isSuccessful()) {
        //         // The order of responses corresponds to the order of the registration tokens.
        //         failedTokens.add(registrationTokens.get(i));
        //         }
        //     }
    }

    public void FCMTopicExample(String topic ){
    // The topic name can be optionally prefixed with "/topics/".
    //String topic = "highScores";

    // See documentation on defining a message payload.
    Message message = Message.builder()
        .putData("score", "850")
        .putData("time", "2:45")
        .setTopic(topic)
        .build();

    // Send a message to the devices subscribed to the provided topic.
    String response = null;
    try {
        response = firebaseMessagingInstance.send(message);
        
    } catch (FirebaseMessagingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // Response is a message ID string.
    System.out.println("Successfully sent message: " + response);
    }
}