package com.example.myapplication.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;
import java.util.List;
import java.util.Map;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final LiveData<Map<String, List<Movie>>> moviesLiveData;

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
}

