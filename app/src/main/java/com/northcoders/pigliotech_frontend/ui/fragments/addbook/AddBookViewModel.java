package com.northcoders.pigliotech_frontend.ui.fragments.addbook;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;
import com.northcoders.pigliotech_frontend.ui.fragments.signup.SignUpEvents;
import com.northcoders.pigliotech_frontend.ui.fragments.signup.SignUpState;

public class AddBookViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<AddBookState> state = new MutableLiveData<>(new AddBookState(false));
    private final MutableLiveData<SignUpEvents> events = new MutableLiveData<>(null);


    public AddBookViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }
}
