package com.example.chatsystemfordevs.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Observable;
import java.util.Observer;

public class RegistrationActivityController extends AppCompatActivity implements Observer {
    private AuthenticationManager authenticationManager;
    private Button button;
    private EditText username, email, password, repeatablePassword, phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationview);
        this.authenticationManager = new AuthenticationManager();
        this.button = findViewById(R.id.NextButton);
        this.username = findViewById(R.id.EditUsername);
        this.email = findViewById(R.id.UserEmailAddress);
        this.password = findViewById(R.id.UserPassword);
        this.phoneNumber = findViewById(R.id.UserPhoneNumber);
        this.repeatablePassword = findViewById(R.id.UserRepeatedPassword);
        registerUser();
    }

    @Override
    public void update(Observable observable, Object o) {
    }

    public void registerUser() {
        this.button.setOnClickListener(view -> {
            String usr = username.getText().toString();
            String usrEmail = email.getText().toString();
            String phoneInput = phoneNumber.getText().toString();
            String pass = password.getText().toString();
            String repeatPass = repeatablePassword.getText().toString();

            if (!TextUtils.isEmpty(usr) && !TextUtils.isEmpty(usrEmail) && !TextUtils.isEmpty(phoneInput) && !TextUtils.isEmpty(pass)) {
                if (authenticationManager.isUsernameAlphaNumeric(usr)
                        && authenticationManager.isEmailValid(usrEmail)
                        && authenticationManager.isPhoneNumberValid(phoneInput)
                        && authenticationManager.isPasswordValidated(pass)
                        && authenticationManager.isPasswordEqual(pass, repeatPass)) {
                    authenticationManager.registerUser(this, usr, usrEmail, pass, phoneInput);
                }else{
                    if(!authenticationManager.isUsernameAlphaNumeric(usr)){
                        this.username.setError("Username needs to be less than 15.\nUsername needs to be alphanumeric");
                    }
                    if (!authenticationManager.isEmailValid(usrEmail)) {
                        this.email.setError("Incorrect email format");
                    }
                    if (!authenticationManager.isPhoneNumberValid(phoneInput)) {
                        this.phoneNumber.setError("Incorrect phone format");
                    }
                    if (!authenticationManager.isPasswordValidated(pass)) {
                        this.password.setError("Password needs to contain at least 8 characters. \n" + "One uppercase and one lowercase character. \n" + "All passwords must contain at least one number. \n" + "All passwords must contain at least one special character.");
                    }
                    if (!authenticationManager.isPasswordEqual(pass, repeatPass)) {
                        this.repeatablePassword.setError("The password is not the same");
                    }
                }
            } else {
                Toast.makeText(this, "Input fields are empty", Toast.LENGTH_LONG).show();
            }
        });
    }
}