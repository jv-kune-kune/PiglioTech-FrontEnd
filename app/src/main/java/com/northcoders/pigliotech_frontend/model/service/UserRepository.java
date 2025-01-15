package com.northcoders.pigliotech_frontend.model.service;

import android.util.Log;

import com.northcoders.pigliotech_frontend.model.Isbn;
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

    public void addUser(User user){

        Call<User> call = userApiService.addUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201 && response.body() != null){
                    Log.i(TAG, "ADD USER: " +response.body());
                }else {
                    Log.e(TAG, "ADD USER: " + (response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "ADD USER NETWORK FAILURE", t);
            }
        });
    }

    public void getUser(String id, Consumer<User> userConsumer){

        Call<User> call = userApiService.getCurrentUser(id);
        Log.i(TAG, "Requested User Id: "+ id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200 && response.body() != null){
                    User user = response.body();
                    userConsumer.accept(user);
                    Log.i(
                            TAG,
                            "GET USER: Successfully retrieved " + response.body().toString()
                    );
                }else {
                    userConsumer.accept(null);
                    Log.e(
                            TAG,
                            "GET USER: Unsuccessful response code" + response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userConsumer.accept(null);
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
    ){

        Call<List<User>> call = userApiService.getUsersByRegion(regionEnum, currentUserId);
        Log.i(TAG, "getUsersByRegion Called, Region: " + regionEnum + ", UserID: " + currentUserId);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200 && response.body() != null){
                    List<User> usersByRegion = response.body();
                    usersByRegionConsumer.accept(usersByRegion);
                    Log.i(
                            TAG,
                            "GET USERS BY REGION: Successfully retrieved " + response.body().toString()
                    );
                }else {
                    usersByRegionConsumer.accept(null);
                    Log.e(
                            TAG,
                            "GET USERS BY REGION: Successfully retrieved "+ response.code()
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

    public void addBook(String userId, Isbn isbn, Consumer<Integer> addBookConsumer){

        Call<User> call = userApiService.addBook(userId, isbn);
        Log.i(TAG, "addBook Called, userId: " + userId + ", ISBN: " + isbn);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201 && response.body() != null){
                    addBookConsumer.accept(response.code());
                    Log.i(TAG, "ADD BOOK: " +response.body());
                }else {
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
}