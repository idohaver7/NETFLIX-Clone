package com.example.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String video;
    private String description;
    private String image;
    private Category category;

    public Movie(@NonNull String id, String title, String video, String description, String image, Category category) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getVideo() { return video; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public Category getCategory() { return category; }

    @Override
    public String toString() { return title; }
}
