package com.example.myapplication.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;
import java.util.List;
import java.util.Map;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final LiveData<Map<String, List<Movie>>> moviesLiveData;
    private final MutableLiveData<Boolean> movieCreatedLiveData = new MutableLiveData<>();


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        moviesLiveData = movieRepository.getMoviesLiveData();
    }

    public void fetchMovies() {
        movieRepository.fetchMovies();
    }

    public LiveData<Map<String, List<Movie>>> getMovies() {
        return moviesLiveData;
    }

    public LiveData<Boolean> createMovie(Movie movie) {
        return movieRepository.createMovie(movie);
    }

    public LiveData<Boolean> updateMovie(Movie movie) {
        return movieRepository.updateMovie(movie);
    }

    public LiveData<Boolean> deleteMovie(Movie movie) {
        return movieRepository.deleteMovie(movie);
    }
}

