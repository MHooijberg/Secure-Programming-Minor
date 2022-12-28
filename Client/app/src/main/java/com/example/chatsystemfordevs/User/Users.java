package com.example.chatsystemfordevs.User;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public abstract class Users {
    private String id;
    private String email;
    @PropertyName("guild")
    private List<String> guildList;
    private boolean isOnline;
    private String phoneNumber;
    private List<String> registrationTokens;
    private String username;

    public Users(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, List<String> registrationTokens, String username) {
        this.id = id;
        this.email = email;
        this.guildList = guildList;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.registrationTokens = registrationTokens;
        this.username = username;
    }

    public Users(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, String username) {
        this.id = id;
        this.email = email;
        this.guildList = guildList;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public Users(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @PropertyName("guild")
    public List<String> getGuildList() {
        return guildList;
    }
    @PropertyName("guild")
    public void setGuildList(List<String> guildList) {
        this.guildList = guildList;
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
