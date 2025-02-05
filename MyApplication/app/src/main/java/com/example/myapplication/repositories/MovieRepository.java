package com.example.myapplication.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.api.MoviesApi;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.WebServiceApi;
import com.example.myapplication.entities.Movie;
import java.util.List;
import java.util.Map;

public class MovieRepository {
    private final MoviesApi moviesApi;
    private final Context context;
    private final MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();

    public MovieRepository(Context context) {
        this.context = context;

        // Get WebServiceApi instance from RetrofitClient
        WebServiceApi webServiceApi = RetrofitClient.getInstance().create(WebServiceApi.class);
        this.moviesApi = new MoviesApi(webServiceApi);
    }

    public MutableLiveData<Map<String, List<Movie>>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public void fetchMovies() {
        String token = getAuthToken();
        if (token == null || token.isEmpty()) {
            Log.e("MOVIE_REPO", "Token is missing! Cannot fetch movies.");
            return;
        }

        Log.d("MOVIE_REPO", "Fetching movies with token: " + token);

        // Use the MovieCallback to get the movies grouped by category
        moviesApi.getMovies(token, new MoviesApi.MovieCallback() {
            @Override
            public void onSuccess(Map<String, List<Movie>> moviesByCategory) {
                moviesLiveData.postValue(moviesByCategory); // Post the data to LiveData
                Log.d("MOVIE_REPO", "Movies loaded successfully with categories: " + moviesByCategory.size());
            }

            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Error fetching movies: " + error);
            }
        });
    }


    private String getAuthToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", "");
    }
}


