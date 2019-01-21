package com.example.antalzoltn.ironman2;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

public class unlocked extends AppCompatActivity {
    private boolean playing = false;
    private VideoView mVideoView;
    private TextView tvReplay;
    private String locale = "en";
    private String TAG = "DEBUG";
    ImageButton replayBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.unlocked);

        Bundle b = getIntent().getExtras();
        if(b != null)
            locale = b.getString("key");
        else
            Log.d(TAG, "onCreate: B == null");
        Log.d(TAG, "unlocked.class : onCreate: local="+locale);

        if (savedInstanceState != null) {
            playing = savedInstanceState.getBoolean("PLAY");
        }
        if(playing)
            return;
        replayBtn = findViewById(R.id.imageButton4);
        tvReplay = findViewById(R.id.textView2);
        Locale current = getResources().getConfiguration().locale;
        Log.d("locale", current.getLanguage());
        mVideoView  = findViewById(R.id.videoView);
        videoPlay();
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        playing = savedInstanceState.getBoolean("PLAY");
        if(playing){

        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("PLAY", playing);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void videoPlay(){
        MediaController videoMediaController = new MediaController(this);
        videoMediaController.setMediaPlayer(mVideoView);
        Uri video;
        if(locale.equals("hu")) {
            video = Uri.parse("android.resource://" + getPackageName() + "/"
                    + R.raw.vasember_hu);
            tvReplay.setText("Újrajátszás");
            Log.d(TAG, "onCreate: Magyart játszik");
        }
        else {
            video = Uri.parse("android.resource://" + getPackageName() + "/"
                    + R.raw.vasember_en);
            tvReplay.setText("Replay");
            Log.d(TAG, "onCreate: English please!");
        }
        mVideoView.setVideoURI(video);
        //mVideoView.setMediaController(videoMediaController); //comment for disable Cotrols! :D
        mVideoView.requestFocus();
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                replayBtn.setVisibility(View.VISIBLE);
                replayBtn.bringToFront();
                tvReplay.setVisibility(View.VISIBLE);
                tvReplay.bringToFront();
            }
        });
        playing = true;
    }

    public void btnReplay(View view) {
        //mVideoView.setVisibility(View.VISIBLE);
        replayBtn.setVisibility(View.INVISIBLE);
        tvReplay.setVisibility(View.INVISIBLE);
        videoPlay();
    }
}
