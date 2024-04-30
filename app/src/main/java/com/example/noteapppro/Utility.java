package com.example.noteapppro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;


//This class is the one dedicated to the tools that I will very often use in the code like the "Toast"
// it will allow me to save time, because it will be enough to call it in my activities
public class Utility {
    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes (){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return   FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }

    static String timestampToString (Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
