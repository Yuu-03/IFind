package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private ProgressBar progressBar;
    TextInputLayout regFname, regUsername, regEmail, regPhoneNum, regPassword, regConfirmPass;
    Button regSubmit;

    ImageView backButton;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        regFname = findViewById(R.id.signUp_fname);
        regUsername = findViewById(R.id.signUp_username);
        regEmail = findViewById(R.id.signUp_email);
        regPhoneNum = findViewById(R.id.signUp_phonenum);
        regPassword = findViewById(R.id.signUp_pass);
        regConfirmPass = findViewById(R.id.confirmPass);
        backButton = findViewById(R.id.back);
        regSubmit = findViewById(R.id.regForm);
        progressBar = findViewById(R.id.progressBar);

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
                String username = regUsername.getEditText().getText().toString().trim();
                String email = regEmail.getEditText().getText().toString().trim();
                String phone = regPhoneNum.getEditText().getText().toString().trim();
                String pass = regPassword.getEditText().getText().toString().trim();
                String confirmPass = regConfirmPass.getEditText().getText().toString().trim();

                //Validate phone number
                String mobileRegex = "[0,6][0-9]{10}"; //first number can be 0 - 6 the rest ca be 0-9
                Matcher mobMatcher;
                Pattern mobPattern = Pattern.compile(mobileRegex);
                mobMatcher = mobPattern.matcher(phone);


                if (!regFname() | !regUsername()| !regEmail()| !regPhoneNum()| !regPassword()| !regConfirmPass()) {
                    return;
                }
                else if (!mobMatcher.find()){
                    regPhoneNum.setError("Invalid phone number. Re-enter your phone number.");
                    regPhoneNum.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    regUser(fname, username, email, phone, pass);

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
    private void regUser(String fname,String username,String email,String phone,String pass){
        //Oncompletelistener will execute when the user registration is successful
        //create user profile
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Update Display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fname).build();
                    firebaseUser.updateProfile(profileChangeRequest);



                    //Save user data in realtime database , fetch data from the database (auth)
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails( fname, username,email,phone); //pass user data to get from the database

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
                                //Toast.makeText(SignUpActivity.this, "SignUp Failed.Please try again.", Toast.LENGTH_SHORT).show();
                                try {
                                    throw task.getException();
                                    //
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    regEmail.setError("Your email is invalid or already in use. Please re-enter your email.");
                                    regEmail.requestFocus();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    regEmail.setError("User is already registered with this email. Use another email.");
                                    regEmail.requestFocus();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        }
                    });


                } else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // validate fields,  if empty an error message helper will show
    private Boolean regFname() {
        String value  = regFname.getEditText().getText().toString();

        if (value.isEmpty()) {
            regFname.setError("Field cannot be empty");
            return false;
        }
        else {
            regFname.setError(null);
            regFname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean regUsername() {
        String value  = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (value.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (value.length()>=15) {
            regUsername.setError("Username too long");
            return false;


        } else if (!value.matches(noWhiteSpace)) {
            regUsername.setError("White space is not allowed");
            return false;

        } else {
            regUsername.setError(null);
            regPhoneNum.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean regEmail() {
        String value  = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (value.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean regPhoneNum() {
        String value  = regPhoneNum.getEditText().getText().toString();

        if (value.isEmpty()) {
            regPhoneNum.setError("Field cannot be empty");
            return false;
        }
        else {
            regPhoneNum.setError(null);
            regPhoneNum.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean regPassword()  {
        String value  = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (value.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        }
        else if (!value.matches(passwordVal)) {
            regPassword.setError("Password too weak");
            return false;
        }
        else {
            regPassword.setError(null);
            regPhoneNum.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean regConfirmPass()  {
        String pass  = regPassword.getEditText().getText().toString();
        String confirmPass  = regConfirmPass.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (confirmPass.isEmpty()) {
            regConfirmPass.setError("Field cannot be empty");
            return false;
        }
        else if (!confirmPass.equals(pass)) {
            regConfirmPass.setError("Password too weak");
            return false;
        }
        else {
            regConfirmPass.setError(null);
            regConfirmPass.setErrorEnabled(false);
            return true;
        }
    }
}



