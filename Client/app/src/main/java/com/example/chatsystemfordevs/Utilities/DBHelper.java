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
}
