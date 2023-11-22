package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    private ImageView profile;
    private TextView username;
    private TextView location;
    private TextView caption;
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
        caption = itemView.findViewById(R.id.captionTv);
    }
    public void bindData(Post post){
        Picasso.get().load(post.getUser().getImageId()).into(this.profile);
        this.username.setText(post.getUser().getUsername());
        this.location.setText(post.getLocation());
        Picasso.get().load(post.getImageId()).into(this.postImage);
        this.likes.setText(post.getLikes() + "");
        this.caption.setText(post.getCaption());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(post.getDate());
        this.date.setText(formattedDate);
        // change declaration after initializing comments
        this.comments.setText("sample");
    }
}
