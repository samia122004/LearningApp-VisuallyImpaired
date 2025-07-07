package com.example.speechrecognition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.speechrecognition.other.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScienceorEnglishmain extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber;
    private Button btnNext;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private List<String> questions = new ArrayList<>();
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap = new HashMap<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scienceorenglishquiz);

        getWindow().setStatusBarColor(ContextCompat.getColor(ScienceorEnglishmain.this,R.color.ochre));

        Intent intent = getIntent();
        String subject = intent.getStringExtra(Constants.SUBJECT);

        // Setup the title based on the subject (English or Science)
        TextView tvTitle = findViewById(R.id.textView26);
        if (subject.equals(Constants.ENGLISH)) {
            tvTitle.setText(Constants.LITERATURE_QUIZ);
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.ENGLISH_QUESTIONS);
        } else if (subject.equals(Constants.SCIENCE)) {
            tvTitle.setText(Constants.GEOGRAPHY_QUIZ);
            databaseReference = FirebaseDatabase.getInstance().getReference(Constants.SCIENCE_QUESTIONS);
        } else {
            Toast.makeText(this, "Invalid subject", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        tvQuestion = findViewById(R.id.textView78);
        tvQuestionNumber = findViewById(R.id.textView18);
        btnNext = findViewById(R.id.btnNextQuestionLiteratureAndGeography);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNextQuestion(subject);
            }
        });

        findViewById(R.id.imageViewStartQuizGeographyOrLiterature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Fetch questions from Firebase
        fetchQuestionsFromFirebase();
    }

    // Handle the Next Button click to proceed to the next question or finish the quiz
    private void handleNextQuestion(String subject) {
        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        if (selectedRadioButton != null) {
            boolean isCorrect = questionsAnswerMap.get(questions.get(currentQuestionIndex)).get(selectedRadioButton.getText().toString());

            if (isCorrect) {
                correctQuestion++;
            }

            currentQuestionIndex++;

            if (currentQuestionIndex < questions.size()) {
                displayNextQuestions();
            } else {
                // Quiz is over, show result page
                Intent intentResult = new Intent(ScienceorEnglishmain.this, FinalResultActivity.class);
                intentResult.putExtra(Constants.SUBJECT, subject);
                intentResult.putExtra(Constants.CORRECT, correctQuestion);
                intentResult.putExtra(Constants.INCORRECT, questions.size() - correctQuestion);
                startActivity(intentResult);
                finish();
            }
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
        }
    }

    // Fetch questions from Firebase
    private void fetchQuestionsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        String question = questionSnapshot.getKey();
                        Map<String, Boolean> options = new HashMap<>();
                        for (DataSnapshot optionSnapshot : questionSnapshot.getChildren()) {
                            options.put(optionSnapshot.getKey(), optionSnapshot.getValue(Boolean.class));
                        }
                        questionsAnswerMap.put(question, options);
                    }
                    questions = new ArrayList<>(questionsAnswerMap.keySet());
                    displayData();
                } else {
                    Toast.makeText(ScienceorEnglishmain.this, "No questions available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScienceorEnglishmain.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display the current question and set the answers to radio buttons
    @SuppressLint("SetTextI18n")
    private void displayNextQuestions() {
        setAnswersToRadioButton();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText("Question: " + (currentQuestionIndex + 1) + " / " + questions.size());

        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setText(getText(R.string.finish)); // Change button text on last question
        } else {
            btnNext.setText(getText(R.string.next)); // Default to "Next" for other questions
        }
    }

    // Initially display the first question and answers
    private void displayData() {
        displayNextQuestions();
    }

    // Set the radio buttons with the current question's options
    private void setAnswersToRadioButton() {
        List<String> options = new ArrayList<>(questionsAnswerMap.get(questions.get(currentQuestionIndex)).keySet());

        radioButton1.setText(options.size() > 0 ? options.get(0) : "");
        radioButton2.setText(options.size() > 1 ? options.get(1) : "");
        radioButton3.setText(options.size() > 2 ? options.get(2) : "");
        radioButton4.setText(options.size() > 3 ? options.get(3) : "");
    }
}
