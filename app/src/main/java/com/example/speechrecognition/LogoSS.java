package com.example.speechrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogoSS extends AppCompatActivity {
    private static final int SPLASH_DURATION = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_ss);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start your main activity here
                Intent intent = new Intent(LogoSS.this, SignIn.class);
                startActivity(intent);
                finish(); // Close the splash activity
            }
        }, SPLASH_DURATION);
    }
}