package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String category;
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
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

        // search query
        viewBinding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (category.equals("Users")) {
                    users.clear();
                    db.collection("Users")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                    User user = queryDocumentSnapshots.getDocuments().get(i).toObject(User.class);
                                    // Check if the username contains the query (case insensitive)
                                    if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                                        users.add(user);
                                    }
                                }
                                updateSearchUserAdapter();
                            });
                } else {
                    posts.clear();
                    db.collection("Posts")
                            .whereGreaterThanOrEqualTo("location", query)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                    Post post = queryDocumentSnapshots.getDocuments().get(i).toObject(Post.class);
                                    // Check if the location contains the query (case insensitive)
                                    if (post.getLocation().toLowerCase().contains(query.toLowerCase())) {
                                        posts.add(post);
                                    }
                                }
                                updateSearchPostAdapter();
                            });
                }

                return false;
            }


            private void updateSearchUserAdapter() {
                searchUserRecyclerView = findViewById(R.id.searchRv);
                SearchUserAdapter userAdapter = new SearchUserAdapter(users);
                searchUserRecyclerView.setAdapter(userAdapter);
                LinearLayoutManager layoutManagerV = new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false);
                searchUserRecyclerView.setLayoutManager(layoutManagerV);
            }

            private void updateSearchPostAdapter() {
                searchPostRecyclerView = findViewById(R.id.searchRv);
                SearchPostAdapter postAdapter = new SearchPostAdapter(posts);
                searchPostRecyclerView.setAdapter(postAdapter);
                LinearLayoutManager layoutManagerV = new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false);
                searchPostRecyclerView.setLayoutManager(layoutManagerV);
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(Search.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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