package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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
        setContentView(R.layout.activity_log_in);

        // Initialize UI elements
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        ImageView backButton = findViewById(R.id.backButton);

        // Initialize the ViewModel
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserViewModel.class);

        // Observe ViewModel for successful login
        userViewModel.getLoggedInUser().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                // Navigate to HomeActivity after successful login
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Observe ViewModel for error messages
        userViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                userViewModel.login(email, password); // Call ViewModel to handle login
            }
        });

        // Handle back button click
        backButton.setOnClickListener(v -> finish());
    }
}
