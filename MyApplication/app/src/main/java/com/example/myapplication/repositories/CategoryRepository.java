package com.example.myapplication.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.myapplication.api.CategoryApi;
import com.example.myapplication.daoes.CategoryDao;
import com.example.myapplication.databases.CategoryDB;
import com.example.myapplication.entities.Category;
import com.example.myapplication.globals.GlobalToken;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final CategoryApi categoryApi;
    private final CategoryListData categoryListData;

    public CategoryRepository(Context context) {
        CategoryDB db = CategoryDB.getInstance(context);
        this.categoryApi = new CategoryApi();
        this.categoryDao = db.categoryDao();
        categoryListData = new CategoryListData();
    }

    // --------------------
    // Create Category
    // --------------------
    public MutableLiveData<Boolean> CreateCategory(Category category, LifecycleOwner context) {
        MutableLiveData<Category> categoryMutableLiveData = new MutableLiveData<>();
        MutableLiveData<Boolean> isCreated = new MutableLiveData<>();

        // Call the API to add a category asynchronously
        categoryApi.createCategory(GlobalToken.token, category, categoryMutableLiveData);

        // Observe the API response
        categoryMutableLiveData.observe(context, addedCategory -> {
            if (addedCategory != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        categoryDao.insert(addedCategory);
                        isCreated.postValue(true);
                    } catch (Exception e) {
                        isCreated.postValue(false);
                        Log.e("CategoryRepository", "Error inserting category: " + e.getMessage(), e);
                    }
                });
            } else {
                isCreated.postValue(false);
                Log.e("CategoryRepository", "Error: addedCategory is null");
            }
        });

        return isCreated;
    }

    // --------------------
    // Update Category
    // --------------------
    public MutableLiveData<Boolean> UpdateCategory(String categoryId, Category category, LifecycleOwner context) {
        MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
        // Call the API to update a category asynchronously
        categoryApi.updateCategory(GlobalToken.token, categoryId, category, isUpdated);
        // Observe the API response
        isUpdated.observe(context, ifUpdated -> {
            if (ifUpdated) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        category.setId(categoryId);
                        categoryDao.update(category);
                        isUpdated.postValue(true);
                    } catch (Exception e) {
                        isUpdated.postValue(false);
                        Log.e("CategoryRepository", "Error updating category: " + e.getMessage(), e);
                    }
                });
            } else {
                isUpdated.postValue(false);
                Log.e("CategoryRepository", "Error: updatedCategory is null");
            }
        });

        return isUpdated;
    }

    // --------------------
    // Delete Category
    // --------------------
    public MutableLiveData<Boolean> deleteCategory(Category category, LifecycleOwner context) {
        MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
        // Call the API to delete a category asynchronously
        categoryApi.deleteCategory(GlobalToken.token, category.getId(), isDeleted);
        // Observe the API response
        isDeleted.observe(context, ifDeleted -> {
            if (ifDeleted) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        categoryDao.delete(category);
                        isDeleted.postValue(true);
                    } catch (Exception e) {
                        isDeleted.postValue(false);
                        Log.e("CategoryRepository", "Error deleting category: " + e.getMessage(), e);
                    }
                });
            } else {
                isDeleted.postValue(false);
                Log.e("CategoryRepository", "Error: deletedCategory is null");
            }
        });

        return isDeleted;
    }

    // --------------------
    // Get Category by ID
    // --------------------
    public MutableLiveData<Category> getCategoryById(String categoryId, LifecycleOwner context) {
        MutableLiveData<Category> category = new MutableLiveData<>();
        // Call the API to get a category asynchronously
        categoryApi.getCategoryById(GlobalToken.token, categoryId, category);
        return category;
    }

    // --------------------
    // Fetch All Categories from API
    // --------------------
    public void fetchAllCategories(LifecycleOwner context) {
        // Create a temporary LiveData to hold the API response.
        MutableLiveData<List<Category>> apiCategoriesLiveData = new MutableLiveData<>();

        // Trigger the API call.
        categoryApi.getAllCategories(GlobalToken.token, apiCategoriesLiveData);

        // Observe the API LiveData.
        apiCategoriesLiveData.observe(context, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                if (categoryList != null && !categoryList.isEmpty()) {
                    Log.d("CATEGORY_API", "Fetched from API: " + categoryList.size() + " categories.");
                    for (Category cat : categoryList) {
                        Log.d("CATEGORY_API", "Category: " + cat.getName() + " | ID: " + cat.getId());
                    }
                    // Update our internal LiveData.
                    categoryListData.postValue(categoryList);

                    // Save categories to local DB in a background thread.
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            List<Category> categoriesFromLiveData = categoryListData.getValue();
                            if (categoriesFromLiveData != null && !categoriesFromLiveData.isEmpty()) {
                                categoryDao.clearTable();
                                categoryDao.insertCategories(categoriesFromLiveData.toArray(new Category[0]));
                                Log.d("CATEGORY_DB", "Categories saved to DB.");
                            } else {
                                Log.e("CATEGORY_DB", "No categories available to insert!");
                            }
                        } catch (Exception e) {
                            Log.e("CATEGORY_DB", "Error inserting categories: " + e.getMessage(), e);
                        }
                    });
                } else {
                    Log.e("CATEGORY_API", "API returned an empty category list!");
                }
                // Remove observer to avoid duplicate updates.
                apiCategoriesLiveData.removeObserver(this);
            }
        });
    }

    // --------------------
    // Expose the internal LiveData of categories
    // --------------------
    public MutableLiveData<List<Category>> get() {
        return categoryListData;
    }

    // --------------------
    // Internal LiveData class to hold the list of categories
    // --------------------
    class CategoryListData extends MutableLiveData<List<Category>> {
        public CategoryListData() {
            super();
            setValue(new LinkedList<>());
        }
    }
}
