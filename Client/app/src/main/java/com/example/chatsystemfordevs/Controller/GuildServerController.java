package com.example.chatsystemfordevs.Controller;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.Adapters.GuildListAdapter;
import com.example.chatsystemfordevs.Adapters.MessageAdapter;
import com.example.chatsystemfordevs.Adapters.RoomListAdapter;
import com.example.chatsystemfordevs.Model.Message;
import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.User.Moderator;
import com.example.chatsystemfordevs.Utilities.CommandKeywords;
import com.example.chatsystemfordevs.Utilities.DBHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuildServerController extends AppCompatActivity implements RoomListAdapter.OnRoomListener, GuildListAdapter.OnGuildListener {
    private DBHelper database;
    private Moderator user;
    private String userDocumentId;
    private EditText sendMessage;
    private MessageAdapter messageAdapter;
    private RoomListAdapter roomListAdapter;
    private GuildListAdapter guildListAdapter;
    private View sideNav, sideMembers;
    private String roomName;
    private String roomId;
    private String guildId;


    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString("documentReference");
            Log.d("TAG","Hello");
            if(intent.getExtras().getString("actionHandler").equals("update")){
                retrieveRecentMessage(message);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("MyData"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

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

        String userEmail = extras.getString("userEmail");
        database = new DBHelper();
        this.getUserInfo(userEmail);

        this.sendMessage = findViewById(R.id.edit_message);
        ImageView sendButton = findViewById(R.id.send_button);

        //Find toolbar in the layout, replace the action bar with the toolbar,
        //hide the action bar, give it a navigation icon and set it to the drawable
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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
        guildId = "SampleGuild";
        roomId = "SampleChannel";
        roomName = "Github";
        getMessages(guildId, roomId);
        getGuilds();
        getRooms(guildId);


        sendButton.setOnClickListener(view -> {
            String message = sendMessage.getText().toString();
            if(!TextUtils.isEmpty(message))
            {
                String typeOfMessage;
                DocumentReference reference = database.getDatabase().collection("Users").document(userDocumentId);
                String date = String.valueOf(System.currentTimeMillis());
                try {
                if(message.startsWith(CommandKeywords.Github.toString())
                        || message.startsWith(CommandKeywords.StackExchange.toString())
                        || message.startsWith(CommandKeywords.OpenSource.toString())){
                    typeOfMessage = "command";
                }else{
                    typeOfMessage = "text";
                }
                Message pojoMessage = new Message(2,message, date,typeOfMessage,reference);
                    database.sendMessageToDatabase(userDocumentId,roomId,pojoMessage,guildId);
                }catch (Exception e){
                    sendMessage.setError("There was a problem with sending a message");
                }
            }else{
                sendMessage.setError("You cannot send an empty message");
            }
        });
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

    private void retrieveRecentMessage(String messageDocument){
        try{
            database.getDatabase().document(messageDocument).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot user = task.getResult();
                    if(user != null){
                        String content = task.getResult().getString("content");
                        String creationDate = task.getResult().getString("creation_date");
                        DocumentReference sender = (DocumentReference) task.getResult().get("user");
                        sender.get().addOnCompleteListener(user1 -> {
                            if(user1.isSuccessful()){
                                String username = user1.getResult().getString("username");
                                MessageAdapter.GuildMessage message = new MessageAdapter.GuildMessage(username,creationDate,content);
                                messageAdapter.addMessageToCollection(message);
                            }
                        });
                    }
                }
            });
        }catch (Exception e){
            sendMessage.setError("There was a problem with retrieving the actual message");
        }
    }

    private void getUserInfo(String userEmail){
        if(user == null){
            database.getDatabase().collection("Users").whereEqualTo("email",userEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        this.userDocumentId = document.getId();
                        String id = document.getString("id");
                        String email = document.getString("email");
                        List<String> guildInfo = (List<String>) document.get("guild");
                        String phoneNumber = document.getString("phoneNumber");
                        String username = document.getString("username");
                        user = new Moderator(id,email,guildInfo,true,phoneNumber,username);
                        return;
                    }
                }else{
                    Toast.makeText(GuildServerController.this, "There was a problem with retrieving user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Fill recycler methods insert provided data into the recyclers
    private void createMessagesRecycler() {
        RecyclerView recyclerViewMessages = findViewById(R.id.messages_recycler);
        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createRoomListRecycler() {
        RecyclerView recyclerViewRoomList = findViewById(R.id.room_list_recycler);
        roomListAdapter = new RoomListAdapter(this, new ArrayList<>(), new ArrayList<>(), this);
        recyclerViewRoomList.setAdapter(roomListAdapter);
        recyclerViewRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createGuildListRecycler() {
        RecyclerView recyclerViewGuildList = findViewById(R.id.guild_list_recycler);
        guildListAdapter = new GuildListAdapter(this, new ArrayList<>(), new ArrayList<>(), this);
        recyclerViewGuildList.setAdapter(guildListAdapter);
        recyclerViewGuildList.setLayoutManager(new LinearLayoutManager(this));
    }

    //Get message data from the database from given guild and room
    public void getMessages(String guildId, String roomName) {
        CollectionReference ref = database.getDatabase().collection("Guilds").document(guildId).collection("Channels").document(roomName).collection("Messages");
        ArrayList<MessageAdapter.GuildMessage> guildMessages = new ArrayList<>();

        //Make pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().size() != 0) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String message = document.get("content").toString();
                    DocumentReference userReference = (DocumentReference) document.get("user");
                    String date = document.get("creation_date").toString();
                    String username = "Placeholder";
                    guildMessages.add(new MessageAdapter.GuildMessage(username, date, message));
                    messageAdapter.setMessages(guildMessages);
                    userReference.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (int i = 0; i < messageAdapter.getItemCount(); i++) {
                                messageAdapter.getMessages().get(i).setUsername(task1.getResult().get("username").toString());
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                    });
                    messageAdapter.notifyDataSetChanged();
                }
            } else {
                messageAdapter.setMessages(guildMessages);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    //Get guild data from the database
    public void getGuilds() {
        CollectionReference ref = database.getDatabase().collection("Guilds");
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        //Make pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    names.add(document.getData().get("name").toString());
                    ids.add(document.getId());
                }
                guildListAdapter.setNames(names);
                guildListAdapter.setIds(ids);
                guildListAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    //Get room data from the database from the given guild
    public void getRooms(String guildId) {
        CollectionReference ref = database.getDatabase().collection("Guilds").document(guildId).collection("Channels");
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        //Makes pulling data from the database asynchronous
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    names.add(document.getData().get("name").toString());
                    ids.add(document.getId());
                }
                roomListAdapter.setNames(names);
                roomListAdapter.setIds(ids);
                roomListAdapter.notifyDataSetChanged();
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
        String selectedRoom = String.valueOf(viewHolders.get(position).getRoomId().getText());
        Log.d(TAG, roomName);
        TextView room_name_text = sideMembers.findViewById(R.id.room_name_text);
        if(!roomId.equals(selectedRoom)){
            roomId = selectedRoom;
            getMessages(guildId, roomId);
            room_name_text.setText(roomName);
        }
    }

    @Override
    public void onGuildClick(int position, ArrayList<GuildListAdapter.GuildListViewHolder> viewHolders) {
        String guildName = String.valueOf(viewHolders.get(position).getGuildName().getText());
        guildId = String.valueOf(viewHolders.get(position).getGuildId().getText());
        Log.d(TAG, guildName);
        Log.d(TAG, guildId);
        TextView guild_name_text = sideNav.findViewById(R.id.guild_name);
        getRooms(guildId);
        guild_name_text.setText(guildName);
    }
}
