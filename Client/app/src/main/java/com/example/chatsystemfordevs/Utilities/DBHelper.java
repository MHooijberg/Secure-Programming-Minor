package com.example.chatsystemfordevs.Utilities;

import static android.service.controls.ControlsProviderService.TAG;

import android.util.Log;

import com.example.chatsystemfordevs.Model.Message;
import com.example.chatsystemfordevs.User.Moderator;
import com.example.chatsystemfordevs.User.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    }

    private void createGuild(DocumentReference document) {
        CollectionReference guildCollection = this.database.collection("Guilds");
        CollectionReference userCollection = this.database.collection("Users");
        HashMap<String, Object> guild = new HashMap<>();
        HashMap<String, Object> messages = new HashMap<>();
        HashMap<String, Object> githubChannel = new HashMap<>();
        HashMap<String, Object> stackOverFlowChannel = new HashMap<>();
        HashMap<String, Object> openSourceChannel = new HashMap<>();
        HashMap<String, Object> users = new HashMap<>();

        users.put("members",Arrays.asList());
        users.put("moderators",Arrays.asList(userCollection.document(document.getId())));

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

                github.collection("Messages");
                stackOverflow.collection("Messages");
                openSource.collection("Messages");
            }).addOnCompleteListener(task ->

                    document.update("guild", FieldValue.arrayUnion(guildCollection.document(task.getResult().getId()))));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessageToDatabase(String userId, String channel, Message message, String guild) {
        CollectionReference usersCollection = this.database.collection("Users");
        DocumentReference userReference = usersCollection.document(userId);
        try {
        userReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentReference> guildGroup = (List<DocumentReference>) task.getResult().get("guild");
                for (DocumentReference guildReference : guildGroup) {
                    guildReference.get().addOnCompleteListener(documentSnapshotTask -> {
                        String id = documentSnapshotTask.getResult().getId();
                        if (id.equals(guild)) {
                            switch (channel) {
                                case "Github":
                                    guildReference.collection("Channels").document("Github").collection("Messages").add(message);
                                    break;
                                case "OpenSource":
                                    guildReference.collection("Channels").document("OpenSource").collection("Messages").add(message);
                                    break;
                                case "StackOverFlow":
                                    guildReference.collection("Channels").document("StackOverFlow").collection("Messages").add(message);
                                    break;
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> System.out.println("There was a problem with the database"));
        }catch (Exception e){
            Log.d("Failure","There was a problem with sending a message:" + e);
        }
    }
}
