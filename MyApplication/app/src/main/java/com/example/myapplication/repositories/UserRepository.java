package com.example.myapplication.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.api.UserApi;
import com.example.myapplication.daoes.UserDao;
import com.example.myapplication.databases.UserDB;
import com.example.myapplication.entities.User;
import com.example.myapplication.globals.GlobalToken;

import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final UserApi userApi;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public UserRepository(Context context) {
        UserDB db = UserDB.getInstance(context);
        this.userApi = new UserApi(context);
        this.userDao = db.userDao();
    }

    // ✅ Login
    public MutableLiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();

        userApi.login(email, password, new UserApi.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                GlobalToken.token = token;
                Log.d("USER_REPO", "Login successful! Token: " + token);
                loginResponse.postValue(token);
            }

            @Override
            public void onError(String error) {
                Log.e("USER_REPO", "Login failed: " + error);
                loginResponse.postValue(null);
            }
        });

        return loginResponse;
    }

    // ✅ Fetch User Details
    public MutableLiveData<User> fetchUserDetails(String token) {
        userApi.getUserDetails(token, new UserApi.UserDetailsCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.postValue(user);
                Executors.newSingleThreadExecutor().execute(() -> userDao.insert(user)); // Cache user in DB
                Log.d("USER_REPO", "User details saved in DB.");
            }

            @Override
            public void onError(String error) {
                Log.e("USER_REPO", "Failed to fetch user details: " + error);
            }
        });

        return userLiveData;
    }

    public void createUser(String name, String email, String password, int age, Uri profilePicture, UserCallback callback) {
        Log.d("USER_REPO", "Creating user with name: " + name + ", email: " + email + ", age: " + age);
        userApi.createUser(name, email, password, age, profilePicture, new UserApi.UserCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("USER_REPO", "User creation successful");
                callback.onSuccess(user);
            }

            @Override
            public void onError(String error) {
                Log.e("USER_REPO", "User creation error: " + error);
                callback.onError(error);
            }
        });
    }



    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }
}
