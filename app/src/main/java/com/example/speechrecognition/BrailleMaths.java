package com.example.speechrecognition;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Locale;

public class BrailleMaths extends AppCompatActivity {
    private HashMap<String, String> brailleNumberMap;
    private boolean[] brailleDots = new boolean[6];
    private TextView selectedNumber;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_braille_maths);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle error
                    }
                }
            }
        });

        // Initialize Braille number mapping
        initBrailleNumberMap();
        selectedNumber = findViewById(R.id.selected_number);

        // Define the buttons (dots) from the layout
        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);

        // Set up listeners for each dot
        setDotListener(dot1, 0);
        setDotListener(dot2, 1);
        setDotListener(dot3, 2);
        setDotListener(dot4, 3);
        setDotListener(dot5, 4);
        setDotListener(dot6, 5);
    }

    // Initialize Braille number mapping
    private void initBrailleNumberMap() {
        brailleNumberMap = new HashMap<>();
        brailleNumberMap.put("100000", "1"); // ⠁
        brailleNumberMap.put("101000", "2"); // ⠃
        brailleNumberMap.put("110000", "3"); // ⠉
        brailleNumberMap.put("110100", "4"); // ⠙
        brailleNumberMap.put("100100", "5"); // ⠑
        brailleNumberMap.put("111000", "6"); // ⠋
        brailleNumberMap.put("111100", "7"); // ⠛
        brailleNumberMap.put("101100", "8"); // ⠓
        brailleNumberMap.put("011000", "9"); // ⠊
        brailleNumberMap.put("011100", "0");//  ⠚
        }

    // Set up listener for dot clicks
    private void setDotListener(Button dotButton, final int index) {
        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the dot's state
                brailleDots[index] = !brailleDots[index];

                // Update button appearance (you might want to change the button color)
                dotButton.setBackgroundColor(brailleDots[index] ? getResources().getColor(android.R.color.holo_green_light) : getResources().getColor(android.R.color.holo_red_light));

                // Update the selected number based on the current Braille dots
                updateSelectedNumber();
            }
        });

        // Add touch listener for haptic feedback
        dotButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Provide haptic feedback using Vibrator
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null && vibrator.hasVibrator()) {
                        vibrator.vibrate(50); // Vibrate for 50 milliseconds
                    }
                }
                return false; // Return false to allow other events to proceed
            }
        });
    }

    // Update the selected number based on the current Braille dots
    private void updateSelectedNumber() {
        StringBuilder brailleKey = new StringBuilder();
        for (boolean dot : brailleDots) {
            brailleKey.append(dot ? "1" : "0");
        }

        String number = brailleNumberMap.get(brailleKey.toString());
        selectedNumber.setText("Selected Number: " + (number != null ? number : "?"));
        if (number != null) {
            textToSpeech.speak(" " + number, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak("This is not a valid number.", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown the TextToSpeech engine when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}