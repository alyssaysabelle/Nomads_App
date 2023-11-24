package com.mobdeve.s13.group8.nomadsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private ArrayList<Post> posts;
    private User currentUser;

    public HomeAdapter(ArrayList<Post> data, User user) {
        this.currentUser = user;
        this.posts = data;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_item_layout, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(posts.get(position), currentUser);

        // like button
        holder.itemView.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView likesTv = holder.itemView.findViewById(R.id.likesTv);
                ImageButton likeButton = holder.itemView.findViewById(R.id.imageButton);

                if (posts.get(position).getLikes().contains(currentUser.getUsername())) {
                    FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                            .document(posts.get(position).getId()).update("likes", FieldValue.arrayRemove(currentUser.getUsername()))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    posts.get(position).removeLike(currentUser.getUsername());

                                    if (posts.get(position).getLikes().size() == 1)
                                        likesTv.setText(posts.get(position).getLikes().size() + " like");
                                    else
                                        likesTv.setText(posts.get(position).getLikes().size() + " likes");

                                    likeButton.setImageResource(R.drawable.not_liked_button);
                                }
                            });
                }
                else {
                    FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                            .document(posts.get(position).getId()).update("likes", FieldValue.arrayUnion(currentUser.getUsername()))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    posts.get(position).addLike(currentUser.getUsername());

                                    if (posts.get(position).getLikes().size() == 1)
                                        likesTv.setText(posts.get(position).getLikes().size() + " like");
                                    else
                                        likesTv.setText(posts.get(position).getLikes().size() + " likes");

                                    likeButton.setImageResource(R.drawable.like_button);
                                }
                            });
                }
            }
        });

        // comment button
        holder.itemView.findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Comments.class);
                intent.putExtra("postId", posts.get(position).getId());
                intent.putExtra("currentUser", currentUser);
                v.getContext().startActivity(intent);
            }
        });

        // clicking on a single post
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewSinglePost.class);
                intent.putExtra("postId", posts.get(position).getId());
                intent.putExtra("currentUser", currentUser);
                
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), "User " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
