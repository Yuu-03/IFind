package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    Button signUp, signIn, forgotPass ;
    ImageView logo;
    TextView signUpText;
    TextInputLayout logEmail,logPassword;
    private static final String ADMIN1 = "WDl2ZTQR4WbK8fnX5GdUbVh1xFG3";
    private static final String ADMIN2 = "c3Z2cDvVWMNmm7ultb3Jd3OxuiB3";

    private static final String ADMIN3 = "jqysm1bI4mWh6pDWQscevf8v56S2";

    private static final String ADMIN4 = "p6xxHXqtxlP5wZ8DbBLmKE2kHr33";
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

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

                                            if (firebaseUser.getUid().equals(ADMIN1)) {
                                                Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                                            } else if (firebaseUser.getUid().equals(ADMIN2)) {
                                                Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                                            } else if (firebaseUser.getUid().equals(ADMIN3)) {
                                                Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                                            } else if (firebaseUser.getUid().equals(ADMIN4)) {
                                                Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                                            }else {
                                                Toast.makeText(LoginActivity.this, "Welcome" + " " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
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


    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
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
                if (firebaseUser.getUid().equals(ADMIN1)) {
                    Toast.makeText(LoginActivity.this, "Already logged in as Admin!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                    finish();
                } else if (firebaseUser.getUid().equals(ADMIN2)) {
                    Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                    finish();
                } else if (firebaseUser.getUid().equals(ADMIN3)) {
                    Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                    finish();
                } else if (firebaseUser.getUid().equals(ADMIN4)) {
                    Toast.makeText(LoginActivity.this, "You are now logged as Admin " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, pendingRequests.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Welcome" + " " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
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