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
        MulticastMessage message = MulticastMessage.builder()
            .putData("action", "update")
            .putData("object type", objectType.name().toLowerCase())
            .putData("object id", objectId)
            .addAllTokens(registrationTokens)
            .setAndroidConfig(AndroidConfig.builder()
                .setTtl(MESSAGE_TTL)
                .build())
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

        // TODO: Uncomment the following code block if it is desired to handle non received messages. Such as a message delivery confirmation feature.
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
        Builder messageBuilder = MulticastMessage.builder()
            .putData("action", "update")
            .putData("object type", messageData.getObjectType().name().toLowerCase())
            .putData("object id", messageData.getObjectId())
            .addAllTokens(messageData.getRegistrationTokens()));

        if (messageData.getHasNotification())
            messageBuilder
                .setAndroidConfig(
                    AndroidConfig.builder()
                        .setTtl(MESSAGE_TTL)
                        .setNotification(
                            AndroidNotification.builder()
                            // Note: More customisations to the notificiation can be added here.
                                .setColor(messageData.getNotificationColor())
                                .setIcon(messageData.getNotificationIcon())
                                .build())
                        .build())
                .setNotification(
                    Notification.builder()
                        .setBody(messageData.getNotificationBody())
                        .setTitle(messageData.getNotificationTitle())
                        .build());
        else
            messageBuilder.setAndroidConfig(
                AndroidConfig.builder()
                .setTtl(MESSAGE_TTL)
                .build());

        MulticastMessage message = messageBuilder.build();
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

        // TODO: Uncomment the following code block if it is desired to handle non received messages. Such as a message delivery confirmation feature.
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
        firestoreInstance.collectionGroup("Channels")
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

                        List<QueryDocumentSnapshot> documents = snapshot.getDocuments();
                        List<ChannelData> channelChanges = snapshot.toObjects(ChannelData.class);
                        if (!isLoading)
                        {
                            if (documents.size() > 0)
                            {
                                // Prevent any actions to be done when the code is loading the first local datasets.
                                if (!isLoading)
                                {
                                    for (QueryDocumentSnapshot channel : documents)
                                    {
                                        // TODO: Handle channel changes.
                                        ApiFuture<DocumentSnapshot> nearFuture = channel.getReference().getParent().getParent().get();
                                        DocumentSnapshot guildSnapshot;
                                        List<DocumentReference> userReferences = null;
                                        try
                                        {
                                            guildSnapshot = nearFuture.get();
                                            if (guildSnapshot != null)
                                                userReferences = (List<DocumentReference>) guildSnapshot.get("users");
                                                if (userReferences != null || userReferences.size() > 0)
                                                    FCMSendMulticast(GetUserRegistrationTokens(userReferences), FCMMessageData.ObjectType.CHANNEL, channel.getId());
                                        }
                                        catch (InterruptedException | ExecutionException e1) { e1.printStackTrace(); }
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
                                    try {
                                        List<String> userRegistrationTokens = GetUserRegistrationTokens(guild.getUsers());

                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    } catch (ExecutionException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
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

    private List<String> GetUserRegistrationTokens(DocumentReference user) throws InterruptedException, ExecutionException {
        List<String> registrationTokens = new ArrayList<String>();
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = user.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists())
        {
            System.out.println("Document data: " + document.getData());
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

        for (DocumentReference user : users)
        {
            // asynchronously retrieve the document
            ApiFuture<DocumentSnapshot> future = user.get();
            // future.get() blocks on response
            DocumentSnapshot document = future.get();
            if (document.exists())
            {
                System.out.println("Document data: " + document.getData());
                List<String> userTokenList = (List<String>) document.get("registrationToken");
                for(String token : userTokenList)
                {
                    registrationToken.add(token);
                }
            }
            else
            {
                System.out.println("No such document!");
            }
        }
        return registrationToken;
    }
}