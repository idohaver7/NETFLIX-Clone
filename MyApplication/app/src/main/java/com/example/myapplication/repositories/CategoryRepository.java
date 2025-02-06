package com.example.myapplication.repositories;

import android.content.Context;
import android.provider.Settings;
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
    private CategoryListData categoryListData;


    public CategoryRepository(Context context) {
        CategoryDB db = CategoryDB.getInstance(context);
        this.categoryApi = new CategoryApi();
        this.categoryDao = db.categoryDao();
        categoryListData=new CategoryListData();
    }

    public MutableLiveData<Boolean> CreateCategory(Category category, LifecycleOwner context) {
        MutableLiveData<Category> categoryMutableLiveData = new MutableLiveData<>();
        MutableLiveData<Boolean> isCreated = new MutableLiveData<>();


        // Call the API to add a category asynchronously
        categoryApi.createCategory(GlobalToken.token, category, categoryMutableLiveData);

        // Observe the categoryLiveData for a change
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

    public MutableLiveData<Boolean> UpdateCategory(String categoryId, Category category, LifecycleOwner context) {
        MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
        // Call the API to update a category asynchronously
        categoryApi.updateCategory(GlobalToken.token, categoryId, category, isUpdated);
        // Observe the categoryLiveData for a change
        isUpdated.observe(context, ifUpdated -> {
            if (ifUpdated) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        category.setId(categoryId);
                        categoryDao.update(category);
                        isUpdated.postValue(true);
                    } catch (Exception e) {
                        isUpdated.postValue(false);
                        Log.e("CategoryRepository", "Error updated category: " + e.getMessage(), e);
                    }
                });
            } else {
                isUpdated.postValue(false);
                Log.e("CategoryRepository", "Error: updatedCategory is null");
            }
        });

        return isUpdated;
    }

    public MutableLiveData<Boolean> deleteCategory(Category category, LifecycleOwner context) {
        MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
        // Call the API to delete a category asynchronously
        categoryApi.deleteCategory(GlobalToken.token, category.getId(), isDeleted);
        // Observe the categoryLiveData for a change
        isDeleted.observe(context, ifDeleted -> {
            if (ifDeleted) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        categoryDao.delete(category);
                        isDeleted.postValue(true);
                    } catch (Exception e) {
                        isDeleted.postValue(false);
                        Log.e("CategoryRepository", "Error deleted category: " + e.getMessage(), e);
                    }
                });
            } else {
                isDeleted.postValue(false);
                Log.e("CategoryRepository", "Error: deletedCategory is null");
            }
        });

        return isDeleted;
    }

    public MutableLiveData<Category> getCategoryById(String categoryId, LifecycleOwner context) {
        MutableLiveData<Category> category = new MutableLiveData<>();
        // Call the API to delete a category asynchronously
        categoryApi.getCategoryById(GlobalToken.token, categoryId, category);
        return category;
    }

    public MutableLiveData<List<Category>> getAllCategories(String categoryId, LifecycleOwner context) {
        MutableLiveData<List<Category>> categories = new MutableLiveData<>();
        categoryApi.getAllCategories(GlobalToken.token, categories);
        categories.observe(context, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    categoryListData.setValue(categories);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            categoryDao.insertCategories(categoryListData.getValue().toArray(new Category[0]));
                        } catch (Exception e) {
                            Log.e("VideoRepository", "Error inserting videos: " + e.getMessage(), e);
                        }
                    });
                    categoryListData.removeObserver(this);
                }
            }
        });

        return categories;
    }

    class CategoryListData extends MutableLiveData<List<Category>> {
        public CategoryListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    //     Retrieves the LiveData containing the list of posts
    public MutableLiveData<List<Category>> get() {
        return categoryListData;
    }


}





