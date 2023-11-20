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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private User currentUser;

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
        viewBinding.loginBtn.setOnClickListener(view -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersRef = db.collection(MyFirestoreReferences.USERS_COLLECTION);

            // Get username and password
            String usernameInput = viewBinding.usernameLogin.getText().toString();
            String passwordInput = viewBinding.passwordLogin.getText().toString();

            // Query for checking username and password
            usersRef.whereEqualTo(MyFirestoreReferences.USERNAME_FIELD, usernameInput)
                    .whereEqualTo(MyFirestoreReferences.PASSWORD_FIELD, passwordInput)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            // Username or password is incorrect
                            Toast.makeText(Login.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Username and password are correct, proceed to HomeScreen
                        // Get the current user's data
                        ArrayList<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            users.add(document.toObject(User.class));
                        }

                        // Get the current user based on username
                        for (User user : users) {
                            if (user.getUsername().equals(usernameInput)) {
                                currentUser = user;

                                // Pass current user to intent
                                Intent intent = new Intent(Login.this, HomeScreen.class);
                                intent.putExtra(IntentKeys.USERNAME.name(), currentUser.getUsername());
                                startActivity(intent);
                                return; // Exit the loop once the user is found
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Login.this, "Error checking username and password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


    }
}