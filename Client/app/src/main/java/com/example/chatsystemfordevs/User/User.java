package com.example.chatsystemfordevs.User;

import java.util.List;

public class User {
    private String email;
    private String Guild;
    private boolean isOnline;
    private String phoneNumber;
    private List<String> registrationTokens;
    private String username;

    public User(String email, String guild, boolean isOnline, String phoneNumber, List<String> registrationTokens, String username) {
        this.email = email;
        Guild = guild;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.registrationTokens = registrationTokens;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuild() {
        return Guild;
    }

    public void setGuild(String guild) {
        Guild = guild;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getRegistrationTokens() {
        return registrationTokens;
    }

    public void setRegistrationTokens(List<String> registrationTokens) {
        this.registrationTokens = registrationTokens;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
