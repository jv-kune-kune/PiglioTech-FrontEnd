package com.northcoders.pigliotech_frontend.model.service;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {

    private FirebaseAuth mAuth;

    public AuthRepository() {
        this.mAuth = FirebaseInstance.getFirebaseAuth();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void signOutUser() {
        mAuth.signOut();
    }
}
