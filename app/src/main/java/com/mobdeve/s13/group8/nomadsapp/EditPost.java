package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityEditPostBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditPost extends AppCompatActivity {
    private EditText locationEt, captionEt, bodyEt;
    private String location, caption, body;
    private TextView date, file;
    private User currentUser;
    private Post currentPost;
    private ImageButton imageBtn;
    //public Uri postUri;
    private FirebaseStorage postStorage;
    private StorageReference postStorageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        ActivityEditPostBinding viewBinding = ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        postStorage = FirebaseStorage.getInstance();
        postStorageReference = postStorage.getReference();

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        currentPost = (Post) getIntent().getSerializableExtra("currentPost");


        imageBtn = findViewById(R.id.imageBtn);
        file = findViewById(R.id.file);


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

        //upload image button
        /*imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });*/

        // update button
        viewBinding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String captionEt = viewBinding.captionEt.getText().toString();
                String locationEt = viewBinding.locationEt.getText().toString();
                String bodyEt = viewBinding.bodyEt.getText().toString();
                if (!caption.isEmpty() && !location.isEmpty() && !body.isEmpty()) {

                    caption = captionEt.toString();
                    location = locationEt.toString();
                    body = bodyEt.toString();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Post post = new Post(currentUser, location, caption, body);

                    db.collection("Posts").document(currentPost.getId()).set(post)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(EditPost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditPost.this, "Failed to update post. Please try again.", Toast.LENGTH_SHORT).show();
                                Log.e("EditPost", "Error updating post to Firestore", e);
                            });
                } else {
                    // Handle the case where caption or location is empty
                    Toast.makeText(EditPost.this, "Caption and location cannot be empty", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        viewBinding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Posts").document(currentPost.getId()).delete()
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(EditPost.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditPost.this, "Failed to delete post. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("EditPost", "Error deleting post to Firestore", e);
                            });
                finish();
            }
        });

    }
}