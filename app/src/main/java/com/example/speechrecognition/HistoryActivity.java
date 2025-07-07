// HistoryActivity.java
package com.example.speechrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speechrecognition.models.QuizResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<QuizResult> quizResults;
    private DatabaseReference historyRef;
    private String userId;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getWindow().setStatusBarColor(ContextCompat.getColor(HistoryActivity.this,R.color.ochre));

        recyclerView = findViewById(R.id.recyclerViewHistory);
        backButton=findViewById(R.id.imageViewHistory);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the drawer when back button is clicked
                Intent intent=new Intent(HistoryActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizResults = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid(); // Initialize userId here
            historyAdapter = new HistoryAdapter(quizResults, this, userId);
            recyclerView.setAdapter(historyAdapter);

            fetchHistory(userId);
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchHistory(String userId) {
        //String userId = auth.getCurrentUser().getUid();
        historyRef = FirebaseDatabase.getInstance().getReference("history").child(userId);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizResults.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot resultSnapshot : dataSnapshot.getChildren()) {
                        QuizResult result = resultSnapshot.getValue(QuizResult.class);
                        if (result != null) {
                            result.setResultId(resultSnapshot.getKey());
                            quizResults.add(result);
                            Log.d("HistoryData", "Subject: " + result.getSubject() + ", Score: " + result.getScore());
                        }
                    }
                    historyAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoryActivity.this, "No history available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HistoryActivity.this, "Failed to load history", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }
}
