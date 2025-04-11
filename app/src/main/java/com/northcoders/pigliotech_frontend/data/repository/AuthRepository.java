package com.northcoders.pigliotech_frontend.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.data.firebase.FirebaseInstance;

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

    public void signOutUser(){
        mAuth.signOut();
    }
}
