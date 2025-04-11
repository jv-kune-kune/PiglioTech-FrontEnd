package com.northcoders.pigliotech_frontend.data.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GooglePingService {
    @GET("https://www.google.com")
    Call<Void> pingGoogle();
}