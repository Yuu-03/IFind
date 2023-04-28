package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView userName,userEmail,userUsername,userPhone,userPassword;

    private Button editButton;

    private String fname,mname,lname,username,email,phone,pass;

    private ImageView userImage;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");

        userName = findViewById(R.id.profName);
        userEmail = findViewById(R.id.profEmail);
        userUsername = findViewById(R.id.profUsername);
        userPhone= findViewById(R.id.profPhone);
        userPassword= findViewById(R.id.profPassword);
        editButton = findViewById(R.id.editButton);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(ProfileActivity.this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }else {
            showProfileActivity(firebaseUser);
        }
    }

    private void showProfileActivity(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User reference from realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails == null) {
                    fname = firebaseUser.getDisplayName();
                    username = readUserDetails.username;
                    email = firebaseUser.getDisplayName();
                    phone = readUserDetails.phone;

                    userName.setText(fname);
                    userUsername.setText(username);
                    userEmail.setText(email);
                    userPhone.setText(phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}