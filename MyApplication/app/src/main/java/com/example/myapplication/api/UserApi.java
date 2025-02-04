package com.example.myapplication.api;

import android.util.Log;
import com.example.myapplication.entities.User;
import com.google.gson.JsonObject;
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
                    try {
                        JsonObject jsonResponse = response.body();
                        Log.d("API_RESPONSE", "Login Response: " + jsonResponse.toString());


                        String token = jsonResponse.has("token") ? jsonResponse.get("token").getAsString() : "";

                        if (!token.isEmpty()) {
                            User user = new User();
                            user.setToken(token);

                            callback.onSuccess(user);
                        } else {
                            callback.onError("Token not received!");
                        }

                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error parsing login response", e);
                        callback.onError("Error parsing login response");
                    }
                } else {
                    Log.e("API_ERROR", "Login failed: " + response.errorBody());
                    callback.onError("Invalid credentials!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // ✅ Fetch user details (New method)
    public void fetchUserDetails(String token, UserCallback callback) {
        api.getUserDetails("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject jsonResponse = response.body();
                        Log.d("API_RESPONSE", "User Details: " + jsonResponse.toString());

                        // ✅ Extract user details
                        String userId = jsonResponse.has("id") ? jsonResponse.get("id").getAsString() : "";
                        String email = jsonResponse.has("email") ? jsonResponse.get("email").getAsString() : "";
                        String name = jsonResponse.has("name") ? jsonResponse.get("name").getAsString() : "";
                        String profilePicture = jsonResponse.has("profilePicture") ? jsonResponse.get("profilePicture").getAsString() : "";
                        int age = jsonResponse.has("age") ? jsonResponse.get("age").getAsInt() : 0;
                        String membership = jsonResponse.has("membership") ? jsonResponse.get("membership").getAsString() : "Basic";

                        // ✅ Create User object with full details
                        User user = new User(userId, email, "", profilePicture, name, age, membership, null, token);
                        callback.onSuccess(user);

                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error parsing user details", e);
                        callback.onError("Error parsing user details");
                    }
                } else {
                    Log.e("API_ERROR", "Fetching user details failed: " + response.errorBody());
                    callback.onError("Could not fetch user details!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // ✅ Create user (Sign-up)
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

    // ✅ Login Callback Interface
    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    // ✅ User Callback Interface (Handles both fetching user details & sign-up)
    public interface UserCallback {
        void onSuccess(User user); // User object may be null for sign-up
        void onError(String error);
    }
}
