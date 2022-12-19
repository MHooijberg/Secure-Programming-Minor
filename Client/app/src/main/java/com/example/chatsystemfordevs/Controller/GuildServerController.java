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
    Toolbar toolbar;

    String[] usernames, dates, messages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guildserverview);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.burger);

        recyclerView = findViewById(R.id.messages_recycler);

        usernames = getResources().getStringArray(R.array.message_usernames);
        dates = getResources().getStringArray(R.array.message_dates);
        messages = getResources().getStringArray(R.array.message_messages);

        adapter = new MessageAdapter(this, usernames, dates, messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
