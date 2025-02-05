package com.example.myapplication.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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
            public void onSuccess(User user) {
                loggedInUser.setValue(user);
                saveAuthToken(user.getToken());
            }

            @Override
            public void onError(String error) {
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

    // ✅ Save token for API calls (but do not auto-login)
    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }
}
