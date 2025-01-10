package com.northcoders.pigliotech_frontend.ui.fragments.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;

public class SignUpViewModel extends ViewModel {
    private AuthRepository authRepository;

    private MutableLiveData<SignUpState> state = new MutableLiveData<>(new SignUpState(false));
    private MutableLiveData<SignUpEvents> events = new MutableLiveData<>(null);

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
        state.setValue(new SignUpState(true));
        // create new user or register new user
        authRepository.getmAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(),
//                                            "Registration successful!",
//                                            Toast.LENGTH_LONG)
//                                    .show(); Send Success Event
                            events.setValue(SignUpEvents.REGISTRATION_SUCCESSFUL);

                            // Set Display Name
                            // Sign in success, update the user's display name
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("Test User Name")
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Display Name", "User profile updated.");
                                            }
                                        }
                                    });


                            state.setValue(new SignUpState(false));
                            // hide the progress bar
//                            progressbar.setVisibility(View.GONE); Emit loading state



                            // if the user created intent to login activity
//                            getActivity().getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.frame_layout_fragment, new ProfileFragment())
//                                    .commit(); emit navigation event
                        }
                        else {
                            state.setValue(new SignUpState(false));

                            // Registration failed
//                            Toast.makeText(
//                                            getContext(),
//                                            "Registration failed!!"
//                                                    + " Please try again later",
//                                            Toast.LENGTH_LONG)
//                                    .show();
                            events.setValue(SignUpEvents.REGISTRATION_FAILED);
//
//                            // hide the progress bar
//                            progressbar.setVisibility(View.GONE); Emit failure event & update state
                        }
                    }
                });

    }

    public LiveData<SignUpState> getState() {
        return state;
    }

    public LiveData<SignUpEvents> getEvents() {
        return events;
    }

    public void eventSeen(){
        events.setValue(null);
    }
}

