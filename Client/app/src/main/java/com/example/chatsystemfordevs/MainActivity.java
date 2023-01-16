package com.example.chatsystemfordevs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatsystemfordevs.Controller.LoginActivityController;
import com.example.chatsystemfordevs.Controller.RegistrationActivityController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClick(View view){
        startActivity(new Intent(this, LoginActivityController.class));
    }

    public void onRegisterTextClick(View view){
        startActivity(new Intent(this, RegistrationActivityController.class));
    }
}