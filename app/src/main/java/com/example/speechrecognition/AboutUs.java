package com.example.speechrecognition;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);
        View aboutus = findViewById(R.id.about_us); // Replace with actual view ID
        if (aboutus != null) {
            // Now you can safely set properties or listeners on the view
            aboutus.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    return insets;
                }
            });
        } else {
            Log.e("AboutUs", "View is null, make sure the ID is correct");
        }
    }
}