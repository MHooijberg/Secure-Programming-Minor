package com.example.chatsystemfordevs.Utilities;

import static android.service.controls.ControlsProviderService.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatsystemfordevs.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.Executor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {
    private final FirebaseFirestore database;
    public DBHelper() {
        this.database = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public void createUser(String username, String email, String phoneNumber) {
        //Initialize the FCM token generation
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            }else{
                CollectionReference guild = createGuildForUser();

                User user = new User(email,guild.getId(),false,phoneNumber, Arrays.asList(task.getResult()),username);

                CollectionReference collection = this.database.collection("Users");
                collection.add(user).addOnSuccessListener(documentReference
                        -> Log.d("Success","A user has been added")).addOnFailureListener(e -> Log.d("Failure", "There was a problem with adding the user"));
            }
        });
    }

    public CollectionReference createGuildForUser(){
        CollectionReference guildCollection = this.database.collection("Guilds");
        HashMap<String,Object> guild = new HashMap<>();
        HashMap<String,Object> messages = new HashMap<>();
        HashMap<String,Object> channels = new HashMap<>();

        guild.put("gitHubAPIKey","Secret");
        guild.put("name","guild name");
        guild.put("stackExchangeAPIKey","key");
        guild.put("users","null");

        channels.put("name","channel name");

        messages.put("content","message");
        messages.put("id","0");
        messages.put("type","text");
        messages.put("user","user");

        //adds the document and gets its id
        guildCollection.add(guild).addOnSuccessListener(
                guildReference -> guildCollection.document(guildReference.getId()).collection("Channels").add(channels).addOnSuccessListener(
                        channelReference -> guildCollection.document(guildReference.getId()).collection("Channels").document(channelReference.getId()).collection("Messages").add(messages)));
        return guildCollection;
    }

}
