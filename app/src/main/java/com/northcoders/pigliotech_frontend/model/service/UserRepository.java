package com.northcoders.pigliotech_frontend.model.service;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.northcoders.pigliotech_frontend.model.User;

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
                    Log.i("ADD USER", response.body().toString());
                }else {
                    Log.e("ADD USER", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("ADD USER", t.getMessage());
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
}
