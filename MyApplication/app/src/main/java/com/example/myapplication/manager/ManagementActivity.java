package com.example.myapplication.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class ManagementActivity extends AppCompatActivity {
    private TextView greetingText;
    private Button addMovieButton, updateMovieButton, deleteMovieButton, showMoviesButton;
    private Button addCategoryButton, deleteCategoryButton, showCategoriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        // Initialize UI Elements
        greetingText = findViewById(R.id.greetingText);
        addMovieButton = findViewById(R.id.addMovieButton);
        updateMovieButton = findViewById(R.id.updateMovieButton);
        deleteMovieButton = findViewById(R.id.deleteMovieButton);
        showMoviesButton = findViewById(R.id.showMoviesButton);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);
        showCategoriesButton = findViewById(R.id.showCategoriesButton);

        // Get user name from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Manager");

        // Set greeting text
        greetingText.setText("Hello, " + userName + "!");

        // Button Click Listeners
//        addMovieButton.setOnClickListener(view -> navigateTo(AddMovieActivity.class));
//        updateMovieButton.setOnClickListener(view -> navigateTo(UpdateMovieActivity.class));
//        deleteMovieButton.setOnClickListener(view -> navigateTo(DeleteMovieActivity.class));
//        showMoviesButton.setOnClickListener(view -> navigateTo(ShowMoviesActivity.class));
//
//        addCategoryButton.setOnClickListener(view -> navigateTo(AddCategoryActivity.class));
//        deleteCategoryButton.setOnClickListener(view -> navigateTo(DeleteCategoryActivity.class));
//        showCategoriesButton.setOnClickListener(view -> navigateTo(ShowCategoriesActivity.class));
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(ManagementActivity.this, activityClass);
        startActivity(intent);
    }
}
