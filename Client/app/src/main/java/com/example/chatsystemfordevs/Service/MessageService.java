package com.example.chatsystemfordevs.Service;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
// Service for listening for any new messages from FCM in the background
public class MessageService extends FirebaseMessagingService  {
    private LocalBroadcastManager localBroadcastManager;
    public MessageService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Handle FCM message when received
        System.out.println("Message from " + remoteMessage.getFrom());
        Map<String, String> map = remoteMessage.getData();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Intent intent = new Intent("MyData");
            intent.putExtra("documentReference", map.get("object reference"));
            intent.putExtra("actionHandler",map.get("action"));
            this.localBroadcastManager.sendBroadcast(intent);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
