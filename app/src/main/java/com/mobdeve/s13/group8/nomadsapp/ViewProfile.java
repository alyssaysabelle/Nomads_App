package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivitySearchBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewProfileBinding;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity {
    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView ownProfileRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ActivityViewProfileBinding viewBinding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        User currentUser = (User) getIntent().getSerializableExtra("currentUser");
        viewBinding.ownUsernameTv.setText(currentUser.getUsername());

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Toast.makeText(ViewProfile.this, "No posts", Toast.LENGTH_SHORT).show();
                } else {
                    posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    Toast.makeText(ViewProfile.this, posts.size() + " posts", Toast.LENGTH_SHORT).show();

                    // Update the adapter with the new list of posts
                    OwnProfileAdapter adapter = new OwnProfileAdapter(posts);
                    ownProfileRecyclerView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(ViewProfile.this, "Error getting posts: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

         */

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
}