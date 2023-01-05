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
import android.widget.Toast;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GuildServerController extends AppCompatActivity implements RoomListAdapter.OnRoomListener, GuildListAdapter.OnGuildListener {
    private DBHelper database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;
    private ArrayList<String> incomingMessages;
    private Moderator user;
    private String username;
    private String userEmail;
    private String userDocumentId;

    MessageAdapter messageAdapter;
    RoomListAdapter roomListAdapter;
    GuildListAdapter guildListAdapter;
    RecyclerView recyclerViewMessages, recyclerViewRoomList, recyclerViewGuildList;
    View sideNav, sideMembers;
    Toolbar toolbar;
    String roomName, guildName, roomId, guildId;
    ArrayList<DocumentReference> availableGuilds;

    public GuildServerController() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);
        Bundle extras = getIntent().getExtras();
        //retrieve the data from the database based on the id such as a username

        userEmail = extras.getString("userEmail");
        this.database = new DBHelper();
        this.getUserInfo(userEmail);

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

        //Create recyclers
        createMessagesRecycler();
        createRoomListRecycler();
        createGuildListRecycler();

        //Connect to database, pull data for messages, guilds and rooms, and insert
        //into respective recycler views

//        getGuilds();
//        getRooms(guildId);
//        getMessages(guildId, roomId);
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
                        this.userDocumentId = document.getId();
                        String id = document.getString("id");
                        userDocumentId = document.getId();
                        String email = document.getString("email");
                        List<String> guildInfo = (List<String>) document.get("guild");
                        String phoneNumber = document.getString("phoneNumber");
                        String username = document.getString("username");
                        user = new Moderator(id,email,guildInfo,true,phoneNumber,username);
                        getAvailableGuilds();
                        return;
                    }
                }else{
                    Toast.makeText(GuildServerController.this, "There was a problem with retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void handleIncomingMessages(){}

    //Fill recycler methods insert provided data into the recyclers
    private void createMessagesRecycler() {
        recyclerViewMessages = findViewById(R.id.messages_recycler);
        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createRoomListRecycler() {
        recyclerViewRoomList = findViewById(R.id.room_list_recycler);
        roomListAdapter = new RoomListAdapter(this, new ArrayList<>(), this);
        recyclerViewRoomList.setAdapter(roomListAdapter);
        recyclerViewRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createGuildListRecycler() {
        recyclerViewGuildList = findViewById(R.id.guild_list_recycler);
        guildListAdapter = new GuildListAdapter(this, new ArrayList<>(), this);
        recyclerViewGuildList.setAdapter(guildListAdapter);
        recyclerViewGuildList.setLayoutManager(new LinearLayoutManager(this));
    }

    //Get message data from the database from given guild and room
    public void getMessages(String guildId, String roomName) {
        CollectionReference ref = database.getDatabase().collection("Guilds").document(guildId).collection("Channels").document(roomName).collection("Messages");
        ArrayList<MessageAdapter.GuildMessage> guildMessages = new ArrayList<>();

        //Make pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String message = document.get("content").toString();
                    DocumentReference userReference;
                    try {
                         userReference = (DocumentReference) document.get("user");
                    } catch (Exception e) {
                        return;
                    }
                    String date = document.get("id").toString();
                    String username = "Placeholder";
                    guildMessages.add(new MessageAdapter.GuildMessage(username, date, message));
                    messageAdapter.setMessages(guildMessages);
                    try {
                        userReference.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (int i = 0; i < messageAdapter.getItemCount(); i++) {
                                    messageAdapter.getMessages().get(i).setUsername(task1.getResult().get("username").toString());
                                }
                            }
                            messageAdapter.notifyDataSetChanged();
                        });
                    } catch (Exception e) {
                        Log.e(TAG, String.valueOf(e));
                    }
                    messageAdapter.notifyDataSetChanged();
                }


            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    //Get room data from the database from the given guild
    public void getRooms(String guildId) {
        CollectionReference ref = database.getDatabase().collection("Guilds").document(guildId).collection("Channels");
        ArrayList<RoomListAdapter.Room> rooms = new ArrayList<>();

        //Makes pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    RoomListAdapter.Room room = new RoomListAdapter.Room(document.getData().get("name").toString(), document.getId());
                    rooms.add(room);
                }
                roomListAdapter.setRooms(rooms);
                roomListAdapter.clearViewHolders();
                roomListAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void getAvailableGuilds() {
        DocumentReference userRef = database.getDatabase().collection("Users").document(userDocumentId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                availableGuilds = (ArrayList<DocumentReference>) task.getResult().get("guilds");
                if (availableGuilds != null) {
                    for (DocumentReference guildRef : availableGuilds) {
                        guildRef.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful() && task1.getResult().getData() != null) {
                                DocumentSnapshot result = task1.getResult();
                                Log.d(TAG, String.valueOf(result.getData()));
                                GuildListAdapter.Guild guild = new GuildListAdapter.Guild(result.get("name").toString(), result.getId());
                                guildListAdapter.addGuild(guild);
                            } else {
                                Log.d(TAG, "getAvailableGuilds: Something went wrong");
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "getAvailableGuilds: No guilds");
                }

            }
        });
    }

    @Override
    public void onRoomSelect(int position, ArrayList<RoomListAdapter.RoomListViewHolder> viewHolders) {
        roomName = String.valueOf(viewHolders.get(position).getRoomName().getText());
        roomId = String.valueOf(viewHolders.get(position).getRoomId().getText());

        TextView room_name_text = sideMembers.findViewById(R.id.room_name_text);
        getMessages(guildId, roomId);
        room_name_text.setText(roomName);
    }

    @Override
    public void onGuildSelect(int position, ArrayList<GuildListAdapter.GuildListViewHolder> viewHolders) {
        guildName = String.valueOf(viewHolders.get(position).getGuildName().getText());
        guildId = String.valueOf(viewHolders.get(position).getGuildId().getText());

        TextView guild_name_text = sideNav.findViewById(R.id.guild_name);
        getRooms(guildId);
        guild_name_text.setText(guildName);
    }
}
