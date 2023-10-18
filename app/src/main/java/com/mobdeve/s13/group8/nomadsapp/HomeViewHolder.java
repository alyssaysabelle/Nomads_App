package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    private ImageView profile;
    private TextView username;
    private TextView location;
    private ImageView postImage;
    private TextView likes;
    private TextView comments;
    private TextView date;
    public HomeViewHolder(View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.profileIv);
        username = itemView.findViewById(R.id.usernameTv);
        location = itemView.findViewById(R.id.locationTv);
        postImage = itemView.findViewById(R.id.postIv);
        likes = itemView.findViewById(R.id.likesTv);
        comments = itemView.findViewById(R.id.commentsTv);
        date = itemView.findViewById(R.id.dateTv);
    }
    public void bindData(Post post){
        this.profile.setImageResource(post.getUser().getImageId());
        this.username.setText(post.getUser().getUsername());
        this.location.setText(post.getLocation());
        this.postImage.setImageResource(post.getImageId());
        this.likes.setText(post.getLikes() + "");
        this.date.setText(post.getDate().toStringFull());
        // change declaration after initializing comments
        this.comments.setText("sample");
    }
}
