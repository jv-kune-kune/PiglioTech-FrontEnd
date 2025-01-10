package com.northcoders.pigliotech_frontend.ui.fragments.signup;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;

public class SignUpViewModel extends ViewModel {
    /*
    TODO: User class implementation
     */

    private final AuthRepository authRepository;

    private final MutableLiveData<SignUpState> state = new MutableLiveData<>(new SignUpState(false));
    private final MutableLiveData<SignUpEvents> events = new MutableLiveData<>(null);

    public SignUpViewModel() {
        this.authRepository = new AuthRepository();
    }

    public void signUp(
            String name,
            String email,
            String password,
            String imageUrl,
            String region
    ){
        // Update the state for the progress loading bar
        state.setValue(new SignUpState(true));
        // create new user or register new user
        authRepository.getmAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Set events value to registration successful for the observer in SignUpFragment
                        events.setValue(SignUpEvents.REGISTRATION_SUCCESSFUL);

                        // On sign in success, update the user's display name
                        updateFirebaseDisplayName(name);

                        // Update the state for the progress loading bar
                        state.setValue(new SignUpState(false));
                    }
                    else {
                        // Update the state for the progress loading bar
                        state.setValue(new SignUpState(false));

                        events.setValue(SignUpEvents.REGISTRATION_FAILED);
                    }
                });

    }

    // Updates the DisplayName for the current Firebase user
    private void updateFirebaseDisplayName(String name){
        FirebaseUser user = authRepository.getmAuth().getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Display Name", "User profile updated.");
                        }
                    });
        }
    }

    public LiveData<SignUpState> getState() {
        return state;
    }

    public LiveData<SignUpEvents> getEvents() {
        return events;
    }

    // Called after each event is observed in the SignUp Fragment.
    public void eventSeen(){
        events.setValue(null);
    }
}