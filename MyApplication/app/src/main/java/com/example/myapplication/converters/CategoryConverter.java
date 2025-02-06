package com.example.myapplication.converters;

import androidx.room.TypeConverter;
import com.example.myapplication.entities.Category;
import com.google.gson.Gson;

public class CategoryConverter {

    @TypeConverter
    public static String fromCategoryToString(Category category) {
        if (category == null) {
            return null;
        }
        return new Gson().toJson(category);
    }

    @TypeConverter
    public static Category fromStringToCategory(String categoryString) {
        if (categoryString == null || categoryString.isEmpty()) {
            return null;
        }
        return new Gson().fromJson(categoryString, Category.class);
    }
}
