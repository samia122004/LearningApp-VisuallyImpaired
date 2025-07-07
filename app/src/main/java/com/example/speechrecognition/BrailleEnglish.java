package com.example.speechrecognition;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
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

public class BrailleEnglish extends AppCompatActivity {
    private HashMap<String, String> brailleAlphabetMap;
    private boolean[] brailleDots = new boolean[6];
    private TextView brailleCharacter;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_braille_english);
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
                    // Set language to US English
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle error
                    }
                }
            }
        });
// Initialize Braille alphabet map
        initBrailleAlphabetMap();

        brailleCharacter = findViewById(R.id.braille_character); // Ensure this is defined in your XML layout

        // Define the buttons (dots) from the layout
        Button dot1 = findViewById(R.id.dot1);
        Button dot2 = findViewById(R.id.dot2);
        Button dot3 = findViewById(R.id.dot3);
        Button dot4 = findViewById(R.id.dot4);
        Button dot5 = findViewById(R.id.dot5);
        Button dot6 = findViewById(R.id.dot6);

        // Set up onClickListeners for each dot
        setDotListener(dot1, 0);
        setDotListener(dot2, 1);
        setDotListener(dot3, 2);
        setDotListener(dot4, 3);
        setDotListener(dot5, 4);
        setDotListener(dot6, 5);

    }
    // Initialize Braille alphabet mapping
    private void initBrailleAlphabetMap() {
        brailleAlphabetMap = new HashMap<>();
        brailleAlphabetMap.put("100000", "A");
        brailleAlphabetMap.put("101000", "B");
        brailleAlphabetMap.put("110000", "C");
        brailleAlphabetMap.put("110100", "D");
        brailleAlphabetMap.put("100100", "E");
        brailleAlphabetMap.put("111000", "F");
        brailleAlphabetMap.put("111100", "G");
        brailleAlphabetMap.put("101100", "H");
        brailleAlphabetMap.put("011000", "I");
        brailleAlphabetMap.put("011100", "J");
        brailleAlphabetMap.put("100010", "K");
        brailleAlphabetMap.put("101010", "L");
        brailleAlphabetMap.put("110010", "M");
        brailleAlphabetMap.put("110110", "N");
        brailleAlphabetMap.put("100110", "O");
        brailleAlphabetMap.put("111010", "P");
        brailleAlphabetMap.put("111110", "Q");
        brailleAlphabetMap.put("101110", "R");
        brailleAlphabetMap.put("011010", "S");
        brailleAlphabetMap.put("011110", "T");
        brailleAlphabetMap.put("100011", "U");
        brailleAlphabetMap.put("101011", "V");
        brailleAlphabetMap.put("011101", "W");
        brailleAlphabetMap.put("110011", "X");
        brailleAlphabetMap.put("110111", "Y");
        brailleAlphabetMap.put("100111", "Z");
        // Add more mappings as necessary
    }
    // Set up listener for dot clicks with additional accessibility features
    private void setDotListener(Button dotButton, final int index) {
        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the dot's state
                brailleDots[index] = !brailleDots[index];

                // Update button appearance
                v.setBackgroundTintList(getResources().getColorStateList(brailleDots[index] ? R.color.colorActiveDot : R.color.colorInactiveDot));

                // Provide haptic feedback using Vibrator
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(50); // Vibrate for 50 milliseconds
                }

                // Update accessibility description
                v.setContentDescription("Dot " + (index + 1) + " is now " + (brailleDots[index] ? "active" : "inactive"));

                // Update Braille character display
                updateBrailleCharacter();
            }
        });
    }

    // Update the displayed Braille character based on the selected dots
    private void updateBrailleCharacter() {
        StringBuilder brailleKey = new StringBuilder();
        for (boolean dot : brailleDots) {
            brailleKey.append(dot ? "1" : "0");
        }

        // Look up the corresponding Braille character
        String character = brailleAlphabetMap.get(brailleKey.toString());
        if (character != null) {
            brailleCharacter.setText("Braille Character: " + character);
            // Speak the character
            textToSpeech.speak(" " + character, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            brailleCharacter.setText("Braille Character: ?");
            // Speak the unknown character indication
            textToSpeech.speak("Not a valid character", TextToSpeech.QUEUE_FLUSH, null, null);
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