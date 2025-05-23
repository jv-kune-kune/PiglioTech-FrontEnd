package com.northcoders.pigliotech_frontend.presentation.features.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.data.models.User;
import com.northcoders.pigliotech_frontend.data.repository.AuthRepository;
import com.northcoders.pigliotech_frontend.data.repository.UserRepository;

import java.util.List;
import java.util.function.Consumer;

public class HomeViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<HomeEvents> events = new MutableLiveData<>(
            new HomeEvents.ClickedUserLibrary(null));

    private final MutableLiveData<HomeState> state = new MutableLiveData<>(
            new HomeState.Loading());

    // Callback function that allows for the asynchronous method to be run in the
    // main thread
    private final Consumer<List<User>> userLibrariesConsumer = userLibraries -> {
        if (userLibraries != null) {
            Log.i("User Libraries Consumer Called", userLibraries.toString());
            state.setValue(
                    new HomeState.Loaded(userLibraries));
        } else {
            state.setValue(new HomeState.Error());
        }
    };

    public HomeViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    // Load the users by Region Excluding the the user.
    public void load() {

        state.setValue(new HomeState.Loading());
        if (authRepository.getAuth().getCurrentUser() != null) {
            String userRegion = authRepository.getAuth().getCurrentUser().getDisplayName();
            String userId = authRepository.getAuth().getCurrentUser().getUid();

            userRepository.getUsersByRegion(
                    userRegion,
                    userId,
                    userLibrariesConsumer);
        }
    }

    public void signOut() {
        authRepository.getAuth().signOut();
        FirebaseAuth.getInstance().signOut();
    }

    public LiveData<HomeEvents> getEvent() {
        return events;
    }

    public LiveData<HomeState> getState() {
        return state;
    }

    // Called by the Adapter when an item in the RecyclerView is clicked and the id
    // is passed to
    // this method and assigned to the Event
    public void onUserClicked(String id) {
        events.setValue(new HomeEvents.ClickedUserLibrary(id));
    }

    // Sets the value of the clicked User back to null after the event has been
    // "seen"
    public void eventSeen() {
        events.setValue(new HomeEvents.ClickedUserLibrary(null));
    }
}