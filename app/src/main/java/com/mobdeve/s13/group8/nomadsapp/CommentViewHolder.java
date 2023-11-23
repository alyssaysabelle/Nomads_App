package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CommentViewHolder extends RecyclerView.ViewHolder{
    private ImageView image;
    private TextView username;
    private TextView comment;
    public CommentViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.commentUserImageIv);
        username = itemView.findViewById(R.id.commentsUsernameTv);
        comment = itemView.findViewById(R.id.commentTv);
    }
    public void bindData(Comment comment){
        Picasso.get().load(comment.getUser().getImageId()).into(this.image);
        this.username.setText(comment.getUser().getUsername());
        this.comment.setText(comment.getComment());
    }
}
