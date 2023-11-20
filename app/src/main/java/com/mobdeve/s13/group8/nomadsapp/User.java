package com.mobdeve.s13.group8.nomadsapp;

public class User {
    private String fullname;
    private String username;
    private String password;
    private String email;
    private int imageId;

    public User() {
    }

    public User(String fullname, String username, String password, String email, int imageId){
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

    public int getImageId() {
        return imageId;
    }

    public String getEmail() {
        return email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
