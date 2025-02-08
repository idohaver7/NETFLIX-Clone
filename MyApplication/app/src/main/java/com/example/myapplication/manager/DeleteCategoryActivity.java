package com.example.myapplication.manager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.DeleteCategoryAdapter;
import com.example.myapplication.entities.Category;
import com.example.myapplication.viewModel.CategoryViewModel;
import java.util.ArrayList;
import java.util.List;

public class DeleteCategoryActivity extends AppCompatActivity implements DeleteCategoryAdapter.OnCategoryDeleteListener {

    private RecyclerView recyclerViewCategories;
    private DeleteCategoryAdapter deleteCategoryAdapter;
    private CategoryViewModel categoryViewModel;
    private ImageView backButton;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        // Set up header views
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Set up RecyclerView
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        deleteCategoryAdapter = new DeleteCategoryAdapter(categoryList, this);
        recyclerViewCategories.setAdapter(deleteCategoryAdapter);

        // Obtain the ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch categories from API/local DB
        categoryViewModel.fetchCategories(this);
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                categoryList = new ArrayList<>(categories);
                deleteCategoryAdapter.updateCategories(categoryList);
            }
        });
    }

    @Override
    public void onCategoryDelete(Category category, int position) {
        // Use the Activity as the LifecycleOwner when deleting.
        LiveData<Boolean> deletionLiveData = categoryViewModel.deleteCategory(category, this);
        deletionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    Toast.makeText(DeleteCategoryActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
                    // Remove the category by object rather than using the position.
                    if (!categoryList.remove(category)) {
                        // Fallback: search for a category with the same ID.
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryList.get(i).getId().equals(category.getId())) {
                                categoryList.remove(i);
                                break;
                            }
                        }
                    }
                    deleteCategoryAdapter.updateCategories(categoryList);
                } else {
                    Toast.makeText(DeleteCategoryActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                }
                // Remove this observer so it fires only once.
                deletionLiveData.removeObserver(this);
            }
        });
    }
}
