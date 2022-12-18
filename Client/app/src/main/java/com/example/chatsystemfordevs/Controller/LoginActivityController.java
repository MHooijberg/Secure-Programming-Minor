package com.example.chatsystemfordevs.Controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;

public class LoginActivityController extends AppCompatActivity {
    private AuthenticationManager authenticationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginview);
    }
}
