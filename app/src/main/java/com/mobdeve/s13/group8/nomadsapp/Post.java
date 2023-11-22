package com.mobdeve.s13.group8.nomadsapp;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    @Exclude
    private String id;
    private User user;
    private String location;
    private String caption;

    private String body;
    private String ImageId;

    @ServerTimestamp
    private Date date;

    private int likes;

    private ArrayList<Comment> comments;

    public Post(){

    }

    public Post(User user, String location, String caption, String body){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.likes = 0;
    }

    public Post(User user, String location, String caption, String body, String ImageId, int likes){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.ImageId = ImageId;
        this.likes = likes;
    }

    public Date getDate() {
        return date;
    }

    public String getImageId() {
        return ImageId;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getCaption() {
        return caption;
    }

    public String getLocation() {
        return location;
    }

    public int getLikes() {
        return likes;
    }

    public void addComments(User user, String comment) {
        comments.add(new Comment(user, comment));
    }

    public ArrayList<Comment> getComments(){
        return comments;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

    public void setLikes(int i) {
        this.likes = i;
    }
}
