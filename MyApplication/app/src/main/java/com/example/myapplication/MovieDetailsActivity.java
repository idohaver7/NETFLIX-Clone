package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView movieImage, playButton, backButton;
    private TextView movieTitle, movieDescription;
    private RecyclerView relatedMoviesRecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> relatedMoviesList = new ArrayList<>();
    private MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Initialize UI Components
        movieImage = findViewById(R.id.movieImage);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        playButton = findViewById(R.id.playButton);
        backButton = findViewById(R.id.backButton);
        relatedMoviesRecyclerView = findViewById(R.id.relatedMoviesRecyclerView);

        // Handle Back Button
        backButton.setOnClickListener(view -> finish());

        // Get movie data from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("movie")) {
            Movie movie = intent.getParcelableExtra("movie");

            if (movie != null) {
                movieTitle.setText(movie.getTitle());
                movieDescription.setText(movie.getDescription());

                // Load movie image (Full Width)
                String imagePath = "file:///android_asset/movies/image/" + movie.getImage();
                Glide.with(this)
                        .load(imagePath)
                        .placeholder(R.drawable.placeholder_movie)
                        .error(R.drawable.placeholder_movie)
                        .into(movieImage);

//                // Play Button Click Action (Placeholder)
//                playButton.setOnClickListener(v -> {
//                    Intent videoIntent = new Intent(MovieDetailsActivity.this, VideoPlayerActivity.class);
//                    videoIntent.putExtra("videoUrl", movie.getVideo());
//                    startActivity(videoIntent);
//                });

                // Fetch related movies
                loadRelatedMovies(movie);
            }
        }
    }

    private void loadRelatedMovies(Movie selectedMovie) {
        movieRepository = new MovieRepository(this);
        movieRepository.getMoviesLiveData().observe(this, moviesByCategory -> {
            relatedMoviesList.clear();
            if (moviesByCategory != null) {
                for (Map.Entry<String, List<Movie>> entry : moviesByCategory.entrySet()) {
                    for (Movie movie : entry.getValue()) {
                        if (!movie.getId().equals(selectedMovie.getId())) { // Exclude the current movie
                            relatedMoviesList.add(movie);
                        }
                    }
                }
            }

            // Set up horizontal RecyclerView
            movieAdapter = new MovieAdapter(this, relatedMoviesList);
            relatedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            relatedMoviesRecyclerView.setAdapter(movieAdapter);
        });

        movieRepository.fetchMovies();
    }
}
