package com.example.myapplication.api;

import com.example.myapplication.entities.Movie;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface WebServiceApi {
    //USERS Routs:

    //Route to the authentication user
    @POST("api/tokens")
    Call<JsonObject> login(@Body JsonObject credentials);

    // Create a new user
    @POST("api/users")
    Call<JsonObject> createUser(@Body JsonObject user);

    // Get user by ID
    @GET("api/users/{id}")
    Call<JsonObject> getUser(@Path("id") String userId);

    //*Movies Routs*

    //Get Movies
    @GET("api/movies")
    Call<List<Movie>> getAllMovies(@Header("Authorization") String token);

    //Add Movie by manager
    @POST("api/movies")
    Call<JsonObject> createMovie(@Header("Authorization") String token, @Body Movie Movie);

    //Get Movie by ID
    @GET("api/movies/{id}")
    Call<Movie> getMovieById(@Path("id") String MovieId,@Header("Authorization") String token);

    //Edit Movie
    @PUT("api/movies/{id}")
    Call<JsonObject> editMovie(@Path("id") String MovieId, @Header("Authorization") String token, @Body Movie movie);

    //Delete Movie
    @DELETE("api/movies/{id}")
    Call<JsonObject> DeleteMovie(@Path("id") String MovieId, @Header("Authorization") String token);

    //Get recommends movies
    @GET("api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendsMovies(@Path("id") String MovieId, @Header("Authorization") String token);

    //Add Movie to watched by of the user
    @POST("api/movies/{id}/recommend")
    Call<List<Movie>> addToWatchedBy(@Path("id") String MovieId, @Header("Authorization") String token);






}
