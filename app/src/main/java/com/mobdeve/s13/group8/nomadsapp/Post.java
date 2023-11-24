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

    private ArrayList<String> likes;

    private ArrayList<Comment> comments;

    public Post(){

    }

    public Post(User user, String location, String caption, String body){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(String id, User user, String location, String caption, String body){
        this.id = id;
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(User user, String location, String caption, String body, String ImageId){
        this.user = user;
        this.location = location;
        this.caption = caption;
        this.body = body;
        this.ImageId = ImageId;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
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

    public ArrayList<String> getLikes() {
        return likes;
    }

    public ArrayList<Comment> getComments() { return comments; }

    public void addLike(String username) {
        likes.add(username);
    }

    public void removeLike(String username) {
        likes.remove(username);
    }

    public void addComments(Comment comment) {
        comments.add(comment);
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

    public void setDate(Date date) { this.date = date; }

}
