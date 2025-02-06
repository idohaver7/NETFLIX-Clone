package com.example.myapplication.daoes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<Movie> index();

    @Query("SELECT * FROM movies WHERE _id = :id")
    Movie getMovie(String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... videoItems);

    @Update
    void update(Movie... videos);

    @Delete
    void delete(Movie... videos);

    @Query("DELETE FROM movies")
    void clearTable();

}