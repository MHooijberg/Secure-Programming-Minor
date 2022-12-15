package com.example.chatsystemfordevs.Utilities;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {
    private final FirebaseFirestore database;

    public DBHelper() {
        this.database = FirebaseFirestore.getInstance();
    }

    public void createUser(String username, String email, String phoneNumber) {
        Map<String, Object> user = new HashMap<>();
        user.put("Email", email);
        user.put("Guild", null);
        user.put("isOnline", true);
        user.put("Phone Number", phoneNumber);
        user.put("Username", username);

        this.database.collection("Users").add(user).addOnSuccessListener(documentReference
                -> Log.d("State","A user has been added")).addOnFailureListener(e -> System.out.println(e.getMessage()));
    }
}
