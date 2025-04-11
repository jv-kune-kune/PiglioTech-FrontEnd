package com.northcoders.pigliotech_frontend.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.data.firebase.FirebaseInstance;

public class AuthRepository {

    private FirebaseAuth mAuth;

    public AuthRepository() {
        this.mAuth = FirebaseInstance.getFirebaseAuth();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    @SuppressWarnings("unused")
    public void setAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @SuppressWarnings("unused")
    public void signOutUser() {
        mAuth.signOut();
    }
}
