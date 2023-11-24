package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityHomeScreenBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView homeRecyclerView;
    private User currentUser;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ActivityHomeScreenBinding viewBinding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // get current user
        username = getIntent().getStringExtra(String.valueOf(IntentKeys.USERNAME));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(MyFirestoreReferences.USERS_COLLECTION);

        usersRef.whereEqualTo(MyFirestoreReferences.USERNAME_FIELD, username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    currentUser = document.toObject(User.class);
                }
            }
        });

        // recycler view
        this.homeRecyclerView = findViewById(R.id.homeRv);
        this.homeRecyclerView.setAdapter(new HomeAdapter(posts, currentUser));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.homeRecyclerView.setLayoutManager(layoutManagerV);

        // user profile button
        viewBinding.userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass current user to view profile
                Intent intent = new Intent(HomeScreen.this, ViewProfile.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        // search button
        viewBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Search.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        // create post button
        viewBinding.createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, CreatePost.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPostData();
    }

    private void loadPostData() {
        // get all posts aside from own post
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference postsRef = db.collection(MyFirestoreReferences.POSTS_COLLECTION);
        postsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                posts.clear(); // Clear the existing posts to avoid duplicates
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    if (!post.getUser().getUsername().equals(username)) {
                        posts.add(post);
                    }
                }

                HomeAdapter homeAdapter = new HomeAdapter(posts, currentUser);
                homeRecyclerView.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();
            } else {
                // Handle the case where fetching posts fails
                Toast.makeText(HomeScreen.this, "Error fetching posts: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}