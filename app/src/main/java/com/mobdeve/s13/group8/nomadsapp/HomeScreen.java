package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityHomeScreenBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private ArrayList<Post> posts = DataGenerator.postsData();
    private RecyclerView homeRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ActivityHomeScreenBinding viewBinding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // recycler view
        this.homeRecyclerView = findViewById(R.id.homeRv);
        this.homeRecyclerView.setAdapter(new HomeAdapter(posts));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.homeRecyclerView.setLayoutManager(layoutManagerV);

        // user profile button
        viewBinding.userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, ViewProfile.class);
                startActivity(intent);
            }
        });

        // search button
        viewBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, Search.class);
                startActivity(intent);
            }
        });

        // create post button
        viewBinding.createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, CreatePost.class);
                startActivity(intent);
            }
        });
    }
}