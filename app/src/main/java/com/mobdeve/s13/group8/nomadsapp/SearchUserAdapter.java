package com.mobdeve.s13.group8.nomadsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUsersViewHolder>{
    private ArrayList<User> user;
    private User currentUser;

    public SearchUserAdapter(ArrayList<User> data, User currentUser){
        this.user = data;
        this.currentUser = currentUser;
    }

    @Override
    public SearchUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_users_layout, parent, false);
        return new SearchUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchUsersViewHolder holder, int position) {
        holder.bindData(user.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.get(position).getUsername().equals(currentUser.getUsername())) {
                    Intent intent = new Intent(v.getContext(), ViewProfile.class);
                    intent.putExtra("currentUser", user.get(position));
                    intent.putExtra("otherUser", user.get(position));
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), ViewOtherProfile.class);
                    intent.putExtra("currentUser", user.get(position));
                    intent.putExtra("otherUser", user.get(position));
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }
}
