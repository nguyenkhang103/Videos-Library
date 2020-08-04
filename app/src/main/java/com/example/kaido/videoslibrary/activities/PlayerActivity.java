package com.example.kaido.videoslibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.kaido.videoslibrary.R;
import com.example.kaido.videoslibrary.models.VideoModel;
import com.example.kaido.videoslibrary.utils.ConfigUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    VideoModel video;
    YouTubePlayerView playerView;
    public static int REQUEST_CODE_PLAY_VIDEO = 12;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        playerView = findViewById(R.id.playerVideo);
        video = new VideoModel();
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        assert data != null;
        video= (VideoModel) data.getSerializable("video");
        playerView.initialize(ConfigUtil.API_KEY,this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(video.getId());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(PlayerActivity.this,REQUEST_CODE_PLAY_VIDEO);
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PLAY_VIDEO && resultCode == RESULT_OK) {
            if(data != null) {
                playerView.initialize(ConfigUtil.API_KEY, PlayerActivity.this);
            }
        }
    }
}
