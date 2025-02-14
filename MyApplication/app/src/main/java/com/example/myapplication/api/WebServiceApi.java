package com.example.myapplication.api;

import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

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
    @Multipart
    @POST("api/users")
    Call<JsonObject> createUser(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("age") RequestBody age,
            @Part MultipartBody.Part profilePicture
    );


    // Get user by ID
    @GET("api/users/{id}")
    Call<JsonObject> getUser(@Path("id") String userId);

    @GET("api/users")
    Call<JsonObject> getUserDetails(@Header("Authorization") String token);

    //*Movies Routs*

    //Get Movies
    @GET("api/movies")
    Call<Map<String, List<Movie>>> getMoviesByCategory(@Header("Authorization") String token);

    //Add Movie by manager
    @POST("api/movies")
    Call<JsonObject> createMovie(@Header("Authorization") String token, @Body JsonObject movie);

    //Get Movie by ID
    @GET("api/movies/{id}")
    Call<Movie> getMovieById(@Path("id") String MovieId,@Header("Authorization") String token);

    //Edit Movie
    @PUT("api/movies/{id}")
    Call<JsonObject> updateMovie(@Header("Authorization") String token, @Path("id") String MovieId, @Body JsonObject movie);

    //Delete Movie
    @DELETE("api/movies/{id}")
    Call<JsonObject> DeleteMovie(@Header("Authorization") String token, @Path("id") String MovieId);

    //Get recommends movies
    @GET("api/movies/{id}/recommend")
    Call<List<Movie>> getRecommendsMovies(@Path("id") String MovieId, @Header("Authorization") String token);

    //Add Movie to watched by of the user
    @POST("api/movies/{id}/recommend")
    Call<JsonObject> addToWatchedBy(@Path("id") String MovieId, @Header("Authorization") String token);
    //Get all movies
    @GET("api/all/movies")
    Call<List<Movie>> getAllMovies(@Header("Authorization") String token);

    //*Category Routs*//

    //get all categories
    @GET("api/categories")
    Call<List<Category>> getAllCategories(@Header("Authorization") String token);
    //create categories
    @POST("api/categories")
    Call<JsonObject> createCategory(@Header("Authorization") String token,@Body JsonObject category);
    //get categoryById
    @GET("api/categories/{id}")
    Call<Category> getCategoryById(@Header("Authorization") String token,@Path("id") String id);
    //update categoryById
    @PATCH("api/categories/{id}")
    Call<JsonObject> updateCategoryById(@Header("Authorization") String token,@Path("id") String id,@Body JsonObject category);
    //delete categoryById
    @DELETE("api/categories/{id}")
    Call<JsonObject> deleteCategoryById(@Header("Authorization") String token,@Path("id") String id);
}
