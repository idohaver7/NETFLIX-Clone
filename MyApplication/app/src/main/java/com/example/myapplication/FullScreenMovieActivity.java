package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.RetrofitClient;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;

import retrofit2.Retrofit;

public class FullScreenMovieActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_movie);
        playerView = findViewById(R.id.player_view);

        // Get video URL from the intent
        Intent intent = getIntent();
        String videoFileName = intent.getStringExtra("videoUrl");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        // Initialize and start player
        initializePlayer(videoFileName);
    }

    private void initializePlayer(String videoFileName) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        // Create media source
        MediaItem mediaItem = MediaItem.fromUri(RetrofitClient.getBase_Url()+ "video/" + videoFileName);

        // Set the media source to the player
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustFullScreen(newConfig.orientation);
    }

    private void adjustFullScreen(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Hide system UI for full-screen experience
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            // Show system UI
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}

