package com.mobdeve.s13.group8.nomadsapp;

public class Comment {
    private User user;
    private String comment;

    public Comment(User user, String comment){
        this.user = user;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }
}
