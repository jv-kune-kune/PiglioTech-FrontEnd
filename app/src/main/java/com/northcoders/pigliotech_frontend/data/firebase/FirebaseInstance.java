package com.northcoders.pigliotech_frontend.data.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseInstance  {

    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
