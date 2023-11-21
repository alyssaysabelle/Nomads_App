package com.mobdeve.s13.group8.nomadsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityCreatePostBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreatePost extends AppCompatActivity {

    private TextView date, file;
    private User currentUser;
    private ImageButton imageBtn;
    public Uri postUri;
    private FirebaseStorage postStorage;
    private StorageReference postStorageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ActivityCreatePostBinding viewBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        postStorage=FirebaseStorage.getInstance();
        postStorageReference=postStorage.getReference();

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        imageBtn= findViewById(R.id.imageBtn);
        file= findViewById(R.id.file);


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
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

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

    private void choosePicture() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            postUri = data.getData();
            imageBtn.setImageURI(postUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = postStorageReference.child("images/" + randomKey);


        String fileName= getFileNameFromUri(postUri);
        file.setText(fileName);

        riverRef.putFile(postUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

                    // Get the download URL from the uploaded image
                    riverRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the user's profile picture URL in Firestore
                        updateUserProfilePicture(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Percentage: " + (int) progressPercent + "%");
                });
    }

    private void updateUserProfilePicture(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User currentUser = (User) getIntent().getSerializableExtra("currentUser");

        // Update the 'profilePictureUrl' field in the user document
        db.collection("Users").document(currentUser.getUsername())
                .update("imageId", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ViewProfile", "User profile picture URL updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("ViewProfile", "Error updating user profile picture URL", e);
                });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}