package com.example.myapplication.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.entities.Category;
import com.example.myapplication.repositories.CategoryRepository;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository categoryRepository;
    // Use the repository's internal LiveData to store categories.
    private final LiveData<List<Category>> categories;
    private final MutableLiveData<Boolean> categoryCreated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> categoryUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> categoryDeleted = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        // Get the internal LiveData from the repository.
        categories = categoryRepository.get();
    }

    /**
     * Triggers the API call to fetch categories.
     * Ensure GlobalToken.token is properly set before calling this.
     */
    public void fetchCategories(LifecycleOwner lifecycleOwner) {
        categoryRepository.fetchAllCategories(lifecycleOwner);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    // 🔹 Create a Category with a valid LifecycleOwner
    public LiveData<Boolean> createCategory(String categoryName, boolean isPromoted, LifecycleOwner owner) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Category newCategory = new Category("", categoryName, isPromoted); // ID is empty; backend generates it.
        categoryRepository.CreateCategory(newCategory, owner).observe(owner, result::setValue);
        return result;
    }

    // 🔹 Update a Category with a valid LifecycleOwner
    public LiveData<Boolean> updateCategory(String categoryId, String categoryName, boolean isPromoted, LifecycleOwner owner) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Category updatedCategory = new Category(categoryId, categoryName, isPromoted);
        categoryRepository.UpdateCategory(categoryId, updatedCategory, owner).observe(owner, result::setValue);
        return result;
    }

    // 🔹 Delete a Category with a valid LifecycleOwner
    public LiveData<Boolean> deleteCategory(Category category, LifecycleOwner owner) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        categoryRepository.deleteCategory(category, owner).observe(owner, result::setValue);
        return result;
    }

    // 🔹 Get Error Messages
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
