package com.northcoders.pigliotech_frontend.presentation.features.addbook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.northcoders.pigliotech_frontend.data.models.Isbn;
import com.northcoders.pigliotech_frontend.data.repository.AuthRepository;
import com.northcoders.pigliotech_frontend.data.repository.UserRepository;

import java.util.function.Consumer;

public class AddBookViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final String TAG = "AddBookViewModel";

    private final MutableLiveData<AddBookState> state = new MutableLiveData<>(new AddBookState(false));
    private final MutableLiveData<AddBookEvents> events = new MutableLiveData<>(null);

    private final Consumer<Integer> addBookConsumer = responseCode ->{
        if (responseCode !=null){
            if (responseCode == 201){
                events.setValue(AddBookEvents.BOOK_ADDED);
                Log.i(TAG, "Book Added: " + responseCode);
            }else if (responseCode == 409){
                events.setValue(AddBookEvents.BOOK_ALREADY_OWNED);
                Log.i(TAG, "Book Already Owned: " + responseCode);
            }
            else {
               events.setValue(AddBookEvents.BOOK_NOT_ADDED);
                Log.e(TAG, "Book Not Added: " + responseCode);
            }
            state.setValue(new AddBookState(false));
        }else {
            state.setValue(new AddBookState(false));
            events.setValue(AddBookEvents.NETWORK_ERROR);
            Log.e(TAG, "NetworkError");
        }
    };

    public AddBookViewModel() {
        this.authRepository = new AuthRepository();
        this.userRepository = new UserRepository();
    }

    public void addBook(String userIsbnInput){

        // Will remove any hyphens input by the User
        String isbnRemovedHyphens = userIsbnInput.replaceAll("-","");
        // Validates the length of the ISBN
        if (isIsbnValid(isbnRemovedHyphens) && authRepository.getmAuth().getCurrentUser() != null){
            state.setValue(new AddBookState(true));
            String userId = authRepository.getmAuth().getCurrentUser().getUid();
            Isbn isbnObject = new Isbn(isbnRemovedHyphens);
            userRepository.addBook(userId, isbnObject, addBookConsumer);
            Log.i(TAG, "Valid ISBN");
        }else {
            events.setValue(AddBookEvents.INVALID_ISBN);
            Log.i(TAG, "Invalid ISBN");
        }
    }

    private boolean isIsbnValid(String isbn){
        String trimmedIsbn = isbn.trim(); // remove whitespace
        return trimmedIsbn.length() == 10|| trimmedIsbn.length() == 13;
    }

    public LiveData<AddBookState> getState() {
        return state;
    }

    public MutableLiveData<AddBookEvents> getEvents() {
        return events;
    }

    // Called after each event is observed in the SignUp Fragment.
    public void eventSeen(){
        events.setValue(null);
    }
}