package com.example.casualleaveapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText emaileditText, passwordeditText;
    Button loginButton;
    FirebaseAuth mAuth;
    final String TAG = "Login Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginBUTTON);
        emaileditText = findViewById(R.id.editText);
        passwordeditText = findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
        Log.d("Login ACtivity", "Oncreate called");
        loginfirebase();

    }

    private void loginfirebase() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginProgressbar.setVisibility(View.VISIBLE);
                String email = emaileditText.getText().toString();
                String password = passwordeditText.getText().toString();
                if (email.isEmpty()) {
                    //loginProgressbar.setVisibility(View.GONE);
                    emaileditText.setError("Please Enter Valid Email");
                }
                if (password.isEmpty()) {
                    //loginProgressbar.setVisibility(View.GONE);
                    passwordeditText.setError("Please Enter Valid Email");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user.isEmailVerified()) {
                                            Toast.makeText(Login.this, "Sign In Successful", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Login.this, teacherPage.class);
                                            startActivity(intent);
                                            //loginProgressbar.setVisibility(View.GONE);

                                        } else {
                                            //loginProgressbar.setVisibility(View.GONE);
                                            Toast.makeText(Login.this, "Login Unsuccessful, Please Verify Email", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                        }


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }


                                    // ...
                                }

                            });
                }
            }
        });

    }


}
