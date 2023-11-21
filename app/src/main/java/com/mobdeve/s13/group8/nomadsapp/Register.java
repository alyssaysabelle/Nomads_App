package com.mobdeve.s13.group8.nomadsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobdeve.s13.group8.nomadsapp.databinding.ActivityRegisterBinding;

import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class Register extends AppCompatActivity {

    private TextView file;
    private ImageButton imageBtn;
    public Uri profileUri, uploadedUri;
    private FirebaseStorage profileStorage;
    private StorageReference profileStorageReference;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActivityRegisterBinding viewBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        profileStorage=FirebaseStorage.getInstance();
        profileStorageReference=profileStorage.getReference();

        imageBtn= findViewById(R.id.imageBtn);
        file= findViewById(R.id.file);

        // redirect to login
        TextView textView = findViewById(R.id.redirectLogin);

        String text = "Already have an account? Log In";
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 24, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        //upload profile
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });



        // register button
        viewBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if email, fullname, password, and username are not empty
                if (viewBinding.emailIn.getText().toString().isEmpty() ||
                        viewBinding.fullNameIn.getText().toString().isEmpty() ||
                        viewBinding.passwordIn.getText().toString().isEmpty() ||
                        viewBinding.usernameIn.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("Users").document(viewBinding.usernameIn.getText().toString())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        // Username already exists
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Username doesn't exist, create a new user
                                        User user = new User(
                                                viewBinding.fullNameIn.getText().toString(),
                                                viewBinding.usernameIn.getText().toString(),
                                                viewBinding.passwordIn.getText().toString(),
                                                viewBinding.emailIn.getText().toString(),
                                                // set default imageUri
                                                uploadedUri.toString()
                                                //"android.resource://com.mobdeve.s13.group8.nomadsapp/drawable/user_profile"
                                                );
                                        Toast.makeText(Register.this, viewBinding.emailIn.getText().toString(), Toast.LENGTH_SHORT).show();
                                        // Add the user to the database
                                        db.collection("Users").document(viewBinding.usernameIn.getText().toString())
                                                .set(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    // User added successfully
                                                    Toast.makeText(Register.this, "Account created!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle the failure to add the user to the database
                                                    Toast.makeText(Register.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
                                                    Log.e("Register", "Error adding user to Firestore", e);
                                                });
                                    }
                                } else {
                                    // Handle the failure to get data from Firestore
                                    Toast.makeText(Register.this, "Failed to check username. Please try again.", Toast.LENGTH_SHORT).show();
                                    Log.e("Register", "Error checking username in Firestore", task.getException());
                                }
                            });
                }

                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void choosePicture() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            profileUri = data.getData();
            imageBtn.setImageURI(profileUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = profileStorageReference.child("images/" + randomKey);


        String fileName= getFileNameFromUri(profileUri);
        file.setText(fileName);

        riverRef.putFile(profileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

                    // Get the download URL from the uploaded image
                    riverRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedUri= uri;

                    });
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Percentage: " + (int) progressPercent + "%");
                });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


}