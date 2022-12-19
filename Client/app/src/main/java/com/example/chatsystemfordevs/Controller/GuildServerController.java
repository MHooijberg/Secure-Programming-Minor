package com.example.chatsystemfordevs.Controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.example.chatsystemfordevs.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

public class GuildServerController extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);
    }
}
