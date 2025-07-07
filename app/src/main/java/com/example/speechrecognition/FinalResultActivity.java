// FinalResultActivity.java
package com.example.speechrecognition;

import static com.example.speechrecognition.other.Constants.totalQuestions;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.speechrecognition.models.QuizResult;
import com.example.speechrecognition.other.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FinalResultActivity extends AppCompatActivity {

    private TextView tvTotalQuestions, tvCorrectAnswers, tvScore, tvIncorrectAnswers, tvtimestamp;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        getWindow().setStatusBarColor(ContextCompat.getColor(FinalResultActivity.this,R.color.ochre));// Ensure this matches your XML file name

        // Initialize UI components
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvScore = findViewById(R.id.tvScore);
        tvIncorrectAnswers = findViewById(R.id.tvIncorrectAnswers);
        tvtimestamp = findViewById(R.id.tvtimestamp);


        // Get intent extras (passed from the quiz activity)
        String subject = getIntent().getStringExtra(Constants.SUBJECT);
        int correct = getIntent().getIntExtra(Constants.CORRECT, 0);
        int incorrect = getIntent().getIntExtra(Constants.INCORRECT, 0);
        int totalQuestions = correct + incorrect;
        long timestamp = System.currentTimeMillis();
        int score = totalQuestions > 0 ? (int) (((double) correct / totalQuestions) * 100) : 0; // Avoid division by zero

        // Set the values
        tvTotalQuestions.setText("Total Questions: " + totalQuestions);
        tvIncorrectAnswers.setText("Incorrect Answers: " + incorrect);
        tvCorrectAnswers.setText("Correct Answers: " + correct);
        tvScore.setText("Score: " + score + "%");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date(timestamp));
        tvtimestamp.setText("Date: "+ date.split(" ")[0] + "   Time: " + date.split(" ")[1]);

        // Save result to Firebase history
        saveResultToHistory(subject, correct, incorrect, score, totalQuestions, timestamp);

        //Initailize textToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US); // Set language
                    onTestComplete(score);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    // Save quiz result to Firebase history
    private void saveResultToHistory(String subject, int correct, int incorrect, int score, int totalQuestions, long timestamp) {
        // Get current user ID from Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("history").child(userId);

        // Create a unique ID for each quiz result
        String resultId = historyRef.push().getKey();
        // Convert timestamp to a readable date format
         // Converting timestamp to readable date
        // Prepare result data
        Map<String, Object> newResultData = new HashMap<>();
        newResultData.put("subject", subject);
        newResultData.put("correct", correct);
        newResultData.put("incorrect", incorrect);
        newResultData.put("score", score);
        newResultData.put("totalQuestions",totalQuestions);
        newResultData.put("timestamp", timestamp);

        // Save the result in Firebase
        if (resultId != null) {
            QuizResult resultData = new QuizResult(subject, score, correct, incorrect, timestamp, totalQuestions);
            historyRef.child(resultId).setValue(resultData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(FinalResultActivity.this, "Result saved in history", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FinalResultActivity.this, "Failed to save result", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Failed to generate result ID", Toast.LENGTH_SHORT).show();
        }
    }
    private void onTestComplete(int score) {
        String feedbackMessage;

        if (score >= 0 && score <= 30) {
            feedbackMessage = "You scored " + score + "Can do better!";
        } else if (score >= 40 && score <= 50) {
            feedbackMessage = "Great! You scored " + score + ".";
        } else if (score > 50) {
            feedbackMessage = "Excellent! You scored " + score + ". Keep it up!";
        } else {
            feedbackMessage = "Invalid score.";
        }

        // Speak the feedback message
        speak(feedbackMessage);
    }

    private void speak(String message) {
        // Stop any ongoing speech before speaking the new message
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown TTS when activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
