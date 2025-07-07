package com.example.speechrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class BrailleLessonsActivity extends AppCompatActivity {
    private ListView subjectsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_braille_lessons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        List<String> subjects = Arrays.asList("English Alphabets", "Maths Numbers");

        subjectsListView = findViewById(R.id.subjects_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);

        subjectsListView.setAdapter(adapter);

        subjectsListView.setOnItemClickListener((parent, view, position, id) -> {
            String subject = subjects.get(position);
            if (subject.equals("English Alphabets")) {
                startActivity(new Intent(BrailleLessonsActivity.this,BrailleEnglish.class));
            } else if (subject.equals("Maths Numbers")) {
                startActivity(new Intent(BrailleLessonsActivity.this,BrailleMaths.class));
            }
        });

    }
}