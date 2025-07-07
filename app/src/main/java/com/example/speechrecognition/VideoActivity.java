package com.example.speechrecognition;

import static androidx.core.util.TimeUtils.formatDuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.speechrecognition.databinding.ActivityVideoBinding;


public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button backButton;
    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.video_view);
        backButton=findViewById(R.id.button);
        rootLayout=findViewById(R.id.video_layout);
        String videoUrl = getIntent().getStringExtra("videoUri");
        // Disable accessibility feedback when video starts playing
        //getWindow().getDecorView().setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the drawer when back button is clicked
                Intent intent=new Intent(VideoActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        if (videoView == null) {
            throw new NullPointerException("VideoView is not properly initialized.");
        }
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        String videoUriString = getIntent().getStringExtra("videoUri");
        if (videoUriString == null) {
            Log.e("VideoActivity", "videoUriString is null");
            throw new NullPointerException("The video URI string is null.");
        }
        Log.d("VideoActivity", "Video URI String: " + videoUriString);
        Uri videoUri = Uri.parse(videoUriString);
        if (videoUri == null) {
            throw new NullPointerException("The video URI is null.");
        }
        videoView.setVideoURI(videoUri);
        videoView.start();

        videoView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        videoView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        String origin = getIntent().getStringExtra("origin");
        if ("Maths".equals(origin)) {
            Intent intent = new Intent(VideoActivity.this, Maths.class);
            startActivity(intent);
            finish();
        } else if ("Science".equals(origin)) {
            Intent intent = new Intent(VideoActivity.this, Science.class);
            startActivity(intent);
            finish();
        } else if ("English".equals(origin)) {
            Intent intent = new Intent(VideoActivity.this, English.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause(); // Pause the video when the activity goes into the background
        }
    }

}