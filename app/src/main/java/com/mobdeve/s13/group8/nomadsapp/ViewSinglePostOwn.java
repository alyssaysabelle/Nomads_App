package com.mobdeve.s13.group8.nomadsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.io.Serializable;
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
    private User currentUser;
    private static final int EDIT_POST_REQUEST_CODE = 123;

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
            public void onClick(View view) { finish(); }
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
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        // get post from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // query for same post id
        //loadPostData();

        viewBinding3.likeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = !isClicked;

                if (isClicked) {
                    view.setBackgroundResource(R.drawable.like_button);
                    updateLikeCount(currentUser, true);
                } else {
                    view.setBackgroundResource(R.drawable.not_liked_button);
                    updateLikeCount(currentUser, false);
                }
            }
        });

        viewBinding3.commentImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSinglePostOwn.this, Comments.class);
                intent.putExtra("postId", postId);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        viewBinding3.editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSinglePostOwn.this, EditPost.class);
                intent.putExtra("postId", postId);
                intent.putExtra("currentUser", currentUser);
                //startActivity(intent);
                startActivityForResult(intent, EDIT_POST_REQUEST_CODE);
                finish();
            }
        });
    }

    private void updateLikeCount (User user, boolean increment) {
        if (increment) {
            FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                    .document(postId).update("likes", FieldValue.arrayUnion(user.getUsername()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            post.addLike(user.getUsername());
                            postLikes.setText(String.valueOf(post.getLikes().size()));
                        } else {
                            Toast.makeText(ViewSinglePostOwn.this, "Error updating likes", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            FirebaseFirestore.getInstance().collection(MyFirestoreReferences.POSTS_COLLECTION)
                    .document(postId).update("likes", FieldValue.arrayRemove(user.getUsername()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            post.removeLike(user.getUsername());
                            postLikes.setText(String.valueOf(post.getLikes().size()));
                        } else {
                            Toast.makeText(ViewSinglePostOwn.this, "Error updating likes", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPostData();
    }


    private void loadPostData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                    // check if likes is null
                    if (post.getLikes() == null)
                        postLikes.setText("0 likes");
                    else {
                        postLikes.setText(String.valueOf(post.getLikes().size()));

                        // check if user already liked the post
                        /*for (User user : post.getLikes()) {
                            if (user.getId().equals(currentUser.getId())) {
                                likeBtn.setBackgroundResource(R.drawable.like_button);
                                isClicked = true;
                            }
                        }*/
                    }

                    // Check if imageId is not null
                    if (post.getImageId() != null) {
                        Picasso.get().load(post.getImageId()).into(ImageId);
                    } else {
                        // Set visibility to GONE if imageId is null
                        findViewById(R.id.postImageIv).setVisibility(View.GONE);
                    }

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
    }

    @Override
    public void onBackPressed() {
        // Create an intent to start the ViewProfile activity
        super.onBackPressed();

        Intent intent = new Intent(ViewSinglePostOwn.this, ViewProfile.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);

        finish();
    }


}