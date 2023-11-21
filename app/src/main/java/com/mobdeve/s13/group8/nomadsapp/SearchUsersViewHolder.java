package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class SearchUsersViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile;
        private TextView username;
        private TextView followers;

        public SearchUsersViewHolder(View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.searchUsersProfilePicture);
            username = itemView.findViewById(R.id.searchUsersUsername);
            followers = itemView.findViewById(R.id.searchUsersFollowers);
        }

    public void bindData(User user){
        Picasso.get().load(user.getImageId()).into(this.profile);
            this.username.setText(user.getUsername());
            if(user.getFollowers() != null)
                this.followers.setText(user.getFollowers().size() + " followers");
            else
                this.followers.setText("0 + followers");
        }
}
