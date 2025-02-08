package com.example.myapplication.manager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.UpdateMovieListAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.viewModel.MovieViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateMovieListActivity extends AppCompatActivity implements UpdateMovieListAdapter.OnMovieUpdateListener {

    private RecyclerView recyclerViewMovies;
    private UpdateMovieListAdapter adapter;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie_list);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UpdateMovieListAdapter(movieList, this);
        recyclerViewMovies.setAdapter(adapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe the movies LiveData. In our design, movies are grouped by category.
        // We'll flatten them into a single list.
        movieViewModel.getMovies().observe(this, moviesByCategory -> {
            movieList.clear();
            if (moviesByCategory != null) {
                for (List<Movie> movies : moviesByCategory.values()) {
                    movieList.addAll(movies);
                }
            }
            adapter.updateMovies(movieList);
        });
        movieViewModel.fetchMovies();
    }

    @Override
    public void onMovieUpdate(Movie movie, int position) {
        // Launch UpdateMovieActivity with the selected movie.
        Intent intent = new Intent(UpdateMovieListActivity.this, UpdateMovieActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
