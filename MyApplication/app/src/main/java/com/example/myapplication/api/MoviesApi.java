package com.example.myapplication.api;

import android.util.Log;
import com.example.myapplication.entities.Movie;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoviesApi {
    private final WebServiceApi webServiceApi;
    private final Retrofit retrofit;

    public MoviesApi() {
        this.retrofit = RetrofitClient.getInstance();
        this.webServiceApi = retrofit.create(WebServiceApi.class);
    }

    // Alternate constructor for testing.
    public MoviesApi(Retrofit retrofit) {
        this.retrofit = retrofit;
        this.webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void getMovies(String token, MovieCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.getMoviesByCategory(authToken).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Movies fetched: " + response.body().size() + " categories");
                    callback.onSuccess(response.body());
                } else {
                    Log.e("API_ERROR", "Failed to fetch movies. Response Code: " + response.code());
                    callback.onError("Failed to fetch movies.");
                }
            }
            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Create a movie using a JSON payload.
     */
    public void createMovie(String token, JsonObject movie, MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.createMovie(authToken, movie).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie created successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to create movie. Response Code: " + response.code());
                    callback.onError("Failed to create movie.");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Update a movie using a JSON payload.
     *
     * @param token    The authentication token.
     * @param movieId  The ID of the movie to update.
     * @param movie    A JsonObject containing the updated fields.
     * @param callback The callback for success or error.
     */
    public void updateMovie(String token, String movieId, JsonObject movie, MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.updateMovie(authToken, movieId, movie).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie updated successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to update movie. Response Code: " + response.code());
                    callback.onError("Failed to update movie.");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void deleteMovie(String token, String movieId, MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.DeleteMovie(authToken, movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie deleted successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to delete movie. Response Code: " + response.code());
                    callback.onError("Failed to delete movie.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface MovieCallback {
        void onSuccess(Map<String, List<Movie>> moviesByCategory);
        void onError(String error);
    }

    public interface MovieActionCallback {
        void onSuccess();
        void onError(String error);
    }
}
