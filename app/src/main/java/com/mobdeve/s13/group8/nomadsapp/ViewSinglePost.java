package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewSinglePost extends AppCompatActivity {

    private TextView username;
    private ImageView profilePic;
    private TextView postDate;
    private TextView postCaption;
    private TextView postLocation;
    private TextView postBody;
    private ImageView ImageId;
    private TextView postLikes;
    private TextView postComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_post);

        username = findViewById(R.id.postUsernameTv);
        profilePic = findViewById(R.id.userIv);
        postDate = findViewById(R.id.postDateTv);
        postCaption = findViewById(R.id.postCaptionTv);
        postLocation = findViewById(R.id.postLocationTv);
        postBody = findViewById(R.id.postBodyTv);
        ImageId = findViewById(R.id.postImageIv);
        postLikes = findViewById(R.id.postLikesTv);
        postComments = findViewById(R.id.postCommentsTv);

        // get data from intent
        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");

        // get post from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // query for same post id
        db.collection(MyFirestoreReferences.POSTS_COLLECTION).whereEqualTo(MyFirestoreReferences.POST_ID_FIELD, postId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Post post = document.toObject(Post.class);
                    username.setText(post.getUser().getUsername());
                    Picasso.get().load(post.getUser().getImageId()).into(profilePic);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy HH:mm:ss", Locale.getDefault());
                    postDate.setText(dateFormat.format(post.getDate()));
                    postCaption.setText(post.getCaption());
                    postLocation.setText(post.getLocation());
                    postBody.setText(post.getBody());
                    postLikes.setText(String.valueOf(post.getLikes()));
                    Picasso.get().load(post.getImageId()).into(ImageId);
                    // check if comments is null
                    if (post.getComments() == null)
                        postComments.setText("0 comments");
                    else
                        postComments.setText(post.getComments().size() + " comments");
                }
            }
            else {
                Toast.makeText(ViewSinglePost.this, "Error getting post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}