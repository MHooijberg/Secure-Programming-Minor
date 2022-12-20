package devlink_server;

import java.io.IOException;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.api.core.SettableApiFuture;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentChange.Type;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

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

    public void InitializeFirebase() throws UnsupportedOperationException, InterruptedException, ExecutionException
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
        try {
            List<String> data = HandleChangedMessages();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    /** Listen to a single document, returning data after the first snapshot. */
    Map<String, Object> listenToDocument() throws Exception {
        final SettableApiFuture<Map<String, Object>> future = SettableApiFuture.create();

        // [START firestore_listen_document]
        DocumentReference docRef = firestoreInstance.collection("cities").document("SF");
        docRef.addSnapshotListener(
            new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirestoreException e)
                {
                    if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                    System.out.println("Current data: " + snapshot.getData());
                    } else {
                    System.out.print("Current data: null");
                    }
                    // [START_EXCLUDE silent]
                    if (!future.isDone()) {
                    future.set(snapshot.getData());
                    }
                    // [END_EXCLUDE]
                }
            });
        // [END firestore_listen_document]

        return future.get(30, TimeUnit.SECONDS);
    }

    private List<String> HandleChangedChannels() throws Exception {
        final SettableApiFuture<List<String>> future = SettableApiFuture.create();

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

                        // TODO: Notify all members to update the display data of a Channel.
                        // List<String> cities = new ArrayList<>();
                        // for (DocumentSnapshot doc : snapshot)
                        // {
                        //     if (doc.get("name") != null)
                        //     {
                        //         cities.add(doc.getString("name"));
                        //     }
                        // }
                        // System.out.println("Current cites in CA: " + cities);

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            //future.set(cities);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]

        return future.get(30, TimeUnit.SECONDS);
    }

    private List<String> HandleChangedGuild() throws Exception {
        final SettableApiFuture<List<String>> future = SettableApiFuture.create();

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

                        // TODO: Notify all members to update the display data of a Guild.
                        // List<String> cities = new ArrayList<>();
                        // for (DocumentSnapshot doc : snapshot)
                        // {
                        //     if (doc.get("name") != null)
                        //     {
                        //         cities.add(doc.getString("name"));
                        //     }
                        // }
                        // System.out.println("Current cites in CA: " + cities);

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            //future.set(cities);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]

        return future.get(30, TimeUnit.SECONDS);
    }
    private List<String> HandleChangedMessages() throws Exception {
        final SettableApiFuture<List<String>> future = SettableApiFuture.create();

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
                        // TODO: Handle Text Message: Send notification and data retrieve request to clients.
                        // TODO: Handle Command: Interact with the corresponding API.
                        List<String> messages = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshot)
                        {
                            // if (doc.get("name") != null)
                            // {
                            //     messages.add(doc.getString("name"));
                            // }
                            messages.add(doc.getString("Contents"));
                        }
                        for (DocumentChange data : snapshot.getDocumentChanges())
                            System.out.println("Changed message: " + data.getDocument().getString("Contents"));
                        System.out.println("Current messages: " + messages);

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
    private List<String> HandleChangedUsers() throws Exception
    {
        final SettableApiFuture<List<String>> future = SettableApiFuture.create();

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

                        // TODO: Notify all members to update the display data of a user.
                        // List<String> cities = new ArrayList<>();
                        // for (DocumentSnapshot doc : snapshot)
                        // {
                        //     if (doc.get("name") != null)
                        //     {
                        //         cities.add(doc.getString("name"));
                        //     }
                        // }
                        // System.out.println("Current cites in CA: " + cities);

                        // [START_EXCLUDE silent]
                        if (!future.isDone()) {
                            //future.set(cities);
                        }
                        // [END_EXCLUDE]
                    }
                }
            );
        // [END firestore_listen_document]

        return future.get(30, TimeUnit.SECONDS);
    } 
}