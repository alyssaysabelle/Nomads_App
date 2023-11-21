package com.mobdeve.s13.group8.nomadsapp;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class Post {
    private User user;
    private String location;
    private String caption;

    private String body;
    private int ImageId;

    private @ServerTimestamp Date timestamp;
    private CustomDate date;
    private int likes;

    private ArrayList<Comment> comments;

    public Post(){

    }
    public Post(User user, String location, String caption, Date timestamp){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.timestamp = timestamp;
        this.likes = 0;
    }

    public Post(User user, String location, String caption, String body, int ImageId, CustomDate date, int likes){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.ImageId = ImageId;
        this.date = date;
        this.likes = likes;
    }

    public CustomDate getDate() {
        return date;
    }

    public int getImageId() {
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
}
