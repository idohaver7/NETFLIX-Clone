package com.example.myapplication.api;

import android.util.Log;
import com.example.myapplication.entities.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApi {
    private final WebServiceApi api;

    public UserApi(WebServiceApi api) {
        this.api = api;
    }

    public void login(String email, String password, LoginCallback callback) {
        JsonObject credentials = new JsonObject();
        credentials.addProperty("email", email);
        credentials.addProperty("password", password);

        api.login(credentials).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().get("token").getAsString();
                    Log.d("USER_API", "Token received: " + token);
                    callback.onSuccess(token);
                } else {
                    Log.e("USER_API", "Invalid credentials. Response Code: " + response.code());
                    callback.onError("Invalid credentials.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("USER_API", "Network error during login: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getUserDetails(String token, UserDetailsCallback callback) {
        Log.d("USER_API", "Fetching user details...");

        api.getUserDetails("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject jsonResponse = response.body();

                    // ✅ Convert JSON response into a User object
                    User user = new User(
                            jsonResponse.get("_id").getAsString(),
                            jsonResponse.get("email").getAsString(),
                            jsonResponse.get("password").getAsString(), // Might not be returned for security reasons
                            jsonResponse.get("profilePicture").getAsString(),
                            jsonResponse.get("name").getAsString(),
                            jsonResponse.has("age") ? jsonResponse.get("age").getAsInt() : 0, // Default to 0 if not available
                            jsonResponse.has("membership") ? jsonResponse.get("membership").getAsString() : "None", // Default if missing
                            new ArrayList<>(), // Placeholder for watchedMovies (parse if needed)
                            token, // Use the same token that was passed
                            jsonResponse.get("isManager").getAsBoolean()
                    );

                    Log.d("USER_API", "User data fetched successfully: " + user.getName() +
                            " | Age: " + user.getAge() +
                            " | Membership: " + user.getMembership() +
                            " | IsManager: " + user.isManager());

                    callback.onSuccess(user);
                } else {
                    Log.e("USER_API", "Failed to fetch user details.");
                    callback.onError("Failed to fetch user details.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("USER_API", "Network error fetching user details: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }



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
                    Log.d("API_RESPONSE", "User created successfully.");
                    callback.onSuccess(null);
                } else {
                    Log.e("API_ERROR", "Sign-up failed: " + response.errorBody());
                    callback.onError("Sign-up failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(String token);
        void onError(String error);
    }

    public interface UserCallback {
        void onSuccess(User user); // User object may be null for sign-up
        void onError(String error);
    }

    public interface UserDetailsCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
