package com.northcoders.pigliotech_frontend.presentation.features.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.northcoders.pigliotech_frontend.data.repository.AuthRepository;

public class MainActivityViewModel extends AndroidViewModel {

    // For getting the Firebase Instance
    private final AuthRepository authRepository;
    private final MutableLiveData<MainActivityEvents> events = new MutableLiveData<>(null);

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepository();
        isUserLoggedIn(); // Is invoked when the ViewModel is instantiated
    }

    private void isUserLoggedIn(){
        if (authRepository.getmAuth().getCurrentUser() != null){
            events.setValue(MainActivityEvents.NAVIGATE_TO_HOME_PAGE);
        }else {
            events.setValue(MainActivityEvents.NAVIGATE_TO_LANDING_PAGE);
        }
    }

    // Sets the event to null after an event is sent to the UI
    public void eventSeen(){
        events.setValue(null);
    }

    public LiveData<MainActivityEvents> getEvents() {
        return events;
    }
}
