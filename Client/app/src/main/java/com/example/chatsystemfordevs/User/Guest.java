package com.example.chatsystemfordevs.User;

import java.util.List;

public class Guest extends Users{
    public Guest(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, List<String> registrationTokens, String username) {
        super(id, email, guildList, isOnline, phoneNumber, registrationTokens, username);
    }

    public Guest(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, String username) {
        super(id, email, guildList, isOnline, phoneNumber, username);
    }
}
