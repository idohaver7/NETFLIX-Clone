package com.example.myapplication.databases;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return ""; // Store an empty string if the list is empty
        }
        return String.join(",", list);
    }

    @TypeConverter
    public List<String> fromStringToList(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the string is empty
        }
        return Arrays.asList(value.split(",")); // Convert comma-separated string to a list
    }
}
