package com.mobdeve.s13.group8.nomadsapp;

import java.util.ArrayList;
import java.util.Collections;

// contains dummy data
public class DataGenerator {
    private static final User user1 = new User("Alyssa Meneses", "alyssaysabelle", "password", R.drawable.user1);
    private static final User user2 = new User("Stacy Kalaw", "stacyselena", "password", R.drawable.user2);
    private static final User user3 = new User("Richard Sy", "richard", "passwoord", R.drawable.user3);

    private static Post post1 = new Post(user1, "Manila, Philippines", "sample", "sample sample sample sample sample", R.drawable.sample_bg, new CustomDate(2023, 10, 10), 123);
    private static Post post2 = new Post(user2, "Baguio, Philippines", "gumana ka pls", "ayoko na gumawa ksjdkjsakdjajkdlja", R.drawable.sample_bg, new CustomDate(2023, 9, 9), 456);
    private static Post post3 = new Post(user3, "Pampanga, Philippines", "nagugustom na ako hlasjlkajljdas", "jksajklasdjkdjlksadjldjsldjsljfak kasjkdjaskljfkajfkjfsakljafs", R.drawable.sample_bg, new CustomDate(2023, 8, 8), 789);

    private static Comment comment1 = new Comment(user1, "Wow! I love the photos you took, makes me want to book a flight to Switzerland right now.");
    private static Comment comment2 = new Comment(user2, "WOW!! :o");
    private static Comment comment3 = new Comment(user3, "Pangit naman diyan e!! >:(");

    public static ArrayList<Post> postsData(){
        ArrayList<Post> posts = new ArrayList<>();

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);

        Collections.shuffle(posts);

        return posts;
    }

    public static ArrayList<User> userData(){
        ArrayList<User> userlist = new ArrayList<>();

        userlist.add(user1);
        userlist.add(user2);
        userlist.add(user3);

        return userlist;
    }

    public static ArrayList<Comment> commentData(){
        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        Collections.shuffle(comments);

        return comments;
    }
}
