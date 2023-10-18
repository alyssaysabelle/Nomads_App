package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityCreatePostBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePost extends AppCompatActivity {

    private TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ActivityCreatePostBinding viewBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // back button
        viewBinding.backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // set current date
        String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date = findViewById(R.id.newDateTv);
        date.setText(currentDate);

        // post button
        viewBinding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* TODO: add declare new post button
                 */
                finish();
            }
        });
    }
}