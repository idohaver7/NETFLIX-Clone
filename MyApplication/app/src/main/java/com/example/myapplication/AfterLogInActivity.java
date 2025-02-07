package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.manager.ManagementActivity;
import com.example.myapplication.viewModel.MovieViewModel;

public class AfterLogInActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MovieViewModel movieViewModel;
    private ImageView darkModeToggle, profileIcon;
    private TextView signOutText, cancelText, managementOption;
    private View signOutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        managementOption = findViewById(R.id.managementOption);

        // Check if the user is a manager
        checkIfManager();

        // Initialize Dark Mode Toggle
        updateDarkModeIcon();
        darkModeToggle.setOnClickListener(view -> toggleDarkMode());

        // Handle Profile Icon Click (Show Sign-Out Menu)
        profileIcon.setOnClickListener(view -> toggleSignOutMenu());

        // Handle Sign Out Click
        signOutText.setOnClickListener(view -> signOutUser());

        // Handle Cancel Click
        cancelText.setOnClickListener(view -> hideSignOutMenu());

        // Handle Management Click
        managementOption.setOnClickListener(view -> openManagementPage());

        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AfterLogInActivity.this, SearchMovieActivity.class);
            startActivity(intent);
        });

    }

    private void checkIfManager() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isManager = prefs.getBoolean("is_manager", false);

        Log.d("MANAGER_CHECK", "User is manager: " + isManager);

        if (isManager) {
            managementOption.setVisibility(View.VISIBLE);
        } else {
            managementOption.setVisibility(View.GONE);
        }
    }

    private void openManagementPage() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isManager = prefs.getBoolean("is_manager", false);

        if (isManager) {
            Intent intent = new Intent(this, ManagementActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Only managers can enter!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleSignOutMenu() {
        if (signOutMenu.getVisibility() != View.VISIBLE) {
            // Get profile icon screen position
            int[] location = new int[2];
            profileIcon.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];

            signOutMenu.setX(x - 190);
            signOutMenu.setY(y + profileIcon.getHeight() - 10);

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

    private void updateDarkModeIcon() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("DarkMode", false);

        if (isDarkMode) {
            darkModeToggle.setImageResource(R.drawable.ic_moon);
        } else {
            darkModeToggle.setImageResource(R.drawable.ic_sun);
        }
    }

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
