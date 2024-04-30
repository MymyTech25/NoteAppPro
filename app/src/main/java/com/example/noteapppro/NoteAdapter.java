package com.example.noteapppro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<NotesClass, NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<NotesClass> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NotesClass note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));

        //To be able to edit the text when you click on it
        holder.itemView.setOnClickListener((v)-> {
            Intent intent= new Intent(context, NotesActivity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);

            //To be able to take the id of what we clicked
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });


    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
       return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, contentTextView, timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.note_title_TV);
            contentTextView = itemView.findViewById(R.id.note_content_TV);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_TV);
        }
    }
}
