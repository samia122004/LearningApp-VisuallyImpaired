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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Maths extends AppCompatActivity {
    RecyclerView recyclerView;
    private adapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths);

        getWindow().setStatusBarColor(ContextCompat.getColor(Maths.this,R.color.ochre));

        recyclerView = findViewById(R.id.rv);
        progressBar=findViewById(R.id.progress_bar);
        if (recyclerView == null) {
            throw new NullPointerException("RecyclerView not found. Check the layout file and ID.");
        }

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("chapters");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object data = dataSnapshot.getValue();
                    Log.d("FirebaseData", "Data: " + data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(database, model.class)
                        .build();
        adapter = new adapter(options, "Maths");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                        recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        String chapterName = adapter.getItem(position).getChpName();
                        String videoUri = null;

                        // Mapping science chapters to their corresponding videos
                        if (chapterName.equals("Introduction To Numbers")) {
                            videoUri = "android.resource://" + getPackageName() + "/" + R.raw.introtomaths;
                        } else if (chapterName.equals("Addition and Subtraction")) {
                            videoUri = "android.resource://" + getPackageName() + "/" + R.raw.add_subtract;
                        }else if (chapterName.equals("Multiplication")) {
                            videoUri = "android.resource://" + getPackageName() + "/" + R.raw.multiplication;
                        }else if (chapterName.equals("Division")) {
                            videoUri = "android.resource://" + getPackageName() + "/" + R.raw.division;
                        }

                        if (videoUri != null) {
                            Intent intent = new Intent(Maths.this, VideoActivity.class);
                            intent.putExtra("origin", "Maths");
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
                })
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to Maths activity
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
            // Hide loading indicator when data is loaded
            if (adapter.getItemCount() > 0) {
                progressBar.setVisibility(View.GONE);

            }
        }


            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                // Check if data is loaded and visible
                if (adapter.getItemCount() > 0) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // Check if data is loaded and visible
                if (adapter.getItemCount() > 0) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                // Check if data is loaded and visible
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
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private final Maths.RecyclerTouchListener.ClickListener clickListener;
        public RecyclerTouchListener(Maths maths, RecyclerView recyclerView, Maths.RecyclerTouchListener.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(maths, new GestureDetector.SimpleOnGestureListener() {
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