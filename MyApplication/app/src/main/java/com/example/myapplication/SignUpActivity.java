package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.viewModel.UserViewModel;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Uri selectedImageUri;
    private UserViewModel userViewModel; // ViewModel instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        profileImageView = findViewById(R.id.profileImageView);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText ageEditText = findViewById(R.id.ageEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        ImageView backButton = findViewById(R.id.backButton);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserViewModel.class);

        // Observe LiveData from ViewModel
        userViewModel.getSignUpSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) { // ✅ Ensure Boolean check
                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                finish(); // ✅ Close sign-up screen
            }
        });

        userViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle profile picture selection
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            profileImageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        profileImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Handle back button click
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // Handle sign-up button click
        signUpButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String ageText = ageEditText.getText().toString().trim();

            Log.d("SIGN_UP", "Name: " + name + ", Email: " + email + ", Age: " + ageText);

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageText);

            if (selectedImageUri != null) {
                Log.d("SIGN_UP", "Selected image URI: " + selectedImageUri.toString());
            } else {
                Log.d("SIGN_UP", "No image selected");
            }

            // Pass the actual image URI to the ViewModel
            userViewModel.signUpUser(name, email, password, age, selectedImageUri);
        });


    }
}
