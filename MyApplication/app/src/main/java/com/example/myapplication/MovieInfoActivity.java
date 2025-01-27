package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.MoviesListAdapter;
import com.example.myapplication.databinding.ActivityMovieInfoBinding;




public class MovieInfoActivity extends AppCompatActivity {
    private ActivityMovieInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieInfoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.playBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FullScreenMovieActivity.class);
            startActivity(intent);
        });
        final MoviesListAdapter adapter = new MoviesListAdapter(this);
        RecyclerView lstMovies=binding.lstRecommendedMovies;
        lstMovies.setAdapter(adapter);
        lstMovies.setLayoutManager(new GridLayoutManager(this,3));



    }
}