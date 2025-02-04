package com.example.myapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.entities.Movie;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Map<String, List<Movie>> moviesByCategory;

    public void setMoviesByCategory(Map<String, List<Movie>> moviesByCategory) {
        if (moviesByCategory != null && !moviesByCategory.isEmpty()) {
            Log.d("CATEGORY_ADAPTER", "Movies by category: " + moviesByCategory.size() + " categories.");
        } else {
            Log.e("CATEGORY_ADAPTER", "No movies in categories.");
        }
        this.moviesByCategory = moviesByCategory;
        notifyDataSetChanged();
    }



    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = (String) moviesByCategory.keySet().toArray()[position];
        holder.categoryTitle.setText(category);

        List<Movie> movies = moviesByCategory.get(category);
        MovieAdapter movieAdapter = new MovieAdapter(holder.itemView.getContext(), movies);
        holder.moviesRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public int getItemCount() {
        return (moviesByCategory == null) ? 0 : moviesByCategory.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView moviesRecyclerView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            moviesRecyclerView = itemView.findViewById(R.id.moviesRecyclerView);
            moviesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
