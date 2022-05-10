package com.healthy_lifestyle.aip_lifestyle.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.healthy_lifestyle.aip_lifestyle.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity implements PlayerView {
    private YouTubePlayerView youTubePlayerView;
    private TextView tvTitle, tvDescription;
    Bundle mBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        showActionBar();
        findById();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            tvTitle.setText(mBundle.getString("title"));
            tvDescription.setText(mBundle.getString("description"));
        }
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = mBundle.getString("videoId");
                youTubePlayer.loadVideo(videoId, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#C9EEDD"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("");
    }

    private void findById() {
        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        tvTitle = findViewById(R.id.txtTitle);
        tvDescription = findViewById(R.id.txtDescription);
    }
}