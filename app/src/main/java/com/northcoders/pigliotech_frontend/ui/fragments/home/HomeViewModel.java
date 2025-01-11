package com.northcoders.pigliotech_frontend.ui.fragments.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomeViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<HomeState> state = new MutableLiveData<>(
            new HomeState.Loading()
    );

    private final Consumer<List<User>> userLibrariesConsumer = userLibraries ->{
      if (userLibraries != null){
          Log.i("User Libraries Consumer Called", userLibraries.toString());
          state.setValue(
                  new HomeState.Loaded(userLibraries)
          );
      }
    };

    public HomeViewModel(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(){
        state.setValue(new HomeState.Loading());
        // TODO Implementation of method from the repo
        userLibrariesConsumer.accept(new ArrayList<>());
    }

    public LiveData<HomeState> getState() {
        return state;
    }
}
