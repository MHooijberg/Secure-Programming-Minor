package com.example.chatsystemfordevs.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;

public class SettingsActivityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsview);
    }

    public void onBackTextClick(View view){
        this.finish();
    }
}