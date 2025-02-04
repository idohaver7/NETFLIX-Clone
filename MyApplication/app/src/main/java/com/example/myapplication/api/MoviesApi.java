package com.example.myapplication.api;

import android.util.Log;
import com.example.myapplication.entities.Movie;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesApi {
    private final WebServiceApi api;

    public MoviesApi(WebServiceApi api) {
        this.api = api;
    }

    public void getMovies(String token, MovieCallback callback) {
        api.getMoviesByCategory("Bearer " + token).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Movies fetched: " + response.body().size() + " categories");

                    // Send the entire category structure
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




    // ✅ Add Movie
    public void createMovie(String token, JsonObject movie, MovieActionCallback callback) {
        api.createMovie("Bearer " + token, movie).enqueue(new Callback<JsonObject>() {
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

    // ✅ Callbacks for API Responses
    public interface MovieCallback {
        void onSuccess(Map<String, List<Movie>> moviesByCategory);  // Expect Map<String, List<Movie>>
        void onError(String error);
    }

    public interface MovieActionCallback {
        void onSuccess();
        void onError(String error);
    }
}
