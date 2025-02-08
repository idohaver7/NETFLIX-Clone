package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.databinding.ActivityMovieDetailsBinding;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;
import com.example.myapplication.viewModel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView movieImage;
    private Button playBtn;
    private ActivityMovieDetailsBinding binding;
    private TextView movieTitle, movieDescription;
    private RecyclerView relatedMoviesRecyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> relatedMoviesList = new ArrayList<>();
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        // Initialize UI Components
        movieImage = binding.imageView;
        movieTitle = binding.movieName;
        movieDescription = binding.videoDescription;
        playBtn = binding.playBtn;
        relatedMoviesRecyclerView =binding.lstRecommendedMovies;
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
//
//                // Initialize ViewModel
                movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
//                }
                playBtn.setOnClickListener(v -> {
                    movieViewModel.addMovieToWatchedBy(movie.getId());
                    Intent videoIntent = new Intent(MovieDetailsActivity.this, FullScreenMovieActivity.class);
                    videoIntent.putExtra("videoUrl", movie.getVideo());
                    startActivity(videoIntent);
                });
                loadRelatedMovies(movie);

            }
        }
    }

    private void loadRelatedMovies(Movie selectedMovie) {
        movieViewModel.getRecommendedMovies().observe(this, recommendedMovies -> {
            relatedMoviesList.clear();
            if (recommendedMovies != null) {
                relatedMoviesList.addAll(recommendedMovies);

                // Set up horizontal RecyclerView
                movieAdapter = new MovieAdapter(this, relatedMoviesList);
                relatedMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
                relatedMoviesRecyclerView.setAdapter(movieAdapter);
            }
        });
        movieViewModel.getRecommendedMovies(selectedMovie.getId());
    }
}
