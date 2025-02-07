package com.example.myapplication.manager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.viewModel.CategoryViewModel;
import com.example.myapplication.viewModel.MovieViewModel;
import java.util.ArrayList;
import java.util.List;

public class UpdateMovieActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText titleEditText, categoryEditText, videoUrlEditText, descriptionEditText, imageUrlEditText;
    private ProgressBar progressBar;
    private Button updateMovieButton;
    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> availableCategories = new ArrayList<>();
    private Movie movieToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        // Initialize views.
        backButton = findViewById(R.id.backButton);
        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        videoUrlEditText = findViewById(R.id.videoUrlEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        imageUrlEditText = findViewById(R.id.imageUrlEditText);
        progressBar = findViewById(R.id.progressBar);
        updateMovieButton = findViewById(R.id.updateMovieButton);

        backButton.setOnClickListener(v -> finish());

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch available categories.
        categoryViewModel.fetchCategories(this);
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                availableCategories = new ArrayList<>(categories);
            }
        });

        // Get the movie to update from the Intent extras.
        movieToUpdate = getIntent().getParcelableExtra("movie");
        if (movieToUpdate == null) {
            Toast.makeText(this, "No movie data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Prepopulate fields with the movie's current data.
        titleEditText.setText(movieToUpdate.getTitle());
        if (movieToUpdate.getCategory() != null) {
            categoryEditText.setText(movieToUpdate.getCategory().getName());
        }
        videoUrlEditText.setText(movieToUpdate.getVideo());
        descriptionEditText.setText(movieToUpdate.getDescription());
        imageUrlEditText.setText(movieToUpdate.getImage());

        updateMovieButton.setOnClickListener(v -> {
            updateMovieButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            String title = titleEditText.getText().toString().trim();
            String categoryInput = categoryEditText.getText().toString().trim();
            String videoUrl = videoUrlEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String imageUrl = imageUrlEditText.getText().toString().trim();

            // Validate inputs.
            if (TextUtils.isEmpty(title)) {
                titleEditText.setError("Movie title is required");
                resetUI();
                return;
            }
            if (TextUtils.isEmpty(categoryInput)) {
                categoryEditText.setError("Category name is required");
                resetUI();
                return;
            }
            if (TextUtils.isEmpty(videoUrl)) {
                videoUrlEditText.setError("Video URL is required");
                resetUI();
                return;
            }
            if (TextUtils.isEmpty(description)) {
                descriptionEditText.setError("Description is required");
                resetUI();
                return;
            }
            if (TextUtils.isEmpty(imageUrl)) {
                imageUrlEditText.setError("Movie poster URL is required");
                resetUI();
                return;
            }

            // Look up the exact category (case-insensitive).
            Category selectedCategory = null;
            for (Category cat : availableCategories) {
                if (cat.getName().equalsIgnoreCase(categoryInput)) {
                    selectedCategory = cat;
                    break;
                }
            }
            if (selectedCategory == null) {
                categoryEditText.setError("Category not found. Please enter a valid category.");
                resetUI();
                return;
            }

            // Create an updated Movie object (keeping the original id).
            Movie updatedMovie = new Movie(movieToUpdate.getId(), title, selectedCategory, videoUrl, description, imageUrl);

            // Call the ViewModel to update the movie.
            movieViewModel.updateMovie(updatedMovie).observe(this, success -> {
                progressBar.setVisibility(View.GONE);
                updateMovieButton.setEnabled(true);
                if (success != null && success) {
                    Toast.makeText(UpdateMovieActivity.this, "Movie updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateMovieActivity.this, "Failed to update movie", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void resetUI() {
        progressBar.setVisibility(View.GONE);
        updateMovieButton.setEnabled(true);
    }
}
