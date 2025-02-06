package com.example.myapplication.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.daoes.CategoryDao;
import com.example.myapplication.entities.Category;

@Database(entities = {Category.class}, version = 1, exportSchema = false)
public abstract class CategoryDB extends RoomDatabase {
    private static com.example.myapplication.databases.CategoryDB instance;

    public abstract CategoryDao categoryDao();

    @SuppressWarnings("deprecation")
    public static synchronized com.example.myapplication.databases.CategoryDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            com.example.myapplication.databases.CategoryDB.class, "CategoryDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
