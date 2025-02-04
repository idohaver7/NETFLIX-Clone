package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies != null) {
            Movie current = movies.get(position);
            holder.movieTitle.setText(current.getTitle());

            String assetImagePath = "file:///android_asset/movies/image/" + current.getImage();

            Glide.with(holder.itemView.getContext())
                    .load(assetImagePath)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_movie)
                            .error(R.drawable.placeholder_movie)
                            .transform(new RoundedCorners(20))) // Rounded corners
                    .into(holder.movieImage);

            // Add slight elevation
            holder.movieImage.setElevation(8f);
        }
    }



    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        ImageView movieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieImage = itemView.findViewById(R.id.movieImage);
        }
    }
}
