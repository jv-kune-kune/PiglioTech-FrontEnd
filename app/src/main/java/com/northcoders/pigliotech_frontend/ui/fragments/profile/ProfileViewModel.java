package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<ProfileState> state = new MutableLiveData<>(
            new ProfileState.Loading()
    );

    private final Consumer<User> userConsumer = user ->{
        if (user != null){

            Log.i("User Consumer Called", user.toString());
            state.setValue(new ProfileState.Loaded(
                    user.getName(),
                    user.getEmail(),
                    user.getRegion(),
                    user.getThumbnail(),
                    new ArrayList<>()
            ));
        }
    };

    public ProfileViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(){
        state.setValue(new ProfileState.Loading());
        String userID = authRepository.getmAuth().getCurrentUser().getUid();
        userRepository.getUser(userID, userConsumer);
    }

    public MutableLiveData<ProfileState> getState() {
        return state;
    }

    public void signOut(){
        authRepository.getmAuth().signOut();
        FirebaseAuth.getInstance().signOut();
    }
}