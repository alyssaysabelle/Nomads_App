package com.mobdeve.s13.group8.nomadsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityViewSinglePostOwnBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewSinglePostOwn extends AppCompatActivity {
    private TextView username;
    private ImageView profilePic;
    private TextView postDate;
    private TextView postCaption;
    private TextView postLocation;
    private TextView postBody;
    private ImageView ImageId;
    private TextView postLikes;
    private TextView postComments;
    private ImageButton likeBtn;
    private ImageButton commentBtn;
    private boolean isClicked = false;
    private Post post;
    private String postId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_post_own);
        ActivityViewSinglePostOwnBinding viewBinding3 = ActivityViewSinglePostOwnBinding.inflate(getLayoutInflater());
        setContentView(viewBinding3.getRoot());
        //set back button
        viewBinding3.backImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        username = findViewById(R.id.postUsernameTv);
        profilePic = findViewById(R.id.userIv);
        postDate = findViewById(R.id.postDateTv);
        postCaption = findViewById(R.id.postCaptionTv);
        postLocation = findViewById(R.id.postLocationTv);
        postBody = findViewById(R.id.postBodyTv);
        ImageId = findViewById(R.id.postImageIv);
        postLikes = findViewById(R.id.postLikesTv);
        postComments = findViewById(R.id.postCommentsTv);
        likeBtn = findViewById(R.id.likeImageBtn);
        commentBtn = findViewById(R.id.commentImageBtn);

        // get data from intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        // get post from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // query for same post id
        db.collection(MyFirestoreReferences.POSTS_COLLECTION).whereEqualTo(MyFirestoreReferences.POST_ID_FIELD, postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    post = document.toObject(Post.class);
                    username.setText(post.getUser().getUsername());
                    Picasso.get().load(post.getUser().getImageId()).into(profilePic);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                    postDate.setText(dateFormat.format(post.getDate()));
                    postCaption.setText(post.getCaption());
                    postLocation.setText(post.getLocation());
                    postBody.setText(post.getBody());
                    postLikes.setText(String.valueOf(post.getLikes().size()));
                    Picasso.get().load(post.getImageId()).into(ImageId);
                    // check if comments is null
                    if (post.getComments() == null)
                        postComments.setText("0 comments");
                    else
                        postComments.setText(post.getComments().size() + " comments");
                }
            } else {
                Toast.makeText(ViewSinglePostOwn.this, "Error getting post", Toast.LENGTH_SHORT).show();
            }
        });

        viewBinding3.likeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = !isClicked;
                /* TODO: fix update likes */
                if (isClicked) {
                    view.setBackgroundResource(R.drawable.like_button);
                    //db.collection("Posts").document(postId).update("likes", FieldValue.increment(1));
                    //updateLikeCount(1);
                } else {
                    view.setBackgroundResource(R.drawable.not_liked_button);
                    //db.collection("Posts").document(postId).update("likes", FieldValue.increment(-1));
                    //updateLikeCount(-1);
                }
            }
        });
    }

    private void updateLikeCount(int increment) {
        FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                .document(postId).update("likes", FieldValue.increment(increment))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //post.setLikes(post.getLikes() + increment);
                        postLikes.setText(String.valueOf(post.getLikes().size()));
                    } else {
                        Toast.makeText(ViewSinglePostOwn.this, "Error updating likes", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
