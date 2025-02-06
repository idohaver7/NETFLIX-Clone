package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.myapplication.converters.CategoryConverter;

@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey
    @NonNull
    private String _id;
    private String title;

    @TypeConverters(CategoryConverter.class) // ✅ Convert Category object
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

    // Getters & Setters
    public String getId() { return _id; }
    public String getTitle() { return title; }
    public Category getCategory() { return category; }
    public String getVideo() { return video; }
    public String getDescription() { return description; }
    public String getImage() { return image; }

    public void setCategory(Category category) { this.category = category; }

    // Parcelable Implementation
    protected Movie(Parcel in) {
        _id = in.readString();
        title = in.readString();

        // ✅ Read Category object properly
        String categoryId = in.readString();
        String categoryName = in.readString();
        boolean categoryPromoted = in.readByte() != 0;
        category = new Category(categoryId, categoryName, categoryPromoted);

        video = in.readString();
        description = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);

        // ✅ Save full Category object
        dest.writeString(category.getId());
        dest.writeString(category.getName());
        dest.writeByte((byte) (category.isPromoted() ? 1 : 0));

        dest.writeString(video);
        dest.writeString(description);
        dest.writeString(image);
    }

    @Override
    public int describeContents() { return 0; }

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
