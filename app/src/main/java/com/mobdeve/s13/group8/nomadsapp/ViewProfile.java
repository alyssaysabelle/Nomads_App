package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobdeve.s13.group8.nomadsapp.databinding.ActivitySearchBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewProfileBinding;

import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity {
    private ArrayList<Post> posts = DataGenerator.postsData();
    private RecyclerView ownProfileRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        ActivityViewProfileBinding viewBinding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

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