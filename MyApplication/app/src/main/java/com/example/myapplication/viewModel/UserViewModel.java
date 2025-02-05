package com.example.myapplication.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.entities.User;
import com.example.myapplication.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Existing methods
    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    // Login method
    public void login(String email, String password) {
        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                Log.d("USER_VIEWMODEL", "Login Successful! Token: " + token);

                saveAuthToken(token); // ✅ Save token

                // ✅ Fetch full user details after login
                userRepository.getUserDetails(token, new UserRepository.UserCallback() {
                    @Override
                    public void onSuccess(User fullUser) {
                        Log.d("USER_VIEWMODEL", "Full User Retrieved: " + fullUser.getName() + " | IsManager: " + fullUser.isManager());

                        loggedInUser.postValue(fullUser); // ✅ Now, full user data is available
                        saveUserSession(fullUser);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("USER_VIEWMODEL", "Error fetching user details: " + error);
                        errorMessage.postValue("Failed to load user details.");
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("USER_VIEWMODEL", "Login failed: " + error);
                errorMessage.setValue(error);
            }
        });
    }



    public void signUpUser(String name, String email, String password, int age, String profilePicture) {
        userRepository.createUser(name, email, password, age, profilePicture, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                signUpSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    // Logout method to clear session
    public void logout() {
        loggedInUser.setValue(null);
        getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit().remove("auth_token").apply();
    }

    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }
    private void saveUserSession(User user) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", user.getToken());
        editor.putBoolean("is_manager", user.isManager()); // ✅ Save isManager field
        editor.putString("user_name", user.getName()); // ✅ Store user's name
        editor.apply();

        Log.d("USER_VIEWMODEL", "User session saved: " + user.getName() + " | Manager: " + user.isManager());
    }
}
