package com.example.myapplication.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.api.MoviesApi;
import com.example.myapplication.daoes.MovieDao;
import com.example.myapplication.databases.MovieDB;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.globals.GlobalToken;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class MovieRepository {
    private final MovieDao movieDao;
    private final MoviesApi moviesApi;
    // LiveData holding movies grouped by category.
    private final MutableLiveData<Map<String, List<Movie>>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> justMoviesLiveData=new MutableLiveData<>();

    public MovieRepository(Context context) {
        MovieDB db = MovieDB.getInstance(context);
        this.moviesApi = new MoviesApi(context);
        this.movieDao = db.movieDao();
    }

    public MutableLiveData<Map<String, List<Movie>>> fetchMovies() {
        if (GlobalToken.token == null || GlobalToken.token.isEmpty()) {
            Log.e("MOVIE_REPO", "Token is missing! Cannot fetch movies.");
            return moviesLiveData;
        }
        Log.d("MOVIE_REPO", "Fetching movies with token: " + GlobalToken.token);
        moviesApi.getMovies(GlobalToken.token, new MoviesApi.MovieCallback() {
            @Override
            public void onSuccess(Map<String, List<Movie>> moviesByCategory) {
                moviesLiveData.postValue(moviesByCategory);
                // Save movies in a background thread.
                Executors.newSingleThreadExecutor().execute(() -> {
                    for (List<Movie> movies : moviesByCategory.values()) {
                        movieDao.insert(movies.toArray(new Movie[0]));
                    }
                });
                Log.d("MOVIE_REPO", "Movies saved in DB.");
            }

            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Error fetching movies: " + error);
            }
        });
        return moviesLiveData;
    }
    public MutableLiveData<List<Movie>> getRecommendedMovies(String movieId) {
        if (GlobalToken.token == null || GlobalToken.token.isEmpty()) {
            Log.e("MOVIE_REPO", "Token is missing! Cannot fetch movies.");
            return justMoviesLiveData;
        }

        Log.d("MOVIE_REPO", "Fetching movies with token: " + GlobalToken.token);

        moviesApi.getRecommendedMovies(GlobalToken.token,movieId, new MoviesApi.MovieListCallback() {
            @Override
            public void onSuccess(List<Movie> recommendedMovies) {
                justMoviesLiveData.postValue(recommendedMovies);
                Log.d("MOVIE_REPO", "Movies saved in DB.");
            }

            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Error fetching movies: " + error);
            }
        });

        return justMoviesLiveData;
    }
    public void addMovieToWatchedBy(String movieId){
        if (GlobalToken.token == null || GlobalToken.token.isEmpty()) {
            Log.e("MOVIE_REPO", "Token is missing! Cannot fetch movies.");
        }
        Log.d("MOVIE_REPO", "Fetching movies with token: " + GlobalToken.token);
        moviesApi.addMovieToWatchList(GlobalToken.token, movieId, new MoviesApi.MovieActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("MOVIE_REPO", "add movie to watched by.");
            }

            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Error add movie to watched by: " + error);

            }
        });
    }



    public MutableLiveData<Boolean> createMovie(Movie movie, Uri videoUri, Uri imageUri) {
        MutableLiveData<Boolean> isCreatedLiveData = new MutableLiveData<>();
        String token = GlobalToken.token;
        // Call the new multipart method.
        moviesApi.createMovieMultipart(token,
                movie.getTitle(),
                movie.getCategory().getId(),
                movie.getDescription(),
                videoUri,
                imageUri,
                new MoviesApi.MovieActionCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("MOVIE_REPO", "Movie created successfully!");
                        Executors.newSingleThreadExecutor().execute(() -> {
                                movieDao.insert(movie);
                        });
                        isCreatedLiveData.postValue(true);
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("MOVIE_REPO", "Movie creation failed: " + error);
                        isCreatedLiveData.postValue(false);
                    }
                });
        return isCreatedLiveData;
    }


    // New updateMovie() method.
    public MutableLiveData<Boolean> updateMovie(Movie movie) {
        MutableLiveData<Boolean> isUpdatedLiveData = new MutableLiveData<>();
        // Build JSON payload for update. Do not include the "id" key.
        JsonObject movieJson = new JsonObject();
        movieJson.addProperty("title", movie.getTitle());
        movieJson.addProperty("category", movie.getCategory().getId());
        movieJson.addProperty("video", movie.getVideo());
        movieJson.addProperty("description", movie.getDescription());
        movieJson.addProperty("image", movie.getImage());
        Log.d("MOVIE_UPDATE_JSON", movieJson.toString());
        moviesApi.updateMovie(GlobalToken.token, movie.getId(), movieJson, new MoviesApi.MovieActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("MOVIE_REPO", "Movie updated successfully!");
                Executors.newSingleThreadExecutor().execute(() -> {
                    movieDao.update(movie);
                });
                isUpdatedLiveData.postValue(true);
            }
            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Movie update failed: " + error);
                isUpdatedLiveData.postValue(false);
            }
        });
        return isUpdatedLiveData;
    }

    public MutableLiveData<Boolean> deleteMovie(Movie movie) {
        MutableLiveData<Boolean> isDeletedLiveData = new MutableLiveData<>();
        moviesApi.deleteMovie(GlobalToken.token, movie.getId(), new MoviesApi.MovieActionCallback() {
            @Override
            public void onSuccess() {
                Log.d("MOVIE_REPO", "Movie deleted successfully!");
                // Optionally, remove from local DB in a background thread.
                Executors.newSingleThreadExecutor().execute(() -> movieDao.delete(movie));
                isDeletedLiveData.postValue(true);
            }
            @Override
            public void onError(String error) {
                Log.e("MOVIE_REPO", "Movie deletion failed: " + error);
                isDeletedLiveData.postValue(false);
            }
        });
        return isDeletedLiveData;
    }

    public MutableLiveData<Map<String, List<Movie>>> getMoviesLiveData() {
        return moviesLiveData;
    }
    public MutableLiveData<List<Movie>> getJustMoviesLiveData(){return justMoviesLiveData;}
}
