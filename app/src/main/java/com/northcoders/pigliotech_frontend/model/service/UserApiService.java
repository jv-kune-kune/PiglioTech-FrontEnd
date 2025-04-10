package com.northcoders.pigliotech_frontend.model.service;

import com.northcoders.pigliotech_frontend.model.Isbn;
import com.northcoders.pigliotech_frontend.model.Match;
import com.northcoders.pigliotech_frontend.model.SwapDismissal;
import com.northcoders.pigliotech_frontend.model.SwapRequest;
import com.northcoders.pigliotech_frontend.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApiService {

    @GET("ping")
    Call<Void> pingBackend();

    @POST("users")
    Call<User> addUser(@Body User user);

    @GET("users/{id}")
    Call<User> getCurrentUser(@Path("id") String id);

    @GET("users")
    Call<List<User>> getUsersByRegion(@Query("region") String regionEnum,
                                      @Query("exclude") String currentUserId
    );

    @POST("users/{id}/books")
    Call<User> addBook(@Path("id") String userId, @Body Isbn isbn);

    @DELETE("users/{id}/books/{isbn}")
    Call<Void> deleteBook(@Path("id") String userID, @Path("isbn") String isbnString);

    @POST("swaps")
    Call<SwapRequest> createSwapRequest(@Body SwapRequest swapRequest);

    @GET("swaps")
    Call<List<Match>> getMatches(@Query("userId") String currentUserId);

    @POST("swaps/dismiss")
    Call<Void> dismissMatch(@Body SwapDismissal swapDismissal);
}