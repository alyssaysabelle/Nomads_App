package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @Exclude
    private String id;
    private String fullname;
    private String username;
    private String password;
    private String email;
    private String imageId;
    private ArrayList<String> followers;
    private ArrayList<String> following;

    public User() {
    }

    public User(String fullname, String username, String password, String email, String imageId){
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageId = imageId;
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImageId() {
        return imageId;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFollower(String follower) {
        if (followers == null) {
            followers = new ArrayList<>();
        }
        this.followers.add(follower);
    }

    public void removeFollower(String follower) {
        this.followers.remove(follower);
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void addFollowing(String following) {
        this.following.add(following);
    }

    public void removeFollowing(String following) {
        this.following.remove(following);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public boolean isFollowing(String username) {
        if (following == null) {
            return false;
        }
        return following != null && following.contains(username);
    }
}
