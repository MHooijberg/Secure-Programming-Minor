package com.example.chatsystemfordevs.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;

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
        authenticationManager = new AuthenticationManager();
        this.button = findViewById(R.id.RegistrationButton);
        this.username = findViewById(R.id.Username);
        this.email = findViewById(R.id.UserEmail);
        this.password = findViewById(R.id.UserPassword);
        this.phoneNumber = findViewById(R.id.PhoneNumber);
        this.repeatablePassword = findViewById(R.id.UserPasswordRepeated);
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
                if (authenticationManager.isEmailValid(usrEmail)
                        && authenticationManager.isPhoneNumberValid(phoneInput)
                        && authenticationManager.isPasswordValidated(pass)
                        && authenticationManager.isPasswordEqual(pass, repeatPass)) {
                    authenticationManager.registerUser(RegistrationActivityController.this, usr, usrEmail, pass, phoneInput);
                    Toast.makeText(this, "User successfully registered", Toast.LENGTH_LONG).show();
                    Intent loginPage = new Intent(this, LoginActivityController.class);
                    startActivity(loginPage);
                }
                if (!authenticationManager.isEmailValid(usrEmail)) {
                    email.setError("Incorrect email format");
                }
                if (!authenticationManager.isPhoneNumberValid(phoneInput)) {
                    phoneNumber.setError("Incorrect phone format");
                }
                if (!authenticationManager.isPasswordValidated(pass)) {
                    password.setError("Password needs to contain at least 8 characters. \n" + "one uppercase and one lowercase character. \n" + "All passwords must contain at least one number. \n" + "All passwords must contain at least one special character.");
                }
                if (!authenticationManager.isPasswordEqual(pass, repeatPass)) {
                    repeatablePassword.setError("The password is not the same");
                }
            } else {
                Toast.makeText(this, "Input fields are empty", Toast.LENGTH_LONG).show();
            }
        });
    }
}