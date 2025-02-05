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
        // Initialize the UserApi with WebServiceApi instance
        WebServiceApi webServiceApi = RetrofitClient.getInstance().create(WebServiceApi.class);
        userApi = new UserApi(webServiceApi);
    }

    public void login(String email, String password, LoginCallback callback) {
        userApi.login(email, password, new UserApi.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }

            @Override
            public void onError(String error) {
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



    // Callbacks for Login and Sign-up
    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
