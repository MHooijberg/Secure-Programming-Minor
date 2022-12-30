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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Adapters.GuildListAdapter;
import com.example.chatsystemfordevs.Adapters.MessageAdapter;
import com.example.chatsystemfordevs.Adapters.RoomListAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;

public class GuildServerController extends AppCompatActivity implements RoomListAdapter.OnRoomListener, GuildListAdapter.OnGuildListener {
    private FirebaseFirestore database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;

    MessageAdapter messageAdapter;
    RoomListAdapter roomListAdapter;
    GuildListAdapter guildListAdapter;
    RecyclerView recyclerViewMessages, recyclerViewRoomList, recyclerViewGuildList;
    View sideNav, sideMembers;
    Toolbar toolbar;
    String roomName, guildName, roomId, guildId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);

        //Find toolbar in the layout, replace the action bar with the toolbar,
        //hide the action bar, give it a navigation icon and set it to the drawable
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.burger);

        //Find side panels, create the layout inflater
        LinearLayout side_nav_layout = findViewById(R.id.side_navigation_layout);
        LinearLayout side_members_layout = findViewById(R.id.side_members_layout);
        LayoutInflater inflater = LayoutInflater.from(this);

        //Inflate the side panels with their respective layouts
        View viewNav = inflater.inflate(R.layout.guild_rooms, side_nav_layout, false);
        side_nav_layout.addView(viewNav);
        View viewMembers = inflater.inflate(R.layout.guild_members, side_members_layout, false);
        side_members_layout.addView(viewMembers);

        //Set the side panels variables for the hide methods
        sideNav = findViewById(R.id.side_navigation_full_layout);
        sideMembers = findViewById(R.id.side_members_full_layout);

        //Connect to database, pull data for messages, guilds and rooms, and insert
        //into respective recycler views
        database = FirebaseFirestore.getInstance();
        getMessages("SampleGuild", "SampleChannel");
        getGuilds();
        getRooms("SampleGuild");
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

    //Fill recycler methods insert provided data into the recyclers
    private void fillMessagesRecycler(ArrayList<MessageAdapter.GuildMessage> data) {
        recyclerViewMessages = findViewById(R.id.messages_recycler);
        messageAdapter = new MessageAdapter(this, data);
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillRoomListRecycler(ArrayList<String> names, ArrayList<String> ids) {
        recyclerViewRoomList = findViewById(R.id.room_list_recycler);
        roomListAdapter = new RoomListAdapter(this, names, ids, this);
        recyclerViewRoomList.setAdapter(roomListAdapter);
        recyclerViewRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillGuildListRecycler(ArrayList<String> names, ArrayList<String> ids) {
        recyclerViewGuildList = findViewById(R.id.guild_list_recycler);
        guildListAdapter = new GuildListAdapter(this, names, ids, this);
        recyclerViewGuildList.setAdapter(guildListAdapter);
        recyclerViewGuildList.setLayoutManager(new LinearLayoutManager(this));
    }

    //Get message data from the database from given guild and room
    public void getMessages(String guildId, String roomName) {
        CollectionReference ref = database.collection("Guilds").document(guildId).collection("Channels").document(roomName).collection("Messages");
        ArrayList<MessageAdapter.GuildMessage> guildMessages = new ArrayList<>();

        //Make pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String message = document.get("content").toString();
                    String username = document.get("user").toString();
                    String date = document.get("id").toString();
                    guildMessages.add(new MessageAdapter.GuildMessage(username, date, message));
                }
                fillMessagesRecycler(guildMessages);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    //Get guild data from the database
    public void getGuilds() {
        CollectionReference ref = database.collection("Guilds");
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        //Make pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    names.add(document.getData().get("name").toString());
                    ids.add(document.getId());
                }
                fillGuildListRecycler(names, ids);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    //Get room data from the database from the given guild
    public void getRooms(String guildId) {
        CollectionReference ref = database.collection("Guilds").document(guildId).collection("Channels");
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        //Makes pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    names.add(document.getData().get("name").toString());
                    ids.add(document.getId());
                }
                fillRoomListRecycler(names, ids);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public static void clickOnRoom(View view) {

    }

    @Override
    public void onRoomClick(int position, ArrayList<RoomListAdapter.RoomListViewHolder> viewHolders) {
        roomName = String.valueOf(viewHolders.get(position).getRoomName().getText());
        roomId = String.valueOf(viewHolders.get(position).getRoomId().getText());
        Log.d(TAG, roomName);
        TextView room_name_text = sideMembers.findViewById(R.id.room_name_text);
        getMessages(guildId, roomId);
        room_name_text.setText(roomName);
    }

    @Override
    public void onGuildClick(int position, ArrayList<GuildListAdapter.GuildListViewHolder> viewHolders) {
        guildName = String.valueOf(viewHolders.get(position).getGuildName().getText());
        guildId = String.valueOf(viewHolders.get(position).getGuildId().getText());
        Log.d(TAG, guildName);
        Log.d(TAG, guildId);
        TextView guild_name_text = sideNav.findViewById(R.id.guild_name);
        getRooms(guildId);
        guild_name_text.setText(guildName);
    }
}
