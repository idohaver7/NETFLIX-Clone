package com.example.myapplication.databases;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.myapplication.daoes.MovieDao;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.converters.CategoryConverter;
import com.example.myapplication.converters.Converters;

@Database(entities = {Movie.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class, CategoryConverter.class})
public abstract class MovieDB extends RoomDatabase {

    private static MovieDB instance;

    public abstract MovieDao movieDao();

    @SuppressWarnings("deprecation")
    public static synchronized MovieDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDB.class, "MoviesDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
