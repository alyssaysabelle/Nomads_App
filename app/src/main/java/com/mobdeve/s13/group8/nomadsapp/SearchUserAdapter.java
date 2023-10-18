package com.mobdeve.s13.group8.nomadsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUsersViewHolder>{
    private ArrayList<User> user;

    public SearchUserAdapter(ArrayList<User> data) {
        this.user = data;
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
    }

    @Override
    public int getItemCount() {
        return user.size();
    }
}
