package com.example.chatsystemfordevs.Utilities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatsystemfordevs.Controller.GuildServerController;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class AuthenticationManager {
    private final FirebaseAuth firebaseAuth;
    private final DBHelper database;

    public AuthenticationManager() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        database = new DBHelper();
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void registerUser(Activity activity, String username, String email, String password, String phoneNumber) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        database.createUser(username, email, phoneNumber);
                    } else {
                        System.out.println(task.getException().getMessage());
                    }
                });
    }

    public void loginUsername(Activity activity,String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        if(Objects.requireNonNull(this.firebaseAuth.getCurrentUser()).isEmailVerified()){
                            Intent loginPage = new Intent(activity, GuildServerController.class);
                            activity.startActivity(loginPage);
                            Log.d(TAG, "signInWithEmail:success");
                        }else{
                            Toast.makeText(activity, "The email needs to be verified before the app can be used", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, task.getException().getMessage());
                    }
                });
    }

    public void resetPassword(String email, String password) {

    }


    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() >= 10 && Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public boolean isPasswordEqual(String password, String repeatedPassword) {
        return TextUtils.equals(password, repeatedPassword);
    }

    public boolean isPasswordValidated(@NonNull String password) {
        return password.length() >= 8 && validateCharacters(password);
    }

    private boolean validateCharacters(@NonNull String password) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        boolean hasNum = false;
        boolean hasCapital = false;
        boolean hasLowChar = false;
        char c;

        for (int i = 0; i < password.length(); i++) {
            c = password.charAt(i);
            if (Character.isDigit(c)) {
                hasNum = true;
            } else if (Character.isLowerCase(c)) {
                hasLowChar = true;
            } else if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (hasNum && hasLowChar && hasCapital && !pattern.matcher(password).matches()) {
                return true;
            }
        }
        return false;
    }
}
