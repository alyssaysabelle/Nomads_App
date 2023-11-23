package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityCommentsBinding;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityCreatePostBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Comments extends AppCompatActivity {
    private FirebaseStorage postStorage;
    private StorageReference postStorageReference;
    private User currentUser;
    private Post post;
    private String postId;
    private TextView commentsCount;
    private ImageButton commentsBackBtn;
    private ArrayList<Comment> comments = new ArrayList<>();
    private RecyclerView commentRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        ActivityCommentsBinding viewBinding3 = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(viewBinding3.getRoot());

        // set back button
        commentsBackBtn = findViewById(R.id.commentsBackBtn);
        commentsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentsCount = findViewById(R.id.commentsCountTv);
        this.comments = new ArrayList<>();

        // recycler view
        this.commentRecyclerView = findViewById(R.id.commentsRv);
        this.commentRecyclerView.setAdapter(new CommentAdapter(comments));
        LinearLayoutManager layoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.commentRecyclerView.setLayoutManager(layoutManagerV);

        postStorage = FirebaseStorage.getInstance();
        postStorageReference = postStorage.getReference();

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // query for the same post id
        db.collection(MyFirestoreReferences.POSTS_COLLECTION).whereEqualTo(MyFirestoreReferences.POST_ID_FIELD, postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                comments.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post = document.toObject(Post.class);

                    // check if comments is null
                    if (post.getComments() == null) {
                        commentsCount.setText("0 comments");
                    } else {
                        comments.addAll(post.getComments()); // AddAll comments to the local list
                        commentsCount.setText(comments.size() + " comments");

                        CommentAdapter commentAdapter = new CommentAdapter(comments);
                        commentRecyclerView.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                Toast.makeText(Comments.this, "Error getting comments", Toast.LENGTH_SHORT).show();
            }
        });

        viewBinding3.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = viewBinding3.commentEt.getText().toString();

                if (!text.isEmpty()) {
                    Comment comment = new Comment(currentUser, text);

                    FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                            .document(postId).update("comments", FieldValue.arrayUnion(comment))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    post.addComments(comment);
                                    comments.add(comment);
                                    commentsCount.setText(post.getComments().size() + " comments");
                                    commentRecyclerView.getAdapter().notifyItemInserted(comments.size() - 1);
                                    viewBinding3.commentEt.setText("");
                                } else {
                                    Toast.makeText(Comments.this, "Error adding comment", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle the case where the text is empty
                    Toast.makeText(Comments.this, "Text cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}