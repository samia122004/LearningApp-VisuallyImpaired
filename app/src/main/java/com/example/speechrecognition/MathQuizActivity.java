package com.example.speechrecognition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.speechrecognition.other.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MathQuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private RadioGroup radioGroup;
    private List<String> questions;
    private List<List<String>> optionsList; // List to store options for each question
    private int correctQuestion = 0;
    private ImageView backButton;
    private final HashMap<String, String> questionsAnswerMap = new HashMap<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz); // Ensure this matches your XML file name
        setTitle(Constants.MATH_QUIZ);
        getWindow().setStatusBarColor(ContextCompat.getColor(MathQuizActivity.this,R.color.ochre));

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.MATH_QUESTIONS);

        tvQuestion = findViewById(R.id.textViewQuestionMath); // Ensure the ID matches your XML layout
        tvQuestionNumber = findViewById(R.id.textViewCurrentQuestionMath); // Ensure the ID matches your XML layout
        btnNext = findViewById(R.id.btnNextQuestionMath); // Ensure the ID matches your XML layout
        radioGroup = findViewById(R.id.radioGroupMath);
        backButton=findViewById(R.id.imageViewQuizOption);// Ensure the ID matches your XML layout

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the drawer when back button is clicked
                Intent intent=new Intent(MathQuizActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Vibrate when a radio button is selected
                vibrate();
            }
        });

        btnNext.setOnClickListener(view -> {
            // Check if an answer is selected
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            vibrate();
            if (selectedRadioButtonId == -1) {
                Toast.makeText(MathQuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the selected answer is correct
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            String correctAnswer = questionsAnswerMap.get(questions.get(currentQuestionIndex));

            if (correctAnswer != null && correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                correctQuestion++;
            }

            currentQuestionIndex++;

            if (currentQuestionIndex < Constants.QUESTION_SHOWING) {
                displayNextQuestion();
            } else {
                Intent intentResult = new Intent(MathQuizActivity.this, FinalResultActivity.class);
                intentResult.putExtra(Constants.SUBJECT, Constants.MATH);
                intentResult.putExtra(Constants.CORRECT, correctQuestion);
                intentResult.putExtra(Constants.INCORRECT, Constants.QUESTION_SHOWING - correctQuestion);
                intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentResult);
                finish();
            }
        });

        fetchQuestionsFromFirebase();
    }

    // Fetch questions and options from Firebase
    private void fetchQuestionsFromFirebase() {
        // Initialize the lists
        questions = new ArrayList<>();
        optionsList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        // Extract the question, options, and correct answer
                        String question = questionSnapshot.child("question").getValue(String.class);
                        String correctAnswerKey = questionSnapshot.child("correctAnswer").getValue(String.class);

                        List<String> options = new ArrayList<>();
                        for (DataSnapshot optionSnapshot : questionSnapshot.child("options").getChildren()) {
                            options.add(optionSnapshot.getValue(String.class));
                        }

                        // Get the correct answer using the correct answer key
                        String correctAnswer = options.get(Integer.parseInt(correctAnswerKey) - 1);

                        // Add to the lists and map
                        questionsAnswerMap.put(question, correctAnswer);
                        questions.add(question);
                        optionsList.add(options);
                        Log.d("FirebaseData", "Question: " + question + ", Answer: " + correctAnswer + ", Options: " + options);
                    }
                    displayFirstQuestion();
                } else {
                    Toast.makeText(MathQuizActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MathQuizActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void displayFirstQuestion() {
        if (questions != null && !questions.isEmpty()) {
            updateQuestionUI();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            updateQuestionUI();
            if (currentQuestionIndex == Constants.QUESTION_SHOWING - 1) {
                btnNext.setText(R.string.finish); // Change button text to "Finish" on last question
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateQuestionUI() {
        tvQuestion.setText(questions.get(currentQuestionIndex) + " = ?");
        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + Constants.QUESTION_SHOWING);

        radioGroup.clearCheck(); // Clear the previous selection

        // Get the options for the current question
        List<String> options = optionsList.get(currentQuestionIndex);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setText(options.get(i));
        }
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //Log.d("VibrationTest", "Attempting to vibrate for " + duration + " ms");
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 100, 250, 0}; // Wait 0 ms, vibrate for 500 ms, wait 250 ms, vibrate for 500 ms
            vibrator.vibrate(pattern, -1); // -1 to not repeat
            //vibrator.vibrate(duration);
            Log.d("VibrationTest", "Vibration triggered.");
        } else {
            Toast.makeText(this, "Vibration not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

}
