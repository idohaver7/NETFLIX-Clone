package com.example.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String id;
    private String email;
    private String password;
    private String profilePicture;
    private String name;
    private int age;
    private String membership;
    private List<String> watchedMovies = new ArrayList<>();
    private String token; // ✅ Add the token field

    public User() {
    }

    // ✅ Main constructor used by Room
    public User(@NonNull String id, String email, String password, String profilePicture, String name, int age, String membership, List<String> watchedMovies, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.name = name;
        this.age = age;
        this.membership = membership;
        this.watchedMovies = watchedMovies;
        this.token = token;
    }

    // ✅ Tell Room to ignore this constructor
    @Ignore
    public User(String token) {
        this.token = token;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getMembership() { return membership; }
    public void setMembership(String membership) { this.membership = membership; }

    public List<String> getWatchedMovies() { return watchedMovies; }
    public void setWatchedMovies(List<String> watchedMovies) { this.watchedMovies = watchedMovies; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
