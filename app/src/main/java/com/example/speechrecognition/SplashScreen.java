package com.example.speechrecognition;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final long ANIMATION_TIME = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                //String userPhoneNumber = sharedPreferences.getString("userPhoneNumber", null);

              /*  if (userPhoneNumber == null) {
                    // No phone number stored, open sendOTP
                    Intent intent = new Intent(Splash.this, sendOTP.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Check if the phone number still exists in the database
                    checkPhoneNumberExistence(userPhoneNumber);
                }*/

               /* Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                startActivity(intent);
                finish();*/
                if (isLoggedIn) {
                    // User is logged in, open Dashboard
                    Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is not logged in, show OTP activity
                    //Intent intent = new Intent(SplashScreen.this, sendOTP.class);
                   // startActivity(intent);
                    finish();
                }
            }
        }, ANIMATION_TIME);
       /* if (isLoggedIn) {
            // User is logged in, open Dashboard
            Intent intent = new Intent(SplashScreen.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else {
            // User is not logged in, show OTP activity
            Intent intent = new Intent(SplashScreen.this, sendOTP.class);
            startActivity(intent);
            finish();
        }*/
       /* private void checkPhoneNumberExistence(String phoneNumber) {
            // Implement your logic to check phone number existence from the backend
            // For demonstration purposes, let's assume this method always returns false

            boolean isPhoneNumberValid = false; // Replace with actual check

            if (isPhoneNumberValid) {
                // Phone number is valid, open Dashboard
                Intent intent = new Intent(Splash.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Phone number is not valid, open sendOTP
                Intent intent = new Intent(Splash.this, sendOTP.class);
                startActivity(intent);
                finish();
            }*/
    }
}
