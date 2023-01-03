package com.example.chatsystemfordevs.Utilities;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.List;


public class LibSignal_KeyGenerator {
    private int startId;

    // Only on install of the application
    static IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
    int registrationId = KeyHelper.generateRegistrationId(false);
    List<PreKeyRecord> preKeys = KeyHelper.generatePreKeys(startId, 100);
    SignedPreKeyRecord signedPreKey = KeyHelper.generateSignedPreKey(identityKeyPair, 5);

    public LibSignal_KeyGenerator() throws InvalidKeyException {

      
    }


}

