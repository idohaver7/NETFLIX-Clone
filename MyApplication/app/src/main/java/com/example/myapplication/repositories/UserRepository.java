package com.example.myapplication.repositories;

import android.content.Context;

import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.UserApi;
import com.example.myapplication.api.WebServiceApi;
import com.example.myapplication.entities.User;

public class UserRepository {

    private final UserApi userApi;

    public UserRepository(Context context) {
        // Initialize the UserApi with WebServiceApi instance
        WebServiceApi webServiceApi = RetrofitClient.getInstance().create(WebServiceApi.class);
        userApi = new UserApi(webServiceApi);
    }

    // Login method
    public void login(String email, String password, LoginCallback callback) {
        userApi.login(email, password, new UserApi.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user); // Pass the user object to the calling ViewModel
            }

            @Override
            public void onError(String error) {
                callback.onError(error); // Pass the error to the calling ViewModel
            }
        });
    }

    // Sign-up method
    public void createUser(String name, String email, String password, int age, String profilePicture, UserCallback callback) {
        userApi.createUser(name, email, password, age, profilePicture, new UserApi.UserCallback() {
            @Override
            public void onSuccess() {
                callback.onSuccess(); // Notify the ViewModel about successful sign-up
            }

            @Override
            public void onError(String error) {
                callback.onError(error); // Notify the ViewModel about the error
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
