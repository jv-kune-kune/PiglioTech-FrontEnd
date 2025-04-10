package com.northcoders.pigliotech_frontend.model.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GooglePingService {
    @GET("https://www.google.com")
    Call<Void> pingGoogle();
}