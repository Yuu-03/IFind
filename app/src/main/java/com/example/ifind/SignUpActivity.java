package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    private FirebaseAuth auth;


    TextInputLayout regFname, regMname, regLname, regUsername, regEmail, regPhoneNum, regPassword;
    Button regSubmit;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        regFname = findViewById(R.id.signUp_fname);
        regMname = findViewById(R.id.signUp_mname);
        regLname = findViewById(R.id.signUp_lname);
        regUsername = findViewById(R.id.signUp_username);
        regEmail = findViewById(R.id.signUp_email);
        regPhoneNum = findViewById(R.id.signUp_phonenum);
        regPassword = findViewById(R.id.signUp_pass);
        backButton = findViewById(R.id.back);
        regSubmit = findViewById(R.id.regForm);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "Log in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            }
        });

        regSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = regFname.getEditText().getText().toString().trim();
                String mname = regMname.getEditText().getText().toString().trim();
                String lname = regLname.getEditText().getText().toString().trim();
                String username = regUsername.getEditText().getText().toString().trim();
                String email = regEmail.getEditText().getText().toString().trim();
                String phone = regPhoneNum.getEditText().getText().toString().trim();
                String pass = regPassword.getEditText().getText().toString().trim();


                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your first name.", Toast.LENGTH_LONG).show();
                    regFname.setError("First name is required.");
                    regFname.requestFocus();
                } else {
                    regFname.setError(null);
                    regFname.setErrorEnabled(false);

                }
                if (TextUtils.isEmpty(mname)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your middle initial.", Toast.LENGTH_LONG).show();
                    regMname.setError("Middle initial is required.");
                    regMname.requestFocus();


                } else {
                    regMname.setError(null);
                    regMname.setErrorEnabled(false);

                }
                if (TextUtils.isEmpty(lname)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your last name.", Toast.LENGTH_LONG).show();
                    regLname.setError("Last name is required.");
                    regLname.requestFocus();

                } else {
                    regLname.setError(null);
                    regLname.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your username.", Toast.LENGTH_LONG).show();
                    regUsername.setError("Username is required.");
                    regUsername.requestFocus();
                } else {
                    regUsername.setError(null);
                    regUsername.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your email.", Toast.LENGTH_LONG).show();
                    regEmail.setError("Email is required.");
                    regEmail.requestFocus();
                }else {
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Please re-enter your email.", Toast.LENGTH_LONG).show();
                    regEmail.setError("Please enter valid email.");
                    regEmail.requestFocus();
                }else {
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SignUpActivity.this, "Please enter phone number.", Toast.LENGTH_LONG).show();
                    regPhoneNum.setError("Enter valid phone number");
                    regPhoneNum.requestFocus();
                }else {
                    regPhoneNum.setError(null);
                    regPhoneNum.setErrorEnabled(false);
                }
                if (phone.length() != 11) {
                    Toast.makeText(SignUpActivity.this, "Please re-enter phone number.", Toast.LENGTH_LONG).show();
                    regPhoneNum.setError("Phone number should be 11 digits");
                    regPhoneNum.requestFocus();
                } else {
                    regPhoneNum.setError(null);
                    regPhoneNum.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignUpActivity.this, "Empty Password", Toast.LENGTH_LONG).show();
                    regPassword.setError("Password is required.");
                    regPassword.requestFocus();

                } else {
                    regPassword.setError(null);
                    regPassword.setErrorEnabled(false);
                }
                if (pass.length() < 8) {
                    Toast.makeText(SignUpActivity.this, "Password should be at least 8 digits.", Toast.LENGTH_LONG).show();
                    regPassword.setError("Password is too weak.");
                    regPassword.requestFocus();

                } else {
                    regUser(fname, mname, lname, username, email, phone, pass);
                    /*
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }

    //Register user
    private void regUser(String fname,String mname,String lname,String username,String email,String phone,String pass){
        //Oncompletelistener will execute when the user registration is successful
        //create user profile
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    /*
                    //Update Display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fname).build();
                    firebaseUser.updateProfile(profileChangeRequest);
                    */


                    //Save user data in realtime database , fetch data from the database (auth)
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails( fname, mname, lname, username, phone); //pass user data to get from the database

                    // Extracts user reference from database for Users
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    //passes the data from helperclass in this code
                    databaseReference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                //send verification email to verify the email address
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(SignUpActivity.this, "SignUp Successful.Please verify your email", Toast.LENGTH_SHORT).show();


                                //Open Login Activity after succesful registration
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                //to prevent user from returning back to Signup activity when pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); //closes register activity
                            }else{
                                Toast.makeText(SignUpActivity.this, "SignUp Failed.Please try again.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                } else {
                    Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



