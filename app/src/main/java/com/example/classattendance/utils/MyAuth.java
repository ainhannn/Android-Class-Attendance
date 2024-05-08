package com.example.classattendance.utils;

import com.example.classattendance.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAuth {
    private static final String UID_DEFAULT = "hMnuzMB8SdMPHxaYMD8ooQHcrXB2";

    private static User modelUser = null;

    public static FirebaseAuth self() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
        modelUser = null;
    }

    public static User getModelUser() {
        return modelUser;
    }

    public static void setModelUser(User user) {
        modelUser = user;
    }

    public static String getUid() {
        return FirebaseAuth.getInstance() != null ? FirebaseAuth.getInstance().getUid() : UID_DEFAULT;
    }
}