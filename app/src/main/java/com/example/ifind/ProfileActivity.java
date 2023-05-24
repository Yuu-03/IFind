package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private TextView userName,userEmail,userUsername,userPhone,userPassword,titleName;

    private Button logoutBttn, mback;

    private String fname,mname,lname,username,email,phone,pass;

    private ImageView userImage;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.profName);
        userEmail = findViewById(R.id.profEmail);
        userUsername = findViewById(R.id.profUsername);
        userPhone= findViewById(R.id.profPhone);
        logoutBttn = findViewById(R.id.logout);
        titleName = findViewById(R.id.titleName);
        mback = findViewById(R.id.back);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                signOutUser();
                finish();
            }
        });
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (firebaseUser == null) {
            Toast.makeText(ProfileActivity.this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }else {
            checkIfEmailVerified(firebaseUser);
            showProfileActivity(firebaseUser);
        }
    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //setup
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("You can not log in unless you verified your email. Please verify your email first.");

        // open email app is user clicks the button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // proceed to new window(email app) and not within this app
                startActivity(intent);
            }
        });
        //create dialog box
        AlertDialog alertDialog = builder.create();

        //show alert dialog
        alertDialog.show();
    }

    private void showProfileActivity(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User reference from realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    fname = firebaseUser.getDisplayName();
                    username = readUserDetails.username;
                    email = firebaseUser.getEmail();
                    phone = readUserDetails.phone;

                    titleName.setText("Welcome,"+fname+"!");
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

    private void signOutUser() {
        Intent profActivity = new Intent(ProfileActivity.this, LoginActivity.class);
        profActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profActivity);
        finish();
    }
}