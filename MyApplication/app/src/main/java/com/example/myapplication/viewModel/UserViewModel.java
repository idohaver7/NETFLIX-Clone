package com.example.myapplication.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.entities.User;
import com.example.myapplication.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Getters for LiveData
    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Login method
    public void login(String email, String password) {
        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                loggedInUser.setValue(user);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    // Sign-up method
    public void signUpUser(String name, String email, String password, int age, String profilePicture) {
        userRepository.createUser(name, email, password, age, profilePicture, new UserRepository.UserCallback() {
            @Override
            public void onSuccess() {
                signUpSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }
}
