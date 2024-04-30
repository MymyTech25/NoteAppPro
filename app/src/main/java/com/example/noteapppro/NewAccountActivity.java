package com.example.noteapppro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class NewAccountActivity extends AppCompatActivity {
    EditText emailET, passwordET, confirmPasswordET;
    ProgressBar progressBar;
    TextView loginBtn;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        emailET = findViewById(R.id.EditText_email);
        passwordET = findViewById(R.id.EditText_password);
        confirmPasswordET = findViewById(R.id.EditText_confirmPassword);
        createBtn = findViewById(R.id.create_btn);
        progressBar = findViewById(R.id.progressBar);
        loginBtn = findViewById(R.id.login_tv_btn);

        createBtn.setOnClickListener(v-> createAccount() );
        loginBtn.setOnClickListener(v-> finish());


    }

    void createAccount(){

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        boolean isValidated = validateText(email,password, confirmPassword);
        if (!isValidated){
            return;
        }

        createAccountFirebase(email, password);


    }

    void createAccountFirebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(NewAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //creating account is done
                            Utility.showToast(NewAccountActivity.this,"Success !! Check your email to verify");

                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            //failure
                            Utility.showToast(NewAccountActivity.this, task.getException().getLocalizedMessage());

                        }

                    }
                });

    }

    boolean validateText(String email, String password, String confirmPassword){
        //validate the date which are input by the user, like email, password, ...

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.setError("Your email is invalid :-(");
            return false;
        }

        if (password.length()<8){
            passwordET.setError("Your password length is invalid :-(");
            return false;
        }

        if (!password.equals(confirmPassword)){
            confirmPasswordET.setError("Password not matched :-(");
            return false;
        }

        return true;
    }



    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createBtn.setVisibility(View.VISIBLE);
        }
    }

}