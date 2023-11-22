package com.mobdeve.s13.group8.nomadsapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(post.getDate());
        this.date.setText(formattedDate);
        Picasso.get().load(post.getImageId()).into(this.image);
        this.caption.setText(post.getCaption());
        this.location.setText(post.getLocation());
    }
}
