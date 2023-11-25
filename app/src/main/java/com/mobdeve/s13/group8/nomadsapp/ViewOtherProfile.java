package com.mobdeve.s13.group8.nomadsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewOtherProfileBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewOtherProfile extends AppCompatActivity {
    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView otherProfileRecyclerView;
    private User currentUser;
    private User otherUser;
    private TextView username, followers;
    private ImageView profilePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_profile);

        ActivityViewOtherProfileBinding viewBinding = ActivityViewOtherProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // get data from intent
        Intent intent = getIntent();
        this.currentUser = (User) intent.getSerializableExtra("currentUser");
        this.otherUser = (User) intent.getSerializableExtra("otherUser");

        username = findViewById(R.id.otherUsernameTv);
        followers = findViewById(R.id.otherFollowerTv);
        profilePic = findViewById(R.id.otherProfilePicture);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        username.setText(otherUser.getUsername());

        // check if followers is null
        if (otherUser.getFollowers() == null)
            followers.setText("0 followers");
        else
            followers.setText(otherUser.getFollowers().size() + " followers");

        Picasso.get().load(otherUser.getImageId()).into(profilePic);


        // get post based on otheruser username
        db.collection("Posts").whereEqualTo("user.username", otherUser.getUsername()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    posts.add(post);
                }
                updateOwnProfileAdapter();
            }
        });

        viewBinding.userBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (currentUser.isFollowing(otherUser.getUsername())){
            viewBinding.followBtn.setText("Following");
            viewBinding.followBtn.setBackgroundColor(Color.parseColor("#818589"));
        }
        else{
            viewBinding.followBtn.setText("Follow");
            viewBinding.followBtn.setBackgroundColor(Color.parseColor("#657B9E"));
        }

        viewBinding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the current user is already following the other user
                if (currentUser.isFollowing(otherUser.getUsername())) {
                    // If already following, implement unfollow logic
                    currentUser.removeFollowing(otherUser.getUsername());
                    otherUser.removeFollower(currentUser.getUsername());
                    if (otherUser.getFollowers() == null)
                        followers.setText("0 followers");
                    else
                        followers.setText(otherUser.getFollowers().size() + " followers");
                    viewBinding.followBtn.setBackgroundColor(Color.parseColor("#657B9E"));
                    viewBinding.followBtn.setText("Follow"); // Change button text to "Follow" or set other appearance
                } else {
                    // If not following, implement follow logic
                    currentUser.addFollowing(otherUser.getUsername());
                    otherUser.addFollower(currentUser.getUsername());
                    followers.setText(otherUser.getFollowers().size() + " followers");
                    viewBinding.followBtn.setBackgroundColor(Color.parseColor("#818589"));
                    viewBinding.followBtn.setText("Following"); // Change button text to "Unfollow" or set other appearance
                }

                // Update user in the database
                FirebaseFirestore.getInstance().collection(MyFirestoreReferences.USERS_COLLECTION)
                        .document(currentUser.getUsername())
                        .set(currentUser)
                        .addOnSuccessListener(documentReference -> {
                            // Toast
                            String toastMessage = currentUser.isFollowing(otherUser.getUsername()) ? "You are now following " : "You have unfollowed ";
                            Toast.makeText(ViewOtherProfile.this, toastMessage + otherUser.getUsername(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Failed to update currentUser in the database
                            Toast.makeText(ViewOtherProfile.this, "Error updating user in the database", Toast.LENGTH_SHORT).show();
                        });

                FirebaseFirestore.getInstance().collection(MyFirestoreReferences.USERS_COLLECTION)
                        .document(otherUser.getUsername())
                        .set(otherUser)
                        .addOnFailureListener(e -> {
                            // Failed to update otherUser in the database
                            Toast.makeText(ViewOtherProfile.this, "Error updating other user in the database", Toast.LENGTH_SHORT).show();
                        });
            }
        });


        // recycler view
        this.otherProfileRecyclerView = findViewById(R.id.otherUserRv);
        this.otherProfileRecyclerView.setAdapter(new OthersProfileAdapter(posts, currentUser));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.otherProfileRecyclerView.setLayoutManager(layoutManagerV);
    }

    private void updateOwnProfileAdapter() {
        OthersProfileAdapter otherProfileAdapter = new OthersProfileAdapter(posts, currentUser);
        otherProfileRecyclerView.setAdapter(otherProfileAdapter);
        otherProfileAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload user data and update follower count
        loadUserData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Reload user data and update follower count
        loadUserData();
    }

    private void loadUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button followBtn = findViewById(R.id.followBtn);


        // Reload user data
        db.collection(MyFirestoreReferences.USERS_COLLECTION)
                .document(otherUser.getUsername())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Update the otherUser object with the latest data
                        otherUser = documentSnapshot.toObject(User.class);
                        updateFollowerCount();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to load user data
                    Toast.makeText(ViewOtherProfile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });

        db.collection(MyFirestoreReferences.USERS_COLLECTION)
                .document(currentUser.getUsername())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Update the otherUser object with the latest data
                        currentUser = documentSnapshot.toObject(User.class);
                        updateFollowerCount();

                        if (currentUser.isFollowing(otherUser.getUsername())){
                            followBtn.setText("Following");
                            followBtn.setBackgroundColor(Color.parseColor("#818589"));
                        }
                        else{
                            followBtn.setText("Follow");
                            followBtn.setBackgroundColor(Color.parseColor("#657B9E"));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to load user data
                    Toast.makeText(ViewOtherProfile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateFollowerCount() {
        // Update the follower count TextView based on the latest data
        if (otherUser.getFollowers() == null) {
            followers.setText("0 followers");
        } else {
            followers.setText(otherUser.getFollowers().size() + " followers");
        }
    }

}