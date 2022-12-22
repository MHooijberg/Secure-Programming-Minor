package com.example.chatsystemfordevs.Controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;

public class LoginActivityController extends AppCompatActivity {
    private AuthenticationManager authenticationManager;
    private EditText username, password;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginview);
        this.authenticationManager = new AuthenticationManager();
        this.username = findViewById(R.id.LoginEmail);
        this.password = findViewById(R.id.LoginPassword);
        this.loginButton = findViewById(R.id.next);
        authenticateUser();
    }

    private void authenticateUser(){
        this.loginButton.setOnClickListener(view -> {
            String userEmail = this.username.getText().toString();
            String userPassword = this.password.getText().toString();
            if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
                if(authenticationManager.isEmailValid(userEmail) && authenticationManager.isPasswordValidated(userPassword)){
                    this.authenticationManager.loginUsername(this,userEmail,userPassword);
                }else{
                    Toast.makeText(this, "Some of the input are wrong or do not meet the required format", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Some of the input fields are empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
