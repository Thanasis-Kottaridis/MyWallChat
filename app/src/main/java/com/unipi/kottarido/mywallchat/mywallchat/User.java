package com.unipi.kottarido.mywallchat.mywallchat;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private String Email;
    private String Username;
    private Uri ProfilePhoto;


    public User(String email, String username, Uri profilePhoto) {
        Email = email;
        Username = username;
        ProfilePhoto = profilePhoto;
    }
}
