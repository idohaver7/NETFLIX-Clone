package com.example.myapplication.manager;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CategoryOnlyAdapter;
import com.example.myapplication.entities.Category;
import com.example.myapplication.viewModel.CategoryViewModel;
import java.util.List;

public class ShowCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategories;
    private CategoryOnlyAdapter categoryOnlyAdapter;
    private CategoryViewModel categoryViewModel;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_categories);

        // Setup back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        // Start with an empty adapter
        categoryOnlyAdapter = new CategoryOnlyAdapter(null);
        recyclerViewCategories.setAdapter(categoryOnlyAdapter);

        // Obtain the ViewModel and trigger the fetch of categories
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        // Make sure GlobalToken.token is set before calling fetchCategories
        categoryViewModel.fetchCategories(this);

        // Observe the categories LiveData
        categoryViewModel.getCategories().observe(this, (List<Category> categories) -> {
            if (categories != null && !categories.isEmpty()) {
                // Update adapter with the new list
                categoryOnlyAdapter = new CategoryOnlyAdapter(categories);
                recyclerViewCategories.setAdapter(categoryOnlyAdapter);
            }
        });
    }
}
