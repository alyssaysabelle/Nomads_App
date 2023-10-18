package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchPostsViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView username;
        private TextView title;
        //private TextView tags;

        public SearchPostsViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.searchPostsProfilePicture);
            username = itemView.findViewById(R.id.searchPostsUsername);
            title = itemView.findViewById(R.id.searchPostsTitle);
            //tags = itemView.findViewById(R.id.searchPostsTags);
        }

    public void bindData(Post post){
            this.profile.setImageResource(post.getUser().getImageId());
            this.username.setText(post.getUser().getUsername());
            this.title.setText(post.getCaption());
            //this.tags.setText(post.get);
        }
}
