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
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.MulticastMessage.Builder;
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

import devlink_server.FCMMessageData.ObjectType;
import io.grpc.netty.shaded.io.netty.channel.MessageSizeEstimator;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentChange.Type;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FirebaseManager {
    // Firebase Settings and Constants:
    private static final int MESSAGE_TTL = 3600*24*7;

    // Private variables
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
            HandleChangedChannels();
            HandleChangedGuilds();
            HandleChangedMessages();
            HandleChangedUsers();
            isLoading = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // If we do not send messages to a single person this method could be depricated.
    public void FCMSendDirect(String registrationToken, FCMMessageData.ObjectType objectType, String objectId){
        // The following information need to be send over:
        // Action: Create, Read, Update, Delete
        // Objects Type: Channel, Guild, Message or User
        Message message = Message.builder()
            .putData("action", "update")
            .putData("object type", objectType.name().toLowerCase())
            .putData("object id", objectId)
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

    public void FCMSendMulticast(List<String> registrationTokens, FCMMessageData.ObjectType objectType, String objectId){
        // The following information need to be send over:
        // Action: Create, Read, Update, Delete
        // Objects Type: Channel, Guild, Message or User

        // Create a message builder and build a message.
        MulticastMessage message = MulticastMessage.builder()
        // Add custom key, value string pairs as custom data:
            // Save what type of action the client should do.
            .putData("action", "update")
            // Save the object type of the object it needs to handle.
            .putData("object type", objectType.name().toLowerCase())
            // Save the id of the object it needs to change.
            .putData("object id", objectId)
        // Set the registration tokens (devices) the message has to be send to.
            .addAllTokens(registrationTokens)
        // Set the configuration for the Android platform.
            .setAndroidConfig(AndroidConfig.builder()
                // Set how long the message should live on the FCM back-end.
                .setTtl(MESSAGE_TTL)
                .build())
            .build();        

        BatchResponse response = null;
        try {
            System.out.println("Sending Message Now!");
            // Send the message to the FCM back-end.
            response = firebaseMessagingInstance.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // See the BatchResponse reference documentation
        // for the contents of response.
        if (response != null)
            // Display how many messages have been send successfully.
            System.out.println(response.getSuccessCount() + " messages were sent successfully");

        // Note: Uncomment the following code block if it is desired to handle non received messages. Such as a message delivery confirmation feature.
        // if (response.getFailureCount() > 0) {
        //     List<SendResponse> responses = response.getResponses();
        //     List<String> failedTokens = new ArrayList<>();
        //     for (int i = 0; i < responses.size(); i++) {
        //         if (!responses.get(i).isSuccessful()) {
        //         // The order of responses corresponds to the order of the registration tokens.
        //         failedTokens.add(registrationTokens.get(i));
        //         }
        //     }
        // }
    }

    public void FCMSendMulticast(FCMMessageData messageData){
        // The following information need to be send over:
        // Action: Create, Read, Update, Delete
        // Objects Type: Channel, Guild, Message or User

        // Create a message builder.
        Builder messageBuilder = MulticastMessage.builder()
        // Add custom key, value string pairs as custom data:
            // Save what type of action the client should do.
            .putData("action", "update")
            // Save the object type of the object it needs to handle.
            .putData("object type", messageData.getObjectType().name().toLowerCase())
            // Save the id of the object it needs to change.
            .putData("object id", messageData.getObjectId())
        // Set the registration tokens (devices) the message has to be send to.
            .addAllTokens(messageData.getRegistrationTokens());

        // If the message should have a notification add the appropriate Android configuration.
        if (messageData.getHasNotification())
            messageBuilder
                // Set the Android configuration.
                .setAndroidConfig(
                    // Create a Android configuration builder.
                    AndroidConfig.builder()
                        // Set how long the message should live on the FCM back-end.
                        .setTtl(MESSAGE_TTL)
                        // Set the notification configuration / settings.
                        .setNotification(
                            // Create a Android notification builder.
                            AndroidNotification.builder()
                            // Note: More customisations to the notificiation can be added here.
                                // Set the notification color to the corresponding supplied message settings.
                                .setColor(messageData.getNotificationColor())
                                // Set the notification icon to the corresponding supplied message settings.
                                .setIcon(messageData.getNotificationIcon())
                                // Build the Android Notification
                                .build())
                        // Build the Android Configuration.
                        .build())
                // Set the general notification information.
                .setNotification(
                    // Create a notification builder.
                    Notification.builder()
                        // Set the body of the notification.
                        .setBody(messageData.getNotificationBody())
                        // Set the title of the notification.
                        .setTitle(messageData.getNotificationTitle())
                        // Build the notification.
                        .build());
        // If no notificatiion has to be set, only set how long the message should live on the FCM back-end.
        else
            // Set the Android configuration.
            messageBuilder.setAndroidConfig(
                // Create a Android configuration builder.
                AndroidConfig.builder()
                    // Set how long the message should live on the FCM back-end.
                    .setTtl(MESSAGE_TTL)
                    // Build the Android configuration.
                    .build());

        System.out.println("Sending Message Now!");
        // Build the message.
        MulticastMessage message = messageBuilder.build();
        BatchResponse response = null;
        try {
            // Send the message.
            response = firebaseMessagingInstance.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // See the BatchResponse reference documentation
        // for the contents of response.
        if (response != null)
            System.out.println(response.getSuccessCount() + " messages were sent successfully");

        // Note: Uncomment the following code block if it is desired to handle non received messages. Such as a message delivery confirmation feature.
        // if (response.getFailureCount() > 0) {
        //     List<SendResponse> responses = response.getResponses();
        //     List<String> failedTokens = new ArrayList<>();
        //     for (int i = 0; i < responses.size(); i++) {
        //         if (!responses.get(i).isSuccessful()) {
        //         // The order of responses corresponds to the order of the registration tokens.
        //         failedTokens.add(registrationTokens.get(i));
        //         }
        //     }
        // }
    }

    public void DatabaseReadExample() throws InterruptedException, ExecutionException
    {
        // Get a document reference from the Firestore database based on a collection and selected document.
        DocumentReference docRef = firestoreInstance.collection("Guilds").document("SampleGuild");
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        // Get the actual snapshot document. (Snapshot is basically a fancy word for the result of the query at this current time.)
        DocumentSnapshot document = future.get();
        // If it exists continue filling or returning data.
        if (document.exists())
        {
            // Print document data.
            System.out.println("Document data: " + document.getData());
        }else
        {
            System.out.println("No such document!");
        }
    }

    private List<DocumentChange> HandleChangedChannels() throws Exception {
        // Complex code from the API which basically is a variable which result of an API call can be set in advance.
        final SettableApiFuture<List<DocumentChange>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        // Creates an event listener by adding a snapshot listener which activates whenever the output of a query changes.
        firestoreInstance.collectionGroup("Channels")
            .addSnapshotListener(
                new EventListener<QuerySnapshot>()
                {
                    // Overridden event method which is executed when an event is fired.
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirestoreException e)
                    {
                        // If there's a Firestore exception don't continue.
                        if (e != null)
                        {
                            System.err.println("Listen failed: " + e);
                            return;
                        }

                        // Get the documents from the query.
                        List<DocumentChange> documents = snapshot.getDocumentChanges();
                        // Continue if at least one document is returned.
                        if (documents.size() > 0)
                        {
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                // For loop for each channel document within the QuerySnapshot.
                                for (DocumentChange channel : documents)
                                {
                                    // Create a ApiFuture to retrieve a query.
                                    // Query: Get the parent of the collection the channel document is currently in.
                                    // Simple: Get the guild of the current channel.
                                    ApiFuture<DocumentSnapshot> nearFuture = channel.getDocument().getReference().getParent().getParent().get();
                                    DocumentSnapshot guildSnapshot;
                                    List<DocumentReference> userReferences = null;
                                    try
                                    {
                                        // Retrieve the DocumentSnapshot of the Query.
                                        guildSnapshot = nearFuture.get();
                                        // If the guild is found and did not encounter an error, continue.
                                        if (guildSnapshot != null)
                                            // Get all the users in the guild document.
                                            userReferences = (List<DocumentReference>) guildSnapshot.get("users");
                                            // Send a message to clients in the guild to update their UI.
                                            if (userReferences != null || userReferences.size() > 0)
                                                FCMSendMulticast(GetUserRegistrationTokens(userReferences), FCMMessageData.ObjectType.CHANNEL, channel.getDocument().getId());
                                    }
                                    catch (InterruptedException | ExecutionException e1) { e1.printStackTrace(); }
                                }
                            }
                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            // Set the result of the event.
                            future.set(documents);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        // Return the event data, and wait for 30 second on the end of the request if this is not closed yet.
        return future.get(30, TimeUnit.SECONDS);
    }

    private List<DocumentChange> HandleChangedGuilds() throws Exception {
        final SettableApiFuture<List<DocumentChange>> future = SettableApiFuture.create();

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

                        List<DocumentChange> documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                for (DocumentChange guild : documents)
                                {
                                    try {
                                        // Get user document references by reading out the user field inside the current guild document.
                                        List<DocumentReference> userReferences = (List<DocumentReference>) guild.getDocument().get("users");
                                        // Send a message if there's at least one user in the guild.
                                        if (userReferences.size() > 0)
                                            FCMSendMulticast(GetUserRegistrationTokens(userReferences), FCMMessageData.ObjectType.GUILD, guild.getDocument().getId());
                                    }
                                    catch (InterruptedException | ExecutionException e1) { e1.printStackTrace(); }
                                }
                            }
                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(documents);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }

    private List<DocumentChange> HandleChangedMessages() throws Exception {
        final SettableApiFuture<List<DocumentChange>> future = SettableApiFuture.create();

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

                        var documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                for (int i = 0; i < documents.size(); i++)
                                {
                                    QueryDocumentSnapshot message = documents.get(i).getDocument();
                                    String type = message.getString("type");
                                    if (type.equals("text"))
                                    {
                                        try {
                                            // Get information about the message: Which guild it was send it, and by who.
                                            DocumentReference author = (DocumentReference) message.get("user");
                                            DocumentSnapshot guild = message.getReference().getParent().getParent().getParent().getParent().get().get();
                                            
                                            
                                            List<DocumentReference> userReferences = new ArrayList<DocumentReference>();
                                            HashMap<String, ArrayList<DocumentReference>> guildUserList = (HashMap<String, ArrayList<DocumentReference>>) guild.get("users");
                                            
                                            // Get all the DocumentReferences to the users in that specific guild
                                            for (Map.Entry<String, ArrayList<DocumentReference>> Role : guildUserList.entrySet())
                                            {
                                                for (DocumentReference userReference : Role.getValue())
                                                    if (!userReference.getPath().equals(author.getPath()))
                                                    {
                                                        userReferences.add(userReference);
                                                    }
                                            }
                                            
                                            // Retrieve all register tokens from those users.
                                            List<String> registrationTokens = GetUserRegistrationTokens(userReferences);
                                            // Create a message with notification.
                                            FCMMessageData messageSettings = new FCMMessageData(
                                                true, 
                                                "Received a new message in <Guild Name>", 
                                                "#00ffff", 
                                                "", 
                                                "Message Received in DevLink", 
                                                message.getId(), 
                                                ObjectType.MESSAGE, 
                                                registrationTokens
                                                );
                                            // Send that message to all clients.
                                            FCMSendMulticast(messageSettings);
                                        } catch (InterruptedException | ExecutionException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    else if (type.equals("text"))
                                    {
                                        // TODO: Change this from sting to a enumeration type.
                                        switch (message.getString("content").toLowerCase())
                                        {
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
                            //System.out.println(documents);

                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(documents);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }
    
    private List<DocumentChange> HandleChangedUsers() throws Exception
    {
        final SettableApiFuture<List<DocumentChange>> future = SettableApiFuture.create();

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

                        List<DocumentChange> documents = snapshot.getDocumentChanges();
                        if (documents.size() > 0)
                        {
                            // Prevent any actions to be done when the code is loading the first local datasets.
                            if (!isLoading)
                            {
                                // Handle each changed user in the event.
                                for (DocumentChange user : documents)
                                {
                                    // Get the reference to the current user's document.             
                                    DocumentReference userReference = user.getDocument().getReference();
                                    try
                                    {
                                        // Create a query to select all Guilds with the changed user in it.
                                        QuerySnapshot query = firestoreInstance
                                            // From the Guilds collection.
                                            .collection("Guilds")
                                            // Select all guild where the "users" array contains the user document reference.
                                            .whereArrayContains("users", userReference)
                                            // Get the API Future from this.
                                            .get()
                                            // Get the Query Snapshot from the API future.
                                            .get();
                                        
                                        // Get all documents retrieved by the query.
                                        List<QueryDocumentSnapshot> guilds = query.getDocuments();
                                        // Create a new list of user document references to send a message to.
                                        List<DocumentReference> userReferences = new ArrayList<DocumentReference>();
                                        // For each guild that is found by the query to contain the changed user add their users to the list.
                                        for(QueryDocumentSnapshot guild : guilds)
                                        {
                                            // Add an collection (another list) to the user document references.
                                            userReferences.addAll((List<DocumentReference>) guild.get("users"));
                                        }
                                        
                                        // Continue if there's at least one user which should update.
                                        if (userReferences != null || userReferences.size() > 0)
                                            // Send a message to the devices of the users which display the changed user's data.
                                            FCMSendMulticast(GetUserRegistrationTokens(userReferences), FCMMessageData.ObjectType.USER, user.getDocument().getId());
                                    }
                                    catch (InterruptedException | ExecutionException e1) { e1.printStackTrace(); }
                                }
                            }
                        }
                        else
                            System.out.println("No result found from the query.");

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            future.set(documents);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]
        return future.get(30, TimeUnit.SECONDS);
    }

    private List<String> GetUserRegistrationTokens(DocumentReference user) throws InterruptedException, ExecutionException {
        List<String> registrationTokens = new ArrayList<String>();
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = user.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists())
        {
            //System.out.println("Document data: " + document.getData());
            registrationTokens = (List<String>) document.get("registrationToken");
        }
        else
        {
            System.out.println("No such document!");
        }
        return registrationTokens;
    }

    private List<String> GetUserRegistrationTokens(List<DocumentReference> users) throws InterruptedException, ExecutionException {
        List<String> registrationToken = new ArrayList<String>();

        // For each user in the user document reference list, retrieve its document and add it's registration tokens to the output.
        for (DocumentReference user : users)
        {
            // asynchronously retrieve the document
            ApiFuture<DocumentSnapshot> future = user.get();
            // future.get() blocks on response
            DocumentSnapshot document = future.get();
            if (document.exists())
            {
                //System.out.println("Document data: " + document.getData());
                List<String> userTokenList = null;
                userTokenList = (List<String>) document.get("registrationTokens");
                if (userTokenList != null || userTokenList.size() != 0)
                    registrationToken.addAll(userTokenList);
            }
            else
            {
                System.out.println("No such document!");
            }
        }
        return registrationToken;
    }
}