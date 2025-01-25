package com.example.myapplication.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.myapplication.R;

@Entity
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int pic;

    public Movie() {
        this.pic = R.drawable.avatar;
    }

    public Movie(int pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public int getPic() {
        return pic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
