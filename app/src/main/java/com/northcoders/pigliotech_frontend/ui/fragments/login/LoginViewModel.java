package com.northcoders.pigliotech_frontend.ui.fragments.login;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<LoginState> state = new MutableLiveData<>(new LoginState(false));
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

        // Update the state of the progressbar
        state.setValue(new LoginState(true));

        // signin existing user
        authRepository.getmAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
//                                Toast.makeText(getContext(),
//                                                "Login successful!!",
//                                                Toast.LENGTH_LONG)
//                                        .show();
                                events.setValue(LoginEvents.LOGIN_SUCCESSFUL);

                                // hide the progress bar
//                                progressBar.setVisibility(View.GONE);
                                state.setValue(new LoginState(false));

                                // if sign-in is successful
                                // intent to home activity
//                                if (getActivity() != null){
//                                    getActivity().getSupportFragmentManager()
//                                            .beginTransaction()
//                                            .replace(
//                                                    R.id.frame_layout_fragment,
//                                                    profileFragment
//                                            ).commit();
//                                }
                            }

                            else {

                                // sign-in failed
//                                Toast.makeText(getContext(),
//                                                "Login failed!!",
//                                                Toast.LENGTH_LONG)
//                                        .show();
//
//                                // hide the progress bar
//                                progressBar.setVisibility(View.GONE);

                                events.setValue(LoginEvents.LOGIN_FAILED);

                                // Update the state for the progressbar
                                state.setValue(new LoginState(false));
                            }
                        }
                );
    }

}
