package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Arvostelusivu extends YouTubeBaseActivity {

    YouTubePlayerView tubePlayerView;
    Button button;
    YouTubePlayer.OnInitializedListener initializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arvostelusivu);

        button = (Button) findViewById(R.id.playButton);
        tubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlay);

        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("xc1RCqidtTg");
                //youTubePlayer.loadVideo("e6d72ac2-d36b-4c58-b64b-761f78f189bc");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tubePlayerView.initialize(TubeConfig.getApiKey(), initializedListener);
            }
        });

    }

    public void goToLuoArvostelu(View view){
        Intent intent = new Intent(this, luoArvostelu.class);
        startActivity(intent);
    }
}

