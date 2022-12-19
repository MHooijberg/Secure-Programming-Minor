package com.example.chatsystemfordevs.Controller;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsystemfordevs.Model.GuildServerModel;
import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.MessageAdapter;
import com.example.chatsystemfordevs.Utilities.MessageData;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class GuildServerController extends AppCompatActivity {
    private FirebaseFirestore database;
    private FirebaseMessagingService firebaseMessaging;
    private GuildServerModel guildModel;

    MessageAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List<MessageData> list = new ArrayList<>();
        list = getData();

        recyclerView = (RecyclerView)findViewById(R.id.messages_recycler);
        adapter = new MessageAdapter(list, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuildServerController.this));
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
        switch (item.getItemId()) {
            case R.id.action_burger:
                //function
                return true;
            case R.id.action_members:
                //function
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    // Sample data for RecyclerView
    private List<MessageData> getData()
    {
        List<MessageData> list = new ArrayList<>();
        list.add(new MessageData("Username1",
                "2022-01-03 08:00",
                "this is a chat message"));
        list.add(new MessageData("username2",
                "2022-01-04 14:54",
                "Wow! that is such a cool message!"));
        list.add(new MessageData("username1",
                "2022-02-28 01:22",
                "Aspernatur reiciendis ut quia impedit iusto nobis. Omnis laudantium velit ex itaque est sunt et. Fugit aut doloremque rerum laborum. Non ea veniam dolorem veritatis reprehenderit reprehenderit voluptatem facilis. Alias laborum voluptatibus quia vel. Sunt id neque deserunt."));

        return list;
    }
}
