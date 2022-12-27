package com.example.chatsystemfordevs.Utilities;

import static android.service.controls.ControlsProviderService.TAG;

import android.util.Log;

import com.example.chatsystemfordevs.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DBHelper {
    private final FirebaseFirestore database;
    public DBHelper() {
        this.database = FirebaseFirestore.getInstance();
    }

    public void createUser(String username, String email, String phoneNumber) {
        //Initialize the FCM token generation
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
            }else{

                User user = new User(email,"null",false,phoneNumber, Arrays.asList(task.getResult()),username);

                CollectionReference collection = this.database.collection("Users");
                collection.add(user).addOnSuccessListener(documentReference
                        -> Log.d("Success","A user has been added")).addOnFailureListener(e -> Log.d("Failure", "Problem with adding the user"));
            }
        });
    }

    public ArrayList<ArrayList> getMessages(String guildId, String roomName) {
        CollectionReference docRef = database.collection("Guilds").document(guildId).collection("Channels").document(roomName).collection("Messages");
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<ArrayList> data = new ArrayList<>();
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> documentData = document.getData();
                    Log.i(TAG, document.getId() + " => " + documentData);
                    messages.add(documentData.get("content").toString());
                    usernames.add(documentData.get("user").toString());
                    dates.add(documentData.get("id").toString());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        Log.i(TAG, messages.toString());
        Log.i(TAG, usernames.toString());
        Log.i(TAG, dates.toString());
        data.add(messages);
        data.add(usernames);
        data.add(dates);
        return data;
    }

    public ArrayList<String> getGuilds() {
        ArrayList<String> names = new ArrayList<>();
        Task<QuerySnapshot> result = database.collection("Guilds").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    names.add(document.getData().get("name").toString());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return names;
    }

    public ArrayList<String> getRooms(String guildId) {
        ArrayList<String> names = new ArrayList<>();
        database.collection("Guilds").document(guildId).collection("Channels").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    names.add(document.getData().get("name").toString());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return names;
    }
}
