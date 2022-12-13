package com.example.chatsystemfordevs.Controller;

import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

public class GuildServerController {
    private FirebaseFirestore database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;
}
