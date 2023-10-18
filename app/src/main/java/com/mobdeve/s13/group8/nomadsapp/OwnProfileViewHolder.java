package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OwnProfileViewHolder extends RecyclerView.ViewHolder {
    private ImageView image;
    private TextView date;
    private TextView location;
    private TextView caption;
    public OwnProfileViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.postImageView);
        date = itemView.findViewById(R.id.fullPostDate);
        location = itemView.findViewById(R.id.location);
        caption = itemView.findViewById(R.id.caption);
    }
    public void bindData(Post post){
        this.date.setText(post.getDate().toStringFull());
        this.image.setImageResource(post.getImageId());
        this.caption.setText(post.getCaption());
        this.location.setText(post.getLocation());
    }
}
