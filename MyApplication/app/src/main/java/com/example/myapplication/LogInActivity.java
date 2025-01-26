package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.WebServiceApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private WebServiceApi api; // Retrofit API instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize UI elements
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        ImageView backButton = findViewById(R.id.backButton);

        // Initialize Retrofit API instance
        api = RetrofitClient.getInstance().create(WebServiceApi.class);

        // Handle login button click
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Handle back button click
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginUser(String email, String password) {
        // Create JSON object for credentials
        JsonObject credentials = new JsonObject();
        credentials.addProperty("email", email);
        credentials.addProperty("password", password);

        // Call the login API
        api.login(credentials).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().get("token").getAsString();
                    System.out.println("Login successful, Token: " + token);

                    // Save token for future use
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("token", token)
                            .apply();

                    // Navigate to HomeActivity
                    Toast.makeText(LogInActivity.this, "you have logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    Toast.makeText(LogInActivity.this, "Email or Password are incorrect, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Error Response: " + response.errorBody());
                    Toast.makeText(LogInActivity.this, "Unexpected error occurred!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Network error: " + t.getMessage());
                Toast.makeText(LogInActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
