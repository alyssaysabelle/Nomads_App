package com.mobdeve.s13.group8.nomadsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OthersProfileAdapter extends RecyclerView.Adapter<OwnProfileViewHolder>{
    private ArrayList<Post> posts;
    private User currentUser;

    public OthersProfileAdapter(ArrayList<Post> data, User user) {
        this.posts = data;
        this.currentUser = user;
    }

    @Override
    public OwnProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_profile_own_user_item_layout, parent, false);
        return new OwnProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OwnProfileViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(posts.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewSinglePost.class);
                intent.putExtra("postId", posts.get(position).getId());
                intent.putExtra("currentUser", currentUser);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
