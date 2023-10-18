package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchUsersViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView username;
        //private TextView followers;

        public SearchUsersViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.searchUsersProfilePicture);
            username = itemView.findViewById(R.id.searchUsersUsername);
        }

    public void bindData(User user){
            this.profile.setImageResource(user.getImageId());
            this.username.setText(user.getUsername());
            //this.followers.setText(post.getUser().getFollowers());
        }
}
