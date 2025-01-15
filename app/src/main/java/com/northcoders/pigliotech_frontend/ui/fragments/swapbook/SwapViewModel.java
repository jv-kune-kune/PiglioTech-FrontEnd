package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

public class SwapViewModel extends ViewModel {

    private final String TAG = "SwapViewModel";
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<SwapState> state = new MutableLiveData<>(new SwapState.Loading());
    private final MutableLiveData<SwapEvents> events = new MutableLiveData<>(null);

    // TODO possibly create a consumer

    public SwapViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(){
        if (authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new SwapState.Loading());
            // TODO Repo methods method calls

        }
    }

    public void acceptButtonClicked(){
        // TODO
        Log.i(TAG, "Accept Button Clicked");
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