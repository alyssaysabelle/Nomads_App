package com.mobdeve.s13.group8.nomadsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostsViewHolder>{
        private ArrayList<Post> posts;
        private User currentUser;

    public SearchPostAdapter(ArrayList<Post> data, User currentUser) {
        this.posts = data;
        this.currentUser= currentUser;
    }

    @Override
    public SearchPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_posts_layout, parent, false);
        return new SearchPostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchPostsViewHolder holder, int position) {
        holder.bindData(posts.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posts.get(position).getUser().getUsername().equals(currentUser.getUsername())) {
                    Intent intent = new Intent(v.getContext(), ViewSinglePostOwn.class);
                    intent.putExtra("postId", posts.get(position).getId());
                    intent.putExtra("currentUser", currentUser);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), ViewSinglePost.class);
                    intent.putExtra("postId", posts.get(position).getId());
                    intent.putExtra("currentUser", currentUser);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
