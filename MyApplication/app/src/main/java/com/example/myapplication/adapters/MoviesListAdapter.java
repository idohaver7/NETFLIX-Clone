/*
package com.example.myapplication.adapters;

import android.content.Context;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.MovieInfoActivity;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;

import java.util.List;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {
    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPic;

        private MovieViewHolder(View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.movie_image);
        }
    }

    private final LayoutInflater mInflater;
    private List<Movie> movies;
    private Context context;

    //Constructor for the MoviesListAdapter.
    public MoviesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (movies != null) {
            final Movie current = movies.get(position);
            holder.ivPic.setImageResource(current.getImage());
        }

    }


    // Sets the list of videos to be displayed in the adapter.
    public void setMovies(List<Movie> s) {
        movies = s;
        notifyDataSetChanged();
    }

    //Returns the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        else return 0;
    }

    //Gets the list of videos in the adapter.
    public List<Movie> getMovies() {
        return movies;
    }

    //Finds the ID of a video in the list by its position.
//    public String getById(String id) {
//        for (Movie movies : movies) {
//            if (movies.== id) {
//                return m.getId();
//            }
//        }
//        return null;
//    }

}


*/
