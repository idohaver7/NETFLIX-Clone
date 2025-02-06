package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private boolean promoted;

    // Constructor
    public Category(String id, String name, boolean promoted) {
        this.id = id;
        this.name = name;
        this.promoted = promoted;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isPromoted() { return promoted; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPromoted(boolean promoted) { this.promoted = promoted; }

    // Parcelable Implementation
    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        promoted = in.readByte() != 0; // Boolean conversion
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (promoted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
