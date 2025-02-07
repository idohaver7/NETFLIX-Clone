package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.repositories.MovieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchMovieActivity extends AppCompatActivity {
    private EditText searchInput;
    private ImageView backButton;
    private RecyclerView searchResultsRecyclerView;
    private MovieAdapter movieAdapter;
    private MovieRepository movieRepository;
    private List<Movie> allMoviesList = new ArrayList<>();
    private List<Movie> filteredMoviesList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        searchInput = findViewById(R.id.searchInput);
        backButton = findViewById(R.id.backButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanCount(3);
        searchResultsRecyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this, filteredMoviesList);
        searchResultsRecyclerView.setAdapter(movieAdapter);

        backButton.setOnClickListener(view -> finish());

        movieRepository = new MovieRepository(this);
        observeMovies();

        // 🔎 Handle Real-Time Search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterMovies();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        searchInput.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (searchInput.getRight() - searchInput.getCompoundDrawables()[2].getBounds().width())) {
                    searchInput.setText("");
                    v.performClick();
                    return true;
                }
            }
            return false;
        });

    }

    private void observeMovies() {
        movieRepository.getMoviesLiveData().observe(this, moviesByCategory -> {
            allMoviesList.clear();
            if (moviesByCategory != null) {
                for (List<Movie> categoryMovies : moviesByCategory.values()) {
                    allMoviesList.addAll(categoryMovies);
                }
            }
        });

        movieRepository.fetchMovies();
    }

    private void filterMovies() {
        String query = searchInput.getText().toString().trim().toLowerCase();
        filteredMoviesList.clear();

        for (Movie movie : allMoviesList) {
            if (movie.getTitle().toLowerCase().contains(query)) {
                filteredMoviesList.add(movie);
            }
        }

        movieAdapter.notifyDataSetChanged();
    }
}
