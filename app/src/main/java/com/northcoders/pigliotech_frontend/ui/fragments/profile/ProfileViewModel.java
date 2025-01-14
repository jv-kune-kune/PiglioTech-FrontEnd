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
    private Boolean isCurrentUser;
    private final String TAG = "ProfileViewModel";

    private final MutableLiveData<ProfileState> state = new MutableLiveData<>(
            new ProfileState.Loading()
    );

    private final Consumer<User> userConsumer = user ->{

        if (user != null){
            if(isCurrentUser) {
                Log.i(TAG, "User Callback Consumer: " + user);
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
                Log.i(TAG, "NonUser Callback Consumer: " + user);
                state.setValue(new ProfileState.OtherUserLoaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        new ArrayList<>(List.of( // TODO test Books
                                new Book("ISBN", "NON USER BOOK", "Author MISC", "pic.com"),
                                new Book("ISBNISBN", "NON USER 2", "MISC 2", "pic.com"),
                                new Book("ISBNISBNISBN", "BOOK NON USER 3", "MISC ", "pic.com")
                        ))
                ));
            }
        }
    };

    public ProfileViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(String nonUserId){
        // If nonUserId is not null, will send their id in the User request instead current user's id
        if(nonUserId != null){
            state.setValue(new ProfileState.Loading());
            this.isCurrentUser = false;
            userRepository.getUser(nonUserId, userConsumer);
        } else {
            if(authRepository.getmAuth().getCurrentUser() != null){
                state.setValue(new ProfileState.Loading());
                this.isCurrentUser = true;
                String userID = authRepository.getmAuth().getCurrentUser().getUid();
                userRepository.getUser(userID, userConsumer);
            }
            // TODO: Error State for this else
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
                Log.i(TAG, "Mapped Region: " + region);
                return region.region;
            }
        }
        return R.string.select_region;
    }
}