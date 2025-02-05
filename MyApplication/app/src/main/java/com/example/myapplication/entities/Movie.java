package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String _id;
    private String title;
    private String image;
    private String description;

    // Constructor
    public Movie(String _id, String title, String image, String description) {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.description = description;
    }

    // Getters
    public String getId() { return _id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public String getDescription() { return description; }


    protected Movie(Parcel in) {
        _id = in.readString();
        title = in.readString();
        image = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
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
