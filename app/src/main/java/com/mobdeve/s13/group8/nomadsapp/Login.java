package com.mobdeve.s13.group8.nomadsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private User currentUser;

    // tentative
    private ArrayList<User> userlist = DataGenerator.userData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityLoginBinding viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // redirect to sign up
        TextView textView = findViewById(R.id.redirectRegister);

        String text = "Don’t have an account?  Sign Up";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 24, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // input text
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);

        // login button
        viewBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameInput = viewBinding.usernameLogin.getText().toString();
                String passwordInput = viewBinding.passwordLogin.getText().toString();

                if(!usernameInput.isEmpty() && !passwordInput.isEmpty()) {
                    User user = findUser(userlist, usernameInput);
                    if(user != null && user.getPassword().equals(passwordInput)){
                        currentUser = user;
                        Intent intent = new Intent(Login.this, HomeScreen.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(
                                Login.this,
                                "Invalid username and/or password.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
                else {
                    Toast.makeText(
                            Login.this,
                            "Please enter your username and password.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    // find user
    public User findUser(ArrayList<User> userlist, String username) {
        for (User user : userlist) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}