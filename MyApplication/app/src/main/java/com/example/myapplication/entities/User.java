package com.example.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private String id;
    private String email;
    private String password;
    private String profilePicture;
    private String name;
    private int age;
    private String membership;
    List<String> watchedMovies;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public List<String> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<String> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }
}
