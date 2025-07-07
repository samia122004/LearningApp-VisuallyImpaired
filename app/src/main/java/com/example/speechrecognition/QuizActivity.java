package com.example.speechrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.speechrecognition.other.Constants;
import com.google.firebase.FirebaseApp;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getWindow().setStatusBarColor(ContextCompat.getColor(QuizActivity.this,R.color.ochre));

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        Log.d("QuizActivity", "Firebase initialized");

        CardView cvMath = findViewById(R.id.cvMath);
        CardView cvScience = findViewById(R.id.cvScience);
        CardView cvEnglish = findViewById(R.id.cvEnglish);

        findViewById(R.id.imageViewQuizOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Click listener for Math quiz
        cvMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Math CardView clicked"); // Log the click event
                Intent intent = new Intent(QuizActivity.this, MathQuizActivity.class);
                intent.putExtra(Constants.SUBJECT, getString(R.string.math));
                Log.d("MainActivity", "Starting MathQuizActivity"); // Log starting of activity
                startActivity(intent);
            }
        });

        // Click listener for Science quiz
        cvScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Science CardView clicked"); // Log the click event
                Intent intent = new Intent(QuizActivity.this, ScienceorEnglishmain.class);
                intent.putExtra(Constants.SUBJECT, getString(R.string.Science));
                Log.d("MainActivity", "Starting ScienceQuizActivity"); // Log starting of activity
                startActivity(intent);
            }
        });

        // Click listener for English quiz
        cvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "English CardView clicked"); // Log the click event
                Intent intent = new Intent(QuizActivity.this, ScienceorEnglishmain.class);
                intent.putExtra(Constants.SUBJECT, getString(R.string.English));
                Log.d("MainActivity", "Starting EnglishQuizActivity"); // Log starting of activity
                startActivity(intent);
            }
        });
    }
}
