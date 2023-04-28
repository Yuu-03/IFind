package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.example.ifind.databinding.ActivityForgotPassBinding;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {
//    ActivityForgotPassBinding binding;
    ImageView backbttn;

    Button resetPass;

    EditText userEmailAdd;

    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        backbttn = findViewById(R.id.back2);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        resetPass = findViewById(R.id.resetPass);


        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ForgotPassActivity.this, "Back", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));

            }
        });


        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailBox = findViewById(R.id.emailBox);

                resetPass.findViewById(R.id.resetPass).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();

                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(ForgotPassActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgotPassActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    /*
    private Boolean validateEmail() {
        String value  = binding.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (value.isEmpty()) {
            binding.email.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailPattern)) {
            binding.email.setError("Invalid email address");
            return false;
        } else {
            binding.email.setError(null);
            return true;
        }
    }
    //validation before reseting password
    private void resetPassword() {
        if (!validateEmail())
        {
            return;
        }
        /*String email = userEmailAdd.getText().toString();

        //email validation
        if (email.isEmpty()) {
            userEmailAdd.setError("Email is required!");
            userEmailAdd.requestFocus();
            return;
        }
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmailAdd.setError("Please provide a valid email!");
            userEmailAdd.requestFocus();
            return;
        }*/
    //progressBar.setVisibility(View.VISIBLE);
    //}
    // */



}


