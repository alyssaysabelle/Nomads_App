package com.mobdeve.s13.group8.nomadsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        viewBinding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // recycler view
        this.otherProfileRecyclerView = findViewById(R.id.otherUserRv);
        this.otherProfileRecyclerView.setAdapter(new OwnProfileAdapter(posts, currentUser));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.otherProfileRecyclerView.setLayoutManager(layoutManagerV);
    }

    private void updateOwnProfileAdapter() {
        OwnProfileAdapter otherProfileAdapter = new OwnProfileAdapter(posts, currentUser);
        otherProfileRecyclerView.setAdapter(otherProfileAdapter);
        otherProfileAdapter.notifyDataSetChanged();
    }
}