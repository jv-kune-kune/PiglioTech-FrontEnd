package com.northcoders.pigliotech_frontend.model.service;

import com.northcoders.pigliotech_frontend.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {

    @POST("user")
    Call<User> addUser(@Body User user);

    @GET("users/{id}")
    Call<User> getCurrentUser(@Path("id") String id);
}
