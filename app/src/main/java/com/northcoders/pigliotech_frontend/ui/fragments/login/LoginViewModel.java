package com.northcoders.pigliotech_frontend.ui.fragments.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.service.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<LoginState> state = new MutableLiveData<>(
            new LoginState(false)
    );
    private final MutableLiveData<LoginEvents> events = new MutableLiveData<>(null);

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }

    public MutableLiveData<LoginState> getState() {
        return state;
    }

    public MutableLiveData<LoginEvents> getEvents() {
        return events;
    }

    public void eventSeen(){
        events.setValue(null);
    }

    public void login(String email, String password){

        if(email.isBlank()){
            // Set the event for a blank email
            events.setValue(LoginEvents.EMAIL_IS_BLANK);
        } else if (password.isBlank()){
            // Set the event for a blank password
            events.setValue(LoginEvents.PASSWORD_IS_BLANK);
        }else {
            // Update the state of the progressbar
            state.setValue(new LoginState(true));

            // SignIn existing user
            authRepository.getmAuth()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            // Set the event for a successful login
                            events.setValue(LoginEvents.LOGIN_SUCCESSFUL);

                            // hide the progress bar
                            state.setValue(new LoginState(false));

                        }else {

                            // Set the event for a failed login
                            events.setValue(LoginEvents.LOGIN_FAILED);

                            // Update the state for the progressbar
                            state.setValue(new LoginState(false));
                        }
                    });
        }
    }
}