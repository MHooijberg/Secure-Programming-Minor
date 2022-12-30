/*
package com.example.chatsystemfordevs.User;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    private String id;
    private String email;
    private List<String> guildList;
    private boolean isOnline;
    private String phoneNumber;
    private List<String> registrationTokens;
    private String username;

    public User(String id,String email, List<String> guild, boolean isOnline, String phoneNumber, List<String> registrationTokens, String username) {
        this.id = id;
        this.email = email;
        this.guildList = guild;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.registrationTokens = registrationTokens;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGuildList() {
        return guildList;
    }

    public void setGuildList(List<String> guildList) {
        this.guildList = guildList;
    }

    public User(String email, List<String> guild, boolean isOnline, String phoneNumber, String username){
        this.email = email;
        this.guildList = guild;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    protected User(Parcel in) {
        email = in.readString();
        guildList = in.readArrayList(null);
        isOnline = in.readByte() != 0;
        phoneNumber = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGuild() {
        return guildList;
    }

    public void setGuild(List<String> guild) {
        guildList = guild;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.createBinderArrayList(guildList);
        parcel.writeByte((byte) (isOnline ? 1 : 0));
        parcel.writeString(phoneNumber);
        parcel.writeString(username);
    }
}
*/
