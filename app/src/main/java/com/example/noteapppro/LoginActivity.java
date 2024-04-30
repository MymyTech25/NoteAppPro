package com.example.noteapppro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    ProgressBar progressBar;
    TextView createBtn;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.EditText_email);
        passwordET = findViewById(R.id.EditText_password);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);
        createBtn = findViewById(R.id.create_tv_btn);


        loginBtn.setOnClickListener(v-> loginUser());
        createBtn.setOnClickListener(v-> startActivity( new Intent(LoginActivity.this, NewAccountActivity.class)));

    }

    void loginUser(){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        boolean isValidated = validateText(email,password);
        if (!isValidated){
            return;
        }

        loginFirebase(email, password);
    }

    void loginFirebase(String email, String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                changeInProgress(false);
                Log.d("LOGIN_FIREBASE", "signInWithEmailAndPassword() completed");

                if (task.isSuccessful()) {
                    // Login successful
                    Log.d("LOGIN_FIREBASE", "Login successful");

                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // Email verified, go to the main activity
                        Log.d("LOGIN_FIREBASE", "Email verified. Redirecting to MainActivity");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Email not verified
                        Log.d("LOGIN_FIREBASE", "Email not verified. Showing toast message");
                        Utility.showToast(LoginActivity.this, "Please, verify your email :-(");
                    }
                } else {
                    // Login failed
                    Log.e("LOGIN_FIREBASE", "signInWithEmailAndPassword() failed: " + task.getException().getMessage());
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    boolean validateText(String email, String password){
        //validate the date which are input by the user, like email, password, ...

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.setError("Your email is invalid :-(");
            return false;
        }

        if (password.length()<8){
            passwordET.setError("Your password length is invalid :-(");
            return false;
        }

        return true;
    }



    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
}