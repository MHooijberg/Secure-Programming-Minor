package com.example.chatsystemfordevs.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;

public class SettingsActivityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_settingsview);
        TextView username = findViewById(R.id.userInfo);
        TextView userText = findViewById(R.id.user_username_text);
        TextView userEmail = findViewById(R.id.user_email_text);

        username.setText(extras.getString("Username"));
        userText.setText(extras.getString("Username"));
        userEmail.setText(extras.getString("UserEmail"));
    }

    public void onBackTextClick(View view){
        this.finish();
    }
}