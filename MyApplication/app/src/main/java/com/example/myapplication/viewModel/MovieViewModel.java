package com.example.myapplication.viewModel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final LiveData<Map<String, List<Movie>>> moviesLiveData;
    private final LiveData<List<Movie>> justMovies;
    private final MutableLiveData<Boolean> movieCreatedLiveData = new MutableLiveData<>();


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        moviesLiveData = movieRepository.getMoviesLiveData();
        justMovies = movieRepository.getJustMoviesLiveData();

    }

    public void fetchMovies() {
        movieRepository.fetchMovies();
    }

    public void addMovieToWatchedBy(String movieId) {
        movieRepository.addMovieToWatchedBy(movieId);
    }

    public LiveData<Map<String, List<Movie>>> getMovies() {
        return moviesLiveData;
    }

    public LiveData<List<Movie>> getRecommendedMovies() {
        return justMovies;
    }

    public void getRecommendedMovies(String movieId) {
        movieRepository.getRecommendedMovies(movieId);
    }
    public LiveData<Boolean> createMovie(Movie movie, Uri videoUri, Uri imageUri) {
        return movieRepository.createMovie(movie, videoUri, imageUri);
    }


    public LiveData<Boolean> updateMovie(Movie movie) {
        return movieRepository.updateMovie(movie);
    }

    public LiveData<Boolean> deleteMovie(Movie movie) {
        return movieRepository.deleteMovie(movie);
    }

}


