package com.example.myapplication.daoes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entities.Category;
import com.example.myapplication.entities.Movie;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> index();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getId(String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category... categories);

    @Update
    void update(Category... categories);

    @Delete
    void delete(Category... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(Category... categories);
    @Query("DELETE FROM Category")
    void clearTable();
}
