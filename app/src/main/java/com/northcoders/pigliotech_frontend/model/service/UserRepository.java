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
import java.util.function.IntConsumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private final UserApiService userApiService;

    public UserRepository() {

        this.userApiService = RetrofitInstance.getService();
    }

    public void addUser(User user, IntConsumer addUserConsumer) {

        Call<User> call = userApiService.addUser(user);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                addUserConsumer.accept(response.code());
                if (response.code() == 201 && response.body() != null) {
                    Log.i(TAG, "ADDED USER: " + response.body());
                } else {
                    Log.e(TAG, "USER NOT ADDED: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                addUserConsumer.accept(-1);
                Log.e(TAG, "ADD USER NETWORK FAILURE", t);
            }
        });
    }

    public void getUser(String id, Consumer<User> getUserConsumer) {

        Call<User> call = userApiService.getCurrentUser(id);
        Log.i(TAG, "Requested User Id: " + id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable
                    t) {
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

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
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
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                usersByRegionConsumer.accept(null);
                Log.e(
                        TAG,
                        "GET USER BY REGION: Network failure",
                        t
                );
            }
        });
    }

    public void addBook(String userId, Isbn isbn, IntConsumer addBookConsumer) {
        Call<User> call = userApiService.addBook(userId, isbn);
        Log.i(TAG, "addBook Called, userId: " + userId + ", ISBN: " + isbn);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                addBookConsumer.accept(response.code());
                if (response.code() == 201 && response.body() != null) {
                    Log.i(TAG, "ADD BOOK: " + response.body());
                } else {
                    Log.e(TAG, "ADD BOOK: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                addBookConsumer.accept(-1);
                Log.e(TAG, "ADD BOOK NETWORK FAILURE", t);
            }
        });
    }

    public void deleteBook(String userId, String isbnString, IntConsumer deleteBookConsumer) {
        Call<Void> call = userApiService.deleteBook(userId, isbnString);
        Log.i(TAG, "deleteBook Called, userId: " + userId + ", ISBN: " + isbnString);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                deleteBookConsumer.accept(response.code());
                if (response.code() == 204) {
                    Log.i(TAG, "DELETE BOOK: Successful");
                } else {
                    Log.e(TAG, "DELETE BOOK: Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                deleteBookConsumer.accept(-1);
                Log.e(TAG, "DELETE BOOK NETWORK FAILURE", t);
            }
        });
    }


    public void createSwapRequest(SwapRequest swapRequest, IntConsumer likeBookConsumer) {
        Call<SwapRequest> call = userApiService.createSwapRequest(swapRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SwapRequest> call, @NonNull Response<SwapRequest> response) {
                likeBookConsumer.accept(response.code());
                if (response.code() == 201 && response.body() != null) {
                    Log.i(TAG, "CREATED SWAP REQUEST: " + response.body());
                } else if (response.code() == 409) {
                    Log.i(TAG, "SWAP REQUEST ALREADY EXISTS: " + response.code());
                } else {
                    Log.e(TAG, "SWAP REQUEST NOT CREATED: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SwapRequest> call, @NonNull Throwable t) {
                likeBookConsumer.accept(-1);
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
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
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
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                usersMatchesConsumer.accept(null);
                Log.e(
                        TAG,
                        "GET USER MATCHES: Network failure",
                        t
                );
            }
        });
    }

    public void dismissMatch(SwapDismissal swapDismissal, IntConsumer dismissMatchConsumer) {
        Call<Void> call = userApiService.dismissMatch(swapDismissal);
        Log.i(TAG, "dismissMatch Called : " + swapDismissal);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                dismissMatchConsumer.accept(response.code());
                if (response.code() == 204) {
                    Log.i(TAG, "DISMISS MATCH: Successful");
                } else {
                    Log.e(TAG, "DISMISS MATCH: Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                dismissMatchConsumer.accept(-1);
                Log.e(TAG, "DISMISS MATCH NETWORK FAILURE", t);
            }
        });
    }
}