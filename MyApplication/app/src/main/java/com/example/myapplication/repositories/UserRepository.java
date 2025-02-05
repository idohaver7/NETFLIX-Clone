package com.example.myapplication.repositories;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.UserApi;
import com.example.myapplication.api.WebServiceApi;
import com.example.myapplication.entities.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserApi userApi;

    public UserRepository(Context context) {
        WebServiceApi webServiceApi = RetrofitClient.getInstance().create(WebServiceApi.class);
        userApi = new UserApi(webServiceApi);
    }

    public void login(String email, String password, LoginCallback callback) {
        Log.d("USER_REPO", "Sending login request for email: " + email);

        userApi.login(email, password, new UserApi.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                Log.d("USER_REPO", "Login successful! Token received.");
                callback.onSuccess(token); // ✅ Pass only the token
            }

            @Override
            public void onError(String error) {
                Log.e("USER_REPO", "Login error: " + error);
                callback.onError(error);
            }
        });
    }

    public void createUser(String name, String email, String password, int age, String profilePicture, UserCallback callback) {
        userApi.createUser(name, email, password, age, profilePicture, new UserApi.UserCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(null);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getUserDetails(String token, UserCallback callback) {
        Log.d("USER_REPO", "Fetching user details with token.");

        userApi.getUserDetails(token, new UserApi.UserDetailsCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("USER_REPO", "User details received: " + user.getName() + " | Manager: " + user.isManager());
                callback.onSuccess(user); // ✅ Pass full user details
            }

            @Override
            public void onError(String error) {
                Log.e("USER_REPO", "Failed to fetch user details: " + error);
                callback.onError(error);
            }
        });
    }



    // Callbacks for Login and Sign-up
    public interface LoginCallback {
        void onSuccess(String token);
        void onError(String error);
    }

    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
