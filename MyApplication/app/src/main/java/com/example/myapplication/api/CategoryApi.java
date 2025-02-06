package com.example.myapplication.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryApi {
    WebServiceApi webServiceApi;
    Retrofit retrofit;

    public CategoryApi() {
        this.retrofit = RetrofitClient.getInstance();
        this.webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void createCategory(String token, Category category, MutableLiveData<Category> categoryMutableLiveData) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", category.getName());
        jsonObject.addProperty("promoted", category.isPromoted());
        Call<JsonObject> call = webServiceApi.createCategory(token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body().get("id").getAsString() != null) {
                    category.setId(response.body().get("id").getAsString());
                    categoryMutableLiveData.postValue(category);
                    Log.d("CreateCategory", "Category added successfully");
                } else {
                    categoryMutableLiveData.postValue(null);
                    Log.d("CreateCategory", "Failed to add Category");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                categoryMutableLiveData.postValue(null);
                Log.e("CreateCategory", "Failed to add Category error: " + t.getMessage());
            }
        });
    }

    public void deleteCategory(String token, String categoryId, MutableLiveData<Boolean> isDeleted) {
        Call<JsonObject> call = webServiceApi.deleteCategoryById(token, categoryId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    isDeleted.postValue(true);
                    Log.d("deleteCategory", "Deleted category");
                } else {
                    isDeleted.postValue(false);
                    Log.d("deleteCategory", "Failed to delete category");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isDeleted.postValue(false);
                Log.e("deleteCategory", "Failed to delete category: " + t.getMessage());
            }
        });

    }

    public void updateCategory(String token, String categoryId, Category category, MutableLiveData<Boolean> isUpdated) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", category.getName());
        jsonObject.addProperty("promoted", category.isPromoted());
        Call<JsonObject> call = webServiceApi.updateCategoryById(token, categoryId,jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    isUpdated.postValue(true);
                    Log.d("deleteCategory", "Deleted category");
                } else {
                    isUpdated.postValue(false);
                    Log.d("deleteCategory", "Failed to delete category");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                isUpdated.postValue(false);
                Log.e("deleteCategory", "Failed to delete category: " + t.getMessage());
            }
        });

    }

    public void getCategoryById(String token, String categoryId, MutableLiveData<Category> categoryMutableLiveData) {
        Call<Category> call = webServiceApi.getCategoryById(token, categoryId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                if (response.isSuccessful()) {
                    categoryMutableLiveData.postValue(response.body());
                    Log.d("getCategoryById", "get category");
                } else {
                    categoryMutableLiveData.postValue(null);
                    Log.d("getCategoryById", "Failed to ger category");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                categoryMutableLiveData.postValue(null);
                Log.e("getCategoryById", "Failed to get category: " + t.getMessage());
            }
        });

    }

    public void getAllCategories(String token, MutableLiveData<List<Category>> categoriesMutableLiveData) {
        Call<List<Category>> call = webServiceApi.getAllCategories(token);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoriesMutableLiveData.postValue(response.body());
                    Log.d("getCategoryById", "get category");
                } else {
                    categoriesMutableLiveData.postValue(null);
                    Log.d("getCategoryById", "Failed to ger category");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                categoriesMutableLiveData.postValue(null);
                Log.e("getCategoryById", "Failed to get category: " + t.getMessage());
            }
        });

    }


}


