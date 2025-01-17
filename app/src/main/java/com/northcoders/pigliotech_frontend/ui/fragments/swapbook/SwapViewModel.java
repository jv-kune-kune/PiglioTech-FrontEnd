package com.northcoders.pigliotech_frontend.ui.fragments.swapbook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.Match;
import com.northcoders.pigliotech_frontend.model.SwapDismissal;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileEvents;
import com.northcoders.pigliotech_frontend.ui.fragments.profile.ProfileState;

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

    private final Consumer<Integer> dimissMatchConsumer = responseCode ->{
        if(responseCode != null){
            if(responseCode == 204){
                events.setValue(SwapEvents.DISMISS_MATCH);
            }else {
                events.setValue(SwapEvents.DISMISS_MATCH_FAILED);
            }
            load();
        }
    };

    public SwapViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(){
        if (authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new SwapState.Loading());
            // TODO Refactor
            String currentUserId = authRepository.getmAuth().getCurrentUser().getUid();
            userRepository.getMatchesForCurrentUser(
                    currentUserId,
                    userMatchesConsumer
            );
        }
    }

    public void declineButtonClicked(Long matchId){
        state.setValue(new SwapState.Loading());
        SwapDismissal swapDismissal = new SwapDismissal(
                getUserId(),
                matchId
        );

        userRepository.dismissMatch(swapDismissal, dimissMatchConsumer);
        Log.i(TAG, "Decline Button Clicked: " + swapDismissal);
    }

    private String getUserId(){
        if(authRepository.getmAuth().getCurrentUser() != null){
            return authRepository.getmAuth().getCurrentUser().getUid();
        }
        return null;
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