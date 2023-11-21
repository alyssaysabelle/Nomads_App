package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Comments extends AppCompatActivity {

    private ArrayList<Comment> comments = new ArrayList<>();
    private RecyclerView commentRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // recycler view
        this.commentRecyclerView = findViewById(R.id.commentsRv);
        this.commentRecyclerView.setAdapter(new CommentAdapter(comments));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.commentRecyclerView.setLayoutManager(layoutManagerV);
    }
}