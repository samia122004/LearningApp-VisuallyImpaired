// HistoryAdapter.java
package com.example.speechrecognition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speechrecognition.models.QuizResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<QuizResult> quizResults;
    DatabaseReference historyRef;
    private Context context;

    public HistoryAdapter(List<QuizResult> quizResults,  Context context, String userId) {
        this.quizResults = quizResults;
        this.context = context;
        this.historyRef = FirebaseDatabase.getInstance().getReference("history").child(userId);

    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizResult result = quizResults.get(position);
        holder.tvSubject.setText("Subject: " + result.getSubject());
        holder.tvScore.setText("Score: " + result.getScore() + "%");
        holder.tvCorrect.setText("Correct: " + result.getCorrect());
        holder.tvIncorrect.setText("Incorrect: " + result.getIncorrect());

        // Format timestamp to readable date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(result.getTimestamp()));
        holder.tvTimestamp.setText("Date: " + date.split(" ")[0] + "   Time: " + date.split(" ")[1]);

        holder.deleteIcon.setOnClickListener(v -> {
            // Show confirmation dialog (optional)
            String resultId = result.getResultId();
            //String resultId = result.getTimestamp() + "";  // Assuming the timestamp is used as a unique ID or fetch actual unique ID if present
            if (resultId != null) {
                // Remove the item from Firebase
                historyRef.child(resultId).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Test result deleted", Toast.LENGTH_SHORT).show();
                        quizResults.remove(position);  // Remove from the local list
                        notifyItemRemoved(position);   // Notify RecyclerView about the removed item
                    } else {
                        Toast.makeText(context, "Failed to delete test result", Toast.LENGTH_SHORT).show();
                    }
                });
                //});
            } else {
                Toast.makeText(context, "Result ID is null", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return quizResults.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvScore, tvCorrect, tvIncorrect, tvTimestamp;
        ImageView deleteIcon;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvCorrect = itemView.findViewById(R.id.tvCorrect);
            tvIncorrect = itemView.findViewById(R.id.tvIncorrect);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
