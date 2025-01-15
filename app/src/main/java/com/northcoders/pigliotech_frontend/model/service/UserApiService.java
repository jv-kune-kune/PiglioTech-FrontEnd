package com.northcoders.pigliotech_frontend.model.service;

import com.northcoders.pigliotech_frontend.model.Book;
import com.northcoders.pigliotech_frontend.model.Isbn;
import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiService {

    @POST("users")
    Call<User> addUser(@Body User user);

    @GET("users/{id}")
    Call<User> getCurrentUser(@Path("id") String id);

    @GET("users")
    Call<List<User>> getUsersByRegion(@Query("region") String regionEnum,
                                      @Query("exclude") String currentUserId
    );

    @POST("{id}/books")
    Call<User> addBook(@Path("id") String userId, @Body Isbn isbn);
}