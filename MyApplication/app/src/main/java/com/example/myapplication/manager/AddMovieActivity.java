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

public class AddMovieActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText titleEditText, categoryEditText, videoUrlEditText, descriptionEditText, imageUrlEditText;
    private ProgressBar progressBar;
    private Button addMovieButton;
    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> availableCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        // Initialize views.
        backButton = findViewById(R.id.backButton);
        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        videoUrlEditText = findViewById(R.id.videoUrlEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        imageUrlEditText = findViewById(R.id.imageUrlEditText);
        progressBar = findViewById(R.id.progressBar);
        addMovieButton = findViewById(R.id.addMovieButton);

        // Set back button to finish the activity.
        backButton.setOnClickListener(v -> finish());

        // Obtain ViewModels.
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch available categories.
        categoryViewModel.fetchCategories(this);
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                availableCategories = new ArrayList<>(categories);
            }
        });

        // Set click listener for "Add Movie" button.
        addMovieButton.setOnClickListener(v -> {
            addMovieButton.setEnabled(false);
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

            // Search for an exact match of the entered category (case-insensitive).
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

            // Create a Movie object using the selected Category.
            Movie movie = new Movie("", title, selectedCategory, videoUrl, description, imageUrl);

            // Call the ViewModel to create the movie.
            movieViewModel.createMovie(movie).observe(this, success -> {
                progressBar.setVisibility(View.GONE);
                addMovieButton.setEnabled(true);
                if (success != null && success) {
                    Toast.makeText(AddMovieActivity.this, "Movie added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddMovieActivity.this, "Failed to add movie", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void resetUI() {
        progressBar.setVisibility(View.GONE);
        addMovieButton.setEnabled(true);
    }
}
