package com.example.ifind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    Button signUp, signIn, forgotPass ;
    ImageView logo;
    TextView signUpText;
    TextInputLayout logEmail,logPassword;

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
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

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
}