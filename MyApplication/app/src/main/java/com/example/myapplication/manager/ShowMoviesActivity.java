package com.example.myapplication.manager;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.viewModel.MovieViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movies);

        // Initialize header views
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish()); // Go back on click

        // Setup RecyclerView with a 2-column grid layout
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3));

        // Initialize adapter with an empty list and set it on the RecyclerView
        movieAdapter = new MovieAdapter(this, new ArrayList<>());
        recyclerViewMovies.setAdapter(movieAdapter);

        // Obtain the MovieViewModel and fetch movies
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.fetchMovies();

        // Observe the LiveData<Map<String, List<Movie>>>
        movieViewModel.getMovies().observe(this, moviesByCategory -> {
            List<Movie> movieList = new ArrayList<>();
            if (moviesByCategory != null) {
                // Flatten the Map structure to a single list of movies
                for (List<Movie> movies : moviesByCategory.values()) {
                    movieList.addAll(movies);
                }
            }
            // Update the adapter with the new list of movies.
            // You can either update the data in the adapter or recreate it.
            movieAdapter = new MovieAdapter(ShowMoviesActivity.this, movieList);
            recyclerViewMovies.setAdapter(movieAdapter);
        });
    }
}
