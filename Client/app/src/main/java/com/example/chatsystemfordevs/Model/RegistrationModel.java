package com.example.chatsystemfordevs.Model;

import android.widget.EditText;

import java.util.Observable;

public class RegistrationModel extends Observable {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText repeatablePassword;

    public RegistrationModel() {}

    public void setUsername(EditText username) {
        this.username = username;
    }

    public void setEmail(EditText email) {
        this.email = email;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public void setRepeatablePassword(EditText repeatablePassword) {
        this.repeatablePassword = repeatablePassword;
    }

    public EditText getUsername() {
        return username;
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getPassword() {
        return password;
    }

    public EditText getRepeatablePassword() {
        return repeatablePassword;
    }
}
