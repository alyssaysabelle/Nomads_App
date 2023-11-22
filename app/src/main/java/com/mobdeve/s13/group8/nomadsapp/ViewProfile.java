package com.mobdeve.s13.group8.nomadsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivitySearchBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewProfileBinding;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ViewProfile extends AppCompatActivity {
    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView ownProfileRecyclerView;
    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_profile);
        ActivityViewProfileBinding viewBinding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        profilePic = findViewById(R.id.otherProfilePicture);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //ActivityViewProfileBinding viewBinding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        //setContentView(viewBinding.getRoot());

        User currentUser = (User) getIntent().getSerializableExtra("currentUser");
        viewBinding.ownUsernameTv.setText(currentUser.getUsername());
        // profilePic.setImageURI(Uri.parse(currentUser.getImageId()));
        Picasso.get().load(Uri.parse(currentUser.getImageId())).into(profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // check if user has posts in db
        db.collection("Posts").whereEqualTo("user.username", currentUser.getUsername()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    posts.add(post);
                }
                updateOwnProfileAdapter();
            }
        });

        // check if user has followers
        if (currentUser.getFollowers() == null) {
            viewBinding.ownFollowerTv.setText("0 followers");
        } else {
            viewBinding.ownFollowerTv.setText(currentUser.getFollowers().size() + " followers");
        }


        // back button
        viewBinding.userBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // logout button
        viewBinding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfile.this, Login.class);
                startActivity(intent);
            }
        });

        // recycler view
        this.ownProfileRecyclerView = findViewById(R.id.ownProfileRv);
        this.ownProfileRecyclerView.setAdapter(new OwnProfileAdapter(posts));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.ownProfileRecyclerView.setLayoutManager(layoutManagerV);
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
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void updateOwnProfileAdapter() {
        OwnProfileAdapter ownProfileAdapter = new OwnProfileAdapter(posts);
        ownProfileRecyclerView.setAdapter(ownProfileAdapter);
        ownProfileAdapter.notifyDataSetChanged();
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = storageReference.child("images/" + randomKey);

        riverRef.putFile(imageUri)
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

}