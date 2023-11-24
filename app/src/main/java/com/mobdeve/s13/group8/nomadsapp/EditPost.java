package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityEditPostBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditPost extends AppCompatActivity {
    private EditText locationEt, captionEt, bodyEt;
    private ImageView imageView;
    private String location, caption, body;
    private Uri imageUri;
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
        //currentPost = (Post) getIntent().getSerializableExtra("currentPost");
        String postId = getIntent().getStringExtra("postId");

        captionEt = findViewById(R.id.captionEt);
        locationEt = findViewById(R.id.locationEt);
        bodyEt = findViewById(R.id.bodyEt);
        imageView = findViewById(R.id.imageView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // query for getting post based on id
        db.collection(MyFirestoreReferences.POSTS_COLLECTION)
                .document(postId).get().addOnSuccessListener(documentSnapshot -> {
                    currentPost = documentSnapshot.toObject(Post.class);
                    captionEt.setText(currentPost.getCaption());
                    locationEt.setText(currentPost.getLocation());
                    bodyEt.setText(currentPost.getBody());
                    if (currentPost.getImageId() != null)
                        Picasso.get().load(Uri.parse(currentPost.getImageId())).into(imageView);
                    else
                        imageView.setVisibility(View.GONE);
                });

        // set current date
        String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date = findViewById(R.id.newDateTv);
        date.setText(currentDate);

        imageBtn = findViewById(R.id.imageBtn);
        file = findViewById(R.id.file);

        // back button
        viewBinding.backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                caption = viewBinding.captionEt.getText().toString();
                location = viewBinding.locationEt.getText().toString();
                body = viewBinding.bodyEt.getText().toString();

                if (!caption.isEmpty() && !location.isEmpty() && !body.isEmpty()) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Post post = new Post(currentPost.getId(), currentUser, location, caption, body);

                    db.collection("Posts").document(currentPost.getId()).set(post)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(EditPost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();

                                // Start ViewSinglePostOwn without expecting a result
                                Intent intent = new Intent(EditPost.this, ViewSinglePostOwn.class);
                                intent.putExtra("postId", currentPost.getId());
                                intent.putExtra("currentUser", currentUser);
                                startActivity(intent);

                                // Finish EditPost activity
                                finish();
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
                            setResult(RESULT_OK);
                            Toast.makeText(EditPost.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditPost.this, "Failed to delete post. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("EditPost", "Error deleting post to Firestore", e);
                            });

                Intent intent = new Intent(EditPost.this, ViewProfile.class);
                startActivity(intent);
            }
        });

    }
}