package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mobdeve.s13.group8.nomadsapp.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String category;
    private ArrayList<Post> posts = DataGenerator.postsData();
    private ArrayList<User> users = DataGenerator.userData();
    private RecyclerView searchPostRecyclerView;
    private RecyclerView searchUserRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActivitySearchBinding viewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // back button
        viewBinding.backImageBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Spinner spinner = findViewById(R.id.category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search, R.layout.selected_dropdown_style);
        adapter.setDropDownViewResource(R.layout.dropdown_style);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
        if(category.equals("Posts")){
            this.searchPostRecyclerView = findViewById(R.id.searchRv);
            this.searchPostRecyclerView.setAdapter(new SearchPostAdapter(posts));
            LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            this.searchPostRecyclerView.setLayoutManager(layoutManagerV);
        }
        else {
            this.searchUserRecyclerView = findViewById(R.id.searchRv);
            this.searchUserRecyclerView.setAdapter(new SearchUserAdapter(users));
            LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            this.searchUserRecyclerView.setLayoutManager(layoutManagerV);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}