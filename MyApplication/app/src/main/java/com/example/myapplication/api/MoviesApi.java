package com.example.myapplication.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApi {
    Retrofit retrofit;
    WebServiceApi webServiceApi;

    public MoviesApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceApi = retrofit.create(WebServiceApi.class);

    }

    public void getMoviesByCategories(MutableLiveData<List<Movie>> movies, String token) {
        Call<List<Movie>> call = webServiceApi.getMoviesByCategory(token);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    movies.postValue(response.body());
                    Log.d("getMoviesByCategory", "succeeded to fetch movies: ");
                } else {
                    Log.e("API Error", "Failed to fetch movies");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });

    }

    public void getVideoRecommendations(String token, String movieId, MutableLiveData<List<Movie>> recommendedVideos) {
        Call<List<Movie>> call = webServiceApi.getRecommendsMovies(movieId, token);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<List<Movie>> call, @NonNull Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Get the recommended videos from the response
                    recommendedVideos.postValue(response.body());
                    Log.d("VideoAPI", "Succeeded to fetch recommended movies.");
                } else {
                    // Handle error and log details
                    Log.e("API Error", "Failed to fetch recommended movies with response code: " + response.code());
                    try {
                        Log.e("MovieAPI", "Error body: " + (response.errorBody() != null ? response.errorBody().string() : "No error body"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Movie>> call, @NonNull Throwable t) {
                Log.e("API Error", "Failed to fetch recommended videos: " + t.getMessage());
            }
        });
    }

    public void getMovieById(String movieID, String token, MutableLiveData<Movie> movie) {
        Call<Movie> call = webServiceApi.getMovieById(movieID, token);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("API Error", "Failed to getMovie: " + t.getMessage());

            }
        });
    }

}