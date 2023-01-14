package com.example.chatsystemfordevs.Cryptography;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.google.firebase.firestore.Blob;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CryptographyManager {
    private KeyStore keyStore;
    private Cipher cipher;
    private final String algorithm;
    private final String blockMode;
    private final String padding;
    private final String transform;
    private byte[] initializationVector;
    private static final String TAG = "CryptographyManager";


    public CryptographyManager() {
        this.algorithm = KeyProperties.KEY_ALGORITHM_AES;
        this.blockMode = KeyProperties.BLOCK_MODE_CBC;
        this.padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
        this.transform = algorithm+"/"+blockMode+"/"+padding;
        try{
            this.keyStore = KeyStore.getInstance("AndroidKeyStore");
            this.keyStore.load(null);
            this.cipher = Cipher.getInstance(this.transform);
            this.cipher.init(Cipher.ENCRYPT_MODE,getKey());
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }

    private Cipher getDecryptCipher(byte[] initializationVector) throws NoSuchPaddingException, NoSuchAlgorithmException{
        Cipher decryptionCipher = Cipher.getInstance(this.transform);
        try{
            decryptionCipher.init(Cipher.DECRYPT_MODE,getKey(), new IvParameterSpec(initializationVector));
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return decryptionCipher;
    }

    private SecretKey getKey() throws UnrecoverableEntryException, KeyStoreException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyStore.SecretKeyEntry existingKey = (KeyStore.SecretKeyEntry) keyStore.getEntry("secret",null);
        if(existingKey == null){
             return generateKey();
        }
        return existingKey.getSecretKey();
    }

    //Generate key
    private SecretKey generateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(new KeyGenParameterSpec.Builder("secret",KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(blockMode)
                .setEncryptionPaddings(padding)
                .setUserAuthenticationRequired(false)
                .setRandomizedEncryptionRequired(true)
                .build());
        return generator.generateKey();

    }

    public Blob encryptMessage(String message) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = this.cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        this.initializationVector = cipher.getIV();
        return Blob.fromBytes(encryptedBytes);
    }

    public Blob getVector(){
        return Blob.fromBytes(this.initializationVector);
    }


    public String decryptMessage(Blob message,Blob initializationVector) throws BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException {
        byte[] theMessage = message.toBytes();
        byte[] vector = initializationVector.toBytes();
        Cipher decryptionCipher = getDecryptCipher(vector);
        byte[] decryptedMessage = decryptionCipher.doFinal(theMessage);

        return new String(decryptedMessage,StandardCharsets.UTF_8);
    }

}
