package com.northcoders.pigliotech_frontend.presentation.features.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.northcoders.pigliotech_frontend.R;
import com.northcoders.pigliotech_frontend.data.models.Region;
import com.northcoders.pigliotech_frontend.data.models.SwapRequest;
import com.northcoders.pigliotech_frontend.data.models.User;
import com.northcoders.pigliotech_frontend.data.repository.AuthRepository;
import com.northcoders.pigliotech_frontend.data.repository.UserRepository;

import java.util.function.Consumer;

public class ProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private Boolean isCurrentUser;
    private final String TAG = "ProfileViewModel";
    private String nonUserId;

    private final MutableLiveData<ProfileState> state = new MutableLiveData<>(
            new ProfileState.Loading());
    private final MutableLiveData<ProfileEvents> events = new MutableLiveData<>(null);

    private final Consumer<User> getUserConsumer = user -> {
        if (user != null) {
            if (isCurrentUser) {
                Log.i(TAG, "User Callback Consumer: " + user);
                state.setValue(new ProfileState.Loaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        user.getBooks()));
            } else {
                Log.i(TAG, "NonUser Callback Consumer: " + user);
                state.setValue(new ProfileState.OtherUserLoaded(
                        user.getName(),
                        user.getEmail(),
                        regionEnumToString(user.getRegion()),
                        user.getThumbnail(),
                        user.getBooks()));
            }
        } else {
            state.setValue(new ProfileState.Error());
            Log.e(TAG, "Error Retrieving User Information");
        }
    };

    private final Consumer<Integer> deleteBookConsumer = responseCode -> {
        if (responseCode != null) {
            if (responseCode == 204) {
                events.setValue(ProfileEvents.BOOK_DELETED);
            } else {
                events.setValue(ProfileEvents.BOOK_NOT_DELETED);
            }
            getCurrentUserLibrary();
        }
    };

    private final Consumer<Integer> likeBookConsumer = responseCode -> {
        if (responseCode != null) {
            if (responseCode == 201) {
                events.setValue(ProfileEvents.BOOK_LIKED);
            } else if (responseCode == 409) {
                events.setValue(ProfileEvents.BOOK_ALREADY_LIKED);
            } else {
                events.setValue(ProfileEvents.LIKE_ERROR);
            }
        }
    };

    public ProfileViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void load(String nonUserId) {
        // If nonUserId is not null, will send their id in the User request instead
        // current user's id
        if (nonUserId != null) {
            this.nonUserId = nonUserId;
            state.setValue(new ProfileState.Loading());
            this.isCurrentUser = false;
            userRepository.getUser(nonUserId, getUserConsumer);
        } else {
            getCurrentUserLibrary();
        }
    }

    private void getCurrentUserLibrary() {
        state.setValue(new ProfileState.Loading());
        this.isCurrentUser = true;
        userRepository.getUser(getUserId(), getUserConsumer);
    }

    public void deleteBook(String isbnString) {
        state.setValue(new ProfileState.Loading());
        String userID = getUserId();
        userRepository.deleteBook(userID, isbnString, deleteBookConsumer);

        Log.i(TAG, "DELETE BOOK BUTTON CLICKED User: " + userID + ", ISBN: " + isbnString);
    }

    public void likeBook(String isbnString) {
        SwapRequest swapRequest = new SwapRequest(getUserId(), nonUserId, isbnString);
        userRepository.createSwapRequest(swapRequest, likeBookConsumer);
        Log.i(TAG, "LIKE BOOK BUTTON CLICKED nonUser: " + nonUserId + ", ISBN: " + isbnString);
    }

    private String getUserId() {
        if (authRepository.getAuth().getCurrentUser() != null) {
            this.isCurrentUser = true;
            return authRepository.getAuth().getCurrentUser().getUid();
        }
        return null;
    }

    public void signOut() {
        authRepository.getAuth().signOut();
        FirebaseAuth.getInstance().signOut();
    }

    private int regionEnumToString(String backendRegion) {
        for (Region region : Region.values()) {
            if (backendRegion.equals(region.toString())) {
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

    public void eventSeen() {
        events.setValue(null);
    }
}