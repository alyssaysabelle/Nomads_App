package com.mobdeve.s13.group8.nomadsapp;

public class User {
    private final String fullname;
    private final String username;
    private final String password;
    private final int imageId;

    public User(String fullname, String username, String password, int imageId){
        this.fullname = fullname;
        this.username = username;
        this.password = password;
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

    public int getImageId() {
        return imageId;
    }
}
