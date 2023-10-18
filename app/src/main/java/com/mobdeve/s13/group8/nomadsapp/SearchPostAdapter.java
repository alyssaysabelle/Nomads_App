package com.mobdeve.s13.group8.nomadsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostsViewHolder>{
        private ArrayList<Post> posts;

    public SearchPostAdapter(ArrayList<Post> data) {
        this.posts = data;
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
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
