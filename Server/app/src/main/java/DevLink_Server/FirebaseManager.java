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
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.conscrypt.OpenSSLEvpCipherAES.AES.ECB;

import com.google.api.core.ApiFuture;
import com.google.api.core.SettableApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firestore.v1.Document;

import io.grpc.netty.shaded.io.netty.channel.MessageSizeEstimator;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentChange.Type;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FirebaseManager {
    private DataverseManager dataverseManager;
    private Firestore firestoreInstance;
    private FirebaseMessaging firebaseMessagingInstance;
    private GitHubManager gitHubManager;
    private StackExchangeManager stackExchangeManager;
    private boolean isLoading;

    public FirebaseManager(){
        dataverseManager = new DataverseManager();
        gitHubManager = new GitHubManager();
        stackExchangeManager = new StackExchangeManager();
    }

    public void InitializeFirebase() throws UnsupportedOperationException, InterruptedException, ExecutionException
    {
        // TODO: Create a method to initialize and connect to the different Firebase Services.
        // Set the environment variable with the following command in the console: $env:GOOGLE_APPLICATION_CREDENTIALS="path/to/the/service-account-file.json"
        isLoading = true;
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
        try {
            HandleChangedGuilds();
            HandleChangedChannels();
            HandleChangedMessages();
            HandleChangedUsers();
            isLoading = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public void DatabaseReadExample() throws InterruptedException, ExecutionException
    {
        DocumentReference docRef = firestoreInstance.collection("Guilds").document("SampleGuild");
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists())
        {
            System.out.println("Document data: " + document.getData());
        }else
        {
            System.out.println("No such document!");
        }
    }

    private List<ChannelData> HandleChangedChannels() throws Exception {
        final SettableApiFuture<List<ChannelData>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        firestoreInstance.collectionGroup("Guilds")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirestoreException e)
                    {
                        if (e != null)
                        {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        // TODO: Create a class / abstract to hold the message data.
                        List<ChannelData> channelChanges = new ArrayList<>();
                        var documents = snapshot.getDocumentChanges();
                        if (!isLoading)
                        {
                            if (documents.size() > 0)
                            {
                                channelChanges = snapshot.toObjects(ChannelData.class);
                                // Prevent any actions to be done when the code is loading the first local datasets.
                                if (!isLoading)
                                {
                                    for (ChannelData channel : channelChanges)
                                    {
                                        // TODO: Handle channel changes.
                                    }
                                }
                            }
                            else
                                System.out.println("No result found from the query.");
                        }

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(channelChanges);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }

    private List<GuildData> HandleChangedGuilds() throws Exception {
        final SettableApiFuture<List<GuildData>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        firestoreInstance.collectionGroup("Guilds")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirestoreException e)
                    {
                        if (e != null)
                        {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        // TODO: Create a class / abstract to hold the message data.
                        List<GuildData> guildChanges = new ArrayList<>();
                        var documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            guildChanges = snapshot.toObjects(GuildData.class);
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                for (GuildData guild : guildChanges)
                                {
                                    // TODO: Handle guild changes.
                                }
                            }
                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(guildChanges);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }
    private List<MessageData> HandleChangedMessages() throws Exception {
        final SettableApiFuture<List<MessageData>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        firestoreInstance.collectionGroup("Messages")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirestoreException e)
                    {
                        if (e != null)
                        {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        // TODO: Create a class / abstract to hold the message data.
                        List<MessageData> messages = new ArrayList<>();
                        var documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            // TODO: find out if you can convert the objects.
                            messages = snapshot.toObjects(MessageData.class);
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                for (MessageData message : messages){
                                    if (message.getType() == MessageData.MessageType.Text){
                                        // TODO: Send notification and data retrieve request to clients.

                                    }
                                    else if (message.getType() == MessageData.MessageType.Command)
                                    {
                                        // TODO: Handle Command: Interact with the corresponding API.
                                        switch (message.getContent().split(" ")[0].toLowerCase()){
                                            case "github":
                                                break;
                                            case "stackexchange":
                                                break;
                                            case "dataverse":
                                                break;
                                        }
                                    }
                                    else
                                        System.out.println("Faulty message!");
                                }
                            }
                            System.out.println(messages);

                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(messages);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }
    private List<UserData> HandleChangedUsers() throws Exception
    {
        final SettableApiFuture<List<UserData>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        firestoreInstance.collectionGroup("Users")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirestoreException e)
                    {
                        if (e != null)
                        {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        // TODO: Create a class / abstract to hold the message data.
                        List<UserData> userChanges = new ArrayList<>();
                        var documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            userChanges = snapshot.toObjects(UserData.class);
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                for (UserData user : userChanges)
                                {
                                    // TODO: Handle user changes.
                                }
                            }
                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(userChanges);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }

    private String GetUserRegistrationToken(DocumentReference user) throws InterruptedException, ExecutionException {
        String registrationToken = null;
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = user.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists())
        {
            System.out.println("Document data: " + document.getData());
            registrationToken = document.getString("registrationToken");
        }
        else
        {
            System.out.println("No such document!");
        }
        return registrationToken;
    }
}