package com.mobdeve.s13.group8.nomadsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        username.setText(otherUser.getUsername());
        // check if followers is null
        if (otherUser.getFollowers() == null)
            followers.setText("0 followers");
        else
            followers.setText(otherUser.getFollowers().size() + " followers");

        Picasso.get().load(otherUser.getImageId()).into(profilePic);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        if (currentUser.isFollowing(otherUser.getUsername()))
            viewBinding.followBtn.setText("Following");
        else
            viewBinding.followBtn.setText("Follow");

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
                    viewBinding.followBtn.setText("Follow"); // Change button text to "Follow" or set other appearance
                } else {
                    // If not following, implement follow logic
                    currentUser.addFollowing(otherUser.getUsername());
                    otherUser.addFollower(currentUser.getUsername());
                    followers.setText(otherUser.getFollowers().size() + " followers");
                    viewBinding.followBtn.setText("Unfollow"); // Change button text to "Unfollow" or set other appearance
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
}