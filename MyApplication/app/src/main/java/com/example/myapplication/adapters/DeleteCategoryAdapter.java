package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.entities.Category;
import java.util.List;

public class DeleteCategoryAdapter extends RecyclerView.Adapter<DeleteCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryDeleteListener {
        void onCategoryDelete(Category category, int position);
    }

    private List<Category> categories;
    private final OnCategoryDeleteListener listener;

    public DeleteCategoryAdapter(List<Category> categories, OnCategoryDeleteListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delete_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryName.setText(category.getName());
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryDelete(category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName;
        Button buttonDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
