package com.example.myapplication.daoes;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entities.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE id = :userId")
    User getUserById(String userId);

    @Query("DELETE FROM User")
    void clearAll();
}
