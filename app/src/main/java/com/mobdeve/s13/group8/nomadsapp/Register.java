package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActivityRegisterBinding viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // redirect to login
        TextView textView = findViewById(R.id.redirectLogin);

        String text = "Already have an account? Log In";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 24, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // register button
        viewBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if email, fullname, password, and username are not empty
                if (viewBinding.emailIn.getText().toString().isEmpty() ||
                        viewBinding.fullNameIn.getText().toString().isEmpty() ||
                        viewBinding.passwordIn.getText().toString().isEmpty() ||
                        viewBinding.usernameIn.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("Users").document(viewBinding.usernameIn.getText().toString())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        // Username already exists
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Username doesn't exist, create a new user
                                        User user = new User(
                                                viewBinding.fullNameIn.getText().toString(),
                                                viewBinding.usernameIn.getText().toString(),
                                                viewBinding.passwordIn.getText().toString(),
                                                viewBinding.emailIn.getText().toString(),
                                                R.drawable.user_profile
                                        );
                                        Toast.makeText(Register.this, viewBinding.emailIn.getText().toString(), Toast.LENGTH_SHORT).show();
                                        // Add the user to the database
                                        db.collection("Users").document(viewBinding.usernameIn.getText().toString())
                                                .set(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    // User added successfully
                                                    Toast.makeText(Register.this, "Account created!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle the failure to add the user to the database
                                                    Toast.makeText(Register.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
                                                    Log.e("Register", "Error adding user to Firestore", e);
                                                });
                                    }
                                } else {
                                    // Handle the failure to get data from Firestore
                                    Toast.makeText(Register.this, "Failed to check username. Please try again.", Toast.LENGTH_SHORT).show();
                                    Log.e("Register", "Error checking username in Firestore", task.getException());
                                }
                            });
                }

                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}