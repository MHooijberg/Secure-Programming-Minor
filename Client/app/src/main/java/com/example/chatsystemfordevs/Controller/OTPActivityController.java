package com.example.chatsystemfordevs.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;

public class OTPActivityController extends AppCompatActivity {
    private AuthenticationManager authenticationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2faview);
    }

    public void onBackTextClick(View view){
        this.finish();
    }

    public void onNextButtonClick(View view){
        startActivity(new Intent(this, GuildServerController.class));
    }
}
