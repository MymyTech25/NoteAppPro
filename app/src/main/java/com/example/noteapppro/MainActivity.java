package com.example.noteapppro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton addNoteBtn;
    RecyclerView recycler_view;
    ImageButton menu_Btn;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.addNote_btn);
        recycler_view = findViewById(R.id.recyclerView);
        menu_Btn = findViewById(R.id.menuBtn);

        addNoteBtn.setOnClickListener((v)->startActivity(new Intent(MainActivity.this, NotesActivity.class)));
        menu_Btn.setOnClickListener((v) -> showMyMenu());

        setUpRecyclerView();
    }

    void showMyMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu_Btn);
        //It is possible to add the number of PopUps we want so that our menu is complete
        popupMenu.getMenu().add("LogOut");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="LogOut"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    void setUpRecyclerView(){
        Query query =  Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>().setQuery(query,NotesClass.class).build();
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recycler_view.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}