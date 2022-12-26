package com.example.chatsystemfordevs.Utilities;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.chatsystemfordevs.Controller.GuildServerController;
import com.example.chatsystemfordevs.Controller.LoginActivityController;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationManager {
    private final FirebaseAuth firebaseAuth;
    private final DBHelper database;
    private static final String emailPatternValidation = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern illegalCharacters = Pattern.compile("[\\/.<>+*@&()^!\"#$%':;=?_`{}|~]");
    private static final int usernameLengthLimit = 15;

    public AuthenticationManager() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.database = new DBHelper();

    }

    public void registerUser(Context context, String username, String email, String password, String phoneNumber) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                        database.createUser(username, email,phoneNumber);
                        verifyEmail(context);
                        Toast.makeText(context, "User successfully registered", Toast.LENGTH_LONG).show();
                        Intent loginPage = new Intent(context, LoginActivityController.class);
                        context.startActivity(loginPage);
                        ((Activity) context).finish();
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean isUsernameAlphaNumeric(String username){
        if(illegalCharacters.matcher(username).find()){
            return false;
        }
        boolean isDigit = false, isAlphabetic = false;
        if(username.length() > usernameLengthLimit) return false;

        for (int i = 0; i< username.length();i++){
            char character = username.charAt(i);
            if(Character.isDigit(character)){
                isDigit = true;
            }else if(Character.isAlphabetic(character)){
                isAlphabetic = true;
            }
            if(isDigit && isAlphabetic) return true;
        }
        return false;
    }

    public boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile(emailPatternValidation);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private void verifyEmail(Context context){
        Objects.requireNonNull(this.firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(emailTask -> {
            if(emailTask.isSuccessful()){
                Toast.makeText(context, "Email verification has been send", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"There was a problem with your email verification",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() >= 10 && Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public boolean isPasswordEqual(String password, String repeatedPassword) {
        return TextUtils.equals(password, repeatedPassword);
    }

    public boolean isPasswordValidated(@NonNull String password) {
        int minimumPasswordLength = 8;
        return password.length() >= minimumPasswordLength && validateCharacters(password);
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
