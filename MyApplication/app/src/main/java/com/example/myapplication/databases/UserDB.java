package com.example.myapplication.databases;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.myapplication.converters.Converters;
import com.example.myapplication.daoes.UserDao;
import com.example.myapplication.entities.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class}) // Add the converters here
public abstract class UserDB extends RoomDatabase {

    private static UserDB instance;

    public abstract UserDao userDao();

    public static synchronized UserDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            UserDB.class, "user_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
