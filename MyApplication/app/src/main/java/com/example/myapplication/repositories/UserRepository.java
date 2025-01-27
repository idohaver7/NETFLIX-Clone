package com.example.myapplication.repositories;

import android.content.Context;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.WebServiceApi;
import com.example.myapplication.entities.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final WebServiceApi api;

    public UserRepository(Context context) {
        api = RetrofitClient.getInstance().create(WebServiceApi.class);
    }

    // Login method
    public void login(String email, String password, LoginCallback callback) {
        JsonObject credentials = new JsonObject();
        credentials.addProperty("email", email);
        credentials.addProperty("password", password);

        api.login(credentials).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = new User();
                    user.setEmail(email);
                    user.setId(response.body().get("id").getAsString());
                    callback.onSuccess(user);
                } else {
                    callback.onError("Invalid credentials!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Sign-up method
    public void createUser(String name, String email, String password, int age, String profilePicture, UserCallback callback) {
        JsonObject user = new JsonObject();
        user.addProperty("name", name);
        user.addProperty("email", email);
        user.addProperty("password", password);
        user.addProperty("age", age);
        user.addProperty("profilePicture", profilePicture);

        api.createUser(user).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Sign-up failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Callbacks for Login and Sign-up
    public interface LoginCallback {
        void onSuccess(User user);

        void onError(String error);
    }

    public interface UserCallback {
        void onSuccess();

        void onError(String error);
    }
}
