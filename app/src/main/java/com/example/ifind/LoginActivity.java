package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    Button signUp, signIn, forgotPass ;
    ImageView logo;
    TextView signUpText;
    TextInputLayout logEmail,logPassword;
    private static final String ADMIN = "s1Olp2MGwRVpuj2UiJh9zcnpu4j1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        logo = findViewById(R.id.img_logo);
        signUpText = findViewById(R.id.textView1);
        logEmail = findViewById(R.id.email_Acc);
        logPassword = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn_bttn);
        forgotPass = findViewById(R.id.forgetPass);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();


        //Code for Sign in
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logEmail.getEditText().getText().toString();
                String pass = logPassword.getEditText().getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        // startActivity(new Intent(LoginActivity.this, ProfileActivity.class));

                                        //Get instance of the current user
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        //check if email is verified before user can access their profile
                                        if (firebaseUser.isEmailVerified()) {

                                            if (firebaseUser.getUid().equals(ADMIN)) {
                                                Toast.makeText(LoginActivity.this, "You are now logged in Admin Ifind.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                                            } else {
                                                Toast.makeText(LoginActivity.this, "You are now logged in.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            }


                                            //open user profile

                                        } else {
                                            firebaseUser.sendEmailVerification();
                                            auth.signOut(); //sign out user if not verified
                                            showAlertBuilder();

                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        logPassword.setError("Empty fields are not allowed");
                    }
                } else if (email.isEmpty()) {
                    logEmail.setError("Empty fields are not allowed");
                } else {
                    logEmail.setError("Please enter correct email");
                }
            }
        });
        //Code for Sign up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        //Code for Forget Password
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Reset password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            }
        });
    }
    /*
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertBuilder();
        }
    }*/

    private void showAlertBuilder() {
        //setup
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    //Check if user is already logged in. If true then it will directly open the home screen else it will go to the log in screen.

    protected void onStart() {
        FirebaseUser firebaseUser = auth.getCurrentUser();

        super.onStart();
        String email = logEmail.getEditText().getText().toString();
        if (auth.getCurrentUser() != null) {
            if (firebaseUser.isEmailVerified()){
                if (firebaseUser.getUid().equals(ADMIN)) {
                    Toast.makeText(LoginActivity.this, "Already logged in Master!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Already logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();//sign out user if not verified
                }


            }
            else {
                //start user profile
                auth.signOut();//close login screen
            }

        } else {
            Toast.makeText(LoginActivity.this, "You can now logged in.", Toast.LENGTH_SHORT).show();

        }
    }
}