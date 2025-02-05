package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.viewModel.UserViewModel;

public class LogInActivity extends AppCompatActivity {

    private UserViewModel userViewModel; // ViewModel instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in); // ✅ Always show login screen

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserViewModel.class);

        // Initialize UI elements
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        ImageView backButton = findViewById(R.id.backButton);

        // Observe ViewModel for successful login
        userViewModel.getLoggedInUser().observe(this, user -> {
            if (user != null && user.getToken() != null) {
                saveAuthToken(user.getToken()); // ✅ Store token for API calls but not auto-login
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }
        });

        // Observe ViewModel for error messages
        userViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Handle login button click
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                userViewModel.login(email, password); // ✅ Login always requires input
            }
        });

        // ✅ Handle back button click
        backButton.setOnClickListener(v -> finish());
    }

    // ✅ Save token for API calls (but do not use for auto-login)
    private void saveAuthToken(String token) {
        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .edit()
                .putString("auth_token", token)
                .apply();
    }

    // ✅ Navigate to AfterLogInActivity after login
    private void navigateToHome() {
        Intent intent = new Intent(LogInActivity.this, AfterLogInActivity.class);
        startActivity(intent);
        finish();
    }
}
