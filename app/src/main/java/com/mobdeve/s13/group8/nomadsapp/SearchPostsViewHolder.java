package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class SearchPostsViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView username;
        private TextView title;
        private TextView location;

        public SearchPostsViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.searchPostsProfilePicture);
            username = itemView.findViewById(R.id.searchPostsUsername);
            title = itemView.findViewById(R.id.searchPostsTitle);
            location = itemView.findViewById(R.id.searchPostsLocation);
        }

    public void bindData(Post post){
        Picasso.get().load(post.getUser().getImageId()).into(this.profile);
        this.username.setText(post.getUser().getUsername());
        this.title.setText(post.getCaption());
        this.location.setText(post.getLocation());
        }
}
