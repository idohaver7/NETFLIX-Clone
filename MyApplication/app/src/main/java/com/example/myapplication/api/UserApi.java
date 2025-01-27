package com.example.myapplication.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("api/tokens")
    Call<JsonObject> login(@Body JsonObject credentials);

    @POST("api/users")
    Call<JsonObject> createUser(@Body JsonObject user);

    @GET("api/users/{id}")
    Call<JsonObject> getUser(@Path("id") String userId);
}
