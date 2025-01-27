package com.northcoders.pigliotech_frontend.ui.fragments.signup;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.northcoders.pigliotech_frontend.model.Region;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.function.Consumer;

public class SignUpViewModel extends ViewModel {

    private final String TAG = "SignUpViewModel";
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<SignUpState> state = new MutableLiveData<>(new SignUpState(false));
    private final MutableLiveData<SignUpEvents> events = new MutableLiveData<>(null);

    private final Consumer<Integer> addUserConsumer = responseCode -> {
        if (responseCode != null) {
            if (responseCode == 201) {
                // Set events value to registration successful for the observer in SignUpFragment
                events.setValue(SignUpEvents.REGISTRATION_SUCCESSFUL);
                Log.i(TAG, "User Added: " + responseCode);
            } else {
                events.setValue(SignUpEvents.REGISTRATION_FAILED);
                deleteFirebaseUser();
                Log.e(TAG, "User Not Added: " + responseCode);
            }
            // Update the state for the progress loading bar
            state.setValue(new SignUpState(false));
        } else {
            // For A Network Error
            deleteFirebaseUser();
            events.setValue(SignUpEvents.NETWORK_ERROR);
            Log.e(TAG, "NetworkError");
        }
    };

    public SignUpViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void signUp(
            String name,
            String email,
            String password,
            String imageUrl,
            String region
    ) {
        if (name.isBlank()) {
            events.setValue(SignUpEvents.NAME_IS_BLANK);
        } else if (email.isBlank()) {
            events.setValue(SignUpEvents.EMAIL_IS_BLANK);
        } else if (password.isBlank()) {
            events.setValue(SignUpEvents.PASSWORD_IS_BLANK);
        } else if (region.equals("Select Region")) {
            events.setValue(SignUpEvents.SELECT_REGION);
        } else {
            // Update the state for the progress loading bar
            state.setValue(new SignUpState(true));
            // create new user or register new user
            authRepository.getmAuth()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (authRepository.getmAuth().getCurrentUser() != null) {

                                // On sign in success, update the Firebase DisplayName property to the Region Enum
                                updateFirebaseDisplayName(regionStringToEnum(region));

                                User newUser = new User(
                                        authRepository.getmAuth().getCurrentUser().getUid(),
                                        name,
                                        email,
                                        regionStringToEnum(region),
                                        imageUrl
                                );
                                userRepository.addUser(newUser, addUserConsumer);
                            } else {
                                // Update the state for the progress loading bar
                                state.setValue(new SignUpState(false));

                                events.setValue(SignUpEvents.REGISTRATION_FAILED);
                            }
                        } else {
                            // Update the state for the progress loading bar
                            state.setValue(new SignUpState(false));

                            events.setValue(SignUpEvents.REGISTRATION_FAILED);
                        }
                    });
        }
    }

    private String regionStringToEnum(String regionString) {
        Log.i(TAG, "Region Name: " + regionString);
        String regionEnum;
        switch (regionString) {
            case "North West" -> regionEnum = Region.NORTH_WEST.name();
            case "North East" -> regionEnum = Region.NORTH_EAST.name();
            case "Yorkshire Humber" -> regionEnum = Region.YORKSHIRE_HUMBER.name();
            case "West Midlands" -> regionEnum = Region.WEST_MIDLANDS.name();
            case "East Midlands" -> regionEnum = Region.EAST_MIDLANDS.name();
            case "East Anglia" -> regionEnum = Region.EAST_ANGLIA.name();
            case "London" -> regionEnum = Region.LONDON.name();
            case "South East" -> regionEnum = Region.SOUTH_EAST.name();
            case "South West" -> regionEnum = Region.SOUTH_WEST.name();

            default -> regionEnum = "Select Region";
        }

        Log.i(TAG, "Region Enum: " + regionEnum);
        return regionEnum;
    }

    // Updates the DisplayName for the current Firebase user to the Region ENUM
    private void updateFirebaseDisplayName(String region) {
        FirebaseUser user = authRepository.getmAuth().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(region)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Display Name updated REGION : " + region);
                        }
                    });
        }
    }

    // Deletes the FirebaseUser Account if the user account it not created in the backend
    private void deleteFirebaseUser() {
        FirebaseUser user = authRepository.getmAuth().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Firebase User Account Deleted");
                } else {
                    Log.e(TAG, "Firebase User NOT Deleted");
                }
            });
        } else {
            Log.i(TAG, "No Firebase User Account to delete");
        }
    }

    public LiveData<SignUpState> getState() {
        return state;
    }

    public LiveData<SignUpEvents> getEvents() {
        return events;
    }

    // Called after each event is observed in the SignUp Fragment.
    public void eventSeen() {
        events.setValue(null);
    }
}