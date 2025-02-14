package com.example.myapplication.manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private EditText titleEditText, descriptionEditText;
    private Spinner categorySpinner;
    private ProgressBar progressBar;
    private Button addMovieButton, selectVideoButton, selectImageButton;
    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> availableCategories = new ArrayList<>();

    // Variables to store the selected files’ URIs
    private Uri selectedVideoUri, selectedImageUri;

    // ActivityResultLaunchers for file selection.
    private ActivityResultLauncher<Intent> videoPickerLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        // (Initialize your views)
        backButton = findViewById(R.id.backButton);
        titleEditText = findViewById(R.id.titleEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        progressBar = findViewById(R.id.progressBar);
        addMovieButton = findViewById(R.id.addMovieButton);
        selectVideoButton = findViewById(R.id.selectVideoButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        backButton.setOnClickListener(v -> finish());

        // Initialize ViewModels.
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch available categories.
        categoryViewModel.fetchCategories(this);
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                availableCategories = new ArrayList<>(categories);
                List<String> categoryNames = new ArrayList<>();
                for (Category cat : availableCategories) {
                    categoryNames.add(cat.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, categoryNames);
                categorySpinner.setAdapter(adapter);
            }
        });

        // Set up file pickers.
        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedVideoUri = result.getData().getData();
                        Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        selectVideoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/mp4"); // or "video/*" if you want to allow all video types
            videoPickerLauncher.launch(intent);
        });
        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Add Movie button click listener.
        addMovieButton.setOnClickListener(v -> {
            addMovieButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                titleEditText.setError("Movie title is required");
                resetUI();
                return;
            }
            if (TextUtils.isEmpty(description)) {
                descriptionEditText.setError("Description is required");
                resetUI();
                return;
            }
            if (selectedVideoUri == null) {
                Toast.makeText(AddMovieActivity.this, "Please select a video file", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }
            if (selectedImageUri == null) {
                Toast.makeText(AddMovieActivity.this, "Please select an image file", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }

            int selectedPosition = categorySpinner.getSelectedItemPosition();
            if (selectedPosition < 0 || selectedPosition >= availableCategories.size()) {
                Toast.makeText(AddMovieActivity.this, "Please select a valid category", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }
            Category selectedCategory = availableCategories.get(selectedPosition);

            // Create a Movie object.
            // For multipart upload, the movie object's video and image fields can be empty or dummy,
            // because the file parts are sent separately.
            Movie movie = new Movie("", title, selectedCategory, "", description, "");

            // Call the ViewModel method that accepts Uris.
            movieViewModel.createMovie(movie, selectedVideoUri, selectedImageUri).observe(this, success -> {
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
