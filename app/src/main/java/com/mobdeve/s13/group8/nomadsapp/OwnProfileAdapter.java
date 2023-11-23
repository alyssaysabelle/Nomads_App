package com.mobdeve.s13.group8.nomadsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OwnProfileAdapter extends RecyclerView.Adapter<OwnProfileViewHolder>{
    private ArrayList<Post> posts;

    public OwnProfileAdapter(ArrayList<Post> data) {
        this.posts = data;
    }

    @Override
    public OwnProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_profile_own_user_item_layout, parent, false);
        return new OwnProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OwnProfileViewHolder holder, int position) {
        holder.bindData(posts.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewSinglePost.class);
                intent.putExtra("postId", posts.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
