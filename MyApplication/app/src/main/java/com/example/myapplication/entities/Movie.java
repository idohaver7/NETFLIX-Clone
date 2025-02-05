package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String _id;
    private String title;
    private Category category;
    private String video;
    private String description;
    private String image;

    // Constructor
    public Movie(String _id, String title, Category category, String video, String description, String image) {
        this._id = _id;
        this.title = title;
        this.category = category;
        this.video = video;
        this.description = description;
        this.image = image;
    }

    // Getters
    public String getId() { return _id; }
    public String getTitle() { return title; }
    public Category getCategory() { return category; }
    public String getVideo() { return video; }
    public String getDescription() { return description; }
    public String getImage() { return image; }

    // Parcelable Implementation
    protected Movie(Parcel in) {
        _id = in.readString();
        title = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        video = in.readString();
        description = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeParcelable(category, flags);
        dest.writeString(video);
        dest.writeString(description);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
