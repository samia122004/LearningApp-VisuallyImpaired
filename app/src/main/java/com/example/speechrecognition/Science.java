package com.example.speechrecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.load.model.Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Science extends AppCompatActivity {
    RecyclerView recyclerView;
    private adapter adapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);

        getWindow().setStatusBarColor(ContextCompat.getColor(Science.this, R.color.ochre));

        // Initialize RecyclerView and ProgressBar
        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progress_bar);

        // Check if recyclerView is correctly initialized
        if (recyclerView == null) {
            throw new NullPointerException("RecyclerView not found. Check the layout file and ID.");
        }

        // Initialize Firebase Database reference for "science" data
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("science");

        // Listen for data change from Firebase
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object data = dataSnapshot.getValue();
                    Log.d("FirebaseData", "Data: " + data);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });

        // FirebaseRecyclerOptions for RecyclerView adapter
        FirebaseRecyclerOptions<model> options = new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(database, model.class)
                .build();
        adapter = new adapter(options, "Science");

        // Set up RecyclerView layout and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        // Handling touch events on RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String chapterName = adapter.getItem(position).getChpName();
                //String videoUri = getVideoUriForChapter(chapterName);

                String videoUri = null;

                // Mapping science chapters to their corresponding videos
                if (chapterName.equals("Our Body")) {
                    videoUri = "android.resource://" + getPackageName() + "/" + R.raw.ourbody2;
                } else if (chapterName.equals("Our Food")) {
                    videoUri = "android.resource://" + getPackageName() + "/" + R.raw.ourfood1;
                }else if (chapterName.equals("Living and Non-Living Things")) {
                    videoUri = "android.resource://" + getPackageName() + "/" + R.raw.livingandnonliving;
                }else if (chapterName.equals("Plants Around Us")) {
                    videoUri = "android.resource://" + getPackageName() + "/" + R.raw.plantsaroundus;
                }

                if (videoUri != null) {
                    Intent intent = new Intent(Science.this, VideoActivity.class);
                    intent.putExtra("origin", "Science");
                    intent.putExtra("videoUri", videoUri);
                    startActivity(intent);
                } else {
                    Log.e("ScienceActivity", "Video URI is null for chapter: " + chapterName);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                // Optional: Handle long click
            }
        }));

    }

    // Helper method to map chapters to video URIs
    private String getVideoUriForChapter(String chapterName) {
        HashMap<String, Integer> chapterVideoMap = new HashMap<>();
        chapterVideoMap.put("Our Body", R.raw.ourbody2);
        chapterVideoMap.put("Our Food", R.raw.ourfood1);
        chapterVideoMap.put("Living and Non-Living Things", R.raw.livingandnonliving);
        chapterVideoMap.put("Plants Around Us", R.raw.plantsaroundus);

        if (chapterVideoMap.containsKey(chapterName)) {
            return "android.resource://" + getPackageName() + "/" + chapterVideoMap.get(chapterName);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish(); // Optionally finish this activity
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() > 0) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // RecyclerTouchListener to handle touch events on RecyclerView items
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private final ClickListener clickListener;

        public RecyclerTouchListener(Science context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            // No need to handle touch events here
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // No need to handle this event
        }

        public interface ClickListener {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
    }


}