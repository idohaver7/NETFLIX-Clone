package com.example.myapplication.adapters;

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

public class DeleteMovieAdapter extends RecyclerView.Adapter<DeleteMovieAdapter.MovieViewHolder> {

    public interface OnMovieDeleteListener {
        void onMovieDelete(Movie movie, int position);
    }

    private List<Movie> movies;
    private final OnMovieDeleteListener listener;

    public DeleteMovieAdapter(List<Movie> movies, OnMovieDeleteListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_delete, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieDelete(movie, position);
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
        Button deleteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitleTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
