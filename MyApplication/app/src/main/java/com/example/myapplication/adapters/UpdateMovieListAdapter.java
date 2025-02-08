package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;
import java.util.List;

public class UpdateMovieListAdapter extends RecyclerView.Adapter<UpdateMovieListAdapter.MovieViewHolder> {

    public interface OnMovieUpdateListener {
        void onMovieUpdate(Movie movie, int position);
    }

    private List<Movie> movies;
    private final OnMovieUpdateListener listener;
    private final Context context;

    public UpdateMovieListAdapter(List<Movie> movies, OnMovieUpdateListener listener) {
        this.movies = movies;
        this.listener = listener;
        // We assume that the context is available from the listener's implementing Activity.
        this.context = (Context) listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_update, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie current = movies.get(position);
        holder.titleTextView.setText(current.getTitle());
        // When the update button is clicked, call the listener.
        holder.updateButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieUpdate(current, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        Button updateButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitleTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}
