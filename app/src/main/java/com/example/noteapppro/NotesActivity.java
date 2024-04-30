package com.example.noteapppro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesActivity extends AppCompatActivity {

        EditText notesTitleET, contentET;
        ImageButton  saveNotesBtn;
        TextView titleTV;
        String title, content, docId;
        Boolean isEditMode = false;

        TextView deleteNote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesTitleET = findViewById(R.id.notesTitle);
        contentET = findViewById(R.id.contentText);
        saveNotesBtn = findViewById(R.id.saveBtn);
        titleTV = findViewById(R.id.title);
        deleteNote = findViewById(R.id.delte_Tv_btn);

        //to receive the data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        notesTitleET.setText(title);
        contentET.setText(content);
        if (isEditMode){
            titleTV.setText("Edit your note");
            deleteNote.setVisibility(View.VISIBLE);
        }



        saveNotesBtn.setOnClickListener( (v)-> saveYourNotes());
        deleteNote.setOnClickListener((v)-> deleteNotesInFirebase());

    }

    void deleteNotesInFirebase(){
        DocumentReference documentReference;

            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note deleted from firebase
                    Utility.showToast(NotesActivity.this, "Your note has been successfully deleted");
                    finish();
                } else {
                    //note failed to be deleted
                    Utility.showToast(NotesActivity.this, "Failure to delete your note");

                }
            }
        });

    }

    void saveYourNotes(){
        String notesTitle = notesTitleET.getText().toString();
        String content = contentET.getText().toString();

        if (notesTitle==null || notesTitle.isEmpty()){
            notesTitleET.setError("Please put a title :-(");
            return;

        }
        NotesClass notesClass = new NotesClass();
        notesClass.setTitle(notesTitle);
        notesClass.setContent(content);
        notesClass.setTimestamp(Timestamp.now());

        saveNotesInFirebase(notesClass);
    }

    void saveNotesInFirebase(NotesClass notesClass){
        DocumentReference documentReference;
        if (isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        } else {
            //create a new note
            documentReference = Utility.getCollectionReferenceForNotes().document();

        }
        documentReference.set(notesClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note added in firebase
                    Utility.showToast(NotesActivity.this, "Your note has been successfully added");
                    finish();
                } else {
                    //note failed to add
                    Utility.showToast(NotesActivity.this, "Failure to add your note");

                }
            }
        });
    }
}