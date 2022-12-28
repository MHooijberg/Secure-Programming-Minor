package com.example.chatsystemfordevs.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.Adapters.GuildListAdapter;
import com.example.chatsystemfordevs.Adapters.MessageAdapter;
import com.example.chatsystemfordevs.Adapters.RoomListAdapter;
import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.User.Moderator;
import com.example.chatsystemfordevs.Utilities.DBHelper;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class GuildServerController extends AppCompatActivity {
    private DBHelper database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;
    private ArrayList<String> incomingMessages;
    private Moderator user;
    private String username;

    MessageAdapter messageAdapter;
    RoomListAdapter roomListAdapter;
    GuildListAdapter guildListAdapter;
    RecyclerView recyclerViewMessages, recyclerViewRoomList, recyclerViewGuildList;
    View sideNav, sideMembers;
    Toolbar toolbar;

    String[] usernames, dates, messages, roomNames, guildNames;

    public GuildServerController() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);
        Bundle extras = getIntent().getExtras();
        //retreive the data from the database based on the id such as a username

        String userEmail = extras.getString("userEmail");
        this.database = new DBHelper();
        this.getUserInfo(userEmail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.burger);

        LinearLayout side_nav_layout = findViewById(R.id.side_navigation_layout);
        LinearLayout side_members_layout = findViewById(R.id.side_members_layout);
        LayoutInflater inflater = LayoutInflater.from(this);

        View viewNav = inflater.inflate(R.layout.guild_rooms, side_nav_layout, false);
        side_nav_layout.addView(viewNav);

        View viewMembers = inflater.inflate(R.layout.guild_members, side_members_layout, false);
        side_members_layout.addView(viewMembers);

        recyclerViewMessages = findViewById(R.id.messages_recycler);
        recyclerViewRoomList = findViewById(R.id.room_list_recycler);
        recyclerViewGuildList = findViewById(R.id.guild_list_recycler);

        usernames = getResources().getStringArray(R.array.message_usernames);
        dates = getResources().getStringArray(R.array.message_dates);
        messages = getResources().getStringArray(R.array.message_messages);
        roomNames = getResources().getStringArray(R.array.room_names);
        guildNames = getResources().getStringArray(R.array.guild_names);

        messageAdapter = new MessageAdapter(this, usernames, dates, messages);
        roomListAdapter = new RoomListAdapter(this, roomNames);
        guildListAdapter = new GuildListAdapter(this, guildNames);

        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewRoomList.setAdapter(roomListAdapter);
        recyclerViewRoomList.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewGuildList.setAdapter(guildListAdapter);
        recyclerViewGuildList.setLayoutManager(new LinearLayoutManager(this));

        sideNav = findViewById(R.id.side_navigation_full_layout);
        sideMembers = findViewById(R.id.side_members_full_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guild_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_members) {
            sideMembers.setVisibility(View.VISIBLE);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            sideNav.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void hideSideNav(View view) {
        sideNav.setVisibility(View.GONE);
        //this.database.createGuildForUser(user.getId());
    }

    public void hideSideMembers(View view) {
        sideMembers.setVisibility(View.GONE);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(this,SettingsActivityController.class);
        intent.putExtra("Username",user.getUsername());
        intent.putExtra("UserEmail",user.getEmail());
        startActivity(intent);
    }

    public class MessageService extends FirebaseMessagingService{

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // Handle FCM messages.

            System.out.println("Message from " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d("TAG", "Message data payload: " + remoteMessage.getData());

                /*       if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

            }

            //This is where messages will be stored
            GuildServerController.this.incomingMessages.add(remoteMessage.getMessageType());

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }
    private void getUserInfo(String userEmail){
        if(user == null){
            this.database.getDatabase().collection("Users").whereEqualTo("email",userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getString("id");
                        String email = document.getString("email");
                        List<String> guildInfo = (List<String>) document.get("guild");
                        String phoneNumber = document.getString("phoneNumber");
                        String username = document.getString("username");
                        //Null needs to be a collection
                        user = new Moderator(id,email,guildInfo,true,phoneNumber,username);
                        return;
                    }
                }else{
                    Toast.makeText(GuildServerController.this, "There was a problem with retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleIncomingMessages(){}
}
