package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.Match;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.List;
import java.util.function.Consumer;

public class SwapViewModel extends ViewModel {

    private final String TAG = "SwapViewModel";
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<SwapState> state = new MutableLiveData<>(new SwapState.Loading());
    private final MutableLiveData<SwapEvents> events = new MutableLiveData<>(null);

    // TODO possibly create a consumer
    private final Consumer<List<Match>> userMatchesConsumer = userMatches ->{
        if (userMatches != null){
            Log.i(TAG, "User Matches Consumer Called: " + userMatches);
            state.setValue(new SwapState.Loaded(userMatches));
        }
    };

    public SwapViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(){
        if (authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new SwapState.Loading());
            // TODO Repo methods method calls
            String currentUserId = authRepository.getmAuth().getCurrentUser().getUid();
            userRepository.getMatchesForCurrentUser(
                    currentUserId,
                    userMatchesConsumer
            );
        }
    }

    public void declineButtonClicked(){
        // TODO
        Log.i(TAG, "Decline Button Clicked");
    }

    public LiveData<SwapState> getState() {
        return state;
    }

    public LiveData<SwapEvents> getEvents() {
        return events;
    }

    // Called after each event is observed in the SignUp Fragment.
    public void eventSeen(){
        events.setValue(null);
    }
}