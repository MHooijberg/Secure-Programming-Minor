package com.example.chatsystemfordevs.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatsystemfordevs.R;
import com.example.chatsystemfordevs.Utilities.AuthenticationManager;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivityController extends AppCompatActivity {
    private AuthenticationManager authenticationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpasswordview);
    }

    public void onBackTextClick(View view){
        this.finish();
    }


    public void onNextButtonClick(View view){
        EditText email_edit_text = findViewById(R.id.email_edit_text);
        String email = email_edit_text.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(
                    this,
                    "Please enter a valid email",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(
                            this,
                            "Email has been sent to your address",
                            Toast.LENGTH_LONG
                    ).show();
                    this.finish();
                } else {
                    Toast.makeText(
                            this,
                            task.getException().getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }

    }
}
