package com.northcoders.pigliotech_frontend.ui.fragments.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.List;
import java.util.function.Consumer;

public class HomeViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<HomeEvents> events = new MutableLiveData<>(null);

    private final MutableLiveData<HomeState> state = new MutableLiveData<>(
            new HomeState.Loading()
    );

    // Callback function that allows for the asynchronous method to be run in the main thread
    private final Consumer<List<User>> userLibrariesConsumer = userLibraries ->{
        if (userLibraries != null){
            Log.i("User Libraries Consumer Called", userLibraries.toString());
            state.setValue(
                    new HomeState.Loaded(userLibraries)
            );
        }
    };

    public HomeViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
        load(); // Load the users on ViewModel Instantiation
    }

    // Load the users by Region Exlcuding the the user.
    public void load(){
        state.setValue(new HomeState.Loading());
        // TODO Implementation of method from the repo
        String userRegion = authRepository.getmAuth().getCurrentUser().getDisplayName();
        String userId = authRepository.getmAuth().getCurrentUser().getUid();

        userRepository.getUsersByRegion(
                userRegion,
                userId,
                userLibrariesConsumer
        );
    }
    public LiveData<HomeEvents> getEvent() {
        return events;
    }

    public LiveData<HomeState> getState() {
        return state;
    }


    public void onUserClicked() {
        events.setValue(HomeEvents.USER_ITEM_CLICKED);
    }

    public void eventSeen(){
        events.setValue(null);
    }
}