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
import com.example.myapplication.globals.GlobalToken;
import com.example.myapplication.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> authToken = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // ✅ Observe Token (UI can react to token updates)
    public LiveData<String> getAuthToken() {
        return authToken;
    }

    // ✅ Observe Logged-in User
    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    // ✅ Login Method
    public void login(String email, String password) {
        userRepository.login(email, password).observeForever(token -> {
            if (token != null) {
                Log.d("USER_VIEWMODEL", "Login Successful! Token: " + token);
                authToken.postValue(token);  // ✅ Store token in LiveData
                saveAuthToken(token);

                // ✅ Fetch Full User Details
                userRepository.fetchUserDetails(token).observeForever(fullUser -> {
                    if (fullUser != null) {
                        Log.d("USER_VIEWMODEL", "User Retrieved: " + fullUser.getName() + " | IsManager: " + fullUser.isManager());

                        loggedInUser.postValue(fullUser);
                        saveUserSession(fullUser);
                    } else {
                        errorMessage.postValue("Failed to load user details.");
                    }
                });

            } else {
                Log.e("USER_VIEWMODEL", "Login failed.");
                errorMessage.postValue("Invalid credentials.");
            }
        });
    }

    // ✅ Sign-Up Method
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

    // ✅ Logout (Clear Session)
    public void logout() {
        loggedInUser.setValue(null);
        authToken.setValue(null);

        SharedPreferences prefs = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Log.d("USER_VIEWMODEL", "User logged out.");
    }

    // ✅ Save Token for API Calls
    private void saveAuthToken(String token) {
        GlobalToken.token = token; // ✅ Store globally
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("auth_token", token).apply();
    }

    // ✅ Save User Session
    private void saveUserSession(User user) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", user.getToken());
        editor.putBoolean("is_manager", user.isManager());
        editor.putString("user_name", user.getName());
        editor.apply();

        Log.d("USER_VIEWMODEL", "User session saved: " + user.getName() + " | Manager: " + user.isManager());
    }
}
