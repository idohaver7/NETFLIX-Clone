package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.viewModel.MovieViewModel;

public class AfterLogInActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MovieViewModel movieViewModel;
    private ImageView darkModeToggle, profileIcon;
    private TextView signOutText, cancelText;
    private View signOutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);

        // Initialize RecyclerView
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        categoryAdapter = new CategoryAdapter();
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, categoryAdapter::setMoviesByCategory);
        movieViewModel.fetchMovies();

        // Toolbar & Menu References
        darkModeToggle = findViewById(R.id.darkModeToggle);
        profileIcon = findViewById(R.id.profileIcon);
        signOutText = findViewById(R.id.signOutText);
        cancelText = findViewById(R.id.cancelText);
        signOutMenu = findViewById(R.id.signOutMenu);

        // Initialize Dark Mode Toggle
        updateDarkModeIcon();  // ✅ Now this method is included!
        darkModeToggle.setOnClickListener(view -> toggleDarkMode());

        // Handle Profile Icon Click (Show Sign-Out Menu)
        profileIcon.setOnClickListener(view -> toggleSignOutMenu());

        // Handle Sign Out Click
        signOutText.setOnClickListener(view -> signOutUser());

        // Handle Cancel Click
        cancelText.setOnClickListener(view -> hideSignOutMenu());
    }

    private void toggleSignOutMenu() {
        if (signOutMenu.getVisibility() != View.VISIBLE) {
            // Get profile icon screen position
            int[] location = new int[2];
            profileIcon.getLocationOnScreen(location);
            int x = location[0];  // X position
            int y = location[1];  // Y position

            // Move signOutMenu below profileIcon dynamically and shift it to the left
            signOutMenu.setX(x - 120);  // ✅ Shift to the left
            signOutMenu.setY(y + profileIcon.getHeight() - 10);  // Position below

            signOutMenu.setVisibility(View.VISIBLE);
            signOutMenu.setAlpha(0f);
            signOutMenu.animate().alpha(1f).setDuration(200);
        } else {
            hideSignOutMenu();
        }
    }

    private void hideSignOutMenu() {
        signOutMenu.animate().alpha(0f).setDuration(200)
                .withEndAction(() -> signOutMenu.setVisibility(View.INVISIBLE));
    }

    // ✅ Toggle Dark Mode
    private void toggleDarkMode() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("DarkMode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Save user preference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("DarkMode", !isDarkMode);
        editor.apply();

        updateDarkModeIcon();
    }

    // ✅ Update the Dark Mode Toggle Icon
    private void updateDarkModeIcon() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("DarkMode", false);

        if (isDarkMode) {
            darkModeToggle.setImageResource(R.drawable.ic_moon);
        } else {
            darkModeToggle.setImageResource(R.drawable.ic_sun);
        }
    }

    // ✅ Sign Out User
    private void signOutUser() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(AfterLogInActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
