package com.mobdeve.s13.group8.nomadsapp;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String fullname;
    private String username;
    private String password;
    private String email;
    private String imageId;
    private ArrayList<User> followers;

    public User() {
    }

    public User(String fullname, String username, String password, String email, String imageId){
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageId = imageId;
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

    public ArrayList<User> getFollowers() {
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

    public void addFollower(User follower) {
        this.followers.add(follower);
    }

    public void removeFollower(User follower) {
        this.followers.remove(follower);
    }
}
