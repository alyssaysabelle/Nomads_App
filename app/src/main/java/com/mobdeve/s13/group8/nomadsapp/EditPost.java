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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityEditPostBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.util.UUID;

public class EditPost extends AppCompatActivity {
    private EditText locationEt, captionEt, bodyEt;
    private ImageView imageView;
    private String location, caption, body, dateTv;
    private Uri imageUri;
    private TextView date, file;
    private User currentUser;
    private Post currentPost;
    private ImageButton imageBtn;
    public Uri postUri;
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
        file = findViewById(R.id.file);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // query for getting post based on id
        db.collection(MyFirestoreReferences.POSTS_COLLECTION)
                .document(postId).get().addOnSuccessListener(documentSnapshot -> {
                    currentPost = documentSnapshot.toObject(Post.class);
                    captionEt.setText(currentPost.getCaption());
                    locationEt.setText(currentPost.getLocation());
                    bodyEt.setText(currentPost.getBody());

                    if (currentPost.getImageId() != null) {
                        imageView = findViewById(R.id.imageView);
                        Picasso.get().load(Uri.parse(currentPost.getImageId())).into(imageView);
                    }
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

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
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

                    Date formattedDate = null;
                    try {
                        formattedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(currentDate);
                    } catch (ParseException e) {
                        e.printStackTrace(); // Handle the parse exception as needed
                    }

                    post.setDate(formattedDate);

                    db.collection("Posts").document(currentPost.getId()).set(post)
                            .addOnSuccessListener(documentReference -> {
                                if(postUri != null){
                                    post.setImageId(imageUri.toString());
                                    db.collection("Posts").document(currentPost.getId()).update("imageId", imageUri.toString());
                                }
                                Toast.makeText(EditPost.this, "Post updated successfully", Toast.LENGTH_SHORT).show();

                                // Start ViewSinglePostOwn without expecting a result
                                Intent intent = new Intent(EditPost.this, ViewSinglePostOwn.class);
                                intent.putExtra("postId", currentPost.getId());
                                intent.putExtra("currentUser", currentUser);
                                startActivity(intent);

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
                            Toast.makeText(EditPost.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditPost.this, "Failed to delete post. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("EditPost", "Error deleting post to Firestore", e);
                            });

                Intent intent = new Intent(EditPost.this, ViewProfile.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
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
                        imageUri = uri;
                        Picasso.get().load(uri).into(imageView);
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