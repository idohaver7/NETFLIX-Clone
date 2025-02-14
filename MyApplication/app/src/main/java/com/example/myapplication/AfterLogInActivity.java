package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.adapters.CategoryAdapter;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.manager.ManagementActivity;
import com.example.myapplication.viewModel.MovieViewModel;
import com.example.myapplication.viewModel.UserViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class AfterLogInActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MovieViewModel movieViewModel;
    private ImageView darkModeToggle, profileIcon;
    private TextView signOutText, cancelText, managementOption;
    private View signOutMenu;
    private String movieString;
    private ExoPlayer player;
    private PlayerView playerView;

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

        // Initialize Toolbar & Menu References FIRST
        darkModeToggle = findViewById(R.id.darkModeToggle);
        profileIcon = findViewById(R.id.profileIcon);
        signOutText = findViewById(R.id.signOutText);
        cancelText = findViewById(R.id.cancelText);
        signOutMenu = findViewById(R.id.signOutMenu);
        managementOption = findViewById(R.id.managementOption);

        // Initialize RecyclerView
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        categoryAdapter = new CategoryAdapter();
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Initialize Player
        playerView = findViewById(R.id.playerLittleMovie);
        playerView.setUseController(false);

        // Initialize MovieViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, movies -> {
            categoryAdapter.setMoviesByCategory(movies);
            // After setting movies to the adapter, select a random movie to play
            if (!movies.isEmpty()) {
                Movie randomMovie = getRandomMovieFromMap(movies);
                if (randomMovie != null && randomMovie.getVideo() != null) {
                    initializePlayer(randomMovie.getVideo());
                }
            }
        });
        movieViewModel.fetchMovies();

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.loadUserDetails();
        userViewModel.getLoggedInUser().observe(this, user -> {
            if (user != null) {
                String profilePicture = user.getProfilePicture();
                if (profilePicture != null && !profilePicture.isEmpty()) {
                    // Normalize slashes: replace all "\" with "/"
                    profilePicture = profilePicture.replace("\\", "/");
                    // Remove the "public/" prefix if it exists (use "public/" with forward slash)
                    if (profilePicture.startsWith("public/")) {
                        profilePicture = profilePicture.substring("public/".length());
                    }
                    // Build the URL without the "public/" part
                    String imageUrl = "http://10.0.2.2:8080/" + profilePicture;
                    Log.d("PROFILE_PIC", "URL: " + imageUrl);
                    Glide.with(AfterLogInActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_user)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GLIDE_ERROR", "Failed to load image", e);
                                    return false; // Allow fallback to error drawable.
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(profileIcon);
                } else {
                    profileIcon.setImageResource(R.drawable.ic_user);
                }
            }
        });





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

    private void initializePlayer(String videoFileName) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        // Create media source from the server if needed, or continue using assets
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse("file:///android_asset/movies/video/" + videoFileName));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    private Movie getRandomMovieFromMap(Map<String, List<Movie>> moviesByCategory) {
        if (moviesByCategory != null && !moviesByCategory.isEmpty()) {
            // Flatten and filter the list
            List<Movie> allMovies = moviesByCategory.values().stream()
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(movie -> movie != null && !"Guardians_Of_The_Galaxy.mp4".equals(movie.getVideo()))
                    .collect(Collectors.toList());

            if (!allMovies.isEmpty()) {
                Random random = new Random();
                return allMovies.get(random.nextInt(allMovies.size()));
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieString != null) {
            initializePlayer(movieString);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
