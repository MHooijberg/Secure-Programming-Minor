package com.example.chatsystemfordevs.Model;

import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Message {
    private Blob content;
    private String creation_date;
    private String type;
    private DocumentReference user;
    private int id;
    private Blob initializationVector;

    public Message(int id, Blob content, String creation_date, String type, DocumentReference user,Blob vector) {
        this.content = content;
        this.creation_date = creation_date;
        this.type = type;
        this.user = user;
        this.id = id;
        this.initializationVector = vector;

    }

    public Blob getInitializationVector() {
        return initializationVector;
    }

    public void setInitializationVector(Blob initializationVector) {
        this.initializationVector = initializationVector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference userReference) {
        this.user = userReference;
    }
}
