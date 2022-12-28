package com.example.chatsystemfordevs.Utilities;

import static android.service.controls.ControlsProviderService.TAG;

import android.util.Log;

import com.example.chatsystemfordevs.User.Moderator;
import com.example.chatsystemfordevs.User.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.HashMap;

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

    public void createGuildForUser(String id) {
        CollectionReference guildCollection = this.database.collection("Guilds");
        HashMap<String, Object> guild = new HashMap<>();
        HashMap<String, Object> messages = new HashMap<>();
        HashMap<String, Object> githubChannel = new HashMap<>();
        HashMap<String, Object> stackOverFlowChannel = new HashMap<>();
        HashMap<String, Object> openSourceChannel = new HashMap<>();
        HashMap<String, Object> users = new HashMap<>();


        users.put("members",Arrays.asList());
        users.put("moderators",Arrays.asList("Users/"+id));

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

            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
