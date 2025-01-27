package com.northcoders.pigliotech_frontend.model.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.northcoders.pigliotech_frontend.model.Isbn;
import com.northcoders.pigliotech_frontend.model.Match;
import com.northcoders.pigliotech_frontend.model.SwapDismissal;
import com.northcoders.pigliotech_frontend.model.SwapRequest;
import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final String TAG = "UserRepository";
    private final UserApiService userApiService;

    public UserRepository() {

        this.userApiService = RetrofitInstance.getService();
    }

    public void addUser(User user, Consumer<Integer> addUserConsumer) {

        Call<User> call = userApiService.addUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201 && response.body() != null) {
                    addUserConsumer.accept(response.code());
                    Log.i(TAG, "ADDED USER: " + response.body());
                } else {
                    addUserConsumer.accept(response.code());
                    Log.e(TAG, "USER NOT ADDED: " + (response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                addUserConsumer.accept(null);
                Log.e(TAG, "ADD USER NETWORK FAILURE", t);
            }
        });
    }

    public void getUser(String id, Consumer<User> getUserConsumer) {

        Call<User> call = userApiService.getCurrentUser(id);
        Log.i(TAG, "Requested User Id: " + id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200 && response.body() != null) {
                    User user = response.body();
                    getUserConsumer.accept(user);
                    Log.i(
                            TAG,
                            "GET USER: Successfully retrieved " + response.body().toString()
                    );
                } else {
                    getUserConsumer.accept(null);
                    Log.e(
                            TAG,
                            "GET USER: Unsuccessful response code" + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getUserConsumer.accept(null);
                Log.e(
                        TAG,
                        "GET USER: Network failure",
                        t
                );
            }
        });
    }

    public void getUsersByRegion(String regionEnum,
                                 String currentUserId,
                                 Consumer<List<User>> usersByRegionConsumer
    ) {

        Call<List<User>> call = userApiService.getUsersByRegion(regionEnum, currentUserId);
        Log.i(TAG, "getUsersByRegion Called, Region: " + regionEnum + ", UserID: " + currentUserId);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<User> usersByRegion = response.body();
                    usersByRegionConsumer.accept(usersByRegion);
                    Log.i(
                            TAG,
                            "GET USERS BY REGION: Successfully retrieved " + response.body().toString()
                    );
                } else {
                    usersByRegionConsumer.accept(null);
                    Log.e(
                            TAG,
                            "GET USERS BY REGION: NOT retrieved " + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                usersByRegionConsumer.accept(null);
                Log.e(
                        TAG,
                        "GET USER BY REGION: Network failure",
                        t
                );
            }
        });
    }

    public void addBook(String userId, Isbn isbn, Consumer<Integer> addBookConsumer) {

        Call<User> call = userApiService.addBook(userId, isbn);
        Log.i(TAG, "addBook Called, userId: " + userId + ", ISBN: " + isbn);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201 && response.body() != null) {
                    addBookConsumer.accept(response.code());
                    Log.i(TAG, "ADD BOOK: " + response.body());
                } else {
                    addBookConsumer.accept(response.code());
                    Log.e(TAG, "ADD BOOK: " + (response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                addBookConsumer.accept(null);
                Log.e(TAG, "ADD USER NETWORK FAILURE", t);
            }
        });
    }

    public void deleteBook(String userId, String isbnString, Consumer<Integer> deleteBookConsumer) {

        Call<Void> call = userApiService.deleteBook(userId, isbnString);
        Log.i(TAG, "deleteBook Called, userId: " + userId + ", ISBN: " + isbnString);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 204) {
                    deleteBookConsumer.accept(response.code());
                    Log.i(TAG, "DELETE BOOK: Successful");
                } else {
                    deleteBookConsumer.accept(response.code());
                    Log.e(TAG, "DELETE BOOK: Failed: " + (response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                deleteBookConsumer.accept(null);
                Log.e(TAG, "ADD USER NETWORK FAILURE", t);
            }
        });
    }

    public void createSwapRequest(SwapRequest swapRequest, Consumer<Integer> likeBookConsumer) {

        Call<SwapRequest> call = userApiService.createSwapRequest(swapRequest);

        call.enqueue(new Callback<SwapRequest>() {
            @Override
            public void onResponse(Call<SwapRequest> call, Response<SwapRequest> response) {
                if (response.code() == 201 && response.body() != null) {
                    likeBookConsumer.accept(response.code());
                    Log.i(TAG, "CREATED SWAP REQUEST: " + response.body());
                } else if (response.code() == 409) {
                    likeBookConsumer.accept(response.code());
                    Log.i(TAG, "SWAP REQUEST ALREADY EXISTS: " + (response.code()));
                } else {
                    likeBookConsumer.accept(response.code());
                    Log.e(TAG, "SWAP REQUEST NOT CREATED: " + (response.code()));
                }
            }

            @Override
            public void onFailure(Call<SwapRequest> call, Throwable t) {
                likeBookConsumer.accept(null);
                Log.e(TAG, "SWAP REQUEST NETWORK FAILURE", t);
            }
        });
    }

    public void getMatchesForCurrentUser(
            String currentUserId,
            Consumer<List<Match>> usersMatchesConsumer
    ) {

        Call<List<Match>> call = userApiService.getMatches(currentUserId);
        Log.i(TAG, "getMatches UserID: " + currentUserId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<Match> usersMatches = response.body();
                    usersMatchesConsumer.accept(usersMatches);
                    Log.i(
                            TAG,
                            "GET USERS MATCHES: Successfully retrieved " + response.body().toString()
                    );
                } else {
                    usersMatchesConsumer.accept(null);
                    Log.e(
                            TAG,
                            "GET USERS MATCHES: Successfully retrieved " + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                usersMatchesConsumer.accept(null);
                Log.e(
                        TAG,
                        "GET USER MATCHES: Network failure",
                        t
                );
            }
        });
    }

    public void dismissMatch(SwapDismissal swapDismissal, Consumer<Integer> dismissMatchConsumer) {

        Call<Void> call = userApiService.dismissMatch(swapDismissal);
        Log.i(TAG, "dismissMatch Called : " + swapDismissal);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 204) {
                    dismissMatchConsumer.accept(response.code());
                    Log.i(TAG, "DISMISS MATCH: Successful");
                } else {
                    dismissMatchConsumer.accept(response.code());
                    Log.e(TAG, "DISMISS MATCH: Failed: " + (response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                dismissMatchConsumer.accept(null);
                Log.e(TAG, "DISMISS MATCH NETWORK FAILURE", t);
            }
        });
    }
}