package com.example.ifind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class OTPVerification extends AppCompatActivity {
    ImageView back;

    Button verify;

    EditText phoneNumEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        verify = findViewById(R.id.verify);
        back = findViewById(R.id.back3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPVerification.this, ForgotPassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPVerification.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }
}