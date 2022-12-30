package com.example.chatsystemfordevs.Utilities;

import static android.service.controls.ControlsProviderService.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatsystemfordevs.User.Moderator;
import com.example.chatsystemfordevs.User.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DBHelper {
    private final FirebaseFirestore database;
    public DBHelper() {
        this.database = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public void createUser(String id,String username, String email, String phoneNumber) {
        //Initialize the FCM token generation
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            }else{
                Users user = new Moderator(id,email,null,false,phoneNumber, Arrays.asList(task.getResult()),username);
                CollectionReference collection = this.database.collection("Users");
                collection.add(user).addOnSuccessListener(documentReference
                        -> Log.d("Success","A user has been added")).addOnFailureListener(e -> Log.d("Failure", "There was a problem with adding the user"));
            }
        });
    }

    public void createGuildForUser(String userId){
        CollectionReference userCollection = this.database.collection("Users");
        userCollection.document(userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                createGuild(task.getResult().getReference());
            }
        });

            //Get the document id and add  it to the guilds
            // when you get it create the guild and add the user document to the guild
    }

    private void createGuild(DocumentReference document) {
        //get the user document based on his id
        // add his reference to the guild hashmap
        CollectionReference guildCollection = this.database.collection("Guilds");
        HashMap<String, Object> guild = new HashMap<>();
        HashMap<String, Object> messages = new HashMap<>();
        HashMap<String, Object> githubChannel = new HashMap<>();
        HashMap<String, Object> stackOverFlowChannel = new HashMap<>();
        HashMap<String, Object> openSourceChannel = new HashMap<>();
        HashMap<String, Object> users = new HashMap<>();

        users.put("members",Arrays.asList());
        users.put("moderators",Arrays.asList("Users/"+document.getId()));

        guild.put("gitHubAPIKey", "Secret");
        guild.put("name", "guild name");
        guild.put("stackExchangeAPIKey", "key");
        guild.put("users", users);

        githubChannel.put("name", "Github");
        stackOverFlowChannel.put("name", "StackOverFlow");
        openSourceChannel.put("name", "OpenSource");

        messages.put("content", "message");
        messages.put("id", "0");
        messages.put("type", "text");
        messages.put("user", "user");

        try {
            guildCollection.add(guild).addOnSuccessListener(guildReference -> {
                guildCollection.document(guildReference.getId()).collection("Channels").document("Github").set(githubChannel);
                guildCollection.document(guildReference.getId()).collection("Channels").document("StackOverFlow").set(stackOverFlowChannel);
                guildCollection.document(guildReference.getId()).collection("Channels").document("OpenSource").set(openSourceChannel);

                DocumentReference github = guildCollection.document(guildReference.getId()).collection("Channels").document("Github");
                DocumentReference stackOverflow = guildCollection.document(guildReference.getId()).collection("Channels").document("StackOverFlow");
                DocumentReference openSource = guildCollection.document(guildReference.getId()).collection("Channels").document("OpenSource");

                github.collection("Messages").add(messages);
                stackOverflow.collection("Messages").add(messages);
                openSource.collection("Messages").add(messages);
            }).addOnCompleteListener(task ->
                    document.update("guild", FieldValue.arrayUnion("Guilds/" + task.getResult().getId())));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessageToDatabase(DocumentReference userReference,String channel, String message,DocumentReference guildChannel){
        //check in which guild he is in
        //check in which channel he is typing the message
        //I need to check in which guild is he writing
        CollectionReference guilds = this.database.collection("Guilds");
        CollectionReference usersCollection = this.database.collection("Users");
        //If I cannot get the userDocument I will query it to find the document based on some fields
        DocumentReference reference = usersCollection.document(userReference.getId());
        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //Maybe I need to specify the collection name like "/Guilds/+"
                DocumentReference guild = task.getResult().getDocumentReference(guildChannel.getId());
                if(guild != null){
                    switch (channel){
                        case "Github":
                            guild.collection("Channels").document("Github").collection("Messages").add(message);
                            break;
                        case "OpenSource":
                            guild.collection("Channels").document("OpenSource").collection("Messages").add(message);
                            break;
                        case "StackOverFlow":
                            guild.collection("Channels").document("StackOverFlow").collection("Messages").add(message);
                            break;
                }
                }
            }
        }).addOnFailureListener(e -> {
            System.out.println("There was a problem with the database");
        });
    }
}
