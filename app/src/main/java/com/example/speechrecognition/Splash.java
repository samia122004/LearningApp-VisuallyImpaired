package com.example.speechrecognition;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    private static final long ANIMATION_TIME = 5000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        mAuth = FirebaseAuth.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (isLoggedIn) {
                    // User is logged in, open Dashboard
                    Intent intent = new Intent(Splash.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {*/
                    // User is not logged in, show OTP activity
                    //Intent intent = new Intent(Splash.this, sendOTP.class);
                    //startActivity(intent);
                    finish();
                //}
                // Start the sendOTP activity
                /*SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                String firstTime = preferences.getString("FirstTimeInstall", "");

                Intent intent;
                if (firstTime.equals("Yes")) {
                    // Not first time, show Dashboard if already verified
                    intent = new Intent(Splash.this, Dashboard.class);
                } else {
                    // First time, show OTP screen
                    intent = new Intent(Splash.this, sendOTP.class);
                }*/
            }
        }, ANIMATION_TIME); // Delay duration in milliseconds
    }



 /*   private void checkPhoneNumberExistence() {
        // Replace with your actual phone number check logic
        String userPhoneNumber = mAuth.getCurrentUser().getPhoneNumber(); // Or retrieve from SharedPreferences

        if (userPhoneNumber != null) {
            // Simulate a network request to check if the phone number exists in the database
            // For demonstration, let's assume we have a method `isPhoneNumberValid`
            if (isPhoneNumberValid(userPhoneNumber)) {
                // Phone number exists, open Dashboard
                Intent intent = new Intent(Splash.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Phone number does not exist, show OTP activity
                Intent intent = new Intent(Splash.this, sendOTP.class);
                startActivity(intent);
                finish();
            }
        } else {
            // Phone number is null, show OTP activity
            Intent intent = new Intent(Splash.this, sendOTP.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // Implement this method to check the phone number existence
        // This might involve making a network request to your backend
        // For now, return false for demonstration purposes
        return false;
}*/
}