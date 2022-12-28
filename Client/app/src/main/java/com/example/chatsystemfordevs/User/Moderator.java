package com.example.chatsystemfordevs.User;

import java.util.List;

public class Moderator extends Users {

    public Moderator(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, List<String> registrationTokens, String username) {
        super(id, email, guildList, isOnline, phoneNumber, registrationTokens, username);
    }

    public Moderator(String id, String email, List<String> guildList, boolean isOnline, String phoneNumber, String username) {
        super(id, email, guildList, isOnline, phoneNumber, username);
    }
}
