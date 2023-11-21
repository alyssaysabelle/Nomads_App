package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityCreatePostBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePost extends AppCompatActivity {

    private TextView date;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ActivityCreatePostBinding viewBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        // back button
        viewBinding.backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // set current date
        String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date = findViewById(R.id.newDateTv);
        date.setText(currentDate);

        // post button
        viewBinding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caption = viewBinding.captionEt.getText().toString();
                String location = viewBinding.locationEt.getText().toString();
                String body = viewBinding.bodyEt.getText().toString();
                if (!caption.isEmpty() && !location.isEmpty() && !body.isEmpty()) {
                    Post post = new Post(currentUser, location, caption, body);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Posts").add(post)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(CreatePost.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle the failure to add the post to the database
                                Toast.makeText(CreatePost.this, "Failed to add post. Please try again.", Toast.LENGTH_SHORT).show();
                                Log.e("CreatePost", "Error adding post to Firestore", e);
                            });
                } else {
                    // Handle the case where caption or location is empty
                    Toast.makeText(CreatePost.this, "Caption and location cannot be empty", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }
}