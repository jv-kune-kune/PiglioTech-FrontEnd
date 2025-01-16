package com.northcoders.pigliotech_frontend.ui.fragments.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.model.Region;
import com.northcoders.pigliotech_frontend.model.User;
import com.northcoders.pigliotech_frontend.model.service.AuthRepository;
import com.northcoders.pigliotech_frontend.model.service.UserRepository;

import java.util.function.Consumer;

public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private Boolean isCurrentUser;
    private final String TAG = "ProfileViewModel";

    private final MutableLiveData<ProfileState> state = new MutableLiveData<>(
            new ProfileState.Loading()
    );
    private final MutableLiveData<ProfileEvents> events = new MutableLiveData<>(null);

    private final Consumer<User> getUserConsumer = user ->{
        if (user != null){
            if(isCurrentUser) {
                Log.i(TAG, "User Callback Consumer: " + user);
                state.setValue(new ProfileState.Loaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        user.getBooks()
                ));
            } else {
                Log.i(TAG, "NonUser Callback Consumer: " + user);
                state.setValue(new ProfileState.OtherUserLoaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        user.getBooks()
                ));
            }
        }
    };

    private final Consumer<Integer> deleteBookConsumer = responseCode ->{
        if(responseCode != null){
            if(responseCode == 204){
                events.setValue(ProfileEvents.BOOK_DELETED);
            }else {
                events.setValue(ProfileEvents.BOOK_NOT_DELETED);
            }
            getCurrentUserLibrary();
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
            userRepository.getUser(nonUserId, getUserConsumer);
        } else {
            getCurrentUserLibrary();
            // TODO: Error State for this else
        }
    }

    private void getCurrentUserLibrary(){
        if(authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new ProfileState.Loading());
            this.isCurrentUser = true;
            String userID = authRepository.getmAuth().getCurrentUser().getUid();
            userRepository.getUser(userID, getUserConsumer);
        }
    }

    public void deleteBook(String isbnString){
        state.setValue(new ProfileState.Loading());
        String userID = getUserId();
        userRepository.deleteBook(userID, isbnString, deleteBookConsumer);

        Log.i(TAG, "DELETE BOOK BUTTON CLICKED User: " + userID + ", ISBN: " + isbnString);
    }

    private String getUserId(){
        if(authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new ProfileState.Loading());
            this.isCurrentUser = true;
            return authRepository.getmAuth().getCurrentUser().getUid();
        }
        return null;
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

    public LiveData<ProfileState> getState() {
        return state;
    }

    public LiveData<ProfileEvents> getEvents() {
        return events;
    }

    public void eventSeen(){
        events.setValue(null);
    }
}