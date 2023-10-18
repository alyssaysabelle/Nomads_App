package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        this.image.setImageResource(comment.getUser().getImageId());
        this.username.setText(comment.getUser().getUsername());
        this.comment.setText(comment.getComment());
    }
}
