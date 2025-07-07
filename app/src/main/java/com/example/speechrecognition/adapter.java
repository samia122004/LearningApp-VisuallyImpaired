package com.example.speechrecognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class adapter extends FirebaseRecyclerAdapter<model, adapter.SubjectViewHolder> {
    private String subjectType;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public adapter(@NonNull FirebaseRecyclerOptions<model> options, String subjectType) {
        super(options);
        this.subjectType = subjectType;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull adapter.SubjectViewHolder holder, int position, @NonNull model model) {
        //holder.chpNoTextView.setTextColor(Color.WHITE);  // Set text color for chapter number
        //holder.chpNameTextView.setTextColor(Color.WHITE);
        holder.bind(model);
    }



    class SubjectViewHolder extends RecyclerView.ViewHolder {
        private final TextView chpNoTextView;
        private final TextView chpNameTextView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            chpNoTextView = itemView.findViewById(R.id.chp_no_text_view);
            chpNameTextView = itemView.findViewById(R.id.chp_name_text_view);

        }

        public void bind(model chapter) {
            chpNoTextView.setText(chapter.getChpNo());
            chpNameTextView.setText(chapter.getChpName());
            itemView.setOnClickListener(v -> {
                // Determine the video URI based on the chapter name and subject type
                String videoUri = getVideoUriForSubjectAndChapter(chapter.getChpName());

                // Open VideoActivity with the chapter name
                if (videoUri != null) {
                    Intent intent = new Intent(itemView.getContext(), VideoActivity.class);
                    intent.putExtra("videoUri", videoUri);
                    itemView.getContext().startActivity(intent);
                } else {
                    Log.e("adapter", "Video URI is null for chapter: " + chapter.getChpName());
                }
            });
        }
        private String getVideoUriForSubjectAndChapter(String chapterName) {
            switch (subjectType) {
                case "Maths":
                    return getMathsVideoUri(chapterName);
                case "Science":
                    return getScienceVideoUri(chapterName);
                case "English":
                    return getEnglishVideoUri(chapterName);
                default:
                    return null;
            }
        }

        // Video URI mappings for Maths
        private String getMathsVideoUri(String chapterName) {
            switch (chapterName) {
                case "Introduction To Numbers":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.introtomaths;
                case "Addition and Subtraction":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.add_subtract;
                case "Multiplication":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.multiplication;
                case "Division":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.division;
                default:
                    return null;
            }
        }

        // Video URI mappings for Science
        private String getScienceVideoUri(String chapterName) {
            switch (chapterName) {
                case "Our Body":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.ourbody2;
                case "Our Food":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.ourfood1;
                case "Living and Non-Living Things":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.livingandnonliving;
                case "Plants Around Us":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.plantsaroundus;
                default:
                    return null;
            }
        }

        // Video URI mappings for English
        private String getEnglishVideoUri(String chapterName) {
            switch (chapterName) {
                case "A Happy Child":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.chp1happychild;
                case "Three Little Pigs":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.chp2threelittle;
                case "After a Bath":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.chp3afterbath;
                case "The Bubble, the Straw and the Shoe":
                    return "android.resource://" + itemView.getContext().getPackageName() + "/" + R.raw.chp4bubble;
                default:
                    return null;
            }
        }
    }
}
