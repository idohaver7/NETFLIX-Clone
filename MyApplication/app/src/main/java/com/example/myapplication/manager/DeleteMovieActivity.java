package com.example.myapplication.manager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.DeleteMovieAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.viewModel.MovieViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteMovieActivity extends AppCompatActivity implements DeleteMovieAdapter.OnMovieDeleteListener {

    private RecyclerView recyclerViewMovies;
    private ImageView backButton;
    private DeleteMovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_movie);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeleteMovieAdapter(movieList, this);
        recyclerViewMovies.setAdapter(adapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Use getMovies() from MovieViewModel which returns LiveData<Map<String, List<Movie>>>
        movieViewModel.getMovies().observe(this, moviesByCategory -> {
            movieList.clear();
            if (moviesByCategory != null) {
                // Iterate over the map's values.
                for (List<Movie> movies : moviesByCategory.values()) {
                    movieList.addAll(movies);
                }
            }
            adapter.updateMovies(movieList);
        });
        movieViewModel.fetchMovies();
    }

    @Override
    public void onMovieDelete(Movie movie, int position) {
        movieViewModel.deleteMovie(movie).observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(DeleteMovieActivity.this, "Movie deleted", Toast.LENGTH_SHORT).show();
                movieList.remove(position);
                adapter.updateMovies(movieList);
            } else {
                Toast.makeText(DeleteMovieActivity.this, "Failed to delete movie", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
