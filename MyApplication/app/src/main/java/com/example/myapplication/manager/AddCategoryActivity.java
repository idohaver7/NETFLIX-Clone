package com.example.myapplication.manager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.viewModel.CategoryViewModel;

public class AddCategoryActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText editTextCategoryName;
    private Switch switchPromoted;
    private Button buttonAddCategory;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        switchPromoted = findViewById(R.id.switchPromoted);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

        // Setup back button to finish the activity when clicked
        backButton.setOnClickListener(v -> finish());

        // Obtain the CategoryViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Set the Add Category button click listener
        buttonAddCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString().trim();
            boolean isPromoted = switchPromoted.isChecked();

            // Validate input
            if (TextUtils.isEmpty(categoryName)) {
                editTextCategoryName.setError("Category name is required");
                return;
            }

            // Call the ViewModel to create a new category and observe the result
            categoryViewModel.createCategory(categoryName, isPromoted, this)
                    .observe(this, success -> {
                        if (success != null && success) {
                            Toast.makeText(AddCategoryActivity.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                            finish();  // Close the activity on success
                        } else {
                            Toast.makeText(AddCategoryActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
