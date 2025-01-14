package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.model.Book;
import com.northcoders.pigliotech_frontend.model.Region;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<ProfileState> state = new MutableLiveData<>(
            new ProfileState.Loading(true)
    );

    private final Consumer<User> userConsumer = user ->{
        if (user != null){
            ProfileState.Loading currentState = (ProfileState.Loading) state.getValue();
            if(currentState.isUser()) {
                Log.i("User Consumer Called", user.toString());
                state.setValue(new ProfileState.Loaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        new ArrayList<>(List.of( // TODO test Books
                                new Book("ISBN", "BOOK 1", "Author 1", "pic.com"),
                                new Book("ISBNISBN", "BOOK 2", "Author 2", "pic.com"),
                                new Book("ISBNISBNISBN", "BOOK 3", "Author3 ", "pic.com")
                        ))
                ));
            } else {
                Log.i("Other User", user.toString());
                state.setValue(new ProfileState.OtherUserLoaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        new ArrayList<>(List.of( // TODO test Books
                                new Book("ISBN", "BOOK 1", "Author 1", "pic.com"),
                                new Book("ISBNISBN", "BOOK 2", "Author 2", "pic.com"),
                                new Book("ISBNISBNISBN", "BOOK 3", "Author3 ", "pic.com")
                        ))
                ));
            }
        }
    };

    public ProfileViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(String s){
        if(s != null){
            state.setValue(new ProfileState.Loading(false));
            String userID = s;
            userRepository.getUser(userID, userConsumer);

        } else {
            state.setValue(new ProfileState.Loading(true));
            String userID = authRepository.getmAuth().getCurrentUser().getUid();
            userRepository.getUser(userID, userConsumer);
        }

    }

    public LiveData<ProfileState> getState() {
        return state;
    }

    public void signOut(){
        authRepository.getmAuth().signOut();
        FirebaseAuth.getInstance().signOut();
    }

    private int regionEnumToString(String backendRegion){
        for (Region region : Region.values()){
            if (backendRegion.equals(region.toString())){
                return region.region;
            }
        }
        return R.string.select_region;
    }
}