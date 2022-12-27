package com.example.chatsystemfordevs.Controller;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Adapters.GuildListAdapter;
import com.example.chatsystemfordevs.Adapters.MessageAdapter;
import com.example.chatsystemfordevs.Adapters.RoomListAdapter;
import com.example.chatsystemfordevs.Utilities.DBHelper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.Map;

public class GuildServerController extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;

    MessageAdapter messageAdapter;
    RoomListAdapter roomListAdapter;
    GuildListAdapter guildListAdapter;
    RecyclerView recyclerViewMessages, recyclerViewRoomList, recyclerViewGuildList;
    View sideNav, sideMembers;
    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);

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


        database = FirebaseFirestore.getInstance();

        getMessages("SampleGuild", "SampleChannel");
        getGuilds();
        getRooms("SampleGuild");

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
    }

    public void hideSideMembers(View view) {
        sideMembers.setVisibility(View.GONE);
    }

    public void onSettingsButtonClick(View view) {
        startActivity(new Intent(this, SettingsActivityController.class));
    }

    private void fillMessagesRecycler(ArrayList<ArrayList> data) {
        recyclerViewMessages = findViewById(R.id.messages_recycler);

        ArrayList<String> messages = data.get(0);
        ArrayList<String> usernames = data.get(1);
        ArrayList<String> dates = data.get(2);

        messageAdapter = new MessageAdapter(this, usernames, dates, messages);

        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillRoomListRecycler(ArrayList<String> names) {
        recyclerViewRoomList = findViewById(R.id.room_list_recycler);

        roomListAdapter = new RoomListAdapter(this, names);
        recyclerViewRoomList.setAdapter(roomListAdapter);
        recyclerViewRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillGuildListRecycler(ArrayList<String> names) {
        recyclerViewGuildList = findViewById(R.id.guild_list_recycler);

        guildListAdapter = new GuildListAdapter(this, names);
        recyclerViewGuildList.setAdapter(guildListAdapter);
        recyclerViewGuildList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getMessages(String guildId, String roomName) {
        CollectionReference ref = database.collection("Guilds").document(guildId).collection("Channels").document(roomName).collection("Messages");
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<ArrayList> data = new ArrayList<>();
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> documentData = document.getData();
                    Log.i(TAG, document.getId() + " => " + documentData);
                    messages.add(documentData.get("content").toString());
                    usernames.add(documentData.get("user").toString());
                    dates.add(documentData.get("id").toString());
                }
                data.add(messages);
                data.add(usernames);
                data.add(dates);
                fillMessagesRecycler(data);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

    }

    public ArrayList<String> getGuilds() {
        CollectionReference ref = database.collection("Guilds");
        ArrayList<String> names = new ArrayList<>();
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    names.add(document.getData().get("name").toString());
                }
                fillGuildListRecycler(names);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return names;
    }

    public ArrayList<String> getRooms(String guildId) {
        CollectionReference ref = database.collection("Guilds").document(guildId).collection("Channels");
        ArrayList<String> names = new ArrayList<>();
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    names.add(document.getData().get("name").toString());
                }
                fillRoomListRecycler(names);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return names;
    }
}
